package NateQuest.Gameplay;

import java.io.Serializable;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import NateQuest.Entity;
import NateQuest.GameScreen;

public class HeroSwitcher extends Entity {

	Hero heroOne;
	Hero heroTwo;

	public HeroSwitcher() {
		this(CUtil.ScreenGettingLoaded);
	}

	public HeroSwitcher(GameScreen gs) {
		super(gs);
		// TODO Auto-generated constructor stub

		heroOne = new Hero(gs);
		heroOne.bIsSleeping = true;

		heroTwo = new Hero(gs);
		heroTwo.bIsSleeping = true;
	}

	public void update() {
		if (KeyManager.HitSpace()) {
			if (heroOne.bIsSleeping) {
				heroOne.bIsSleeping = false;
				heroTwo.bIsSleeping = true;
			} else {
				heroOne.bIsSleeping = true;
				heroTwo.bIsSleeping = false;
			}
		}
	}

}
