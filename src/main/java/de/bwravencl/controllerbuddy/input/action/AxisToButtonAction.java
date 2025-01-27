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

public final class AxisToButtonAction extends ToButtonAction<Float> implements ISuspendableAction {

	public static final float DEFAULT_MIN_AXIS_VALUE = 0.5f;
	public static final float DEFAULT_MAX_AXIS_VALUE = 1f;

	private float minAxisValue = 0.5f;
	private float maxAxisValue = 1f;

	@Override
	public void doAction(final Input input, final Float value) {
		final var down = !isSuspended() && value >= minAxisValue && value <= maxAxisValue;

		input.setButton(buttonId, down);
	}

	public float getMaxAxisValue() {
		return maxAxisValue;
	}

	public float getMinAxisValue() {
		return minAxisValue;
	}

	public void setMaxAxisValue(final float maxAxisValue) {
		this.maxAxisValue = maxAxisValue;
	}

	public void setMinAxisValue(final float minAxisValue) {
		this.minAxisValue = minAxisValue;
	}

}
