package NateQuest.Editor;

import NateQuest.Entity;
import NateQuest.GameScreen;

public class Editor extends Entity {

	public EditorController parentController;

	public Editor(GameScreen gs) {
		super(gs);
	}

	public void setActive(boolean isActive) {
		if (drawable != null)
			drawable.bIgnore = !isActive;
		this.bIsSleeping = !isActive;
	}

}
