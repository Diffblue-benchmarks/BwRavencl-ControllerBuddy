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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import de.bwravencl.controllerbuddy.input.LockKey;
import de.bwravencl.controllerbuddy.input.Mode;
import de.bwravencl.controllerbuddy.input.ScanCode;
import net.brockmatt.util.ResourceBundleUtil;

public class OnScreenKeyboard extends JFrame {

	private abstract class AbstractKeyboardButton extends JButton {

		private static final long serialVersionUID = 4567858619453576258L;

		public AbstractKeyboardButton(final String text) {
			super(text);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(55, 55);
		}

		protected abstract void poll(final Input input);

		protected abstract void press();

		protected abstract void release();

		protected abstract void toggleLock();

	}

	private class DefaultKeyboardButton extends AbstractKeyboardButton {

		private static final long serialVersionUID = -1739002089027358633L;

		private static final long MIN_REPEAT_PRESS_TIME = 150L;

		private boolean changed;

		private boolean doDownUp;

		private long beginPress;

		private final String scanCodeName;

		private final KeyStroke keyStroke;

		private TimerTask lockTimerTask;

		public DefaultKeyboardButton(final String scanCodeName) {
			super(scanCodeName);

			this.scanCodeName = scanCodeName;

			final Integer[] keyCodes;
			final Integer[] modifierCodes;

			if (ScanCode.LEFT_ALT.equals(scanCodeName) || ScanCode.RIGHT_ALT.equals(scanCodeName)
					|| ScanCode.LEFT_SHIFT.equals(scanCodeName) || ScanCode.RIGHT_SHIFT.equals(scanCodeName)
					|| ScanCode.LEFT_CONTROL.equals(scanCodeName) || ScanCode.RIGHT_CONTROL.equals(scanCodeName)
					|| ScanCode.LEFT_WINDOWS.equals(scanCodeName) || ScanCode.RIGHT_WINDOWS.equals(scanCodeName)) {
				keyCodes = new Integer[0];
				modifierCodes = new Integer[] { ScanCode.nameToScanCodeMap.get(scanCodeName) };
			} else {
				keyCodes = new Integer[] { ScanCode.nameToScanCodeMap.get(scanCodeName) };
				modifierCodes = new Integer[0];
			}

			keyStroke = new KeyStroke(keyCodes, modifierCodes);

			addChangeListener(new ChangeListener() {

				private boolean lastPressed;

				@Override
				public void stateChanged(final ChangeEvent e) {
					final boolean pressed = getModel().isPressed();
					if (pressed != lastPressed) {
						if (pressed) {
							beginPress = System.currentTimeMillis();
							press();

							if (lockTimerTask != null)
								lockTimerTask.cancel();
							lockTimerTask = new TimerTask() {

								@Override
								public void run() {
									if (heldButtons.contains(DefaultKeyboardButton.this)) {
										DefaultKeyboardButton.this.setForeground(Color.GRAY);
										press();
									}
								}

							};
							Main.getTimer().schedule(lockTimerTask, MIN_REPEAT_PRESS_TIME);
						} else {
							if (System.currentTimeMillis() - beginPress < MIN_REPEAT_PRESS_TIME) {
								release();
								releaseAllAfterPoll = true;

							} else
								DefaultKeyboardButton.this.setForeground(KEYBOARD_BUTTON_DEFAULT_FOREGROUND);

							lockTimerTask.cancel();
						}

						lastPressed = pressed;
					}
				}
			});
		}

		@Override
		public Dimension getPreferredSize() {
			final Dimension preferredSize = super.getPreferredSize();

			if (ScanCode.TAB.equals(scanCodeName))
				preferredSize.width *= 1.5;
			else if (ScanCode.RIGHT_SHIFT.equals(scanCodeName) || ScanCode.BACK_SLASH.equals(scanCodeName)
					|| ScanCode.NUM_PAD0.equals(scanCodeName))
				preferredSize.width *= 2;
			else if (ScanCode.LEFT_SHIFT.equals(scanCodeName) || ScanCode.RETURN.equals(scanCodeName)
					|| ScanCode.BACK_SPACE.equals(scanCodeName))
				preferredSize.width *= 2.5;
			else if (ScanCode.SPACE.equals(scanCodeName))
				preferredSize.width *= 5.5;

			return preferredSize;
		}

