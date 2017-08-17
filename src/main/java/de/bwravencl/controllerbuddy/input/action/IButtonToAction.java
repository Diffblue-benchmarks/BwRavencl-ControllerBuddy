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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface IButtonToAction extends IAction {

	static final float DEFAULT_ACTIVATION_VALUE = 1.0f;
	static final Set<IButtonToAction> actionToWasDown = new HashSet<>();
	static final Map<IButtonToAction, Long> actionToDownSinceMap = new HashMap<>();

	static boolean isDownUpAction(final IAction action) {
		if (action instanceof ToKeyAction) {
			final ToKeyAction toKeyAction = (ToKeyAction) action;
			return toKeyAction.isDownUp();
		} else if (action instanceof ToMouseButtonAction) {
			final ToMouseButtonAction toMouseButtonAction = (ToMouseButtonAction) action;
			return toMouseButtonAction.isDownUp();
		} else if (action instanceof ButtonToCycleAction)
			return true;

		return false;
	}

	float getActivationValue();

	void setActivationValue(Float activationValue);

}
