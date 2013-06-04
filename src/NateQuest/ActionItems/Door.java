package NateQuest.ActionItems;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.Sprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;

public class Door extends Actionable {

	final String[] OPS = { "open", "close" };
	SheetedSprite sprite;

	public Door() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Door(GameScreen gs) {
		super(gs);
		addOps(OPS);
		sprite = new SheetedSprite(CUtil.QuickImage("cavespawner"), 3, 300);
		drawable = new Drawable(this, sprite, LayerConstants.TILEMAP_OVERLAY);
		drawable.setPosition(500, 400);
		this.position = new Vector2f(500, 400);
		this.collidable = new CollidableCircle(this, 64);
		collidable.mask = CollidableMasks.EDITOR;
	}

	@Override
	public void action(int actionID, Entity caller) {
		if (actionID == 0) {
			sprite.lock(1);
		} else if (actionID == 1) {
			sprite.lock(0);
		}
	}

}
