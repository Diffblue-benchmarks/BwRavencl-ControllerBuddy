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

package de.bwravencl.controllerbuddy.gui;

import static de.bwravencl.controllerbuddy.gui.GuiUtils.invokeOnEventDispatchThreadIfRequired;
import static de.bwravencl.controllerbuddy.gui.GuiUtils.loadFrameLocation;
import static de.bwravencl.controllerbuddy.gui.GuiUtils.makeWindowTopmost;
import static de.bwravencl.controllerbuddy.gui.GuiUtils.setEnabledRecursive;
import static de.bwravencl.controllerbuddy.gui.GuiUtils.usingOceanTheme;
import static org.lwjgl.glfw.GLFW.GLFW_CONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_DISCONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetGamepadName;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwJoystickIsGamepad;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;
import static org.lwjgl.glfw.GLFW.glfwSetJoystickCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.text.DefaultFormatter;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lwjgl.glfw.GLFWJoystickCallback;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.oracle.si.Singleton;
import com.oracle.si.Singleton.SingletonApp;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.WinDef.UINT;

import de.bwravencl.controllerbuddy.gui.GuiUtils.FrameDragListener;
import de.bwravencl.controllerbuddy.input.Input;
import de.bwravencl.controllerbuddy.input.Input.VirtualAxis;
import de.bwravencl.controllerbuddy.input.Mode;
import de.bwravencl.controllerbuddy.input.OverlayAxis;
import de.bwravencl.controllerbuddy.input.Profile;
import de.bwravencl.controllerbuddy.input.action.AxisToRelativeAxisAction;
import de.bwravencl.controllerbuddy.input.action.IAction;
import de.bwravencl.controllerbuddy.json.ActionTypeAdapter;
import de.bwravencl.controllerbuddy.json.ModeAwareTypeAdapterFactory;
import de.bwravencl.controllerbuddy.output.ClientVJoyOutputThread;
import de.bwravencl.controllerbuddy.output.LocalVJoyOutputThread;
import de.bwravencl.controllerbuddy.output.OutputThread;
import de.bwravencl.controllerbuddy.output.ServerOutputThread;
import de.bwravencl.controllerbuddy.output.VJoyOutputThread;
import de.bwravencl.controllerbuddy.util.ResourceBundleUtil;
import de.bwravencl.controllerbuddy.version.Version;
import de.bwravencl.controllerbuddy.version.VersionUtils;

public final class Main implements SingletonApp {

	private final class AddModeAction extends AbstractAction {

		private static final long serialVersionUID = -4881923833724315489L;

