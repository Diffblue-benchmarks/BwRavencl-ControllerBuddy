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

import de.bwravencl.controllerbuddy.input.Input;
import de.bwravencl.controllerbuddy.input.Mode;
import net.java.games.input.Component;

public class ButtonToWheelAction implements IActionsParent {

	private static final int MAX_ACTIONS = 8;

	private static void suspendComponent(final Mode mode, final String componentName) {
		final List<IAction> actions = mode.getComponentToActionsMap().get(componentName);
		for (final IAction a : actions)
			if (a instanceof ISuspendableAction)
				((ISuspendableAction) a).suspend(componentName);
	}

	private transient boolean wasUp = true;

	private transient boolean open = false;

	private float activationValue = DEFAULT_ACTIVATION_VALUE;

	private String xAxis;

	private String yAxis;

	private List<IAction> actions = new ArrayList<>();

	private transient IAction selectedAction;

	private transient Component xComponent;

	private transient Component yComponent;

	@Override
	public Object clone() throws CloneNotSupportedException {
		final ButtonToWheelAction wheelAction = (ButtonToWheelAction) super.clone();
		wheelAction.setxAxis(xAxis);
		wheelAction.setyAxis(yAxis);
		cloneActions(this, wheelAction);

		return wheelAction;
	}

	@Override
	public void doAction(final Input input, final float value) {
		if (xComponent == null)
			for (final Component c : Input.getComponents(input.getController()))
				if (c.getIdentifier().getName().equals(xAxis)) {
					xComponent = c;
					break;
				}

		if (yComponent == null)
			for (final Component c : Input.getComponents(input.getController()))
				if (c.getIdentifier().getName().equals(yAxis)) {
					yComponent = c;
					break;
				}

		final boolean active = value >= activationValue;
		handleAxisSuspension(active);

		if (active && wasUp) {
			if (!open) {
				System.out.println("Open Wheel!");
				open = true;
			}
			if (xComponent != null && yComponent != null)
				selectedAction = getActionFromCoordinates(xComponent.getPollData(), yComponent.getPollData());
			else
				selectedAction = null;
			System.out.println("Selected Action " + selectedAction);
			wasUp = false;
		} else {
			if (selectedAction != null && !wasUp) {
				selectedAction.doAction(input, 1.0f);
				System.out.println("Do Action " + selectedAction);
			}

			selectedAction = null;
			if (open) {
				System.out.println("Close Wheel!");
				open = false;
			}
			wasUp = true;
		}
	}

	private IAction getActionFromCoordinates(final float x, final float y) {
		final int index = getIndexFromCoordinates(x, y);
		if (index < 0)
			return null;
		else
			return actions.get(index);
	}

	@Override
	public List<IAction> getActions() {
		return actions;
	}

	@Override
	public float getActivationValue() {
		return activationValue;
	}

	private int getIndexFromCoordinates(final float x, final float y) {
		final boolean isLeftXThird = x < -1.0f / 3.0f;
		final boolean isRightXThird = x > 1.0f / 3.0f;
		final boolean isUpperYThird = y < -1.0f / 3.0f;
		final boolean isLowerYThird = y > 1.0f / 3.0f;

		if (isLarge()) {
			if (isUpperYThird) {
				if (isLeftXThird)
					return 0;
				else if (isRightXThird)
					return 2;
				else
					return 1;
			} else if (isLowerYThird) {
				if (isLeftXThird)
					return 6;
				else if (isRightXThird)
					return 5;
				else
					return 4;
			} else if (isLeftXThird)
				return 7;
			else if (isRightXThird)
				return -1;
			else
				return 3;
		} else {
			if (!isUpperYThird && !isLowerYThird && !isLeftXThird && !isRightXThird)
				return -1;

			final boolean isLeftHalf = x < 0.0f;

			if (x < -0.5f) {
				if (isLeftHalf)
					return 0;
				else
					return 1;
			} else if (isLeftHalf)
				return 3;
			else
				return 2;
		}
	}

	@Override
	public int getMaxActions() {
		return MAX_ACTIONS;
	}

	public String getxAxis() {
		return xAxis;
	}

	public String getyAxis() {
		return yAxis;
	}

	private void handleAxisSuspension(final boolean suspend) {
		for (final ButtonToModeAction buttonToModeAction : ButtonToModeAction.getButtonToModeActionStack()) {
			final Mode mode = buttonToModeAction.getMode();
			suspendComponent(mode, xAxis);
			suspendComponent(mode, yAxis);
		}
	}

	private boolean isLarge() {
		return actions.size() > 4;
	}

	@Override
	public void setActions(final List<IAction> actions) {
		while (actions.size() > getMaxActions())
			actions.remove(actions.size() - 1);

		this.actions = actions;
	}

	@Override
	public void setActivationValue(final Float activationValue) {
		this.activationValue = activationValue;
	}

	public void setxAxis(final String xAxis) {
		this.xAxis = xAxis;
	}

	public void setyAxis(final String yAxis) {
		this.yAxis = yAxis;
	}

	@Override
	public String toString() {
		return rb.getString("BUTTON_TO_WHEEL_ACTION_STRING");
	}

}
