package NateQuest.Drawables;

import GameClasses.CUtil;
import GameClasses.Sprite;

public class Fader {

	Sprite target;
	float maxTimer;
	float timer = 0;
	public boolean isFading = false;
	boolean isFadingOut = false;

	public Fader(Sprite target) {
		this.target = target;
	}

	public void update() {
		if (isFading) {
			timer += CUtil.ElapsedTime;
			if (isFadingOut) {
				target.setAlpha(1.0f - (timer / maxTimer) * 1.0f);
			} else {
				target.setAlpha((timer / maxTimer) * 1.0f);
			}
			if (timer > maxTimer) {
				isFading = false;
			}
		}
	}

	public void fadeOut(float max) {
		maxTimer = max;
		this.timer = 0;
		isFading = true;
		isFadingOut = true;
		target.setAlpha(1);
	}

	public void fadeIn(float max) {
		maxTimer = max;
		this.timer = 0;
		isFading = true;
		isFadingOut = false;
		target.setAlpha(0);
	}

}
