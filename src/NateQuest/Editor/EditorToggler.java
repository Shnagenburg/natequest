package NateQuest.Editor;

import java.util.ArrayList;

import org.newdawn.slick.Input;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.ActionItems.WarpIns.WarpIn;
import NateQuest.ActionItems.WarpIns.WarpInController;
import NateQuest.ActionItems.AreaTrigger;
import NateQuest.ActionItems.AreaTriggerCorner;
import NateQuest.ActionItems.KillCountController;
import NateQuest.SavingAndLoading.SaverAndLoader;

public class EditorToggler {
	static Class[] entitiesNotShown = { WarpIn.class, WarpInController.class,
			AreaTrigger.class, AreaTriggerCorner.class,
			KillCountController.class };

	public static boolean isEditing = false;

	public static void handleInput(GameScreen screen) {
		if (KeyManager.isKeyHit(Input.KEY_8)) {
			if (isEditing)
				deactivateEditor(screen);
			else
				enableEditor(screen);
		} else if (KeyManager.isKeyHit(Input.KEY_MINUS)) {
			disableEditorAndSave(screen);
		} else if (KeyManager.isKeyHit(Input.KEY_EQUALS)) {
			enableEditorAndLoad(screen);
		} else if (KeyManager.isKeyHit(Input.KEY_9)) {
			saveWithName(screen);
		} else if (KeyManager.isKeyHit(Input.KEY_0)) {
			loadWithName(screen);
		}

	}

	public static void enableEditor(GameScreen screen) {
		Debugger.print("Activating...");
		setInvisWarpsAndControllers(screen, false);
		screen.setSleepingAll(true);
		new EditorController(screen);
		screen.reblendMaps();
		isEditing = true;
	}

	public static void deactivateEditor(GameScreen screen) {
		Debugger.print("Deactivating...");
		setInvisWarpsAndControllers(screen, true);
		screen.setSleepingAll(false);
		EditorController controller = (EditorController) screen
				.findEntity(EditorController.class);
		if (controller != null) {
			controller.markForDeletion();
		}
		screen.flush();
		isEditing = false;
	}

	public static void disableEditorAndSave(GameScreen screen) {
		Debugger.print("Disabling editor and saving temporary level...");
		deactivateEditor(screen);
		SaverAndLoader.superSave(screen, "temp.sav");
	}

	public static void enableEditorAndLoad(GameScreen screen) {
		Debugger.print("Activating and loading temporary save...");
		CUtil.GameHandle.loadLevel("temp.sav");
	}

	public static void saveWithName(GameScreen screen) {
		Debugger.print("Saving level with name...");
		deactivateEditor(screen);
		SaverAndLoader.saveWithNameInput(screen);
	}

	public static void loadWithName(GameScreen screen) {
		Debugger.print("loading level with name...");
		CUtil.GameHandle.loadLevelWithInput();
	}

	public static void setInvisWarpsAndControllers(GameScreen screen,
			boolean isCloaked) {
		ArrayList<Entity> entities = null;
		if (screen == null)
			return;
		for (Class entityType : entitiesNotShown) {
			entities = screen.findEntities(entityType);
			setIsInvis(entities, isCloaked);
		}

	}

	private static void setIsInvis(ArrayList<Entity> entities, boolean isCloaked) {
		if (entities != null && !entities.isEmpty()) {
			for (Entity w : entities) {
				w.drawable.bIgnore = isCloaked;
			}
		}
	}
}
