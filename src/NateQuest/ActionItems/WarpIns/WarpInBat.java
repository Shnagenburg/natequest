package NateQuest.ActionItems.WarpIns;

import GameClasses.CUtil;
import NateQuest.GameScreen;
import NateQuest.Enemies.Bat;

public class WarpInBat extends WarpIn {
	public WarpInBat() {
		this(CUtil.ScreenGettingLoaded);
	}

	public WarpInBat(GameScreen gs) {
		super(gs);
	}

	@Override
	protected void setEntityType() {
		entityType = Bat.class;
	}
}
