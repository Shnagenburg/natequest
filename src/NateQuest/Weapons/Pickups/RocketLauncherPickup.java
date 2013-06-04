package NateQuest.Weapons.Pickups;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Weapons.Gun;
import NateQuest.Weapons.RocketLauncher;

public class RocketLauncherPickup extends WeaponPickup {

	public RocketLauncherPickup() {
		this(CUtil.ScreenGettingLoaded);
	}

	public RocketLauncherPickup(GameScreen gs) {
		super(gs);
		weapon = new RocketLauncher(gs, null);
		drawable = new Drawable(this, new Sprite("rocketlauncherpickup"),
				LayerConstants.CHARACTERS);
		setup();
	}
}
