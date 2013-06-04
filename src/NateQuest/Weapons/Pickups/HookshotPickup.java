package NateQuest.Weapons.Pickups;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Weapons.Hookshot;
import NateQuest.Weapons.StarRifle;

public class HookshotPickup extends WeaponPickup {

	public HookshotPickup() {
		this(CUtil.ScreenGettingLoaded);
	}

	public HookshotPickup(GameScreen gs) {
		super(gs);
		weapon = new Hookshot(gs, null);
		drawable = new Drawable(this, new Sprite("hookshotpickup"),
				LayerConstants.CHARACTERS);
		setup();
	}

}
