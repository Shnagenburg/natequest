package NateQuest.VFX;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.Fader;

public class VisualFX extends Entity {

	Fader fader;

	public VisualFX(GameScreen gs, Vector2f pos) {
		super(gs);
		this.position = new Vector2f(pos.x, pos.y);
		saveThisEntity = false;
	}

	public void addFader(Fader fade) {
		fader = fade;
	}

	public void update() {
		if (fader != null) {
			fader.update();
			if (fader.isFading == false) {
				this.markForDeletion();
			}
		}

		drawable.update();
		if (drawable.mSprite.getHasDonePass()) {
			this.markForDeletion();
		}
	}

}
