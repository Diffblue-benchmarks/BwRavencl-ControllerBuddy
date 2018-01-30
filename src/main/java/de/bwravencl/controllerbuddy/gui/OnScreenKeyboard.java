/* Copyright (C) 2018  Matteo Hausner
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

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.bwravencl.controllerbuddy.input.Input;
import de.bwravencl.controllerbuddy.input.KeyStroke;
import de.bwravencl.controllerbuddy.input.ScanCode;

public class OnScreenKeyboard extends JFrame {

	private static class KeyboardButton extends JButton {

		private static final long serialVersionUID = 4608180593368737289L;

		private static final long MIN_LONG_CLICK_TIME = 500L;

		private static final Color DEFAULT_BACKGROUND = new JButton().getBackground();

		private static final Color DEFAULT_FOREGROUND = new JButton().getForeground();

		private static final Color HELD_BACKGROUND = new Color(128, 128, 128, 64);

		private static final Set<KeyboardButton> heldButtons = new HashSet<>();

		protected static void releaseAll() {
			for (final KeyboardButton[] row : keyboardButtons)
				for (final KeyboardButton kb : row)
					kb.release();
		}

		private final KeyStroke keyStroke;

		public KeyboardButton(final String scanCodeName) {
			super(scanCodeName);
			keyStroke = new KeyStroke(new Integer[] { ScanCode.nameToScanCodeMap.get(scanCodeName) }, new Integer[0]);

			addChangeListener(new ChangeListener() {

				private boolean lastPressed;
				private long beginPress;

				@Override
				public void stateChanged(final ChangeEvent e) {
					final boolean pressed = getModel().isPressed();
					if (pressed != lastPressed) {
						if (pressed) {
							beginPress = System.currentTimeMillis();
							hold();
							new Timer().schedule(new TimerTask() {

								@Override
								public void run() {
									if (heldButtons.contains(KeyboardButton.this))
										KeyboardButton.this.setForeground(Color.GRAY);
								}

							}, MIN_LONG_CLICK_TIME);
						} else if (System.currentTimeMillis() - beginPress < MIN_LONG_CLICK_TIME) {
							releaseAll();
							beginPress = 0L;
						} else
							KeyboardButton.this.setForeground(DEFAULT_FOREGROUND);

						lastPressed = pressed;
					}
				}
			});
		}

		protected void hold() {
			setBackground(HELD_BACKGROUND);
			if (heldButtons.add(this))
				System.out.println("Start hold " + getText());
		}

		protected void poll(final Input input) {
			if (heldButtons.contains(this))
				input.getDownKeyStrokes().add(keyStroke);
		}

		protected void release() {
			setBackground(DEFAULT_BACKGROUND);
			if (heldButtons.remove(this))
				System.out.println("End hold " + getText());
		}

	}

	private static final long serialVersionUID = -111088315813179371L;

	private static final Color ROW_BACKGROUND = new Color(255, 255, 255, 64);

	private static final KeyboardButton[][] keyboardButtons = {
			{ new KeyboardButton(ScanCode.ESCAPE), new KeyboardButton(ScanCode.F1), new KeyboardButton(ScanCode.F2),
					new KeyboardButton(ScanCode.F3), new KeyboardButton(ScanCode.F4), new KeyboardButton(ScanCode.F5),
					new KeyboardButton(ScanCode.F6), new KeyboardButton(ScanCode.F7), new KeyboardButton(ScanCode.F8),
					new KeyboardButton(ScanCode.F9), new KeyboardButton(ScanCode.F10), new KeyboardButton(ScanCode.F11),
					new KeyboardButton(ScanCode.F12) },
			{ new KeyboardButton(ScanCode.APOSTROPHE), new KeyboardButton(ScanCode.D1), new KeyboardButton(ScanCode.D2),
					new KeyboardButton(ScanCode.D3), new KeyboardButton(ScanCode.D4), new KeyboardButton(ScanCode.D5),
					new KeyboardButton(ScanCode.D6), new KeyboardButton(ScanCode.D7), new KeyboardButton(ScanCode.D8),
					new KeyboardButton(ScanCode.D9), new KeyboardButton(ScanCode.D0),
					new KeyboardButton(ScanCode.SUBTRACT), new KeyboardButton(ScanCode.ADD) } };

	private static final Border defaultButtonBorder = UIManager.getBorder("Button.border");
	private static final Border focusedButtonBorder = BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.RED, 3), ((CompoundBorder) defaultButtonBorder).getInsideBorder());

	public static void main(final String[] args) throws IOException {
		EventQueue.invokeLater(() -> {
			new OnScreenKeyboard();
		});
	};

	private int selectedRow = 0;
	private int selectedColumn = 0;

	public OnScreenKeyboard() {
		setType(JFrame.Type.UTILITY);
		setFocusableWindowState(false);
		setUndecorated(true);
		setBackground(Main.TRANSPARENT);
		setAlwaysOnTop(true);

		final JPanel parentPanel = new JPanel(new GridLayout(0, 1));
		parentPanel.setBackground(Main.TRANSPARENT);

		for (int row = 0; row < keyboardButtons.length; row++) {
			final JPanel rowPanel = new JPanel();
			rowPanel.setBackground(ROW_BACKGROUND);
			rowPanel.setBorder(
					BorderFactory.createEmptyBorder(row == 0 ? 5 : 0, 5, row == keyboardButtons.length ? 5 : 0, 5));

			for (int column = 0; column < keyboardButtons[row].length; column++)
				rowPanel.add(keyboardButtons[row][column]);

			parentPanel.add(rowPanel);
		}

		focusCurrentButton();
		add(parentPanel);
		updateLocation();

		// new Thread() {
		//
		// @Override
		// public void run() {
		// while (true) {
		// final int randomNum = ThreadLocalRandom.current().nextInt(0, 5 + 1);
		// switch (randomNum) {
		// case 0:
		// up();
		// break;
		// case 1:
		// left();
		// break;
		// case 2:
		// right();
		// break;
		// case 3:
		// down();
		// break;
		// case 4:
		// hold();
		// break;
		// case 5:
		// release();
		// break;
		// default:
		// break;
		// }
		//
		// try {
		// Thread.sleep(250L);
		// } catch (final InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		//
		// }.start();
	}

	public void down() {
		if (selectedRow < keyboardButtons.length - 1) {
			unfocusCurrentButton();
			selectedRow++;
			selectedColumn = Math.min(selectedColumn, keyboardButtons[selectedRow].length - 1);
			keyboardButtons[selectedRow][selectedColumn].setBorder(focusedButtonBorder);
		}
	}

	private void focusCurrentButton() {
		keyboardButtons[selectedRow][selectedColumn].setBorder(focusedButtonBorder);
	}

	public void hold() {
		keyboardButtons[selectedRow][selectedColumn].hold();
	}

	public void left() {
		if (selectedColumn > 0) {
			unfocusCurrentButton();
			selectedColumn--;
			focusCurrentButton();
		}
	}

	public void poll(final Input input) {
		for (final KeyboardButton[] row : keyboardButtons)
			for (final KeyboardButton kb : row)
				kb.poll(input);
	}

	public void release() {
		for (final KeyboardButton[] row : keyboardButtons)
			for (final KeyboardButton kb : row)
				kb.release();
	}

	public void right() {
		if (selectedColumn < keyboardButtons[selectedRow].length - 1) {
			unfocusCurrentButton();
			selectedColumn++;
			focusCurrentButton();
		}
	}

	@Override
	public void setVisible(final boolean b) {
		super.setVisible(b);

		if (b)
			setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
	}

	private void unfocusCurrentButton() {
		keyboardButtons[selectedRow][selectedColumn].setBorder(focusedButtonBorder);
	}

	public void up() {
		if (selectedRow > 0) {
			unfocusCurrentButton();
			selectedRow--;
			selectedColumn = Math.min(selectedColumn, keyboardButtons[selectedRow].length - 1);
			focusCurrentButton();
		}
	}

	protected void updateLocation() {
		pack();
		final Rectangle rectangle = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		final int x = (int) rectangle.getMaxX() / 2 - getWidth() / 2;
		final int y = (int) rectangle.getMaxY() - getHeight();
		setLocation(x, y);
	}

}