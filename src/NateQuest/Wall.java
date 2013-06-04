package NateQuest;

import java.io.Serializable;

import javax.swing.text.Position;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;

public class Wall extends Entity {

	public Wall() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Wall(GameScreen gs) {
		super(gs);
		SheetedSprite sprite = new SheetedSprite(
				CUtil.ResourcePool.getImageByName("picture"), 1, 100);
		collidable = new Collidable(this);
		collidable.mask = CollidableMasks.NEUTRAL;
		drawable = new Drawable(this, sprite, LayerConstants.WALLS);
		drawable.mSprite.setPositionRounded(position.x, position.y);
		collidable.matchSprite(drawable.mSprite);
	}

	public void update() {
		drawable.mSprite.setPositionRounded(position.x, position.y);
		collidable.matchSprite(drawable.mSprite);
		super.update();
	}

}
