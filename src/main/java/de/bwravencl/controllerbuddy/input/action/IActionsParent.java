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

import java.util.ArrayList;
import java.util.List;

public interface IActionsParent extends IButtonToAction {

	public static final int UNLIMITED_MAX_ACTIONS = -1;

	default public void cloneActions(final IActionsParent actionsParent, final IActionsParent clonedActionsParent)
			throws CloneNotSupportedException {
		final List<IAction> clonedActions = new ArrayList<>();
		for (final IAction a : actionsParent.getActions())
			clonedActions.add((IAction) a.clone());
		clonedActionsParent.setActions(clonedActions);
	}

	List<IAction> getActions();

	int getMaxActions();

	void setActions(final List<IAction> actions);

}
