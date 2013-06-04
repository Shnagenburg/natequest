package NateQuest.VFX;

import java.awt.RenderingHints.Key;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.KeyManager;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;

public class DebugMarker extends VisualFX {

	public DebugMarker(GameScreen gs, Vector2f pos) {
		super(gs, pos);
		saveThisEntity = false;
		drawable = new Drawable(this, new Sprite("hook"), LayerConstants.DEBUG);
		drawable.setPositionRounded(pos.x, pos.y);
	}

	public void update() {
		if (KeyManager.isKeyHit(Input.KEY_F)) {
			this.markForDeletion();
		}
	}

}
