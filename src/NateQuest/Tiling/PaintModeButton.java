package NateQuest.Tiling;

import java.io.Serializable;

import GameClasses.Sprite;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;

public class PaintModeButton implements Serializable {

	public enum Type {
		PEN, BOX, CIRCLE, ERASE
	}

	Type type;
	Drawable drawable;
	PaintModeSelector parent;

	public PaintModeButton(Type t, Sprite icon, PaintModeSelector parent) {
		type = t;
		this.parent = parent;
		icon.bIsCameraed = false;
		drawable = new Drawable(parent.parent, icon, LayerConstants.HUD);
	}

}
