package NateQuest.ActionItems.WarpIns;

import GameClasses.CUtil;
import NateQuest.GameScreen;
import NateQuest.Enemies.Lizard;

public class WarpInLizard extends WarpIn {

	public WarpInLizard() {
		this(CUtil.ScreenGettingLoaded);
	}

	public WarpInLizard(GameScreen gs) {
		super(gs);
	}

	@Override
	protected void setEntityType() {
		entityType = Lizard.class;
	}
}
