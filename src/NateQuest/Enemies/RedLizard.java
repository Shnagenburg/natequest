package NateQuest.Enemies;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import NateQuest.GameScreen;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;

public class RedLizard extends Lizard {

	public RedLizard() {
		this(CUtil.ScreenGettingLoaded);
	}

	public RedLizard(GameScreen gs) {
		super(gs);
	}

	@Override
	protected void init() {
		FIRE_TIME = 130;
		MOVE_SPEED = 0.34f;
		MOVE_TIME = 1200;
		health = 30;
		SheetedSprite sprite = new SheetedSprite(
				CUtil.QuickImage("redlizard4frames"), 4, 150);
		drawable = new Drawable(this, sprite, LayerConstants.CHARACTERS);
		collidable = new CollidableCircle(this,
				(int) (drawable.mSprite.getHeight() * 0.75f));
		findTargetHero();
		startMoving();
	}

}
