package de.bwravencl.RemoteStick.input.action;

import java.util.ArrayList;
import java.util.List;

import de.bwravencl.RemoteStick.input.Input;

public class CycleAction implements IAction {

	private boolean wasUp = true;
	private int index = 0;
	private List<IAction> actions = new ArrayList<IAction>();

	public List<IAction> getActions() {
		return actions;
	}

	public void setActions(List<IAction> actions) {
		this.actions = actions;
	}

	@Override
	public void doAction(Input joystick, float value) {
		if (value < 0.5)
			wasUp = true;
		else if (wasUp) {
			actions.get(index).doAction(joystick, value);

			if (index == actions.size() - 1)
				index = 0;
			else
				index++;

			wasUp = false;
		}
	}
	
	@Override
	public String toString() {
		return "Cycle";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		final CycleAction cycleAction = (CycleAction) super.clone();

		final List<IAction> clonedActions = new ArrayList<IAction>();
		for (IAction a : actions)
			clonedActions.add((IAction) a.clone());
		cycleAction.setActions(clonedActions);

		return cycleAction;
	}
}