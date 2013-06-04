package NateQuest.VFX;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import NateQuest.GameScreen;
import NateQuest.Drawables.DrawableWithText;
import NateQuest.Drawables.LayerConstants;

public class DamageNumber extends VisualFX {

	final float MOVE_SPEED = 0.02f;
	final float TIMER_MAX = 1000;
	float timer = 0;

	public DamageNumber(GameScreen gs, Vector2f pos, int damage) {
		super(gs, pos);
		saveThisEntity = false;
		DrawableWithText dwt = new DrawableWithText(this, null,
				LayerConstants.HUD, "-" + damage);
		dwt.setTextColor(Color.red);
		drawable = dwt;
		drawable.setPositionRounded(pos.x, pos.y);
		drawable.setIsCamerad(true);
	}

	public void update() {
		timer += CUtil.ElapsedTime;
		if (timer > TIMER_MAX) {
			this.markForDeletion();
		}
		position.y -= CUtil.ElapsedTime * MOVE_SPEED;
		drawable.setPositionRounded(position.x, position.y);
	}

}
