package NateQuest.Weapons.Pickups;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Weapons.SineWaveGun;
import NateQuest.Weapons.StarRifle;

public class SinewavePickup extends WeaponPickup {

	public SinewavePickup() {
		this(CUtil.ScreenGettingLoaded);
	}

	public SinewavePickup(GameScreen gs) {
		super(gs);
		weapon = new SineWaveGun(gs, null);
		drawable = new Drawable(this, new Sprite("sinewavepickup"),
				LayerConstants.CHARACTERS);
		setup();
	}
}
