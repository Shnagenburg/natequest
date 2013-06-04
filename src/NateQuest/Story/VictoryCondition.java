package NateQuest.Story;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Hero;

public class VictoryCondition extends Entity {

	float MAX_WAIT_TIME = 5000;
	Hero hero;
	float timer = 0;

	public VictoryCondition() {
		this(CUtil.ScreenGettingLoaded);
	}

	public VictoryCondition(GameScreen gs) {
		super(gs);

		drawable = new Drawable(this, new Sprite("puchie"),
				LayerConstants.TILEMAP_OVERLAY);
		collidable = new Collidable(this);
		collidable.matchSprite(drawable.mSprite);
		collidable.mask = CollidableMasks.EVENT;

	}

	public void update() {
		grabHero();
		hero.bIsSleeping = true;

		timer += CUtil.ElapsedTime;
		if (timer > MAX_WAIT_TIME) {
			CUtil.GameHandle.showVictoryScreen();
		}

	}

	public void grabHero() {
		if (hero == null) {
			hero = (Hero) parentScreen.findEntity(Hero.class);
		}
	}
}
