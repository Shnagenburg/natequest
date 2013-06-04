package NateQuest.Weapons.Pickups;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Weapons.RocketLauncher;
import NateQuest.Weapons.StarRifle;

public class MagnetGunPickup extends WeaponPickup {

	public MagnetGunPickup() {
		this(CUtil.ScreenGettingLoaded);
	}

	public MagnetGunPickup(GameScreen gs) {
		super(gs);
		weapon = new StarRifle(gs, null);
		drawable = new Drawable(this, new Sprite("magnetpickup"),
				LayerConstants.CHARACTERS);
		setup();
	}
}
