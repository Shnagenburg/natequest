package NateQuest.Editor;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.text.DefaultEditorKit.CutAction;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.LevelPool;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.LevelParameters;
import NateQuest.Drawables.DrawableWithText;
import NateQuest.SavingAndLoading.SaverAndLoader;
import NateQuest.Story.DialogueWindow;
import UserInterface.DanButton;

public class LevelSelector extends Editor {

	final int BUT_WIDTH = 200;
	final int BUT_HEIGHT = 20;

	ArrayList<DanButton> buttons;
	DanButton saveButton;
	DanButton newLevelButton;

	public LevelSelector(GameScreen gs) {
		super(gs);

		anchor();
	}

	public void anchor() {
		if (buttons == null)
			buttons = new ArrayList<DanButton>();
		for (DanButton b : buttons)
			b.markForDeletion();
		buttons.clear();
		if (saveButton != null)
			saveButton.markForDeletion();
		if (newLevelButton != null)
			newLevelButton.markForDeletion();

		ArrayList<GameScreen> levels = CUtil.LevelPool.getLevels();
		for (int i = 0; i < levels.size(); i++) {
			DanButton b = new DanButton(parentScreen, new Vector2f(
					BUT_WIDTH / 2, BUT_HEIGHT + (i * BUT_HEIGHT)), BUT_WIDTH,
					BUT_HEIGHT, "button");
			b.setMessage(levels.get(i).levelName);
			buttons.add(b);
		}
		saveButton = new DanButton(parentScreen, new Vector2f(
				(int) CUtil.Dimensions.getWidth() - BUT_WIDTH / 2f,
				(int) CUtil.Dimensions.getHeight() - BUT_HEIGHT * 6f),
				BUT_WIDTH, BUT_HEIGHT, "Save " + parentScreen.levelName);

		newLevelButton = new DanButton(parentScreen, new Vector2f(
				(int) CUtil.Dimensions.getWidth() - BUT_WIDTH / 2f,
				(int) CUtil.Dimensions.getHeight() - BUT_HEIGHT * 4f),
				BUT_WIDTH, BUT_HEIGHT, "New Level");

	}

	private void refreshList() {
		anchor();
	}

	private void changeLevel(String name) {
		CUtil.GameHandle.loadLevel(name);
	}

	public void update() {
		for (DanButton button : buttons)
			if (button.isHit)
				changeLevel(button.getMessage());
		if (saveButton.isHit)
			CUtil.GameHandle.markForSave(parentScreen, parentScreen.levelName);
		if (newLevelButton.isHit) {
			makeNewLevel();
		}
	}

	public void makeNewLevel() {
		int chunkWidth = Integer.parseInt(CUtil
				.getConsoleInput("Chunk width: "));
		int chunkHeight = Integer.parseInt(CUtil
				.getConsoleInput("Chunk height: "));
		LevelParameters newLevelParameters = new LevelParameters();
		newLevelParameters.width = chunkWidth;
		newLevelParameters.height = chunkHeight;
		CUtil.GameHandle.createNewLevel(newLevelParameters);
	}

	@Override
	public void setActive(boolean isActive) {
		super.setActive(isActive);
		if (!isActive) {
			if (buttons != null) {
				for (DanButton b : buttons)
					b.markForDeletion();
			}
			buttons = null;
			if (saveButton != null)
				saveButton.markForDeletion();
			saveButton = null;
			if (newLevelButton != null)
				newLevelButton.markForDeletion();
			newLevelButton = null;
		} else {
			anchor();
		}

	}

	public void setDrawableStatus(boolean isInvis) {
		if (!isInvis)
			refreshList();

		for (DanButton b : buttons)
			b.drawable.bIgnore = isInvis;
		saveButton.drawable.bIgnore = isInvis;
		newLevelButton.drawable.bIgnore = isInvis;
	}

	@Override
	public void markForDeletion() {
		super.markForDeletion();
		if (buttons != null) {
			for (DanButton b : buttons)
				b.markForDeletion();
		}
		if (saveButton != null) {
			saveButton.markForDeletion();
		}
		if (newLevelButton != null) {
			newLevelButton.markForDeletion();
		}
	}

	public String toString() {
		return "Level Select";
	}

}