		@Override
		protected void poll(final Input input) {
			if (!changed)
				return;

			if (doDownUp) {
				input.getDownUpKeyStrokes().add(keyStroke);
				doDownUp = false;
			}

			final Set<KeyStroke> downKeyStrokes = input.getDownKeyStrokes();

			if (heldButtons.contains(this)) {
				if (System.currentTimeMillis() - beginPress >= MIN_REPEAT_PRESS_TIME)
					downKeyStrokes.add(keyStroke);
			} else
				downKeyStrokes.remove(keyStroke);

			changed = false;
		}

		@Override
		protected void press() {
			setBackground(KEYBOARD_BUTTON_HELD_BACKGROUND);

			if (heldButtons.add(this))
				beginPress = System.currentTimeMillis();

			changed = true;
			anyChanges = true;
		}

		@Override
		protected void release() {
			setBackground(KEYBOARD_BUTTON_DEFAULT_BACKGROUND);
			if (heldButtons.remove(this)) {
				if (System.currentTimeMillis() - beginPress < MIN_REPEAT_PRESS_TIME)
					doDownUp = true;

				beginPress = 0L;
				changed = true;
				anyChanges = true;
			}
		}

		@Override
		protected void toggleLock() {
			if (heldButtons.contains(this))
				release();
			else {
				press();
				beginPress = 0L;
			}
		}

	}

	private class LockKeyButton extends AbstractKeyboardButton {

		private static final long serialVersionUID = 4014130700331413635L;

		private boolean changed;

		private boolean locked;

		private final int virtualKeyCode;

		private boolean wasUp = true;

		public LockKeyButton(final int virtualKeyCode) {
			super(LockKey.virtualKeyCodeToLockKeyMap.get(virtualKeyCode).name);
			this.virtualKeyCode = virtualKeyCode;

			addActionListener(arg0 -> {
				toggleLock();
			});
		}

		@Override
		public Dimension getPreferredSize() {
			final Dimension preferredSize = super.getPreferredSize();

			if (virtualKeyCode == KeyEvent.VK_CAPS_LOCK)
				preferredSize.width *= 2;

			return preferredSize;
		}

		@Override
		protected void poll(final Input input) {
			if (!changed)
				return;

			if (locked)
				input.getOnLockKeys().add(virtualKeyCode);
			else
				input.getOffLockKeys().add(virtualKeyCode);

			changed = false;
		}

		@Override
		protected void press() {
			if (wasUp) {
				toggleLock();
				wasUp = false;
			}
		}

		@Override
		protected void release() {
			wasUp = true;
		}

		@Override
		protected void toggleLock() {
			locked = !locked;
			setForeground(locked ? Color.GREEN : KEYBOARD_BUTTON_DEFAULT_FOREGROUND);
			changed = true;
			anyChanges = true;
		}

	}

	private static final Color KEYBOARD_BUTTON_DEFAULT_BACKGROUND = new JButton().getBackground();

	private static final Color KEYBOARD_BUTTON_DEFAULT_FOREGROUND = new JButton().getForeground();

	private static final Color KEYBOARD_BUTTON_HELD_BACKGROUND = new Color(128, 128, 128);

	private static final Set<AbstractKeyboardButton> heldButtons = ConcurrentHashMap.newKeySet();

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

