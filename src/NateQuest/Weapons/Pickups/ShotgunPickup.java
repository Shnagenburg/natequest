package NateQuest.Weapons.Pickups;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Weapons.RocketLauncher;
import NateQuest.Weapons.Shotgun;

public class ShotgunPickup extends WeaponPickup {

	public ShotgunPickup() {
		this(CUtil.ScreenGettingLoaded);
	}

	public ShotgunPickup(GameScreen gs) {
		super(gs);
		weapon = new Shotgun(gs, null);
		drawable = new Drawable(this, new Sprite("shotgunpickup"),
				LayerConstants.CHARACTERS);
		setup();
	}
}
