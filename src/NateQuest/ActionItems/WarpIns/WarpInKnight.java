package NateQuest.ActionItems.WarpIns;

import GameClasses.CUtil;
import NateQuest.GameScreen;
import NateQuest.Enemies.Knight;

public class WarpInKnight extends WarpIn {
	public WarpInKnight() {
		this(CUtil.ScreenGettingLoaded);
	}

	public WarpInKnight(GameScreen gs) {
		super(gs);
	}

	@Override
	protected void setEntityType() {
		entityType = Knight.class;
	}
}
