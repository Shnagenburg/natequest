package NateQuest.Gameplay;

import java.io.Serializable;

import GameClasses.CUtil;
import GameClasses.MouseManager;
import GameClasses.Sprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;

public class Crossheir extends Entity {

	Hero hero;

	private Crossheir() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Crossheir(GameScreen gs) {
		super(gs);
		// TODO Auto-generated constructor stub
		drawable = new Drawable(this, new Sprite(
				CUtil.ResourcePool.getImageByName("crossheir")),
				LayerConstants.HUD);
		getHero();
		gs.crossheir = this;
		saveThisEntity = false;
	}

	private void getHero() {
		for (Entity e : parentScreen.entities) {
			if (e.getClass() == Hero.class) {
				hero = (Hero) e;
				return;
			}
		}
	}

	public void update() {
		position.x = MouseManager.getCameredX();
		position.y = MouseManager.getCameredY();
		// Debugger.print("" + drawable);
		// Debugger.print("" + drawable.mSprite);
		drawable.mSprite.setPositionRounded(position.x, position.y);
		parentScreen.camera.update(drawable.mSprite);

		if (hero == null)
			getHero();
		setHeroAngle();
	}

	public void setHeroAngle() {
		float angle = (float) Math.atan((position.y - hero.position.y)
				/ (position.x - hero.position.x));
		angle = (float) Math.toDegrees(angle)
				- (position.x >= hero.position.x ? 45 : 225);
		hero.drawable.setRotation(angle);
	}

	@Override
	public void attach(GameScreen parentScreen) {
		super.attach(parentScreen);
		parentScreen.crossheir = this;
	}

}
