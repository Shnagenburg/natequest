package NateQuest.ActionItems;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.ActionItems.WarpIns.WarpIn;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;

public class KillCountController extends Actionable {

	final String[] OPS = { "Kill all" };
	boolean hasPlayedAction = false; // just so it doesnt spam
	int killCount = 0;

	public KillCountController() {
		this(CUtil.ScreenGettingLoaded);
	}

	public KillCountController(GameScreen gs) {
		super(gs);
		addOps(OPS);
		drawable = new Drawable(this, new Sprite("orc"), LayerConstants.DEBUG);
		collidable = new CollidableCircle(this, 16);
		collidable.mask = CollidableMasks.EDITOR;
	}

	public void update() {
		// Debugger.print("ss: " + parentScreen.enemies.size());
		if (parentScreen.enemies.isEmpty() && noWarpsLeft()) {
			checkAndPlayActions(0);
		}
	}

	private void checkAndPlayActions(int actionId) {
		if (hasPlayedAction == false) {
			playActions(0);
			hasPlayedAction = true;
		}
	}

	private boolean noWarpsLeft() {
		for (Entity e : parentScreen.entities) {
			if (e instanceof WarpIn) {
				return false;
			}
		}
		return true;
	}

}
