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
import de.bwravencl.controllerbuddy.input.Mode;

public final class AxisToCursorAction extends InvertableAction<Float>
		implements ISuspendableAction, IModeChangeListenerAction {

	public enum MouseAxis {
		X, Y
	}

	public static final float DEFAULT_DEAD_ZONE = 0.15f;
	public static final float DEFAULT_EXPONENT = 2f;
	public static final float DEFAULT_MAX_CURSOR_SPEED = 2000f;

	private float deadZone = DEFAULT_DEAD_ZONE;
	private float exponent = DEFAULT_EXPONENT;
	private float maxCursorSpeed = DEFAULT_MAX_CURSOR_SPEED;
	private MouseAxis axis = MouseAxis.X;
	private transient long lastCallTime;
	private transient float remainingD = 0f;

	@Override
	public void doAction(final Input input, final Float value) {
		final var currentTime = System.currentTimeMillis();
		var elapsedTime = input.getOutputThread().getPollInterval();
		if (lastCallTime > 0L)
			elapsedTime = currentTime - lastCallTime;
		lastCallTime = currentTime;

		if (!isSuspended() && Math.abs(value) > deadZone) {
			final var rateMultiplier = (float) elapsedTime / (float) 1000L;

			var d = Input.normalize(Math.signum(value) * (float) Math.pow(Math.abs(value) * 100f, exponent),
					(float) -Math.pow(100f, exponent), (float) Math.pow(100f, exponent), -maxCursorSpeed,
					maxCursorSpeed) * rateMultiplier;

			d = invert ? -d : d;
			d += remainingD;

			if (d >= -1f && d <= 1f)
				remainingD = d;
			else {
				remainingD = 0f;

				if (axis.equals(MouseAxis.X))
					input.setCursorDeltaX((int) (input.getCursorDeltaX() + d));
				else
					input.setCursorDeltaY((int) (input.getCursorDeltaY() + d));
			}
		} else
			lastCallTime = 0L;
	}

	public MouseAxis getAxis() {
		return axis;
	}

	public float getDeadZone() {
		return deadZone;
	}

	public float getExponent() {
		return exponent;
	}

	public float getMaxCursorSpeed() {
		return maxCursorSpeed;
	}

	@Override
	public void onModeChanged(final Mode newMode) {
		for (final var actions : newMode.getAxisToActionsMap().values())
			if (actions.contains(this)) {
				lastCallTime = 0L;
				break;
			}
	}

	public void setAxis(final MouseAxis axis) {
		this.axis = axis;
	}

	public void setDeadZone(final float deadZone) {
		this.deadZone = deadZone;
	}

	public void setExponent(final float exponent) {
		this.exponent = exponent;
	}

	public void setMaxCursorSpeed(final float maxCursorSpeed) {
		this.maxCursorSpeed = maxCursorSpeed;
	}

	@Override
	public String toString() {
		return rb.getString("AXIS_TO_CURSOR_ACTION_STRING");
	}

}
