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
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

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
import de.bwravencl.controllerbuddy.input.Mode;
import de.bwravencl.controllerbuddy.input.ScanCode;
import net.brockmatt.util.ResourceBundleUtil;

public class OnScreenKeyboard extends JFrame {

	private class KeyboardButton extends JButton {

		private static final long serialVersionUID = 4608180593368737289L;

		private static final long MIN_LONG_CLICK_TIME = 500L;

		private boolean changed = false;

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
							press();
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
							KeyboardButton.this.setForeground(KEYBOARD_BUTTON_DEFAULT_FOREGROUND);

						lastPressed = pressed;
					}
				}
			});
		}

		protected void poll(final Input input) {
			if (changed) {
				final Set<KeyStroke> downKeyStrokes = input.getDownKeyStrokes();

				if (heldButtons.contains(this))
					downKeyStrokes.add(keyStroke);
				else
					downKeyStrokes.remove(keyStroke);
			}
		}

		protected void press() {
			setBackground(KEYBOARD_BUTTON_HELD_BACKGROUND);
			if (heldButtons.add(this)) {
				changed = true;
				anyChanges = true;
			}
		}

		protected void release() {
			setBackground(KEYBOARD_BUTTON_DEFAULT_BACKGROUND);
			if (heldButtons.remove(this)) {
				changed = true;
				anyChanges = true;
			}
		}

		protected void releaseAll() {
			for (final KeyboardButton[] row : keyboardButtons)
				for (final KeyboardButton kb : row)
					kb.release();
		}

		protected void toggleLock() {
			if (heldButtons.contains(this))
				release();
			else
				press();
		}

	}

	private static final Color KEYBOARD_BUTTON_DEFAULT_BACKGROUND = new JButton().getBackground();

	private static final Color KEYBOARD_BUTTON_DEFAULT_FOREGROUND = new JButton().getForeground();

	private static final Color KEYBOARD_BUTTON_HELD_BACKGROUND = new Color(128, 128, 128);

	private static final Set<KeyboardButton> heldButtons = new HashSet<>();

	private static final long serialVersionUID = -111088315813179371L;

	private static final UUID ON_SCREEN_KEYBOARD_MODE_UUID = UUID.fromString("daf53639-9518-48db-bd63-19cde7bf9a96");

	public static final Mode onScreenKeyboardMode;

	private static final Color ROW_BACKGROUND = new Color(255, 255, 255, 64);

	private static final Border defaultButtonBorder = UIManager.getBorder("Button.border");

	private static final Border focusedButtonBorder = BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.RED, 3), ((CompoundBorder) defaultButtonBorder).getInsideBorder());

	static {
		onScreenKeyboardMode = new Mode(ON_SCREEN_KEYBOARD_MODE_UUID);
		final ResourceBundle rb = new ResourceBundleUtil().getResourceBundle(Main.STRING_RESOURCE_BUNDLE_BASENAME,
				Locale.getDefault());
		onScreenKeyboardMode.setDescription(rb.getString("ON_SCREEN_KEYBOARD_MODE_DESCRIPTION"));
	}

	private final KeyboardButton[][] keyboardButtons = {
			{ new KeyboardButton(ScanCode.ESCAPE), new KeyboardButton(ScanCode.F1), new KeyboardButton(ScanCode.F2),
					new KeyboardButton(ScanCode.F3), new KeyboardButton(ScanCode.F4), new KeyboardButton(ScanCode.F5),
					new KeyboardButton(ScanCode.F6), new KeyboardButton(ScanCode.F7), new KeyboardButton(ScanCode.F8),
					new KeyboardButton(ScanCode.F9), new KeyboardButton(ScanCode.F10), new KeyboardButton(ScanCode.F11),
					new KeyboardButton(ScanCode.F12) },
			{ new KeyboardButton(ScanCode.APOSTROPHE), new KeyboardButton(ScanCode.D1), new KeyboardButton(ScanCode.D2),
					new KeyboardButton(ScanCode.D3), new KeyboardButton(ScanCode.D4), new KeyboardButton(ScanCode.D5),
					new KeyboardButton(ScanCode.D6), new KeyboardButton(ScanCode.D7), new KeyboardButton(ScanCode.D8),
					new KeyboardButton(ScanCode.D9), new KeyboardButton(ScanCode.D0),
					new KeyboardButton(ScanCode.SUBTRACT), new KeyboardButton(ScanCode.ADD) } };;

	private int selectedRow = 0;
	private int selectedColumn = 0;

	private boolean anyChanges = false;

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
	}

	private void focusCurrentButton() {
		keyboardButtons[selectedRow][selectedColumn].setBorder(focusedButtonBorder);
	}

	public void moveSelectorDown() {
		if (selectedRow < keyboardButtons.length - 1) {
			unfocusCurrentButton();
			selectedRow++;
			selectedColumn = Math.min(selectedColumn, keyboardButtons[selectedRow].length - 1);
			focusCurrentButton();
		}
	}

	public void moveSelectorLeft() {
		if (selectedColumn > 0) {
			unfocusCurrentButton();
			selectedColumn--;
			focusCurrentButton();
		}
	}

	public void moveSelectorRight() {
		if (selectedColumn < keyboardButtons[selectedRow].length - 1) {
			unfocusCurrentButton();
			selectedColumn++;
			focusCurrentButton();
		}
	}

	public void moveSelectorUp() {
		if (selectedRow > 0) {
			unfocusCurrentButton();
			selectedRow--;
			selectedColumn = Math.min(selectedColumn, keyboardButtons[selectedRow].length - 1);
			focusCurrentButton();
		}
	}

	public void poll(final Input input) {
		if (anyChanges)
			for (final KeyboardButton[] row : keyboardButtons)
				for (final KeyboardButton kb : row)
					kb.poll(input);
	}

	public void press() {
		keyboardButtons[selectedRow][selectedColumn].press();
	}

	public void release() {
		for (final KeyboardButton[] row : keyboardButtons)
			for (final KeyboardButton kb : row)
				kb.release();
	}

	@Override
	public void setVisible(final boolean b) {
		super.setVisible(b);

		if (b)
			setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
	}

	public void toggleLock() {
		keyboardButtons[selectedRow][selectedColumn].toggleLock();
	}

	private void unfocusCurrentButton() {
		final KeyboardButton keyBoardButton = keyboardButtons[selectedRow][selectedColumn];

		keyBoardButton.setBorder(defaultButtonBorder);
	}

	protected void updateLocation() {
		pack();
		final Rectangle rectangle = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		final int x = (int) rectangle.getMaxX() / 2 - getWidth() / 2;
		final int y = (int) rectangle.getMaxY() - getHeight();
		setLocation(x, y);
	}

}