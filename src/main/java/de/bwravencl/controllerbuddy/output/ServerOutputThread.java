/* Copyright (C) 2019  Matteo Hausner
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package de.bwravencl.controllerbuddy.output;

import java.io.IOException;
import java.io.StringWriter;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.bwravencl.controllerbuddy.gui.Main;
import de.bwravencl.controllerbuddy.input.Input;
import de.bwravencl.controllerbuddy.version.VersionUtils;

public final class ServerOutputThread extends OutputThread {

	private enum ServerState {
		Listening, Connected
	}

	private static final Logger log = Logger.getLogger(ServerOutputThread.class.getName());

	public static final int DEFAULT_PORT = 28789;
	public static final int DEFAULT_TIMEOUT = 2000;
	public static final String PROTOCOL_MESSAGE_DELIMITER = ":";
	public static final String PROTOCOL_MESSAGE_CLIENT_HELLO = "CLIENT_HELLO";
	public static final String PROTOCOL_MESSAGE_SERVER_HELLO = "SERVER_HELLO";
	public static final String PROTOCOL_MESSAGE_UPDATE = "UPDATE";
	public static final String PROTOCOL_MESSAGE_UPDATE_REQUEST_ALIVE = PROTOCOL_MESSAGE_UPDATE + "_ALIVE";
	public static final String PROTOCOL_MESSAGE_CLIENT_ALIVE = "CLIENT_ALIVE";
	private static final int REQUEST_ALIVE_INTERVAL = 100;

	private int port = DEFAULT_PORT;
	private int timeout = DEFAULT_TIMEOUT;
	private DatagramSocket serverSocket;
	private InetAddress clientIPAddress;

	public ServerOutputThread(final Main main, final Input input) {
		super(main, input);
	}

	private void deInit() {
		if (serverSocket != null)
			serverSocket.close();

		SwingUtilities.invokeLater(() -> {
			if (ServerOutputThread.this.isAlive()) {
				main.setStatusBarText(rb.getString("STATUS_SOCKET_CLOSED"));
				main.stopAll();
			}
		});
	}

	@Override
	Logger getLogger() {
		return log;
	}

	public int getPort() {
		return port;
	}

	public int getTimeout() {
		return timeout;
	}

	@Override
	public void run() {
		logStart();

		final var clientPort = port + 1;
		var serverState = ServerState.Listening;
		DatagramPacket receivePacket;
		String message;
		var counter = 0L;

		try {
			serverSocket = new DatagramSocket(port);
			final var receiveBuf = new byte[1024];

			SwingUtilities.invokeLater(() -> {
				main.setStatusBarText(MessageFormat.format(rb.getString("STATUS_LISTENING"), port));
			});

			while (!Thread.currentThread().isInterrupted())
				switch (serverState) {
				case Listening:
					counter = 0;
					receivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);
					serverSocket.setSoTimeout(0);
					serverSocket.receive(receivePacket);
					clientIPAddress = receivePacket.getAddress();
					message = new String(receivePacket.getData(), 0, receivePacket.getLength(),
							StandardCharsets.US_ASCII);

					if (message.startsWith(PROTOCOL_MESSAGE_CLIENT_HELLO)) {
						final var messageParts = message.split(PROTOCOL_MESSAGE_DELIMITER);

						if (messageParts.length == 4) {
							minAxisValue = Integer.parseInt(messageParts[1]);
							maxAxisValue = Integer.parseInt(messageParts[2]);
							setnButtons(Integer.parseInt(messageParts[3]));

							final var sw = new StringWriter();
							sw.append(PROTOCOL_MESSAGE_SERVER_HELLO);
							sw.append(PROTOCOL_MESSAGE_DELIMITER);
							sw.append(VersionUtils.getMajorAndMinorVersion());
							sw.append(PROTOCOL_MESSAGE_DELIMITER);
							sw.append(String.valueOf(pollInterval));

							final var sendBuf = sw.toString().getBytes("ASCII");
							final var sendPacket = new DatagramPacket(sendBuf, sendBuf.length, clientIPAddress,
									clientPort);
							serverSocket.send(sendPacket);

							serverState = ServerState.Connected;
							SwingUtilities.invokeLater(() -> {
								main.setStatusBarText(MessageFormat.format(rb.getString("STATUS_CONNECTED_TO"),
										clientIPAddress.getCanonicalHostName(), clientPort, pollInterval));
							});
						}
					}
					break;
				case Connected:
					Thread.sleep(pollInterval);

					final var sw = new StringWriter();
					var doAliveCheck = false;
					if (counter % REQUEST_ALIVE_INTERVAL == 0) {
						sw.append(PROTOCOL_MESSAGE_UPDATE_REQUEST_ALIVE);
						doAliveCheck = true;
					} else
						sw.append(PROTOCOL_MESSAGE_UPDATE);
					sw.append(PROTOCOL_MESSAGE_DELIMITER + counter);

					if (!input.poll())
						controllerDisconnected();
					else {
						for (final var v : input.getAxes().values())
							sw.append(PROTOCOL_MESSAGE_DELIMITER + v);

						for (var i = 0; i < nButtons; i++) {
							sw.append(PROTOCOL_MESSAGE_DELIMITER + input.getButtons()[i]);
							input.getButtons()[i] = false;
						}

						sw.append(PROTOCOL_MESSAGE_DELIMITER + input.getCursorDeltaX() + PROTOCOL_MESSAGE_DELIMITER
								+ input.getCursorDeltaY());
						input.setCursorDeltaX(0);
						input.setCursorDeltaY(0);

						sw.append(PROTOCOL_MESSAGE_DELIMITER + input.getDownMouseButtons().size());
						for (final var b : input.getDownMouseButtons())
							sw.append(PROTOCOL_MESSAGE_DELIMITER + b);

						sw.append(PROTOCOL_MESSAGE_DELIMITER + input.getDownUpMouseButtons().size());
						for (final var b : input.getDownUpMouseButtons())
							sw.append(PROTOCOL_MESSAGE_DELIMITER + b);
						input.getDownUpMouseButtons().clear();

						sw.append(PROTOCOL_MESSAGE_DELIMITER + input.getDownKeyStrokes().size());
						for (final var keyStroke : input.getDownKeyStrokes()) {
							sw.append(PROTOCOL_MESSAGE_DELIMITER + keyStroke.getModifierCodes().length);
							for (final var c : keyStroke.getModifierCodes())
								sw.append(PROTOCOL_MESSAGE_DELIMITER + c);

							sw.append(PROTOCOL_MESSAGE_DELIMITER + keyStroke.getKeyCodes().length);
							for (final var c : keyStroke.getKeyCodes())
								sw.append(PROTOCOL_MESSAGE_DELIMITER + c);
						}

						sw.append(PROTOCOL_MESSAGE_DELIMITER + input.getDownUpKeyStrokes().size());
						for (final var keyStroke : input.getDownUpKeyStrokes()) {
							sw.append(PROTOCOL_MESSAGE_DELIMITER + keyStroke.getModifierCodes().length);
							for (final var c : keyStroke.getModifierCodes())
								sw.append(PROTOCOL_MESSAGE_DELIMITER + c);

							sw.append(PROTOCOL_MESSAGE_DELIMITER + keyStroke.getKeyCodes().length);
							for (final var c : keyStroke.getKeyCodes())
								sw.append(PROTOCOL_MESSAGE_DELIMITER + c);
						}
						input.getDownUpKeyStrokes().clear();

						sw.append(PROTOCOL_MESSAGE_DELIMITER + input.getScrollClicks());
						input.setScrollClicks(0);

						sw.append(PROTOCOL_MESSAGE_DELIMITER + input.getOnLockKeys().size());
						for (final var c : input.getOnLockKeys())
							sw.append(PROTOCOL_MESSAGE_DELIMITER + c);
						input.getOnLockKeys().clear();

						sw.append(PROTOCOL_MESSAGE_DELIMITER + input.getOffLockKeys().size());
						for (final var c : input.getOffLockKeys())
							sw.append(PROTOCOL_MESSAGE_DELIMITER + c);
						input.getOffLockKeys().clear();

						final var sendBuf = sw.toString().getBytes("ASCII");

						final var sendPacket = new DatagramPacket(sendBuf, sendBuf.length, clientIPAddress, clientPort);
						serverSocket.send(sendPacket);

						if (doAliveCheck) {
							receivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);
							serverSocket.setSoTimeout(timeout);
							try {
								serverSocket.receive(receivePacket);

								if (clientIPAddress.equals(receivePacket.getAddress())) {
									message = new String(receivePacket.getData(), 0, receivePacket.getLength(),
											StandardCharsets.US_ASCII);

									if (PROTOCOL_MESSAGE_CLIENT_ALIVE.equals(message))
										counter++;
								}
							} catch (final SocketTimeoutException e) {
								serverState = ServerState.Listening;
								main.scheduleStatusBarText(
										MessageFormat.format(rb.getString("STATUS_LISTENING"), port));
							}
						} else
							counter++;
					}

					break;
				}
		} catch (final BindException e) {
			log.log(Level.WARNING, "Could not bind socket on port " + port);
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(main.getFrame(),
						MessageFormat.format(rb.getString("COULD_NOT_OPEN_SOCKET_DIALOG_TEXT"), port),
						rb.getString("ERROR_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);
			});
		} catch (final SocketException e) {
			log.log(Level.FINE, e.getMessage(), e);
		} catch (final IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(main.getFrame(), rb.getString("GENERAL_INPUT_OUTPUT_ERROR_DIALOG_TEXT"),
						rb.getString("ERROR_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);
			});
		} catch (final InterruptedException e) {
		} finally {
			deInit();
		}

		logStop();
	}

	public void setPort(final int port) {
		this.port = port;
	}

	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	@Override
	public void stopOutput() {
		super.stopOutput();
		serverSocket.close();
	}

}
