package de.bwravencl.RemoteStick.input.action;

import de.bwravencl.RemoteStick.input.Input;

public class AxisToRelativeAxisAction extends AxisToAxisAction {

	public static final String description = "Relative Axis";

	public final float DEFAULT_DEAD_ZONE = 0.25f;
	public final float DEFAULT_SENSITIVITY = 1.0f;

	private float deadZone = DEFAULT_DEAD_ZONE;
	private float sensitivity = DEFAULT_SENSITIVITY;

	public float getDeadZone() {
		return deadZone;
	}

	public void setDeadZone(float deadZone) {
		this.deadZone = deadZone;
	}

	public float getSensitivity() {
		return sensitivity;
	}

	public void setSensitivity(float sensitivity) {
		this.sensitivity = sensitivity;
	}

	@Override
	public void doAction(Input joystick, float value) {
		if (Math.abs(value) > deadZone) {
			final float d = value * sensitivity
					* (float) joystick.getServerThread().getUpdateRate()
					/ (float) 1000L;

			final float oldValue = Input.normalize(joystick.getAxis()[axisId],
					0.0f, joystick.getMaxAxisValue(), -1.0f, 1.0f);

			joystick.setAxis(axisId, oldValue + (invert ? -d : d));
		}
	}

	@Override
	public String toString() {
		return "Relative Axis";
	}

}