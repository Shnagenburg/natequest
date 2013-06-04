package NateQuest.Story;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.MouseManager;
import GameClasses.SoundManager;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import UserInterface.DanButton;

public class OptionsMenu extends GameScreen {

	DanButton musicToggle, sfxToggle, backButton;

	public OptionsMenu() {
		entities = new ArrayList<Entity>();
		drawables = new ArrayList<Drawable>();
		makeButtons();
	}

	public void update() {
		super.update();
		updateButtons();
	}

	private void makeButtons() {
		musicToggle = new DanButton(this, new Vector2f(300, 200), 300, 200,
				"Turn music off");
		sfxToggle = new DanButton(this, new Vector2f(700, 200), 300, 200,
				"Turn sfx off");
		backButton = new DanButton(this, new Vector2f(400, 500), 300, 200,
				"Back to the quest!");
	}

	private void updateButtons() {
		if (musicToggle.isHit) {
			SoundManager.setMusicIsOn(!SoundManager.getMusicIsOn());
			musicToggle.setMessage("Turn music "
					+ (SoundManager.getMusicIsOn() ? "off" : "on"));
		}
		if (sfxToggle.isHit) {
			SoundManager.setSFXIsOn(!SoundManager.getSFXisOn());
			musicToggle.setMessage("Turn sfx "
					+ (SoundManager.getSFXisOn() ? "off" : "on"));
		}
		if (backButton.isHit) {
			CUtil.GameHandle.returnFromMenu();
		}
	}

}
