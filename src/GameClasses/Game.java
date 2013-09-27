package GameClasses;

import java.applet.*;
import java.awt.Menu;
import java.awt.event.*;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.LevelParameters;
import NateQuest.Wall;
import NateQuest.ActionItems.LevelTransition;
import NateQuest.Editor.EditorToggler;
import NateQuest.Gameplay.Hero;
import NateQuest.SavingAndLoading.SaverAndLoader;
import NateQuest.Story.MainMenu;
import NateQuest.Story.OptionsMenu;
import NateQuest.Story.PersistantEntities;
import NateQuest.Story.VictoryCondition;
import NateQuest.Story.VictoryScreen;
import Screens.*;
import Screens.ScreenManager.Control;

// The main control for the game.
public class Game {
	boolean isMarkedForSave = false;
	String tenativeSaveName = "";
	GameScreen tenativeSaveScreen = null;

	ResourcePool resourcePool;
	LevelPool levelPool;

	public GameScreen currentLevel, tempLevelForOptions, optionsScreen;
	GameScreen levelone;
	GameScreen leveltwo;
	// Really just a holder for the hero and such
	public GameScreen persistantLevel;
	public ArrayList<GameScreen> levels;
	ScreenShifter shifter;
	public PersistantEntities persistantEntities;
	Color darkness;
	Color clear;

	public Game() {

		CUtil.SlickGameContainer.setMouseGrabbed(true);
		clear = new Color(0, 0, 0, 0);
		darkness = new Color(0, 0, 0, 127);
		CUtil.GameHandle = this;
		resourcePool = new ResourcePool();
		CUtil.ResourcePool = resourcePool;
		// levels = new ArrayList<GameScreen>();
		levelPool = new LevelPool("default_world");
		CUtil.LevelPool = levelPool;

		optionsScreen = new OptionsMenu();
		 prodMode();
		// devMode();
		SoundManager.startJammin();
	}

	private void prodMode() {
		levelone = new MainMenu();
		currentLevel = levelone;
		CUtil.CurrentGame = levelone;

		for (GameScreen s : levelPool.getLevels()) {
			EditorToggler.setInvisWarpsAndControllers(s, true);
		}
	}

	private void devMode() {
		levelone = levelPool.getLevelByName("default_map");
		currentLevel = levelone;
		CUtil.CurrentGame = levelone;
		EditorToggler.enableEditor(currentLevel);
	}

	public void saveAndLoad() {
		if (!CUtil.LevelPool.getLevelMap().containsKey("default_map")) {
			SaverAndLoader.superSave(levelone, "default_map");
		}
		levelone = null;
		levelone = SaverAndLoader.superLoad("default_map");
		currentLevel = levelone;
		CUtil.CurrentGame = levelone;
	}

	public void update() {
		SoundManager.checkMusic();
		if (shifter != null) {
			shifter.update();
		} else {
			currentLevel.update();
		}

		if (KeyManager.HitEnter()) {
			currentLevel = SaverAndLoader.superLoad("file.sav");
			CUtil.CurrentGame = currentLevel;
		}
		if (currentLevel != optionsScreen && KeyManager.HitEsc()) {
			gotoMenu();
		} else if (KeyManager.HitEsc()) {
			returnFromMenu();
		}
		executeSave();
		/*
		 * if (KeyManager.isKeyHit(Input.KEY_1)) { currentLevel = levelone;
		 * CUtil.CurrentGame = currentLevel; } if
		 * (KeyManager.isKeyHit(Input.KEY_2)) { currentLevel = leveltwo;
		 * CUtil.CurrentGame = currentLevel; }
		 */
	}

	public void draw(Graphics g) {

		if (shifter != null) {
			shifter.draw(g);
		} else {
			currentLevel.draw(g);
		}

	}

	public void loadLevel(String name) {
		// currentLevel = SaverAndLoader.superLoad(name);
		Debugger.print("Switching to level " + name);
		currentLevel = CUtil.LevelPool.getLevelByName(name);
		CUtil.CurrentGame = currentLevel;
		EditorToggler.enableEditor(currentLevel);
	}

	public void loadLevelWithInput() {
		currentLevel = SaverAndLoader.loadWithNameInput();
		CUtil.CurrentGame = currentLevel;
		EditorToggler.enableEditor(currentLevel);
	}

	public void CreateScreenShiter(LevelTransition source,
			LevelTransition target) {
		shifter = new ScreenShifter(source, target);
	}

	public void markForSave(GameScreen gs, String name) {
		isMarkedForSave = true;
		tenativeSaveName = name;
		tenativeSaveScreen = gs;
	}

	public void executeSave() {
		if (isMarkedForSave) {
			EditorToggler.disableEditorAndSave(tenativeSaveScreen);
			SaverAndLoader.superSave(tenativeSaveScreen, tenativeSaveName);
			isMarkedForSave = false;
			EditorToggler.enableEditor(currentLevel);
		}
	}

	public void createNewLevel(LevelParameters params) {
		currentLevel = new GameScreen(params);
		CUtil.CurrentGame = currentLevel;
	}

	public void showVictoryScreen() {
		currentLevel = new VictoryScreen();
		CUtil.CurrentGame = currentLevel;
	}

	public void startGame() {
		currentLevel = CUtil.LevelPool.getLevelByName("a-1");
		CUtil.CurrentGame = currentLevel;
	}

	public void gotoMenu() {
		CUtil.SlickGameContainer.setMouseGrabbed(false);
		tempLevelForOptions = currentLevel;
		currentLevel = optionsScreen;
		CUtil.CurrentGame = optionsScreen;
	}

	public void returnFromMenu() {
		CUtil.SlickGameContainer.setMouseGrabbed(true);
		currentLevel = tempLevelForOptions;
		CUtil.CurrentGame = tempLevelForOptions;
	}

}
