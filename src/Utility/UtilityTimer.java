package Utility;

import GameClasses.CUtil;

public class UtilityTimer {
	float timer = 0, max;

	public UtilityTimer(float max) {
		this.max = max;
	}

	public void update() {
		timer += CUtil.ElapsedTime;
	}

	public boolean isUp() {
		return timer >= max;
	}

	public void reset() {
		timer = 0;
	}
}
