package Utility;

import GameClasses.CUtil;
import NateQuest.Debugger;

public class UtilityOscillator {

	float min, max, rate, value, timer = 0, accelerant;
	boolean isAbsoluteValued = false;

	public UtilityOscillator(float min, float max, float rate, int accelerant) {
		this.max = max;
		this.min = min;
		this.rate = rate;
		this.accelerant = accelerant;
	}

	public UtilityOscillator(float min, float max, float rate) {
		this(min, max, rate, 0);
	}

	public void update() {
		timer += CUtil.ElapsedTime;
		float percent = (timer % rate) / rate;
		float sin = (float) Math.sin(percent * Math.PI * 2); // -1 to 1
		if (accelerant > 0) {
			sin = accelerateSine(sin);
		}
		sin = (sin / 2) + 0.5f; // 0 to 1
		if (isAbsoluteValued) {
			sin = Math.round(sin);
		}
		float range = max - min;
		value = min + (range * sin);
	}

	public float accelerateSine(float sin) {
		float polarity = 0;
		if (sin > 0) {
			polarity = 1.0f;
		}
		if (sin < 0) {
			polarity = -1.0f;
		}
		for (int i = 0; i < accelerant; i++) {
			sin = (sin + polarity) / 2.0f;
		}
		return sin;
	}

	public float getValue() {
		return value;
	}

	public void setIsAbsoluteValued(boolean value) {
		isAbsoluteValued = value;
	}

	public boolean getHasDonePass() {
		return timer > rate;
	}

	public boolean getHasDonePasses(int passes) {
		return timer * passes > rate;
	}

	public boolean getHasDonePasses(float passes) {
		return timer * passes > rate;
	}
}
