package NateQuest.Story;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;

public class Billboard extends Entity {

	public String spriteName = "image name here";

	public Billboard() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Billboard(GameScreen gs) {
		super(gs);
		drawable = new Drawable(this, new Sprite("billboard"),
				LayerConstants.HUD);
		collidable = new CollidableCircle(this, 16);
		collidable.mask = CollidableMasks.EDITOR;
	}

	public void update() {
		checkAndPostImage();
	}

	private void checkAndPostImage() {
		String currentSpriteName = drawable.mSprite.spriteName;
		if (!currentSpriteName.equals(spriteName)
				&& CUtil.ResourcePool.imageExists(spriteName)) {
			Sprite newSprite = new Sprite(spriteName);
			newSprite.camera = drawable.mSprite.camera;
			drawable.mSprite = newSprite;
			setPosition(position.x, position.y);
		}
	}

}
