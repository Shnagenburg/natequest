package NateQuest.Weapons;

import GameClasses.CUtil;
import GameClasses.MouseManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Gameplay.Crossheir;
import NateQuest.Gameplay.Hero;

public class Sword extends Entity {

	final float SWING_TIME = 1000;

	Drawable drawable;
	Sprite swordSprite;
	Hero parentHero;
	Crossheir crossheir;
	boolean isSwinging;
	float timer;
	float angle;

	public Sword() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Sword(GameScreen parent) {
		super(parent);
		this.parentHero = parentHero;
		swordSprite = new Sprite(CUtil.QuickImage("dude"));
		swordSprite.setScale(1.0f);
		isSwinging = false;
		timer = 0;
		getCrossHeir();
		angle = 0;
	}

	public void update() {
		if (isSwinging) {
			angle += CUtil.ElapsedTime * 0.2f;
			drawable.mSprite.setRotation(angle);
		}
		if (MouseManager.getUpDownLeftMouseClick()) {
			if (crossheir == null)
				getCrossHeir();
			if (crossheir != null)
				swingSword();
		}
	}

	private void swingSword() {
		isSwinging = true;
		drawable = new Drawable(this, swordSprite, 3);
		drawable.mSprite.setPositionRounded(parentHero.position.x,
				parentHero.position.y);
		Debugger.print("sdas");
	}

	public void getCrossHeir() {
		for (Entity e : parentScreen.entities) {
			if (e.getClass() == Crossheir.class) {
				crossheir = (Crossheir) e;
				Debugger.print("got crossheir");
				return;
			}
		}
		Debugger.print("couldnt find a crossheir!");
	}
}
