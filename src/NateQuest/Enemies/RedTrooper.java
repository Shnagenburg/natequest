package NateQuest.Enemies;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Drawables.MultiSprited;

public class RedTrooper extends Trooper {

	public RedTrooper() {
		this(CUtil.ScreenGettingLoaded);
	}

	public RedTrooper(GameScreen gs) {
		super(gs);
	}

	@Override
	protected void init() {
		Sprite headSprite = new Sprite(CUtil.QuickImage("orcheadred"));
		Sprite sprite = new Sprite(CUtil.ResourcePool.getImageByName("orcarms"));
		Sprite gunSprite = new Sprite(CUtil.QuickImage("orcgun1"));
		drawable = new MultiSprited(this, sprite, LayerConstants.CHARACTERS);
		((MultiSprited) drawable).addSprite(gunSprite);
		((MultiSprited) drawable).addSprite(headSprite);
		health = 11;
		FIRE_TIME = 800;
	}
}
