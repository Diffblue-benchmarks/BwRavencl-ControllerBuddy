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

public abstract class ToMouseButtonAction<V extends Number> implements IAction<V> {

	public static final int DEFAULT_MOUSE_BUTTON = 1;

	boolean downUp = false;
	transient boolean wasUp = true;
	transient boolean initiator = false;

	int mouseButton = DEFAULT_MOUSE_BUTTON;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public int getMouseButton() {
		return mouseButton;
	}

	public boolean isDownUp() {
		return downUp;
	}

	public void setDownUp(final boolean downUp) {
		this.downUp = downUp;
	}

	public void setMouseButton(final int mouseButton) {
		this.mouseButton = mouseButton;
	}

	@Override
	public String toString() {
		return rb.getString("TO_MOUSE_BUTTON_ACTION_STRING");
	}

}
