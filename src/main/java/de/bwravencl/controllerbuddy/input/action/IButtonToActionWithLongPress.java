/* Copyright (C) 2017  Matteo Hausner
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

import java.util.List;

import de.bwravencl.controllerbuddy.input.Input;
import de.bwravencl.controllerbuddy.input.Profile;

public interface IButtonToActionWithLongPress extends IButtonToAction {

	static final long MIN_LONG_PRESS_TIME = 1000L;
	static final boolean DEFAULT_LONG_PRESS = false;

	default float handleLongPress(final float value) {
		final float activationValue = getActivationValue();

		if (isLongPress()) {
			final long currentTime = System.currentTimeMillis();

			if (value == activationValue) {
				if (!actionToDownSinceMap.containsKey(this))
					actionToDownSinceMap.put(this, currentTime);
				else if (currentTime - actionToDownSinceMap.get(this) >= MIN_LONG_PRESS_TIME)
					return value;
			} else if (actionToDownSinceMap.containsKey(this)) {
				if (currentTime - actionToDownSinceMap.get(this) >= MIN_LONG_PRESS_TIME) {
					for (final List<IAction> actions : Input.getProfile().getActiveMode().getComponentToActionsMap()
							.values())
						if (actions.contains(this)) {
							actionToWasDown.removeAll(actions);
							break;
						}

					if (!Profile.isDefaultMode(Input.getProfile().getActiveMode()))
						for (final List<IAction> actions : Input.getProfile().getModes().get(0)
								.getComponentToActionsMap().values())
							if (actions.contains(this)) {
								actionToWasDown.removeAll(actions);
								break;
							}
				}
				actionToDownSinceMap.remove(this);
			}

			return activationValue - 1.0f;
		} else if (IButtonToAction.isDownUpAction(this)) {
			if (value == activationValue)
				actionToWasDown.add(this);
			else if (actionToWasDown.contains(this)) {
				actionToWasDown.remove(this);
				return activationValue;
			}

			return activationValue - 1.0f;
		} else
			return value;
	}

	boolean isLongPress();

	void setLongPress(Boolean longPress);
}
