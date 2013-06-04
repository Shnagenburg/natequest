package NateQuest.VFX;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;

public class WarpEffect extends VisualFX {

	final float SPIN_SPEED = 0.8f;
	float angle = 0;
	int passes = 0;
	float timer = 0;
	final float MAX_TIMER = 1000;
	public boolean isDoneAnimating = false;

	public WarpEffect(GameScreen gs, SheetedSprite sprite, Vector2f position) {
		super(gs, position);
		drawable = new Drawable(this, sprite, LayerConstants.TILEMAP_OVERLAY);
		drawable.mSprite.setPositionRounded(position.x, position.y);
	}

	public WarpEffect(GameScreen gs, Sprite sprite, Vector2f position) {
		super(gs, position);
		drawable = new Drawable(this, sprite, LayerConstants.TILEMAP_OVERLAY);
		drawable.mSprite.setPositionRounded(position.x, position.y);
	}

	public void update() {
		super.update();
		timer += CUtil.ElapsedTime;
		if (timer > MAX_TIMER) {
			isDoneAnimating = true;
		}
		angle += CUtil.ElapsedTime * SPIN_SPEED;
		drawable.mSprite.setRotation(angle);
	}

}
