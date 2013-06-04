package NateQuest.ActionItems.WarpIns;

import java.util.ArrayList;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.ActionItems.Actionable;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;

public class WarpInController extends Actionable {

	float studderDelay = 200;
	final String[] OPS = { "warpIn", "studderedWarpIn" };
	public int warpGroup = 0;

	public WarpInController() {
		this(CUtil.ScreenGettingLoaded);
	}

	public WarpInController(GameScreen gs) {
		super(gs);
		addOps(OPS);
		drawable = new Drawable(this, new Sprite("warpcontrol"),
				LayerConstants.HUD);
		collidable = new CollidableCircle(this, 16);
		collidable.mask = CollidableMasks.EDITOR;
	}

	@Override
	public void action(int actionID, Entity caller) {
		ArrayList<WarpIn> warpIns = getWarpIns();
		if (warpIns == null) {
			return;
		}
		if (actionID == 0) {
			for (WarpIn warp : warpIns) {
				warp.startWarping();
			}
		} else if (actionID == 1) {
			for (int i = 0; i < warpIns.size(); i++) {
				warpIns.get(i).setWarpingAfterTime(i * studderDelay);
			}
		}
	}

	private ArrayList<WarpIn> getWarpIns() {
		ArrayList<Entity> candidates;
		candidates = parentScreen.findEntities(WarpIn.class);
		if (candidates == null) {
			return null;
		}
		ArrayList<WarpIn> warpIns = new ArrayList<WarpIn>();
		for (Entity e : candidates) {
			WarpIn warp = (WarpIn) e;
			if (warp.warpGroup == warpGroup) {
				warpIns.add(warp);
			}
		}
		return warpIns;
	}

}
