package NateQuest.ActionItems.WarpIns;

import GameClasses.CUtil;
import NateQuest.GameScreen;
import NateQuest.Enemies.Bat;
import NateQuest.Enemies.RedTrooper;

public class WarpInRedTrooper extends WarpIn {
	public WarpInRedTrooper() {
		this(CUtil.ScreenGettingLoaded);
	}

	public WarpInRedTrooper(GameScreen gs) {
		super(gs);
	}

	@Override
	protected void setEntityType() {
		entityType = RedTrooper.class;
	}
}
