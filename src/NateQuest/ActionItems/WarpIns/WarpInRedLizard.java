package NateQuest.ActionItems.WarpIns;

import GameClasses.CUtil;
import NateQuest.GameScreen;
import NateQuest.Enemies.Lizard;
import NateQuest.Enemies.RedLizard;

public class WarpInRedLizard extends WarpIn {
	public WarpInRedLizard() {
		this(CUtil.ScreenGettingLoaded);
	}

	public WarpInRedLizard(GameScreen gs) {
		super(gs);
	}

	@Override
	protected void setEntityType() {
		entityType = RedLizard.class;
	}
}
