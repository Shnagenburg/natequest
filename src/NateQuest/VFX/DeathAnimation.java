package NateQuest.VFX;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;

public class DeathAnimation extends VisualFX {

	final float SPIN_SPEED = 0.8f;
	float angle = 0;
	int passes = 0;

	public DeathAnimation(GameScreen gs, SheetedSprite sprite, Vector2f position) {
		super(gs, position);
		drawable = new Drawable(this, sprite, 2);
		drawable.mSprite.setPositionRounded(position.x, position.y);
	}

	public void update() {
		super.update();
		angle += CUtil.ElapsedTime * SPIN_SPEED;
		drawable.mSprite.setRotation(angle);
	}
}
