package NateQuest.Story;

import java.awt.Rectangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import GameClasses.Sprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.ImageCreator;
import NateQuest.Drawables.DrawableWithText;
import NateQuest.Drawables.LayerConstants;

public class DialogueWindow extends Entity {

	DrawableWithText dwt;

	public DialogueWindow() {
		this(CUtil.ScreenGettingLoaded);
	}

	public DialogueWindow(GameScreen gs) {
		super(gs);
		Sprite s = new Sprite(ImageCreator.createImage(200, 200, 235, 235, 235,
				127));
		s.bIsCameraed = false;
		// Sprite s = new Sprite("nate");
		dwt = new DrawableWithText(this, s, LayerConstants.HUD,
				"THIS IS A MESSAGE!");
		dwt.setTextColor(Color.red);
		drawable = dwt;
		drawable.setPositionRounded(position.x, position.y);
		drawable.setIsCamerad(false);
	}

	public void setMessage(String message) {
		dwt.setText(message);
	}

	public void update() {
		super.update();
		if (KeyManager.HitSpace()) {
			this.markForDeletion();
		}
	}

}
