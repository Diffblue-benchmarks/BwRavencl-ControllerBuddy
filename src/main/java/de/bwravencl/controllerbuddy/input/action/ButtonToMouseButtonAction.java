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

package de.bwravencl.controllerbuddy.input.action;

import de.bwravencl.controllerbuddy.input.Input;

public final class ButtonToMouseButtonAction extends ToMouseButtonAction<Byte> implements IButtonToAction {

	private boolean longPress = DEFAULT_LONG_PRESS;

	@Override
	public void doAction(final Input input, Byte value) {
		value = handleLongPress(input, value);

		if (value == 0) {
			if (downUp)
				wasUp = true;
			else if (initiator) {
				input.getDownMouseButtons().remove(mouseButton);
				initiator = false;
			}
		} else if (downUp) {
			if (wasUp) {
				input.getDownUpMouseButtons().add(mouseButton);
				initiator = true;
				wasUp = false;
			}
		} else {
			input.getDownMouseButtons().add(mouseButton);
			initiator = true;
		}
	}

	@Override
	public boolean isLongPress() {
		return longPress;
	}

	@Override
	public void setLongPress(final boolean longPress) {
		this.longPress = longPress;
	}

}
