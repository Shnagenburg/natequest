package NateQuest.ActionItems.WarpIns;

import GameClasses.CUtil;
import NateQuest.GameScreen;
import NateQuest.Enemies.Wizard;

public class WarpInWizard extends WarpIn {

	public WarpInWizard() {
		this(CUtil.ScreenGettingLoaded);
	}

	public WarpInWizard(GameScreen gs) {
		super(gs);
	}

	@Override
	protected void setEntityType() {
		entityType = Wizard.class;
	}
}