	private final AbstractKeyboardButton[][] keyboardButtons = {
			{ new DefaultKeyboardButton(ScanCode.ESCAPE), new DefaultKeyboardButton(ScanCode.F1),
					new DefaultKeyboardButton(ScanCode.F2), new DefaultKeyboardButton(ScanCode.F3),
					new DefaultKeyboardButton(ScanCode.F4), new DefaultKeyboardButton(ScanCode.F5),
					new DefaultKeyboardButton(ScanCode.F6), new DefaultKeyboardButton(ScanCode.F7),
					new DefaultKeyboardButton(ScanCode.F8), new DefaultKeyboardButton(ScanCode.F9),
					new DefaultKeyboardButton(ScanCode.F10), new DefaultKeyboardButton(ScanCode.F11),
					new DefaultKeyboardButton(ScanCode.F12), new DefaultKeyboardButton(ScanCode.SYS_RQ),
					new LockKeyButton(KeyEvent.VK_SCROLL_LOCK), new DefaultKeyboardButton(ScanCode.PAUSE) },
			{ new DefaultKeyboardButton(ScanCode.GRAVE), new DefaultKeyboardButton(ScanCode.D1),
					new DefaultKeyboardButton(ScanCode.D2), new DefaultKeyboardButton(ScanCode.D3),
					new DefaultKeyboardButton(ScanCode.D4), new DefaultKeyboardButton(ScanCode.D5),
					new DefaultKeyboardButton(ScanCode.D6), new DefaultKeyboardButton(ScanCode.D7),
					new DefaultKeyboardButton(ScanCode.D8), new DefaultKeyboardButton(ScanCode.D9),
					new DefaultKeyboardButton(ScanCode.D0), new DefaultKeyboardButton(ScanCode.MINUS),
					new DefaultKeyboardButton(ScanCode.EQUALS), new DefaultKeyboardButton(ScanCode.BACK_SPACE),
					new LockKeyButton(KeyEvent.VK_NUM_LOCK), new DefaultKeyboardButton(ScanCode.DIVIDE),
					new DefaultKeyboardButton(ScanCode.MULTIPLY), new DefaultKeyboardButton(ScanCode.NUM_PAD_MINUS) },
			{ new DefaultKeyboardButton(ScanCode.TAB), new DefaultKeyboardButton(ScanCode.Q),
					new DefaultKeyboardButton(ScanCode.W), new DefaultKeyboardButton(ScanCode.E),
					new DefaultKeyboardButton(ScanCode.R), new DefaultKeyboardButton(ScanCode.T),
					new DefaultKeyboardButton(ScanCode.Y), new DefaultKeyboardButton(ScanCode.U),
					new DefaultKeyboardButton(ScanCode.I), new DefaultKeyboardButton(ScanCode.O),
					new DefaultKeyboardButton(ScanCode.P), new DefaultKeyboardButton(ScanCode.LEFT_BRACKET),
					new DefaultKeyboardButton(ScanCode.RIGHT_BRACKET), new DefaultKeyboardButton(ScanCode.BACK_SLASH),
					new DefaultKeyboardButton(ScanCode.NUM_PAD7), new DefaultKeyboardButton(ScanCode.NUM_PAD8),
					new DefaultKeyboardButton(ScanCode.NUM_PAD9), new DefaultKeyboardButton(ScanCode.NUM_PAD_PLUS) },
			{ new LockKeyButton(KeyEvent.VK_CAPS_LOCK), new DefaultKeyboardButton(ScanCode.A),
					new DefaultKeyboardButton(ScanCode.S), new DefaultKeyboardButton(ScanCode.D),
					new DefaultKeyboardButton(ScanCode.F), new DefaultKeyboardButton(ScanCode.G),
					new DefaultKeyboardButton(ScanCode.H), new DefaultKeyboardButton(ScanCode.J),
					new DefaultKeyboardButton(ScanCode.K), new DefaultKeyboardButton(ScanCode.L),
					new DefaultKeyboardButton(ScanCode.SEMI_COLON), new DefaultKeyboardButton(ScanCode.APOSTROPHE),
					new DefaultKeyboardButton(ScanCode.RETURN), new DefaultKeyboardButton(ScanCode.NUM_PAD4),
					new DefaultKeyboardButton(ScanCode.NUM_PAD5), new DefaultKeyboardButton(ScanCode.NUM_PAD6) },
			{ new DefaultKeyboardButton(ScanCode.LEFT_SHIFT), new DefaultKeyboardButton(ScanCode.Z),
					new DefaultKeyboardButton(ScanCode.X), new DefaultKeyboardButton(ScanCode.C),
					new DefaultKeyboardButton(ScanCode.V), new DefaultKeyboardButton(ScanCode.B),
					new DefaultKeyboardButton(ScanCode.N), new DefaultKeyboardButton(ScanCode.M),
					new DefaultKeyboardButton(ScanCode.COMMA), new DefaultKeyboardButton(ScanCode.PERIOD),
					new DefaultKeyboardButton(ScanCode.SLASH), new DefaultKeyboardButton(ScanCode.RIGHT_SHIFT),
					new DefaultKeyboardButton(ScanCode.NUM_PAD1), new DefaultKeyboardButton(ScanCode.NUM_PAD2),
					new DefaultKeyboardButton(ScanCode.NUM_PAD3) },
			{ new DefaultKeyboardButton(ScanCode.LEFT_CONTROL), new DefaultKeyboardButton(ScanCode.LEFT_WINDOWS),
					new DefaultKeyboardButton(ScanCode.LEFT_ALT), new DefaultKeyboardButton(ScanCode.SPACE),
					new DefaultKeyboardButton(ScanCode.RIGHT_ALT), new DefaultKeyboardButton(ScanCode.RIGHT_WINDOWS),
					new DefaultKeyboardButton(ScanCode.RIGHT_CONTROL), new DefaultKeyboardButton(ScanCode.UP_ARROW),
					new DefaultKeyboardButton(ScanCode.DOWN_ARROW), new DefaultKeyboardButton(ScanCode.LEFT_ARROW),
					new DefaultKeyboardButton(ScanCode.RIGHT_ARROW), new DefaultKeyboardButton(ScanCode.NUM_PAD0),
					new DefaultKeyboardButton(ScanCode.NUM_PAD_COMMA),
					new DefaultKeyboardButton(ScanCode.NUM_PAD_ENTER) } };