		private AddModeAction() {
			putValue(NAME, rb.getString("ADD_MODE_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("ADD_MODE_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			final var mode = new Mode();
			input.getProfile().getModes().add(mode);

			setUnsavedChanges(true);
			updateModesPanel();
		}

	}

	private final class ChangeVJoyDirectoryAction extends AbstractAction {

		private static final long serialVersionUID = -7672382299595684105L;

		private ChangeVJoyDirectoryAction() {
			putValue(NAME, "...");
			putValue(SHORT_DESCRIPTION, rb.getString("CHANGE_VJOY_DIRECTORY_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			final var vJoyDirectoryFileChooser = new JFileChooser(
					preferences.get(PREFERENCES_VJOY_DIRECTORY, VJoyOutputThread.getDefaultInstallationPath()));
			vJoyDirectoryFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			if (vJoyDirectoryFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				final var vjoyDirectory = vJoyDirectoryFileChooser.getSelectedFile();
				final var dllFile = new File(vjoyDirectory,
						VJoyOutputThread.getArchFolderName() + File.separator + VJoyOutputThread.LIBRARY_FILENAME);
				if (dllFile.exists()) {
					final var vjoyPath = vjoyDirectory.getAbsolutePath();
					preferences.put(PREFERENCES_VJOY_DIRECTORY, vjoyPath);
					vJoyDirectoryLabel1.setText(vjoyPath);
				} else
					JOptionPane.showMessageDialog(frame,
							MessageFormat.format(rb.getString("INVALID_VJOY_DIRECTORY_DIALOG_TEXT"),
									VJoyOutputThread.getDefaultInstallationPath()),
							rb.getString("ERROR_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private final class DisplayIndicatorAction extends AbstractAction {

		private static final long serialVersionUID = 3316770144012465987L;

		private final VirtualAxis virtualAxis;

		private DisplayIndicatorAction(final VirtualAxis virtualAxis) {
			this.virtualAxis = virtualAxis;

			putValue(NAME, rb.getString("DISPLAY_INDICATOR_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("DISPLAY_INDICATOR_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			if (((JCheckBox) e.getSource()).isSelected())
				input.getProfile().getVirtualAxisToOverlayAxisMap().put(virtualAxis, new OverlayAxis());
			else
				input.getProfile().getVirtualAxisToOverlayAxisMap().remove(virtualAxis);

			setUnsavedChanges(true);
			updateOverlayPanel();
		}

	}

	private final class InvertIndicatorAction extends AbstractAction {

		private static final long serialVersionUID = 3316770144012465987L;

		private final VirtualAxis virtualAxis;

		private InvertIndicatorAction(final VirtualAxis virtualAxis) {
			this.virtualAxis = virtualAxis;

			putValue(NAME, rb.getString("INVERT_INDICATOR_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("INVERT_INDICATOR_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			input.getProfile().getVirtualAxisToOverlayAxisMap().get(virtualAxis).inverted = ((JCheckBox) e.getSource())
					.isSelected();

			setUnsavedChanges(true);
			updateOverlayPanel();
		}

	}

	private final class NewAction extends AbstractAction {

		private static final long serialVersionUID = 5703987691203427504L;

		private NewAction() {
			putValue(NAME, rb.getString("NEW_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("NEW_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			newProfile();
		}

	}

	private final class OpenAction extends AbstractAction {

		private static final long serialVersionUID = -8932510785275935297L;

		private OpenAction() {
			putValue(NAME, rb.getString("OPEN_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("OPEN_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
				loadProfile(fileChooser.getSelectedFile());
		}

	}

	private enum OutputType {
		NONE, LOCAL, CLIENT, SERVER
	}

	private static final class ProfileFileChooser extends JFileChooser {

		private static final long serialVersionUID = -4669170626378955605L;

		private ProfileFileChooser() {
			setFileFilter(
					new FileNameExtensionFilter(rb.getString("PROFILE_FILE_DESCRIPTION"), PROFILE_FILE_EXTENSION));
			setSelectedFile(new File(PROFILE_FILE_SUFFIX));
		}

		@Override
		public void approveSelection() {
			final var file = getSelectedFile();
			if (file.exists() && getDialogType() == SAVE_DIALOG) {
				final int result = JOptionPane.showConfirmDialog(this,
						MessageFormat.format(file.getName(), rb.getString("FILE_EXISTS_DIALOG_TEXT")),
						rb.getString("FILE_EXISTS_DIALOG_TITLE"), JOptionPane.YES_NO_CANCEL_OPTION);
				switch (result) {
				case JOptionPane.NO_OPTION:
					return;
				case JOptionPane.CLOSED_OPTION:
					return;
				case JOptionPane.CANCEL_OPTION:
					cancelSelection();
					return;
				default:
					break;
				}
			}
			super.approveSelection();
		}
	}

	private final class QuitAction extends AbstractAction {

		private static final long serialVersionUID = 8952460723177800923L;

		private QuitAction() {
			putValue(NAME, rb.getString("QUIT_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("QUIT_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			quit();
		}

	}

	private final class RemoveModeAction extends AbstractAction {

		private static final long serialVersionUID = -1056071724769862582L;

		private final Mode mode;

		private RemoveModeAction(final Mode mode) {
			this.mode = mode;

			putValue(NAME, rb.getString("REMOVE_MODE_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION,
					MessageFormat.format(rb.getString("REMOVE_MODE_ACTION_DESCRIPTION"), mode.getDescription()));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			input.getProfile().removeMode(input, mode);
			setUnsavedChanges(true);
			updateModesPanel();
		}

	}

	private final class SaveAction extends AbstractAction {

		private static final long serialVersionUID = -8469921697479550983L;

		private SaveAction() {
			putValue(NAME, rb.getString("SAVE_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("SAVE_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			if (currentFile != null)
				saveProfile(currentFile);
			else
				saveProfileAs();
		}

	}

	private final class SaveAsAction extends AbstractAction {

		private static final long serialVersionUID = -8469921697479550983L;

		private SaveAsAction() {
			putValue(NAME, rb.getString("SAVE_AS_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("SAVE_AS_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			saveProfileAs();
		}

	}

	private final class SelectControllerAction extends AbstractAction {

		private static final long serialVersionUID = -2043467156713598592L;

		private final int jid;

		private SelectControllerAction(final int jid) {
			this.jid = jid;

			final var name = glfwGetGamepadName(jid);
			putValue(NAME, name);
			putValue(SHORT_DESCRIPTION,
					MessageFormat.format(rb.getString("SELECT_CONTROLLER_ACTION_DESCRIPTION"), name));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			setSelectedJid(jid);
		}

	}

	private final class SelectIndicatorColorAction extends AbstractAction {

		private static final long serialVersionUID = 3316770144012465987L;

		private final VirtualAxis virtualAxis;

		private SelectIndicatorColorAction(final VirtualAxis virtualAxis) {
			this.virtualAxis = virtualAxis;

			putValue(NAME, rb.getString("CHANGE_INDICATOR_COLOR_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("CHANGE_INDICATOR_COLOR_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			final var overlayAxis = input.getProfile().getVirtualAxisToOverlayAxisMap().get(virtualAxis);

			final var newColor = JColorChooser.showDialog(frame, "Choose Background Color", overlayAxis.color);
			if (newColor != null)
				overlayAxis.color = newColor;

			setUnsavedChanges(true);
			updateOverlayPanel();
		}

	}

	private final class SetHostAction extends AbstractAction implements FocusListener {

		private static final long serialVersionUID = -7674562782751876814L;

		private final JTextField hostTextField;

		private SetHostAction(final JTextField hostTextField) {
			this.hostTextField = hostTextField;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			setHost();
		}

		@Override
		public void focusGained(final FocusEvent e) {
		}

		@Override
		public void focusLost(final FocusEvent e) {
			setHost();
		}

		private void setHost() {
			final var host = hostTextField.getText();

			if (host != null && host.length() > 0)
				preferences.put(PREFERENCES_HOST, host);
			else
				hostTextField.setText(preferences.get(PREFERENCES_HOST, ClientVJoyOutputThread.DEFAULT_HOST));
		}

	}

	private final class SetModeDescriptionAction extends AbstractAction implements DocumentListener {

		private static final long serialVersionUID = -6706537047137827688L;

		private final Mode mode;
		private final JTextField modeDescriptionTextField;

		private SetModeDescriptionAction(final Mode mode, final JTextField modeDescriptionTextField) {
			this.mode = mode;
			this.modeDescriptionTextField = modeDescriptionTextField;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			setModeDescription();
		}

		@Override
		public void changedUpdate(final DocumentEvent e) {
			setModeDescription();
		}

		@Override
		public void insertUpdate(final DocumentEvent e) {
			setModeDescription();
		}

		@Override
		public void removeUpdate(final DocumentEvent e) {
			setModeDescription();
		}

		private void setModeDescription() {
			final var description = modeDescriptionTextField.getText();

			if (description != null && description.length() > 0) {
				mode.setDescription(description);
				setUnsavedChanges(true);
			}
		}

	}

	private final class ShowAboutDialogAction extends AbstractAction {

		private static final long serialVersionUID = -2578971543384483382L;

		private ShowAboutDialogAction() {
			putValue(NAME, rb.getString("SHOW_ABOUT_DIALOG_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("SHOW_ABOUT_DIALOG_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			final var icon = new ImageIcon(Main.class.getResource(Main.ICON_RESOURCE_PATHS[2]));
			JOptionPane.showMessageDialog(frame,
					MessageFormat.format(rb.getString("ABOUT_DIALOG_TEXT"), Version.VERSION), (String) getValue(NAME),
					JOptionPane.INFORMATION_MESSAGE, icon);
		}

	}

	private final class ShowAction extends AbstractAction {

		private static final long serialVersionUID = 8578159622754054457L;

		private ShowAction() {
			putValue(NAME, rb.getString("SHOW_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("SHOW_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			final var openEvent = new WindowEvent(frame, WindowEvent.WINDOW_OPENED);
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(openEvent);
			frame.setVisible(true);
			frame.setExtendedState(Frame.NORMAL);
		}

	}

	private final class ShowLicensesAction extends AbstractAction {

		private static final long serialVersionUID = 2471952794110895043L;

		private ShowLicensesAction() {
			putValue(NAME, rb.getString("SHOW_LICENSES_DIALOG_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("SHOW_LICENSES_DIALOG_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			try (final var bufferedReader = new BufferedReader(new InputStreamReader(
					ClassLoader.getSystemResourceAsStream(Main.LICENSES_FILENAME), StandardCharsets.UTF_8))) {
				final var text = bufferedReader.lines().collect(Collectors.joining("\n"));
				final var textArea = new JTextArea(text);
				textArea.setLineWrap(true);
				textArea.setEditable(false);
				final var scrollPane = new JScrollPane(textArea);
				scrollPane.setPreferredSize(new Dimension(600, 400));
				JOptionPane.showMessageDialog(frame, scrollPane, (String) getValue(NAME), JOptionPane.DEFAULT_OPTION);
			} catch (final IOException e1) {
				log.log(Level.SEVERE, e1.getMessage(), e1);
			}
		}

	}

	private final class StartClientAction extends AbstractAction {

		private static final long serialVersionUID = 3975574941559749481L;

		private StartClientAction() {
			putValue(NAME, rb.getString("START_CLIENT_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("START_CLIENT_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			startClient();
		}

	}

	private final class StartLocalAction extends AbstractAction {

		private static final long serialVersionUID = -2003502124995392039L;

		private StartLocalAction() {
			putValue(NAME, rb.getString("START_LOCAL_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("START_LOCAL_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			startLocal();
		}

	}

	private final class StartServerAction extends AbstractAction {

		private static final long serialVersionUID = 1758447420975631146L;

		private StartServerAction() {
			putValue(NAME, rb.getString("START_SERVER_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("START_SERVER_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			startServer();
		}

	}

	private final class StopClientAction extends AbstractAction {

		private static final long serialVersionUID = -2863419586328503426L;

		private StopClientAction() {
			putValue(NAME, rb.getString("STOP_CLIENT_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("STOP_CLIENT_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			stopClient(true);
		}

	}

	private final class StopLocalAction extends AbstractAction {

		private static final long serialVersionUID = -4859431944733030332L;

		private StopLocalAction() {
			putValue(NAME, rb.getString("STOP_LOCAL_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("STOP_LOCAL_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			stopLocal(true);
		}

	}

	private final class StopServerAction extends AbstractAction {

		private static final long serialVersionUID = 6023207463370122769L;

		private StopServerAction() {
			putValue(NAME, rb.getString("STOP_SERVER_ACTION_NAME"));
			putValue(SHORT_DESCRIPTION, rb.getString("STOP_SERVER_ACTION_DESCRIPTION"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			stopServer(true);
		}

	}

	private static final Options options = new Options();
	private static final String SINGLETON_ID;
	private static final Logger log = Logger.getLogger(Main.class.getName());
	public static final boolean windows = Platform.isWindows() && !Platform.isWindowsCE();
	public static final String STRING_RESOURCE_BUNDLE_BASENAME = "strings";
	private static final ResourceBundle rb = new ResourceBundleUtil().getResourceBundle(STRING_RESOURCE_BUNDLE_BASENAME,
			Locale.getDefault());
	private static String PROFILE_FILE_EXTENSION = "json";
	private static String PROFILE_FILE_SUFFIX = "." + PROFILE_FILE_EXTENSION;
	static final int DIALOG_BOUNDS_X = 100;
	static final int DIALOG_BOUNDS_Y = 100;
	static final int DIALOG_BOUNDS_WIDTH = 930;
	static final int DIALOG_BOUNDS_HEIGHT = 640;
	static final int DIALOG_BOUNDS_X_Y_OFFSET = 25;
	static final Dimension BUTTON_DIMENSION = new Dimension(110, 25);
	private static final String OPTION_AUTOSTART = "autostart";
	private static final String OPTION_PROFILE = "profile";
	private static final String OPTION_TRAY = "tray";
	private static final String OPTION_VERSION = "version";
	private static final String OPTION_AUTOSTART_VALUE_LOCAL = "local";
	private static final String OPTION_AUTOSTART_VALUE_CLIENT = "client";
	private static final String OPTION_AUTOSTART_VALUE_SERVER = "server";
	private static final String PREFERENCES_POLL_INTERVAL = "poll_interval";
	private static final String PREFERENCES_LAST_CONTROLLER = "last_controller";
	private static final String PREFERENCES_LAST_PROFILE = "last_profile";
	public static final String PREFERENCES_VJOY_DIRECTORY = "vjoy_directory";
	private static final String PREFERENCES_VJOY_DEVICE = "vjoy_device";
	private static final String PREFERENCES_HOST = "host";
	private static final String PREFERENCES_PORT = "port";
	private static final String PREFERENCES_TIMEOUT = "timeout";
	private static final String PREFERENCES_DARK_THEME = "dark_theme";
	private static final String PREFERENCES_SHOW_OVERLAY = "show_overlay";
	private static final String PREFERENCES_SHOW_VR_OVERLAY = "show_vr_overlay";
	private static final String PREFERENCES_PREVENT_POWER_SAVE_MODE = "prevent_power_save_mode";
	private static final long OVERLAY_POSITION_UPDATE_INTERVAL = 10000L;
	private static final String[] ICON_RESOURCE_PATHS = { "/icon_16.png", "/icon_32.png", "/icon_64.png",
			"/icon_128.png" };
	private static final String LICENSES_FILENAME = "licenses.txt";
	static final Color TRANSPARENT = new Color(255, 255, 255, 0);
	private static final int INVALID_JID = GLFW_JOYSTICK_1 - 1;

	static {
		options.addOption(OPTION_AUTOSTART, true, MessageFormat.format(rb.getString("AUTOSTART_OPTION_DESCRIPTION"),
				Main.windows ? rb.getString("LOCAL_FEEDER_OR_CLIENT_OR_SERVER") : rb.getString("SERVER")));
		options.addOption(OPTION_PROFILE, true, rb.getString("PROFILE_OPTION_DESCRIPTION"));
		options.addOption(OPTION_TRAY, false, rb.getString("TRAY_OPTION_DESCRIPTION"));
		options.addOption(OPTION_VERSION, false, rb.getString("VERSION_OPTION_DESCRIPTION"));

		final var mainClassPackageName = Main.class.getPackageName();
		SINGLETON_ID = mainClassPackageName.substring(0, mainClassPackageName.lastIndexOf('.'));

		try {
			UIManager.setLookAndFeel(new MetalLookAndFeel());
		} catch (final UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}
	}

	private static String assembleControllerLoggingMessage(final String prefix, final int jid) {
		return prefix + " controller " + glfwGetGamepadName(jid) + " (" + jid + ")" + " [" + glfwGetJoystickGUID(jid)
				+ "]";
	}

	private static int getExtendedKeyCodeForMenu(final AbstractButton button,
			final Set<Integer> alreadyAssignedKeyCodes) {
		var keyCode = KeyEvent.VK_UNDEFINED;

		final var text = button.getText();
		if (text != null && text.length() > 0) {
			var index = 0;
			do {
				keyCode = KeyEvent.getExtendedKeyCodeForChar(text.charAt(index));
				index++;
			} while (keyCode != KeyEvent.VK_UNDEFINED && !alreadyAssignedKeyCodes.add(keyCode));
		}

		return keyCode;
	}

	private static int getExtendedKeyCodeForMenuItem(final AbstractButton button) {
		final var action = button.getAction();
		if (action != null)
			if (action instanceof NewAction)
				return KeyEvent.VK_N;
			else if (action instanceof OpenAction)
				return KeyEvent.VK_O;
			else if (action instanceof SaveAction)
				return KeyEvent.VK_S;
			else if (action instanceof StartLocalAction || action instanceof StopLocalAction)
				return KeyEvent.VK_L;
			else if (action instanceof StartClientAction || action instanceof StopClientAction)
				return KeyEvent.VK_C;
			else if (action instanceof StartServerAction || action instanceof StopServerAction)
				return KeyEvent.VK_E;

		return KeyEvent.VK_UNDEFINED;
	}

	private static void handleUncaughtException(final Throwable e, final Component parentComponent) {
		log.log(Level.SEVERE, e.getMessage(), e);

		if (parentComponent != null)
			GuiUtils.invokeOnEventDispatchThreadIfRequired(() -> {
				final var sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));

				JOptionPane.showMessageDialog(parentComponent,
						MessageFormat.format(rb.getString("UNCAUGHT_EXCEPTION_DIALOG_TEXT"), sw.toString()),
						rb.getString("ERROR_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);

				terminate(1);
			});
		else
			terminate(1);
	}

	private static boolean isModalDialogShowing() {
		final var windows = Window.getWindows();
		if (windows != null)
			for (final Window w : windows)
				if (w.isShowing() && w instanceof Dialog && ((Dialog) w).isModal())
					return true;

		return false;
	}

	public static void main(final String[] args) {
		if (!Singleton.invoke(SINGLETON_ID, args)) {
			Thread.setDefaultUncaughtExceptionHandler((t, e) -> handleUncaughtException(e, null));

			log.log(Level.INFO, "Launching " + rb.getString("APPLICATION_NAME") + " " + Version.VERSION);

			SwingUtilities.invokeLater(() -> {
				try {
					final var commandLine = new DefaultParser().parse(options, args);
					if (commandLine.hasOption(OPTION_VERSION))
						System.out.println(rb.getString("APPLICATION_NAME") + " " + Version.VERSION);
					else {
						final var cmdProfilePath = commandLine.getOptionValue(OPTION_PROFILE);
						final var main = new Main(cmdProfilePath);

						if (!commandLine.hasOption(OPTION_TRAY))
							main.frame.setVisible(true);
						if (commandLine.hasOption(OPTION_AUTOSTART)) {
							final var autostartOption = commandLine.getOptionValue(OPTION_AUTOSTART);

							if (Main.windows)
								if (OPTION_AUTOSTART_VALUE_LOCAL.equals(autostartOption)) {
									main.startLocal();
									return;
								} else if (OPTION_AUTOSTART_VALUE_CLIENT.equals(autostartOption)) {
									main.startClient();
									return;
								}
							if (OPTION_AUTOSTART_VALUE_SERVER.equals(autostartOption))
								main.startServer();
							else
								JOptionPane.showMessageDialog(main.frame,
										MessageFormat
												.format(rb.getString("INVALID_VALUE_FOR_OPTION_AUTOSTART_DIALOG_TEXT"),
														OPTION_AUTOSTART, autostartOption,
														MessageFormat.format(
																Main.windows
																		? rb.getString(
																				"LOCAL_FEEDER_OR_CLIENT_OR_SERVER")
																		: rb.getString("SERVER"),
																rb.getString("ERROR_DIALOG_TITLE"),
																JOptionPane.ERROR_MESSAGE)));
						}
					}
				} catch (final ParseException e) {
					final var helpFormatter = new HelpFormatter();
					helpFormatter.printHelp(rb.getString("APPLICATION_NAME"), options, true);
				}
			});
		}
	}

	private static void terminate(final int status) {
		log.log(Level.INFO, "Terminated (" + status + ")");
		System.exit(status);
	}

	private static void waitForThreadToFinish(final Thread thread) {
		while (thread != null && thread.isAlive())
			try {
				Thread.sleep(100L);
			} catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
			}
	}

	private final Preferences preferences = Preferences.userRoot().node("/" + SINGLETON_ID.replace('.', '/'));
	private final Map<VirtualAxis, JProgressBar> virtualAxisToProgressBarMap = new HashMap<>();
	private volatile LocalVJoyOutputThread localThread;
	private volatile ClientVJoyOutputThread clientThread;
	private volatile ServerOutputThread serverThread;
	private int selectedJid = INVALID_JID;
	private Input input;
	private OutputType lastOutputType = OutputType.NONE;
	private final JFrame frame;
	private final OpenAction openAction = new OpenAction();
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu fileMenu = new JMenu(rb.getString("FILE_MENU"));
	private final JMenu deviceMenu = new JMenu(rb.getString("DEVICE_MENU"));
	private final JMenu localMenu = new JMenu(rb.getString("LOCAL_MENU"));
	private final JMenu clientMenu = new JMenu(rb.getString("CLIENT_MENU"));
	private final JMenu serverMenu = new JMenu(rb.getString("SERVER_MENU"));
	private final JMenuItem newMenuItem = fileMenu.add(new NewAction());
	private final JMenuItem openMenuItem = fileMenu.add(openAction);
	private final JMenuItem saveMenuItem = fileMenu.add(new SaveAction());
	private final JMenuItem saveAsMenuItem = fileMenu.add(new SaveAsAction());
	private JRadioButtonMenuItem startLocalRadioButtonMenuItem;
	private JRadioButtonMenuItem stopLocalRadioButtonMenuItem;
	private JRadioButtonMenuItem startClientRadioButtonMenuItem;
	private JRadioButtonMenuItem stopClientRadioButtonMenuItem;
	private final JRadioButtonMenuItem startServerRadioButtonMenuItem;
	private final JRadioButtonMenuItem stopServerRadioButtonMenuItem;
	private MenuItem showMenuItem;
	private final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
	private JPanel modesPanel;
	private JScrollPane modesScrollPane;
	private JPanel modesListPanel;
	private JPanel addModePanel;
	private JPanel overlayPanel;
	private AssignmentsComponent assignmentsComponent;
	private final JScrollPane settingsScrollPane = new JScrollPane();
	private final JPanel settingsPanel;
	private JScrollPane indicatorsScrollPane;
	private JPanel indicatorsListPanel;
	private TimerTask overlayTimerTask;
	private JLabel vJoyDirectoryLabel1;
	private JTextField hostTextField;
	private final JLabel statusLabel = new JLabel(rb.getString("STATUS_READY"));
	private TrayIcon trayIcon;
	private boolean unsavedChanges = false;
	private String loadedProfile = null;
	private File currentFile;
	private ServerSocket serverSocket;
	private volatile boolean scheduleOnScreenKeyboardModeSwitch;
	private JLabel currentModeLabel;
	private final JFileChooser fileChooser = new ProfileFileChooser();
	private final Timer timer = new Timer();
	private volatile OpenVrOverlay openVrOverlay;
	private FrameDragListener overlayFrameDragListener;
	private FlowLayout indicatorPanelFlowLayout;
	private JPanel indicatorPanel;
	private Rectangle prevMaxWindowBounds;
	private volatile JFrame overlayFrame;

	private final OnScreenKeyboard onScreenKeyboard = new OnScreenKeyboard(this);

	private Main(final String cmdProfilePath) {
		Singleton.start(this, SINGLETON_ID);

		frame = new JFrame();

		Thread.setDefaultUncaughtExceptionHandler((t, e) -> handleUncaughtException(e, frame));

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent e) {
				super.windowClosing(e);

				if (showMenuItem != null)
					showMenuItem.setEnabled(true);
			}

			@Override
			public void windowDeiconified(final WindowEvent e) {
				super.windowDeiconified(e);

				if (showMenuItem != null)
					showMenuItem.setEnabled(false);
			}

			@Override
			public void windowIconified(final WindowEvent e) {
				super.windowIconified(e);

				if (showMenuItem != null)
					showMenuItem.setEnabled(true);
			}

			@Override
			public void windowOpened(final WindowEvent e) {
				super.windowOpened(e);

				if (showMenuItem != null)
					showMenuItem.setEnabled(false);
			}

		});

		frame.setBounds(DIALOG_BOUNDS_X, DIALOG_BOUNDS_Y, DIALOG_BOUNDS_WIDTH, DIALOG_BOUNDS_HEIGHT);
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		final var icons = new ArrayList<Image>();
		for (final var path : ICON_RESOURCE_PATHS) {
			final var icon = new ImageIcon(Main.class.getResource(path));
			icons.add(icon.getImage());
		}
		frame.setIconImages(icons);

		frame.setJMenuBar(menuBar);

		menuBar.add(fileMenu);
		final QuitAction quitAction = new QuitAction();
		fileMenu.add(quitAction);
		menuBar.add(deviceMenu);

		if (windows) {
			menuBar.add(localMenu, 2);

			final var buttonGroupLocalState = new ButtonGroup();
			startLocalRadioButtonMenuItem = new JRadioButtonMenuItem(rb.getString("START_MENU_ITEM"));
			startLocalRadioButtonMenuItem.setAction(new StartLocalAction());
			buttonGroupLocalState.add(startLocalRadioButtonMenuItem);
			localMenu.add(startLocalRadioButtonMenuItem);

			stopLocalRadioButtonMenuItem = new JRadioButtonMenuItem(rb.getString("STOP_MENU_ITEM"));
			stopLocalRadioButtonMenuItem.setAction(new StopLocalAction());
			buttonGroupLocalState.add(stopLocalRadioButtonMenuItem);
			localMenu.add(stopLocalRadioButtonMenuItem);

			menuBar.add(clientMenu);

			final var buttonGroupClientState = new ButtonGroup();

			startClientRadioButtonMenuItem = new JRadioButtonMenuItem(rb.getString("START_MENU_ITEM"));
			startClientRadioButtonMenuItem.setAction(new StartClientAction());
			buttonGroupClientState.add(startClientRadioButtonMenuItem);
			clientMenu.add(startClientRadioButtonMenuItem);

			stopClientRadioButtonMenuItem = new JRadioButtonMenuItem(rb.getString("STOP_MENU_ITEM"));
			stopClientRadioButtonMenuItem.setAction(new StopClientAction());
			buttonGroupClientState.add(stopClientRadioButtonMenuItem);
			clientMenu.add(stopClientRadioButtonMenuItem);
		}

		menuBar.add(serverMenu);

		final var buttonGroupServerState = new ButtonGroup();
		startServerRadioButtonMenuItem = new JRadioButtonMenuItem(rb.getString("START_MENU_ITEM"));
		startServerRadioButtonMenuItem.setAction(new StartServerAction());
		buttonGroupServerState.add(startServerRadioButtonMenuItem);
		serverMenu.add(startServerRadioButtonMenuItem);

		stopServerRadioButtonMenuItem = new JRadioButtonMenuItem(rb.getString("STOP_MENU_ITEM"));
		stopServerRadioButtonMenuItem.setAction(new StopServerAction());
		buttonGroupServerState.add(stopServerRadioButtonMenuItem);
		serverMenu.add(stopServerRadioButtonMenuItem);

		final var helpMenu = new JMenu(rb.getString("HELP_MENU"));
		menuBar.add(helpMenu);
		helpMenu.add(new ShowLicensesAction());
		helpMenu.add(new ShowAboutDialogAction());

		frame.getContentPane().add(tabbedPane);

		settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridBagLayout());

		settingsScrollPane.setViewportView(settingsPanel);
		tabbedPane.addTab(rb.getString("SETTINGS_TAB"), null, settingsScrollPane);

		final var panelGridBagConstraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0d, 0d,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 5);

		final var panelFlowLayout = new FlowLayout(FlowLayout.LEADING, 10, 10);

		final var pollIntervalPanel = new JPanel(panelFlowLayout);
		settingsPanel.add(pollIntervalPanel, panelGridBagConstraints);

		final var pollIntervalLabel = new JLabel(rb.getString("POLL_INTERVAL_LABEL"));
		pollIntervalLabel.setPreferredSize(new Dimension(120, 15));
		pollIntervalPanel.add(pollIntervalLabel);

		final var pollIntervalSpinner = new JSpinner(new SpinnerNumberModel(
				preferences.getInt(PREFERENCES_POLL_INTERVAL, OutputThread.DEFAULT_POLL_INTERVAL), 1, 100, 1));
		final var pollIntervalSpinnerEditor = new JSpinner.NumberEditor(pollIntervalSpinner,
				"# " + rb.getString("MILLISECOND_SYMBOL"));
		((DefaultFormatter) pollIntervalSpinnerEditor.getTextField().getFormatter()).setCommitsOnValidEdit(true);
		pollIntervalSpinner.setEditor(pollIntervalSpinnerEditor);
		pollIntervalSpinner.addChangeListener(
				e -> preferences.putInt(PREFERENCES_POLL_INTERVAL, (int) ((JSpinner) e.getSource()).getValue()));
		pollIntervalPanel.add(pollIntervalSpinner);

		if (windows) {
			final var vJoyDirectoryPanel = new JPanel(panelFlowLayout);
			settingsPanel.add(vJoyDirectoryPanel, panelGridBagConstraints);

			final var vJoyDirectoryLabel = new JLabel(rb.getString("VJOY_DIRECTORY_LABEL"));
			vJoyDirectoryLabel.setPreferredSize(new Dimension(120, 15));
			vJoyDirectoryPanel.add(vJoyDirectoryLabel);

			vJoyDirectoryLabel1 = new JLabel(
					preferences.get(PREFERENCES_VJOY_DIRECTORY, VJoyOutputThread.getDefaultInstallationPath()));
			vJoyDirectoryPanel.add(vJoyDirectoryLabel1);

			final var vJoyDirectoryButton = new JButton(new ChangeVJoyDirectoryAction());
			vJoyDirectoryPanel.add(vJoyDirectoryButton);

			final var vJoyDevicePanel = new JPanel(panelFlowLayout);
			settingsPanel.add(vJoyDevicePanel, panelGridBagConstraints);

			final var vJoyDeviceLabel = new JLabel(rb.getString("VJOY_DEVICE_LABEL"));
			vJoyDeviceLabel.setPreferredSize(new Dimension(120, 15));
			vJoyDevicePanel.add(vJoyDeviceLabel);

			final var vJoyDeviceSpinner = new JSpinner(new SpinnerNumberModel(
					preferences.getInt(PREFERENCES_VJOY_DEVICE, VJoyOutputThread.DEFAULT_VJOY_DEVICE), 1, 16, 1));
			final var vJoyDeviceSpinnerEditor = new JSpinner.NumberEditor(vJoyDeviceSpinner, "#");
			((DefaultFormatter) vJoyDeviceSpinnerEditor.getTextField().getFormatter()).setCommitsOnValidEdit(true);
			vJoyDeviceSpinner.setEditor(vJoyDeviceSpinnerEditor);
			vJoyDeviceSpinner.addChangeListener(
					e -> preferences.putInt(PREFERENCES_VJOY_DEVICE, (int) ((JSpinner) e.getSource()).getValue()));
			vJoyDevicePanel.add(vJoyDeviceSpinner);

			final var hostPanel = new JPanel(panelFlowLayout);
			settingsPanel.add(hostPanel, panelGridBagConstraints);

			final var hostLabel = new JLabel(rb.getString("HOST_LABEL"));
			hostLabel.setPreferredSize(new Dimension(120, 15));
			hostPanel.add(hostLabel);

			hostTextField = new JTextField(preferences.get(PREFERENCES_HOST, ClientVJoyOutputThread.DEFAULT_HOST), 15);
			final var setHostAction = new SetHostAction(hostTextField);
			hostTextField.addActionListener(setHostAction);
			hostTextField.addFocusListener(setHostAction);
			hostPanel.add(hostTextField);
		}

		final var portPanel = new JPanel(panelFlowLayout);
		settingsPanel.add(portPanel, panelGridBagConstraints);

		final var portLabel = new JLabel(rb.getString("PORT_LABEL"));
		portLabel.setPreferredSize(new Dimension(120, 15));
		portPanel.add(portLabel);

		final var portSpinner = new JSpinner(new SpinnerNumberModel(
				preferences.getInt(PREFERENCES_PORT, ServerOutputThread.DEFAULT_PORT), 1024, 65535, 1));
		final var portSpinnerEditor = new JSpinner.NumberEditor(portSpinner, "#");
		((DefaultFormatter) portSpinnerEditor.getTextField().getFormatter()).setCommitsOnValidEdit(true);
		portSpinner.setEditor(portSpinnerEditor);
		portSpinner.addChangeListener(
				e -> preferences.putInt(PREFERENCES_PORT, (int) ((JSpinner) e.getSource()).getValue()));
		portPanel.add(portSpinner);

		final var timeoutPanel = new JPanel(panelFlowLayout);
		settingsPanel.add(timeoutPanel, panelGridBagConstraints);

		final var timeoutLabel = new JLabel(rb.getString("TIMEOUT_LABEL"));
		timeoutLabel.setPreferredSize(new Dimension(120, 15));
		timeoutPanel.add(timeoutLabel);

		final var timeoutSpinner = new JSpinner(new SpinnerNumberModel(
				preferences.getInt(PREFERENCES_TIMEOUT, ServerOutputThread.DEFAULT_TIMEOUT), 10, 60000, 1));
		final var timeoutSpinnerEditor = new JSpinner.NumberEditor(timeoutSpinner,
				"# " + rb.getString("MILLISECOND_SYMBOL"));
		((DefaultFormatter) timeoutSpinnerEditor.getTextField().getFormatter()).setCommitsOnValidEdit(true);
		timeoutSpinner.setEditor(timeoutSpinnerEditor);
		timeoutSpinner.addChangeListener(
				e -> preferences.putInt(PREFERENCES_TIMEOUT, (int) ((JSpinner) e.getSource()).getValue()));
		timeoutPanel.add(timeoutSpinner);

		final var darkThemePanel = new JPanel(panelFlowLayout);
		settingsPanel.add(darkThemePanel, panelGridBagConstraints);

		final var darkThemeLabel = new JLabel(rb.getString("DARK_THEME_LABEL"));
		darkThemeLabel.setPreferredSize(new Dimension(120, 15));
		darkThemePanel.add(darkThemeLabel);

		final var darkThemeCheckBox = new JCheckBox(rb.getString("DARK_THEME_CHECK_BOX"));
		darkThemeCheckBox.setSelected(preferences.getBoolean(PREFERENCES_DARK_THEME, false));
		darkThemeCheckBox.addActionListener(e -> {
			final var darkTheme = ((JCheckBox) e.getSource()).isSelected();
			preferences.putBoolean(PREFERENCES_DARK_THEME, darkTheme);
			updateTheme();
		});
		darkThemePanel.add(darkThemeCheckBox);

		final var alwaysOnTopSupported = Toolkit.getDefaultToolkit().isAlwaysOnTopSupported();
		if (alwaysOnTopSupported || preferences.getBoolean(PREFERENCES_SHOW_OVERLAY, alwaysOnTopSupported)) {
			final var overlaySettingsPanel = new JPanel(panelFlowLayout);
			settingsPanel.add(overlaySettingsPanel, panelGridBagConstraints);

			final var overlayLabel = new JLabel(rb.getString("OVERLAY_LABEL"));
			overlayLabel.setPreferredSize(new Dimension(120, 15));
			overlaySettingsPanel.add(overlayLabel);

			final var showOverlayCheckBox = new JCheckBox(rb.getString("SHOW_OVERLAY_CHECK_BOX"));
			showOverlayCheckBox.setSelected(preferences.getBoolean(PREFERENCES_SHOW_OVERLAY, true));
			showOverlayCheckBox.addActionListener(e -> {
				final var showOverlay = ((JCheckBox) e.getSource()).isSelected();
				preferences.putBoolean(PREFERENCES_SHOW_OVERLAY, showOverlay);
			});
			overlaySettingsPanel.add(showOverlayCheckBox);
		}

		if (windows) {
			if (preferences.getBoolean(PREFERENCES_SHOW_VR_OVERLAY, true)) {
				final var vrOverlaySettingsPanel = new JPanel(panelFlowLayout);
				settingsPanel.add(vrOverlaySettingsPanel, panelGridBagConstraints);

				final var vrOverlayLabel = new JLabel(rb.getString("VR_OVERLAY_LABEL"));
				vrOverlayLabel.setPreferredSize(new Dimension(120, 15));
				vrOverlaySettingsPanel.add(vrOverlayLabel);

				final var showVrOverlayCheckBox = new JCheckBox(rb.getString("SHOW_VR_OVERLAY_CHECK_BOX"));
				showVrOverlayCheckBox.setSelected(preferences.getBoolean(PREFERENCES_SHOW_VR_OVERLAY, true));
				showVrOverlayCheckBox.addActionListener(e -> {
					final var showVrOverlay = ((JCheckBox) e.getSource()).isSelected();
					preferences.putBoolean(PREFERENCES_SHOW_VR_OVERLAY, showVrOverlay);
				});
				vrOverlaySettingsPanel.add(showVrOverlayCheckBox);
			}

			final var preventPowerSaveModeSettingsPanel = new JPanel(panelFlowLayout);
			settingsPanel.add(preventPowerSaveModeSettingsPanel, panelGridBagConstraints);

			final var preventPowerSaveModeLabel = new JLabel(rb.getString("POWER_SAVE_MODE_LABEL"));
			preventPowerSaveModeLabel.setPreferredSize(new Dimension(120, 15));
			preventPowerSaveModeSettingsPanel.add(preventPowerSaveModeLabel);

			final var preventPowerSaveModeCheckBox = new JCheckBox(rb.getString("PREVENT_POWER_SAVE_MODE_CHECK_BOX"));
			preventPowerSaveModeCheckBox.setSelected(preferences.getBoolean(PREFERENCES_PREVENT_POWER_SAVE_MODE, true));
			preventPowerSaveModeCheckBox.addActionListener(e -> {
				final var preventPowerSaveMode = ((JCheckBox) e.getSource()).isSelected();
				preferences.putBoolean(PREFERENCES_PREVENT_POWER_SAVE_MODE, preventPowerSaveMode);
			});
			preventPowerSaveModeSettingsPanel.add(preventPowerSaveModeCheckBox);
		}

		if (SystemTray.isSupported()) {
			final var popupMenu = new PopupMenu();

			final var showAction = new ShowAction();
			showMenuItem = new MenuItem((String) showAction.getValue(Action.NAME));
			showMenuItem.addActionListener(showAction);
			popupMenu.add(showMenuItem);

			popupMenu.addSeparator();

			final var openMenuItem = new MenuItem((String) openAction.getValue(Action.NAME));
			openMenuItem.addActionListener(openAction);
			popupMenu.add(openMenuItem);

			popupMenu.addSeparator();

			final var quitMenuItem = new MenuItem((String) quitAction.getValue(Action.NAME));
			quitMenuItem.addActionListener(quitAction);
			popupMenu.add(quitMenuItem);

			trayIcon = new TrayIcon(frame.getIconImage());
			trayIcon.addActionListener(showAction);
			trayIcon.setPopupMenu(popupMenu);
			try {
				SystemTray.getSystemTray().add(trayIcon);
			} catch (final AWTException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		updateTitleAndTooltip();

		settingsPanel.add(Box.createGlue(), new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1d, 1d,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		final var outsideBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		final var insideBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		statusLabel.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
		frame.add(statusLabel, BorderLayout.SOUTH);

		updateTheme();

		final var glfwInitialized = glfwInit();
		if (!glfwInitialized) {
			log.log(Level.SEVERE, "Could not initialize GLFW");

			if (windows)
				JOptionPane.showMessageDialog(frame, rb.getString("COULD_NOT_INITIALIZE_GLFW_DIALOG_TEXT_WINDOWS"),
						rb.getString("ERROR_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);
			else {
				JOptionPane.showMessageDialog(frame, rb.getString("COULD_NOT_INITIALIZE_GLFW_DIALOG_TEXT"),
						rb.getString("ERROR_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);
				quit();
			}
		}

		final var presentJids = new HashSet<Integer>();
		for (var jid = GLFW_JOYSTICK_1; jid <= GLFW_JOYSTICK_LAST; jid++)
			if (glfwJoystickPresent(jid) && glfwJoystickIsGamepad(jid))
				presentJids.add(jid);

		final var lastControllerGuid = preferences.get(PREFERENCES_LAST_CONTROLLER, null);
		for (final var jid : presentJids) {
			final var lastControllerFound = lastControllerGuid != null
					? lastControllerGuid.equals(glfwGetJoystickGUID(jid))
					: false;

			if (!isSelectedJidValid() || lastControllerFound)
				selectedJid = jid;

			if (lastControllerFound) {
				log.log(Level.INFO, assembleControllerLoggingMessage("Selected previously used", jid));
				break;
			} else
				log.log(Level.INFO, "Previously used controller is not present");
		}

		newProfile();

		onControllersChanged(true);

		glfwSetJoystickCallback(new GLFWJoystickCallback() {

			@Override
			public void invoke(final int jid, final int event) {
				final var disconnected = event == GLFW_DISCONNECTED;
				if (disconnected || glfwJoystickIsGamepad(jid)) {
					if (disconnected) {
						log.log(Level.INFO, assembleControllerLoggingMessage("Disconnected", jid));
						if (selectedJid == jid) {
							selectedJid = INVALID_JID;
							input.deInit();
						}
					} else if (event == GLFW_CONNECTED)
						log.log(Level.INFO, assembleControllerLoggingMessage("Connected", jid));

					invokeOnEventDispatchThreadIfRequired(() -> onControllersChanged(false));
				}
			}
		});

		if (glfwInitialized && presentJids.isEmpty()) {
			if (windows)
				JOptionPane.showMessageDialog(frame, rb.getString("NO_CONTROLLER_CONNECTED_DIALOG_TEXT_WINDOWS"),
						rb.getString("INFORMATION_DIALOG_TITLE"), JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(frame, rb.getString("NO_CONTROLLER_CONNECTED_DIALOG_TEXT"),
						rb.getString("INFORMATION_DIALOG_TITLE"), JOptionPane.INFORMATION_MESSAGE);
		} else {
			final var profilePath = cmdProfilePath != null ? cmdProfilePath
					: preferences.get(PREFERENCES_LAST_PROFILE, null);
			if (profilePath != null) {
				loadProfile(new File(profilePath));
				if (loadedProfile == null && cmdProfilePath == null) {
					log.log(Level.INFO, "Removing " + PREFERENCES_LAST_PROFILE + " from preferences");
					preferences.remove(PREFERENCES_LAST_PROFILE);
				}
			}
		}
	}

	private void deInitOverlay() {
		if (openVrOverlay != null) {
			openVrOverlay.stop();
			openVrOverlay = null;
		}

		if (overlayFrame != null) {
			for (var i = 0; i < 10; i++) {
				overlayFrame.dispose();

				if (!overlayFrame.isDisplayable())
					break;

				try {
					Thread.sleep(100L);
				} catch (final InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

			overlayFrame = null;
		}

		currentModeLabel = null;
		virtualAxisToProgressBarMap.clear();

		onScreenKeyboard.setVisible(false);
	}

	public void displayChargingStateInfo(final boolean charging) {
		if (trayIcon != null && input != null)
			trayIcon.displayMessage(rb.getString("CHARGING_STATE_CAPTION"),
					MessageFormat.format(
							rb.getString(charging ? "CHARGING_STATE_CHARGING" : "CHARGING_STATE_DISCHARGING"),
							input.getBatteryState() / 100f),
					MessageType.INFO);
	}

	public void displayLowBatteryWarning(final float batteryCharge) {
		SwingUtilities.invokeLater(() -> {
			if (trayIcon != null)
				trayIcon.displayMessage(rb.getString("LOW_BATTERY_CAPTION"),
						MessageFormat.format("{0,number,percent}", batteryCharge), MessageType.WARNING);
		});
	}

	public JFrame getFrame() {
		return frame;
	}

	Input getInput() {
		return input;
	}

	public OnScreenKeyboard getOnScreenKeyboard() {
		return onScreenKeyboard;
	}

	JFrame getOverlayFrame() {
		return overlayFrame;
	}

	public Preferences getPreferences() {
		return preferences;
	}

	public Timer getTimer() {
		return timer;
	}

	public void handleOnScreenKeyboardModeChange() {
		if (scheduleOnScreenKeyboardModeSwitch) {
			for (final var buttonToModeActions : input.getProfile().getButtonToModeActionsMap().values())
				for (final var buttonToModeAction : buttonToModeActions)
					if (OnScreenKeyboard.onScreenKeyboardMode.equals(buttonToModeAction.getMode(input))) {
						buttonToModeAction.doAction(input, Byte.MAX_VALUE);
						break;
					}

			scheduleOnScreenKeyboardModeSwitch = false;
		}
	}

	private void initOverlay() {
		if (!preferences.getBoolean(PREFERENCES_SHOW_OVERLAY, Toolkit.getDefaultToolkit().isAlwaysOnTopSupported()))
			return;

		final var modes = input.getProfile().getModes();
		final var multipleModes = modes.size() > 1;
		final var virtualAxisToOverlayAxisMap = input.getProfile().getVirtualAxisToOverlayAxisMap();
		if (!multipleModes && virtualAxisToOverlayAxisMap.isEmpty())
			return;

		var longestDescription = "";
		for (final var mode : modes) {
			final var description = mode.getDescription();
			if (description.length() > longestDescription.length())
				longestDescription = description;
		}

		overlayFrame = new JFrame("Overlay");
		overlayFrame.setType(JFrame.Type.UTILITY);
		overlayFrame.setLayout(new BorderLayout());
		overlayFrame.setFocusableWindowState(false);
		overlayFrame.setUndecorated(true);
		overlayFrame.setBackground(TRANSPARENT);
		overlayFrame.setAlwaysOnTop(true);

		if (multipleModes) {
			currentModeLabel = new JLabel(input.getProfile().getActiveMode().getDescription());
			final var font = currentModeLabel.getFont().deriveFont(Font.BOLD);
			currentModeLabel.setFont(font);
			final var fontMetrics = currentModeLabel.getFontMetrics(font);
			currentModeLabel.setPreferredSize(
					new Dimension(fontMetrics.stringWidth(longestDescription), fontMetrics.getHeight()));
			currentModeLabel.setForeground(Color.RED);
			overlayFrame.add(currentModeLabel, BorderLayout.PAGE_END);
		}

		indicatorPanelFlowLayout = new FlowLayout();
		indicatorPanel = new JPanel(indicatorPanelFlowLayout);
		indicatorPanel.setBackground(TRANSPARENT);

		for (final var virtualAxis : Input.VirtualAxis.values()) {
			final var overlayAxis = virtualAxisToOverlayAxisMap.get(virtualAxis);
			if (overlayAxis != null) {
				final var borderColor = Color.BLACK;

				final var dententValues = new HashSet<Float>();
				for (final var mode : input.getProfile().getModes())
					for (final var actions : mode.getAxisToActionsMap().values())
						for (final var action : actions)
							if (action instanceof AxisToRelativeAxisAction) {
								final var detentValue = ((AxisToRelativeAxisAction) action).getDetentValue();
								if (detentValue != null)
									dententValues.add(detentValue);
							}

				final var progressBar = new JProgressBar(SwingConstants.VERTICAL) {

					private static final long serialVersionUID = 8167193907929992395L;

					@Override
					protected void paintComponent(final Graphics g) {
						super.paintComponent(g);

						final var width = getWidth();
						final var height = getHeight();

						final var subdivisions = 3;
						for (var i = 1; i <= subdivisions; i++) {
							g.setColor(borderColor);
							final var y = i * (height / (subdivisions + 1));
							g.drawLine(0, y, width, y);
						}

						for (final var detentValue : dententValues) {
							g.setColor(Color.RED);
							final var y = (int) Input.normalize(detentValue, -1f, 1f, 0, height);
							g.drawLine(0, y, width, y);
						}
					}

					@Override
					public void setMaximum(final int n) {
						if (overlayAxis.inverted)
							super.setMinimum(-n);
						else
							super.setMaximum(n);
					}

					@Override
					public void setMinimum(final int n) {
						if (overlayAxis.inverted)
							super.setMaximum(-n);
						else
							super.setMinimum(n);
					}

					@Override
					public void setValue(final int n) {
						super.setValue(overlayAxis.inverted ? -n : n);
					}
				};

				progressBar.setPreferredSize(new Dimension(20, 150));
				progressBar.setBorder(BorderFactory.createLineBorder(borderColor));
				progressBar.setBackground(Color.LIGHT_GRAY);
				progressBar.setForeground(overlayAxis.color);
				progressBar.setValue(1);

				indicatorPanel.add(progressBar);
				virtualAxisToProgressBarMap.put(virtualAxis, progressBar);
			}
		}

		overlayFrame.add(indicatorPanel);

		overlayFrameDragListener = new FrameDragListener(this, overlayFrame) {

			@Override
			public void mouseDragged(final MouseEvent e) {
				super.mouseDragged(e);
				final var maxWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
				updateOverlayAlignment(maxWindowBounds);
			}

		};
		overlayFrame.addMouseListener(overlayFrameDragListener);
		overlayFrame.addMouseMotionListener(overlayFrameDragListener);

		prevMaxWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		updateOverlayLocation(prevMaxWindowBounds);

		overlayFrame.setVisible(true);
	}

	private void initVrOverlay() {
		if (!windows || !preferences.getBoolean(PREFERENCES_SHOW_VR_OVERLAY, true))
			return;

		try {
			openVrOverlay = new OpenVrOverlay(this);
		} catch (final Exception e) {
			openVrOverlay = null;
		}
	}

	private boolean isSelectedJidValid() {
		return selectedJid >= GLFW_JOYSTICK_1 && selectedJid <= GLFW_JOYSTICK_LAST;
	}

	private void loadProfile(final File file) {
		stopAll();

		log.log(Level.INFO, "Loading profile " + file.getAbsolutePath());

		var profileLoaded = false;

		try {
			final var jsonString = Files.readString(file.toPath());
			final var actionAdapter = new ActionTypeAdapter();
			final var gson = new GsonBuilder().registerTypeAdapterFactory(new ModeAwareTypeAdapterFactory())
					.registerTypeAdapter(IAction.class, actionAdapter).create();

			try {
				final var profile = gson.fromJson(jsonString, Profile.class);
				final var versionsComparisonResult = VersionUtils.compareVersions(profile.getVersion());
				if (versionsComparisonResult.isEmpty()) {
					log.log(Level.WARNING, "Trying to load a profile without version information");
					JOptionPane.showMessageDialog(frame,
							MessageFormat.format(rb.getString("PROFILE_VERSION_MISMATCH_DIALOG_TEXT"),
									rb.getString("AN_UNKNOWN")),
							rb.getString("WARNING_DIALOG_TITLE"), JOptionPane.WARNING_MESSAGE);
				} else {
					final var v = versionsComparisonResult.get();
					if (v < 0) {
						log.log(Level.WARNING, "Trying to load a profile for an older release");
						JOptionPane.showMessageDialog(frame,
								MessageFormat.format(rb.getString("PROFILE_VERSION_MISMATCH_DIALOG_TEXT"),
										rb.getString("AN_OLDER")),
								rb.getString("WARNING_DIALOG_TITLE"), JOptionPane.WARNING_MESSAGE);
					} else if (v > 0) {
						log.log(Level.WARNING, "Trying to load a profile for a newer release");
						JOptionPane.showMessageDialog(frame,
								MessageFormat.format(rb.getString("PROFILE_VERSION_MISMATCH_DIALOG_TEXT"),
										rb.getString("A_NEWER")),
								rb.getString("WARNING_DIALOG_TITLE"), JOptionPane.WARNING_MESSAGE);
					}
				}

				final var unknownActionClasses = actionAdapter.getUnknownActionClasses();
				if (!unknownActionClasses.isEmpty()) {
					log.log(Level.WARNING, "Encountered the unknown actions while loading profile:"
							+ String.join(", ", unknownActionClasses));
					JOptionPane.showMessageDialog(frame,
							MessageFormat.format(rb.getString("UNKNOWN_ACTION_TYPES_DIALOG_TEXT"),
									String.join("\n", unknownActionClasses)),
							rb.getString("WARNING_DIALOG_TITLE"), JOptionPane.WARNING_MESSAGE);
				}

				profileLoaded = input.setProfile(profile, input.getJid());
				if (profileLoaded) {
					saveLastProfile(file);
					updateModesPanel();
					updateOverlayPanel();
					loadedProfile = file.getName();
					setUnsavedChanges(false);
					setStatusBarText(
							MessageFormat.format(rb.getString("STATUS_PROFILE_LOADED"), file.getAbsolutePath()));
					scheduleStatusBarText(rb.getString("STATUS_READY"));
					fileChooser.setSelectedFile(file);

					restartLast();
				}
			} catch (final JsonParseException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		} catch (final NoSuchFileException e) {
			log.log(Level.FINE, e.getMessage(), e);
		} catch (final IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

		if (!profileLoaded) {
			log.log(Level.SEVERE, "Could load profile");
			JOptionPane.showMessageDialog(frame, rb.getString("COULD_NOT_LOAD_PROFILE_DIALOG_TEXT"),
					rb.getString("ERROR_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void newActivation(final String... args) {
		log.log(Level.INFO, "New activation with arguments: " + Arrays.toString(args));

		try {
			final var commandLine = new DefaultParser().parse(options, args);

			final var cmdProfilePath = commandLine.getOptionValue(OPTION_PROFILE);
			if (cmdProfilePath != null) {
				loadProfile(new File(cmdProfilePath));
				if (commandLine.getOptions().length == 1)
					return;
			}
		} catch (final ParseException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

		SwingUtilities
				.invokeLater(() -> JOptionPane.showMessageDialog(frame, rb.getString("ALREADY_RUNNING_DIALOG_TEXT"),
						rb.getString("ERROR_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE));
	}

	private void newProfile() {
		stopAll();

		currentFile = null;

		if (input != null)
			input.deInit();

		input = new Input(this, selectedJid);

		loadedProfile = null;
		updateTitleAndTooltip();
		updateModesPanel();
		updateOverlayPanel();
		setStatusBarText(rb.getString("STATUS_READY"));
		fileChooser.setSelectedFile(new File(PROFILE_FILE_SUFFIX));
	}

	private void onControllersChanged(final boolean selectFirstTab) {
		final var presentJids = new HashSet<Integer>();
		for (var jid = GLFW_JOYSTICK_1; jid <= GLFW_JOYSTICK_LAST; jid++)
			if (glfwJoystickPresent(jid) && glfwJoystickIsGamepad(jid)) {
				presentJids.add(jid);

				if (!isSelectedJidValid())
					setSelectedJid(jid);
			}

		final var controllerConnected = !presentJids.isEmpty();
		if (!controllerConnected)
			selectedJid = INVALID_JID;

		final var previousSelectedTabIndex = tabbedPane.getSelectedIndex();
		fileMenu.remove(newMenuItem);
		fileMenu.remove(openMenuItem);
		fileMenu.remove(saveMenuItem);
		fileMenu.remove(saveAsMenuItem);
		if (fileMenu.getItemCount() > 1)
			fileMenu.remove(0);
		deviceMenu.removeAll();
		menuBar.remove(deviceMenu);
		menuBar.remove(localMenu);
		menuBar.remove(serverMenu);
		tabbedPane.remove(modesPanel);
		tabbedPane.remove(assignmentsComponent);
		tabbedPane.remove(overlayPanel);

		if (controllerConnected) {
			fileMenu.insert(newMenuItem, 0);
			fileMenu.insert(openMenuItem, 1);
			fileMenu.insert(saveMenuItem, 2);
			fileMenu.insert(saveAsMenuItem, 3);
			fileMenu.insertSeparator(4);

			for (final var jid : presentJids)
				deviceMenu.add(new SelectControllerAction(jid));
			menuBar.add(deviceMenu, 1);

			if (windows)
				menuBar.add(localMenu, 2);

			menuBar.add(serverMenu, windows ? 4 : 2);

			modesPanel = new JPanel(new BorderLayout());
			tabbedPane.insertTab(rb.getString("MODES_TAB"), null, modesPanel, null,
					tabbedPane.indexOfComponent(settingsScrollPane));

			modesListPanel = new JPanel();
			modesListPanel.setLayout(new GridBagLayout());

			modesScrollPane = new JScrollPane();
			modesScrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
			modesPanel.add(modesScrollPane, BorderLayout.CENTER);

			addModePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			final var addButton = new JButton(new AddModeAction());
			addButton.setPreferredSize(BUTTON_DIMENSION);
			addModePanel.add(addButton);
			modesPanel.add(addModePanel, BorderLayout.SOUTH);

			assignmentsComponent = new AssignmentsComponent(this);
			tabbedPane.insertTab(rb.getString("ASSIGNMENTS_TAB"), null, assignmentsComponent, null,
					tabbedPane.indexOfComponent(settingsScrollPane));

			overlayPanel = new JPanel(new BorderLayout());

			indicatorsListPanel = new JPanel();
			indicatorsListPanel.setLayout(new GridBagLayout());

			indicatorsScrollPane = new JScrollPane();
			indicatorsScrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
			overlayPanel.add(indicatorsScrollPane, BorderLayout.CENTER);
			tabbedPane.insertTab(rb.getString("OVERLAY_TAB"), null, overlayPanel, null,
					tabbedPane.indexOfComponent(settingsScrollPane));
		} else
			log.log(Level.INFO, "No controllers connected");

		if (selectFirstTab || !controllerConnected)
			tabbedPane.setSelectedIndex(0);
		else if (previousSelectedTabIndex < tabbedPane.getTabCount())
			tabbedPane.setSelectedIndex(previousSelectedTabIndex);

		updateMenuShortcuts();
		updateModesPanel();
		updateOverlayPanel();
		updatePanelAccess();

		frame.getContentPane().invalidate();
		frame.getContentPane().repaint();
	}

	private void onOutputThreadsChanged() {
		final var localActive = localThread != null && localThread.isAlive();
		final var clientActive = clientThread != null && clientThread.isAlive();
		final var serverActive = serverThread != null && serverThread.isAlive();

		final var noneActive = !localActive && !clientActive && !serverActive;

		if (startLocalRadioButtonMenuItem != null) {
			startLocalRadioButtonMenuItem.setSelected(localActive);
			startLocalRadioButtonMenuItem.setEnabled(noneActive);
		}

		if (stopLocalRadioButtonMenuItem != null) {
			stopLocalRadioButtonMenuItem.setSelected(!localActive);
			stopLocalRadioButtonMenuItem.setEnabled(localActive);
		}

		if (startClientRadioButtonMenuItem != null) {
			startClientRadioButtonMenuItem.setSelected(clientActive);
			startClientRadioButtonMenuItem.setEnabled(noneActive);
		}

		if (stopClientRadioButtonMenuItem != null) {
			stopClientRadioButtonMenuItem.setSelected(!clientActive);
			stopClientRadioButtonMenuItem.setEnabled(clientActive);
		}

		if (startServerRadioButtonMenuItem != null) {
			startServerRadioButtonMenuItem.setSelected(serverActive);
			startServerRadioButtonMenuItem.setEnabled(noneActive);
		}

		if (stopServerRadioButtonMenuItem != null) {
			stopServerRadioButtonMenuItem.setSelected(!serverActive);
			stopServerRadioButtonMenuItem.setEnabled(serverActive);
		}

		updateMenuShortcuts();
		updatePanelAccess();
	}

	public boolean preventPowerSaveMode() {
		return preferences.getBoolean(PREFERENCES_PREVENT_POWER_SAVE_MODE, true);
	}

	public void quit() {
		if (serverSocket != null)
			try {
				serverSocket.close();
			} catch (final IOException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}

		if (input != null)
			input.deInit();

		stopAll();
		glfwTerminate();
		Singleton.stop();
		terminate(0);
	}

	private void repaintOverlay() {
		if (overlayFrame != null) {
			overlayFrame.getContentPane().validate();
			overlayFrame.getContentPane().repaint();
		}

		if (onScreenKeyboard.isVisible()) {
			onScreenKeyboard.getContentPane().validate();
			onScreenKeyboard.getContentPane().repaint();
		}
	}

	public void restartLast() {
		switch (lastOutputType) {
		case LOCAL:
			startLocal();
			break;
		case CLIENT:
			startClient();
			break;
		case SERVER:
			startServer();
			break;
		case NONE:
			break;
		}
	}

	private void saveLastProfile(final File file) {
		currentFile = file;
		preferences.put(PREFERENCES_LAST_PROFILE, file.getAbsolutePath());
	}

	private void saveProfile(File file) {
		input.reset();

		if (!file.getName().toLowerCase(Locale.getDefault()).endsWith(PROFILE_FILE_SUFFIX))
			file = new File(file.getAbsoluteFile() + PROFILE_FILE_SUFFIX);

		log.log(Level.INFO, "Saving profile " + file.getAbsolutePath());

		final var profile = input.getProfile();
		profile.setVersion(VersionUtils.getMajorAndMinorVersion());

		final var gson = new GsonBuilder().registerTypeAdapterFactory(new ModeAwareTypeAdapterFactory())
				.registerTypeAdapter(IAction.class, new ActionTypeAdapter()).setPrettyPrinting().create();
		final var jsonString = gson.toJson(profile);
		try {
			Files.writeString(file.toPath(), jsonString);
			saveLastProfile(file);
			loadedProfile = file.getName();
			setUnsavedChanges(false);
			setStatusBarText(MessageFormat.format(rb.getString("STATUS_PROFILE_SAVED"), file.getAbsolutePath()));
			scheduleStatusBarText(rb.getString("STATUS_READY"));
		} catch (final IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			JOptionPane.showMessageDialog(frame, rb.getString("COULD_NOT_SAVE_PROFILE_DIALOG_TEXT"),
					rb.getString("ERROR_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void saveProfileAs() {
		fileChooser.setSelectedFile(currentFile);
		if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
			saveProfile(fileChooser.getSelectedFile());
	}

	public void scheduleStatusBarText(final String text) {
		class StatusBarTextTimerTask extends TimerTask {

			private final String newText;
			private final String originalText;

			public StatusBarTextTimerTask(final String newText) {
				this.newText = newText;
				originalText = statusLabel.getText();
			}

			@Override
			public void run() {
				SwingUtilities.invokeLater(() -> {
					if (statusLabel.getText().equals(originalText))
						setStatusBarText(newText);
				});
			}
		}

		timer.schedule(new StatusBarTextTimerTask(text), 5000L);
	}

	public void setOverlayText(final String text) {
		if (currentModeLabel != null)
			invokeOnEventDispatchThreadIfRequired(() -> {
				if (currentModeLabel != null)
					currentModeLabel.setText(text);
			});
	}

	public void setSelectedJid(final int jid) {
		if (selectedJid == jid)
			return;

		selectedJid = jid;

		final var guid = glfwGetJoystickGUID(jid);
		if (guid != null) {
			log.log(Level.INFO, "Selected controller " + selectedJid + "(" + guid + ")");
			preferences.put(PREFERENCES_LAST_CONTROLLER, guid);
		}

		stopAll();

		Profile previousProfile = null;
		if (input != null) {
			input.deInit();
			previousProfile = input.getProfile();
		}

		input = new Input(this, selectedJid);

		if (previousProfile != null)
			input.setProfile(previousProfile, selectedJid);
	}

	public void setStatusBarText(final String text) {
		if (statusLabel != null) {
			statusLabel.setText(text);
			repaintOverlay();
		}
	}

	void setUnsavedChanges(final boolean unsavedChanges) {
		this.unsavedChanges = unsavedChanges;
		updateTitleAndTooltip();
	}

	private void startClient() {
		lastOutputType = OutputType.CLIENT;
		clientThread = new ClientVJoyOutputThread(Main.this, input);
		clientThread.setvJoyDevice(
				new UINT(preferences.getInt(PREFERENCES_VJOY_DEVICE, VJoyOutputThread.DEFAULT_VJOY_DEVICE)));
		clientThread.setHost(hostTextField.getText());
		clientThread.setPort(preferences.getInt(PREFERENCES_PORT, ServerOutputThread.DEFAULT_PORT));
		clientThread.setTimeout(preferences.getInt(PREFERENCES_TIMEOUT, ServerOutputThread.DEFAULT_TIMEOUT));
		clientThread.start();

		onOutputThreadsChanged();
	}

	private void startLocal() {
		if (!isSelectedJidValid())
			return;

		lastOutputType = OutputType.LOCAL;
		localThread = new LocalVJoyOutputThread(Main.this, input);
		localThread.setvJoyDevice(
				new UINT(preferences.getInt(PREFERENCES_VJOY_DEVICE, VJoyOutputThread.DEFAULT_VJOY_DEVICE)));
		localThread.setPollInterval(preferences.getInt(PREFERENCES_POLL_INTERVAL, OutputThread.DEFAULT_POLL_INTERVAL));
		localThread.start();

		onOutputThreadsChanged();

		initOverlay();
		initVrOverlay();
		startOverlayTimerTask();
	}

	private void startOverlayTimerTask() {
		stopOverlayTimerTask();

		overlayTimerTask = new TimerTask() {

			@Override
			public void run() {
				SwingUtilities.invokeLater(() -> {
					if (!isModalDialogShowing()) {
						if (overlayFrame != null)
							makeWindowTopmost(overlayFrame);

						if (onScreenKeyboard.isVisible())
							makeWindowTopmost(onScreenKeyboard);
					}

					final var maxWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
							.getMaximumWindowBounds();
					if (!maxWindowBounds.equals(prevMaxWindowBounds)) {
						prevMaxWindowBounds = maxWindowBounds;

						updateOverlayLocation(maxWindowBounds);
						onScreenKeyboard.updateLocation();
					}

					repaintOverlay();
				});
			}
		};

		timer.schedule(overlayTimerTask, OVERLAY_POSITION_UPDATE_INTERVAL, OVERLAY_POSITION_UPDATE_INTERVAL);
	}

	private void startServer() {
		if (!isSelectedJidValid())
			return;

		lastOutputType = OutputType.SERVER;
		serverThread = new ServerOutputThread(Main.this, input);
		serverThread.setPort(preferences.getInt(PREFERENCES_PORT, ServerOutputThread.DEFAULT_PORT));
		serverThread.setTimeout(preferences.getInt(PREFERENCES_TIMEOUT, ServerOutputThread.DEFAULT_TIMEOUT));
		serverThread.setPollInterval(preferences.getInt(PREFERENCES_POLL_INTERVAL, OutputThread.DEFAULT_POLL_INTERVAL));
		serverThread.start();

		onOutputThreadsChanged();

		initOverlay();
		startOverlayTimerTask();
	}

	public void stopAll() {
		if (windows) {
			stopLocal(false);
			stopClient(false);
		}
		stopServer(false);

		System.gc();
	}

	private void stopClient(final boolean resetLastOutputType) {
		if (clientThread != null)
			clientThread.stopOutput();

		if (resetLastOutputType)
			lastOutputType = OutputType.NONE;

		waitForThreadToFinish(clientThread);

		invokeOnEventDispatchThreadIfRequired(() -> {
			onOutputThreadsChanged();
		});
	}

	private void stopLocal(final boolean resetLastOutputType) {
		if (localThread != null)
			localThread.stopOutput();

		if (resetLastOutputType)
			lastOutputType = OutputType.NONE;

		invokeOnEventDispatchThreadIfRequired(() -> {
			stopOverlayTimerTask();
			deInitOverlay();
		});

		waitForThreadToFinish(localThread);

		invokeOnEventDispatchThreadIfRequired(() -> {
			onOutputThreadsChanged();
		});
	}

	private void stopOverlayTimerTask() {
		if (overlayTimerTask != null)
			overlayTimerTask.cancel();
	}

	private void stopServer(final boolean resetLastOutputType) {
		if (serverThread != null)
			serverThread.stopOutput();

		if (resetLastOutputType)
			lastOutputType = OutputType.NONE;

		invokeOnEventDispatchThreadIfRequired(() -> {
			stopOverlayTimerTask();
			deInitOverlay();
		});

		waitForThreadToFinish(serverThread);

		invokeOnEventDispatchThreadIfRequired(() -> {
			onOutputThreadsChanged();
		});
	}

	public void toggleOnScreenKeyboard() {
		if (localThread != null && localThread.isAlive() || clientThread != null && clientThread.isAlive()
				|| serverThread != null && serverThread.isAlive())
			SwingUtilities.invokeLater(() -> {
				onScreenKeyboard.setVisible(!onScreenKeyboard.isVisible());
				repaintOverlay();
			});
	}

	private void updateMenuShortcuts() {
		final var menuCount = menuBar.getMenuCount();
		final var alreadyAssignedMenuKeyCodes = new HashSet<Integer>(menuCount);

		for (var i = 0; i < menuCount; i++) {
			final var menu = menuBar.getMenu(i);
			menu.setMnemonic(getExtendedKeyCodeForMenu(menu, alreadyAssignedMenuKeyCodes));

			final var menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
			for (var j = 0; j < menu.getItemCount(); j++) {
				final var menuItem = menu.getItem(j);
				if (menuItem != null) {
					KeyStroke keyStroke = null;

					if (menuItem.isEnabled()) {
						final var keyCode = getExtendedKeyCodeForMenuItem(menuItem);

						if (keyCode != KeyEvent.VK_UNDEFINED)
							keyStroke = KeyStroke.getKeyStroke(keyCode, menuShortcutKeyMask);
					}

					menuItem.setAccelerator(keyStroke);
				}
			}
		}
	}

	void updateModesPanel() {
		if (modesListPanel == null)
			return;

		modesListPanel.removeAll();

		final var modes = input.getProfile().getModes();
		for (var i = 0; i < modes.size(); i++) {
			final var mode = modes.get(i);

			final var modePanel = new JPanel(new GridBagLayout());
			modesListPanel.add(modePanel, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0d, 0d,
					GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 5));

			final var modeNoLabel = new JLabel(MessageFormat.format(rb.getString("MODE_LABEL_NO"), i + 1));
			modeNoLabel.setPreferredSize(new Dimension(100, 15));
			modePanel.add(modeNoLabel, new GridBagConstraints(0, 0, 1, 1, 0d, 0d, GridBagConstraints.BASELINE,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			modePanel.add(Box.createGlue(), new GridBagConstraints(1, GridBagConstraints.RELATIVE, 1, 1, 1d, 1d,
					GridBagConstraints.BASELINE, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			final var descriptionTextField = new JTextField(mode.getDescription(), 20);
			modePanel.add(descriptionTextField, new GridBagConstraints(2, 0, 1, 1, 1d, 1d, GridBagConstraints.BASELINE,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			final var setModeDescriptionAction = new SetModeDescriptionAction(mode, descriptionTextField);
			descriptionTextField.addActionListener(setModeDescriptionAction);
			descriptionTextField.getDocument().addDocumentListener(setModeDescriptionAction);

			modePanel.add(Box.createGlue(), new GridBagConstraints(3, GridBagConstraints.RELATIVE, 1, 1, 1d, 1d,
					GridBagConstraints.BASELINE, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			if (Profile.defaultMode.equals(mode) || OnScreenKeyboard.onScreenKeyboardMode.equals(mode)) {
				descriptionTextField.setEditable(false);
				modePanel.add(Box.createHorizontalStrut(BUTTON_DIMENSION.width));
			} else {
				final var deleteButton = new JButton(new RemoveModeAction(mode));
				deleteButton.setPreferredSize(BUTTON_DIMENSION);
				modePanel.add(deleteButton, new GridBagConstraints(4, GridBagConstraints.RELATIVE, 1, 1, 0d, 0d,
						GridBagConstraints.BASELINE, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
		}

		modesListPanel.add(Box.createGlue(), new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1d, 1d,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		modesScrollPane.setViewportView(modesListPanel);
	}

	private void updateOverlayAlignment(final Rectangle maxWindowBounds) {
		final var inLowerHalf = overlayFrame.getY() + overlayFrame.getHeight() / 2 < maxWindowBounds.height / 2;

		if (currentModeLabel != null) {
			overlayFrame.remove(currentModeLabel);
			overlayFrame.add(currentModeLabel, inLowerHalf ? BorderLayout.PAGE_START : BorderLayout.PAGE_END);
		}

		var alignment = SwingConstants.RIGHT;
		var flowLayoutAlignment = FlowLayout.RIGHT;
		if (overlayFrame.getX() + overlayFrame.getWidth() / 2 < maxWindowBounds.width / 2) {
			alignment = SwingConstants.LEFT;
			flowLayoutAlignment = FlowLayout.LEFT;
		}

		if (currentModeLabel != null)
			currentModeLabel.setHorizontalAlignment(alignment);

		indicatorPanelFlowLayout.setAlignment(flowLayoutAlignment);
		indicatorPanel.invalidate();

		overlayFrame.setBackground(TRANSPARENT);
		overlayFrame.pack();
	}

	public void updateOverlayAxisIndicators() {
		for (final var virtualAxis : Input.VirtualAxis.values())
			if (virtualAxisToProgressBarMap.containsKey(virtualAxis)) {
				OutputThread outputThread = null;
				if (localThread != null && localThread.isAlive())
					outputThread = localThread;
				else if (clientThread != null && clientThread.isAlive())
					outputThread = clientThread;
				else if (serverThread != null && serverThread.isAlive())
					outputThread = serverThread;

				if (outputThread != null) {
					final var progressBar = virtualAxisToProgressBarMap.get(virtualAxis);

					final var newMinimum = -outputThread.getMaxAxisValue();
					if (progressBar.getMinimum() != newMinimum)
						progressBar.setMinimum(newMinimum);

					final var newMaximum = outputThread.getMinAxisValue();
					if (progressBar.getMaximum() != newMaximum)
						progressBar.setMaximum(newMaximum);

					final var newValue = -input.getAxes().get(virtualAxis);
					if (progressBar.getValue() != newValue)
						progressBar.setValue(newValue);
				}
			}
	}

	private void updateOverlayLocation(final Rectangle maxWindowBounds) {
		if (overlayFrame != null && overlayFrameDragListener != null && !overlayFrameDragListener.isDragging()) {
			overlayFrame.pack();
			final var x = maxWindowBounds.width - overlayFrame.getWidth();
			final var y = maxWindowBounds.height - overlayFrame.getHeight();
			final var defaultLocation = new Point(x, y);
			loadFrameLocation(preferences, overlayFrame, defaultLocation, maxWindowBounds);
			updateOverlayAlignment(maxWindowBounds);
		}
	}

	private void updateOverlayPanel() {
		if (indicatorsListPanel == null)
			return;

		indicatorsListPanel.removeAll();

		for (final var virtualAxis : Input.VirtualAxis.values()) {
			final var indicatorPanel = new JPanel(new GridBagLayout());
			indicatorsListPanel.add(indicatorPanel, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0d, 0d,
					GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 5));

			final var virtualAxisLabel = new JLabel(
					MessageFormat.format(rb.getString("AXIS_LABEL"), virtualAxis.toString()));
			virtualAxisLabel.setPreferredSize(new Dimension(100, 15));
			indicatorPanel.add(virtualAxisLabel, new GridBagConstraints(0, 0, 1, 1, 0d, 0d, GridBagConstraints.BASELINE,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			final var virtualAxisToOverlayAxisMap = input.getProfile().getVirtualAxisToOverlayAxisMap();
			final var overlayAxis = virtualAxisToOverlayAxisMap.get(virtualAxis);
			final var enabled = overlayAxis != null;

			final var colorLabel = new JLabel();
			if (enabled) {
				colorLabel.setOpaque(true);
				colorLabel.setBackground(overlayAxis.color);
			} else
				colorLabel.setText(rb.getString("INDICATOR_DISABLED_LABEL"));
			colorLabel.setHorizontalAlignment(SwingConstants.CENTER);

			colorLabel.setPreferredSize(new Dimension(100, 15));
			colorLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			indicatorPanel.add(colorLabel, new GridBagConstraints(1, 0, 1, 1, 1d, 0d, GridBagConstraints.BASELINE,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			final var colorButton = new JButton(new SelectIndicatorColorAction(virtualAxis));
			colorButton.setPreferredSize(BUTTON_DIMENSION);
			colorButton.setEnabled(enabled);
			indicatorPanel.add(colorButton, new GridBagConstraints(2, 0, 1, 1, 1d, 0d, GridBagConstraints.BASELINE,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			final var invertedCheckBox = new JCheckBox(new InvertIndicatorAction(virtualAxis));
			invertedCheckBox.setSelected(enabled && overlayAxis.inverted);
			invertedCheckBox.setEnabled(enabled);
			indicatorPanel.add(invertedCheckBox, new GridBagConstraints(3, 0, 1, 1, 1d, 0d, GridBagConstraints.BASELINE,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			final var displayCheckBox = new JCheckBox(new DisplayIndicatorAction(virtualAxis));
			displayCheckBox.setSelected(enabled);
			indicatorPanel.add(displayCheckBox, new GridBagConstraints(4, GridBagConstraints.RELATIVE, 1, 1, 0d, 0d,
					GridBagConstraints.BASELINE, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}

		indicatorsListPanel.add(Box.createGlue(), new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1d, 1d,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		indicatorsScrollPane.setViewportView(indicatorsListPanel);
	}

	private void updatePanelAccess() {
		final var localActive = localThread != null && localThread.isAlive();
		final var serverActive = serverThread != null && serverThread.isAlive();
		final var panelsEnabled = !localActive && !serverActive;

		setEnabledRecursive(modesListPanel, panelsEnabled);
		setEnabledRecursive(addModePanel, panelsEnabled);

		if (assignmentsComponent != null)
			assignmentsComponent.setEnabled(panelsEnabled);

		if (!panelsEnabled)
			setEnabledRecursive(indicatorsListPanel, false);
		else
			updateOverlayPanel();

		setEnabledRecursive(settingsPanel, panelsEnabled);
	}

	@SuppressWarnings("unchecked")
	private void updateTheme() {
		final var theme = preferences.getBoolean(PREFERENCES_DARK_THEME, false) ? new DefaultMetalTheme()
				: new OceanTheme();
		MetalLookAndFeel.setCurrentTheme(theme);

		try {
			UIManager.setLookAndFeel(new MetalLookAndFeel());
		} catch (final UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}

		final var keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			final var key = keys.nextElement();
			final var font = UIManager.getFont(key);
			if (font != null) {
				final var newFontUIResource = new FontUIResource(font.getName(), Font.PLAIN, font.getSize());
				UIManager.put(key, newFontUIResource);
			}
		}

		if (usingOceanTheme()) {
			final var buttonGradient = UIManager.get("Button.gradient");
			final var panelBackground = UIManager.getColor("Panel.background");

			if (buttonGradient instanceof List && panelBackground != null) {
				final var factor = .94f;

				final var buttonGradientColor = new Color(Math.max((int) (panelBackground.getRed() * factor), 0),
						Math.max((int) (panelBackground.getGreen() * factor), 0),
						Math.max((int) (panelBackground.getBlue() * factor), 0), panelBackground.getAlpha());

				((List<Object>) buttonGradient).replaceAll(v -> v instanceof Color ? buttonGradientColor : v);
			}
		}

		SwingUtilities.updateComponentTreeUI(frame);
		SwingUtilities.updateComponentTreeUI(fileChooser);
		SwingUtilities.updateComponentTreeUI(onScreenKeyboard);
	}

	public void updateTitleAndTooltip() {
		final String title;

		if (!isSelectedJidValid())
			title = rb.getString("APPLICATION_NAME");
		else {
			final String profile;
			if (loadedProfile != null)
				profile = (unsavedChanges ? "*" : "") + loadedProfile;
			else
				profile = rb.getString("UNSAVED");

			title = MessageFormat.format(rb.getString("MAIN_FRAME_TITLE"), profile);
		}

		frame.setTitle(title);

		if (trayIcon != null && input != null) {
			final String toolTip;

			if (input.getDualShock4ProductId() != null)
				toolTip = MessageFormat.format(
						rb.getString(input.isCharging() ? "BATTERY_TOOLTIP_CHARGING" : "BATTERY_TOOLTIP_DISCHARGING"),
						title, input.getBatteryState() / 100f);
			else
				toolTip = title;

			trayIcon.setToolTip(toolTip);
		}
	}

}
