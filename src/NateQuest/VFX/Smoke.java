package NateQuest.VFX;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;

public class Smoke extends VisualFX {

	public Smoke(GameScreen gs, Vector2f pos) {

		super(gs, pos);

		drawable = new Drawable(this, new SheetedSprite(
				CUtil.ResourcePool.getImageByName("smoke3frames"), 3, 300), 4);
		drawable.mSprite.setPositionRounded(position.x, position.y);
	}

}