	private boolean anyChanges;
	private boolean releaseAllAfterPoll;

	private int selectedRow;
	private int selectedColumn;

	public OnScreenKeyboard() {
		setType(JFrame.Type.UTILITY);
		setFocusableWindowState(false);
		setUndecorated(true);
		setBackground(Main.TRANSPARENT);
		setAlwaysOnTop(true);

		final JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));
		parentPanel.setBackground(Main.TRANSPARENT);

		for (int row = 0; row < keyboardButtons.length; row++) {
			final FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
			flowLayout.setHgap(0);
			flowLayout.setVgap(0);
			final JPanel rowPanel = new JPanel(flowLayout);
			rowPanel.setBackground(ROW_BACKGROUND);
			rowPanel.setBorder(
					BorderFactory.createEmptyBorder(row == 0 ? 5 : 0, 5, row == keyboardButtons.length - 1 ? 5 : 0, 5));

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
		if (anyChanges) {
			anyChanges = false;

			for (final AbstractKeyboardButton[] row : keyboardButtons)
				for (final AbstractKeyboardButton kb : row)
					kb.poll(input);
		}

		if (releaseAllAfterPoll) {
			releaseAll();
			releaseAllAfterPoll = false;
		}
	}

	public void pressSelected() {
		keyboardButtons[selectedRow][selectedColumn].press();
	}

	private void releaseAll() {
		for (final AbstractKeyboardButton[] row : keyboardButtons)
			for (final AbstractKeyboardButton kb : row)
				kb.release();
	}

	public void releaseSelected() {
		keyboardButtons[selectedRow][selectedColumn].release();
		releaseAllAfterPoll = true;
	}

	@Override
	public void setVisible(final boolean b) {
		super.setVisible(b);

		if (b)
			setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
		else
			releaseAll();
	}

	public void toggleLock() {
		keyboardButtons[selectedRow][selectedColumn].toggleLock();
	}

	private void unfocusCurrentButton() {
		final AbstractKeyboardButton keyBoardButton = keyboardButtons[selectedRow][selectedColumn];

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