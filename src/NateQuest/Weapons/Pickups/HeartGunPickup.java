package NateQuest.Weapons.Pickups;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Weapons.Gun;

public class HeartGunPickup extends WeaponPickup {

	public HeartGunPickup() {
		this(CUtil.ScreenGettingLoaded);
	}

	public HeartGunPickup(GameScreen gs) {
		super(gs);
		weapon = new Gun(gs, null);
		drawable = new Drawable(this, new Sprite("lasergunpickup"),
				LayerConstants.CHARACTERS);
		setup();
	}

}
