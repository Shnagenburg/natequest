package NateQuest.Editor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import GameClasses.MouseManager;
import GameClasses.Sprite;
import GameClasses.ScreenShifter.ShiftType;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Wall;
import NateQuest.ActionItems.AreaTrigger;
import NateQuest.ActionItems.FlipSwitch;
import NateQuest.ActionItems.KillCountController;
import NateQuest.ActionItems.LevelTransition;
import NateQuest.ActionItems.PushBlock;
import NateQuest.ActionItems.WarpIns.WarpIn;
import NateQuest.ActionItems.WarpIns.WarpInBat;
import NateQuest.ActionItems.WarpIns.WarpInController;
import NateQuest.ActionItems.WarpIns.WarpInKnight;
import NateQuest.ActionItems.WarpIns.WarpInLizard;
import NateQuest.ActionItems.WarpIns.WarpInRedLizard;
import NateQuest.ActionItems.WarpIns.WarpInRedTrooper;
import NateQuest.ActionItems.WarpIns.WarpInWizard;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Enemies.Bat;
import NateQuest.Enemies.EnemySpawner;
import NateQuest.Enemies.GiantCrab;
import NateQuest.Enemies.Knight;
import NateQuest.Enemies.Lizard;
import NateQuest.Enemies.RedLizard;
import NateQuest.Enemies.RedTrooper;
import NateQuest.Enemies.Trooper;
import NateQuest.Enemies.Wizard;
import NateQuest.Gameplay.Crossheir;
import NateQuest.SavingAndLoading.SaverAndLoader;
import NateQuest.Story.Billboard;
import NateQuest.Story.Checkpoint;
import NateQuest.Story.VictoryCondition;
import NateQuest.Tiling.TilePainter;
import NateQuest.Weapons.Pickups.HeartGunPickup;
import NateQuest.Weapons.Pickups.HookshotPickup;
import NateQuest.Weapons.Pickups.MagnetGunPickup;
import NateQuest.Weapons.Pickups.RocketLauncherPickup;
import NateQuest.Weapons.Pickups.ShotgunPickup;
import NateQuest.Weapons.Pickups.SinewavePickup;
import UserInterface.DanButton;
import UserInterface.DanMenu;

public class ThingEditor extends Editor {

	Class[] entityClasses = { Trooper.class, RedTrooper.class, Wizard.class,
			Lizard.class, RedLizard.class, Bat.class, Knight.class,
			GiantCrab.class,

			AreaTrigger.class, WarpInController.class, WarpIn.class,
			WarpInRedTrooper.class, WarpInWizard.class, WarpInLizard.class,
			WarpInRedLizard.class, WarpInBat.class, WarpInKnight.class,

			FlipSwitch.class, LevelTransition.class, KillCountController.class,

			HeartGunPickup.class, RocketLauncherPickup.class,
			ShotgunPickup.class, MagnetGunPickup.class, SinewavePickup.class,
			HookshotPickup.class,

			Billboard.class, Checkpoint.class, VictoryCondition.class };
	Entity currentEntity;
	Crossheir crossheir;
	public boolean isPlayingLevel = false;
	Stack<Entity> actionStack = new Stack<Entity>();
	ArrayList<Entity> entitySet = new ArrayList<Entity>();

	ArrayList<Class> classSet = new ArrayList<Class>();

	public ThingEditor(GameScreen gs) {
		super(gs);
		// TODO Auto-generated constructor stub

		sleepAll();
		currentEntity = new Trooper(gs);
		currentEntity.bIsSleeping = true;
		crossheir = gs.crossheir;

		Debugger.print("Editor commands: 6 - Test run; 7 - Stop test; 8 - save level; 9 - switch level; u - undo:");

		setUpSet();
		saveThisEntity = false;
	}

	public void setDrawableStatus(boolean isInvis) {
		if (currentEntity != null && currentEntity.drawable != null)
			currentEntity.drawable.bIgnore = isInvis;

		if (isInvis && currentEntity != null) {
			currentEntity.markForDeletion();
			currentEntity = null;
		}
	}

	private void setUpSet() {
		for (Class c : entityClasses) {
			classSet.add(c);
		}
	}

	public void update() {
		getCrossheir();
		handleRotateEntity();
		handlePlaceItem();
		// handlePlayLevel();
		handleUndo();
		handleEntitySwitch();
		handleDelete();
	}

	private void getCrossheir() {
		if (crossheir == null) {
			crossheir = (Crossheir) parentScreen.findEntity(Crossheir.class);
		}
	}

	private void handleDelete() {
		if (KeyManager.isKeyHit(Input.KEY_X) && !isPlayingLevel) {
			for (Entity e : parentScreen.entities) {
				Debugger.peek(e);
				if (e.collidable != null) {
					Debugger.peek(e.collidable);
				}
				if (e.collidable != null
						&& e.collidable.isColliding(crossheir.position)
						&& e != currentEntity
						&& e.getClass() != DanButton.class) {
					if (actionStack.contains(e))
						actionStack.remove(e);
					e.markForDeletion();
				}
			}
		}
	}

	private void handleEntitySwitch() {
		if ((KeyManager.isKeyHit(Input.KEY_1) || KeyManager
				.isKeyHit(Input.KEY_2)) && !isPlayingLevel) {
			int i = classSet.indexOf(currentEntity.getClass());
			if (KeyManager.isKeyHit(Input.KEY_1)) {
				i = i - 1;
			} else {
				i = i + 1;
			}
			if (i < 0) {
				i = classSet.size() - 1;
			} else if (i > classSet.size() - 1) {
				i = 0;
			}
			currentEntity.removeEntity();
			currentEntity = entityFromClass(classSet.get(i));
			currentEntity.bIsSleeping = true;
		}
	}

	private Entity entityFromClass(Class<? extends Entity> c) {
		CUtil.ScreenGettingLoaded = parentScreen;
		try {
			return c.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void handleRotateEntity() {
		if (KeyManager.isKeyHit(Input.KEY_F)) {
			for (Entity e : parentScreen.entities) {
				if (e.collidable != null
						&& e.collidable.isColliding(crossheir.position)) {
					if (e.drawable != null) {
						e.drawable.setRotation(e.drawable.getRotation() + 90);
					}
				}
			}

		}
	}

	private void handleUndo() {
		if (KeyManager.isKeyHit(Input.KEY_U)) {
			if (!actionStack.isEmpty()) {
				Entity toUndo = actionStack.pop();
				toUndo.removeEntity();
			}
		}
	}

	private void handlePlaceItem() {
		if (!isPlayingLevel) {
			if (currentEntity == null) {
				currentEntity = new Trooper(parentScreen);
				currentEntity.bIsSleeping = true;
			}

			int mX = (int) crossheir.position.x;
			int mY = (int) crossheir.position.y;
			currentEntity.setPosition(mX, mY);
			if (currentEntity.collidable != null)
				currentEntity.collidable
						.matchSprite(currentEntity.drawable.mSprite);

			if (MouseManager.getUpDownLeftMouseClick()
					&& !hitEditorControllerButton()) {
				if (currentEntity.getClass() == LevelTransition.class) {
					LevelTransition t = (LevelTransition) currentEntity;
					t = setUpLevelTransitions(t);
					actionStack.push(t);
					currentEntity = entityFromClass(currentEntity.getClass());
					currentEntity.bIsSleeping = true;
				} else {
					actionStack.push(currentEntity);
					currentEntity = entityFromClass(currentEntity.getClass());
					currentEntity.bIsSleeping = true;
				}
			}
		}
	}

	// If we hit an editor controller button, we dont want to place an entity
	private boolean hitEditorControllerButton() {
		for (Entity e : parentScreen.entities) {
			if (e.getClass() == EditorController.class) {
				EditorController controller = (EditorController) e;
				return controller.editorSelector.anyOptionHit();
			}
		}
		return false;
	}

	private LevelTransition setUpLevelTransitions(LevelTransition toEdit) {
		System.out.println("Set Up Transition...");
		Scanner user_input = new Scanner(System.in);

		System.out.println("Source ID: ");
		String sourceId = user_input.next();
		System.out.println("Target ID: ");
		String targetId = user_input.next();
		System.out.println("Target Level Name: ");
		String targetName = user_input.next();
		System.out.println("Shift Style: (su, sd, sl, sr, fu, fd, fl, fr) ");
		String shiftstyle = user_input.next();

		toEdit.sourceID = Integer.parseInt(sourceId);
		toEdit.targetID = Integer.parseInt(targetId);
		toEdit.targetName = targetName;
		toEdit.style = stringToShiftStyle(shiftstyle);

		return toEdit;
	}

	private ShiftType stringToShiftStyle(String shiftstyle) {
		if (shiftstyle.equals("su")) {
			return ShiftType.MOVE_UP;
		} else if (shiftstyle.equals("sd")) {
			return ShiftType.MOVE_DOWN;
		} else if (shiftstyle.equals("sl")) {
			return ShiftType.MOVE_LEFT;
		} else if (shiftstyle.equals("sr")) {
			return ShiftType.MOVE_RIGHT;
		}

		else if (shiftstyle.equals("fu")) {
			return ShiftType.FADE_UP;
		} else if (shiftstyle.equals("fd")) {
			return ShiftType.FADE_DOWN;
		} else if (shiftstyle.equals("fl")) {
			return ShiftType.FADE_LEFT;
		} else if (shiftstyle.equals("fr")) {
			return ShiftType.FADE_RIGHT;
		} else {
			return null;
		}
	}

	public void sleepAll() {
		for (Entity e : parentScreen.entities)
			if (e != this && e.getClass() != EditorController.class)
				e.bIsSleeping = true;
		Crossheir cross = (Crossheir) parentScreen.findEntity(Crossheir.class);
		if (cross != null)
			cross.bIsSleeping = false;
	}

	private void handlePlayLevel() {
		if (KeyManager.isKeyHit(Input.KEY_6) && !isPlayingLevel) {
			playLevel(true);
		} else if (KeyManager.isKeyHit(Input.KEY_7)) {
			CUtil.GameHandle.loadLevel("temp.sav");
			isPlayingLevel = false;
			currentEntity = new Trooper(parentScreen);
			currentEntity.bIsSleeping = true;
			parentScreen.camera.bIgnoreMaxes = true;
			parentScreen.camera.setPainterBounds();
			reactivateEditor();
			sleepAll();
		}
	}

	public void playLevel(boolean saveLevel) {

		if (currentEntity != null) {
			currentEntity.removeEntity();
			currentEntity = null;
		}

		if (saveLevel) {
			EditorToggler.deactivateEditor(parentScreen);
			parentScreen.flush();
			SaverAndLoader.superSave(parentScreen, "temp.sav");
		}

		parentScreen.setSleepingAll(false);
		parentScreen.camera.bIgnoreMaxes = false;
		parentScreen.camera.setNormalBounds();
		deactivateEditor();
		isPlayingLevel = true;

	}

	@Override
	public void setActive(boolean isActive) {
		super.setActive(isActive);

		if (currentEntity != null && currentEntity.drawable != null)
			currentEntity.drawable.bIgnore = !isActive;

	};

	private void deactivateEditor() {
		EditorController controller = null;
		for (Entity e : parentScreen.entities) {
			if (e.getClass() == EditorController.class) {
				controller = (EditorController) e;
				break;
			}
		}
		controller.deactivateAllEditors();
		controller.removeEditorMenu();
		this.setActive(true);
	}

	private void reactivateEditor() {
		EditorController controller = null;
		for (Entity e : parentScreen.entities) {
			if (e.getClass() == EditorController.class) {
				controller = (EditorController) e;
				break;
			}
		}

		controller.createEditorMenu();
	}

	public String toString() {
		return "Entity Editor";
	}

	@Override
	public void markForDeletion() {
		super.markForDeletion();
		if (currentEntity != null)
			currentEntity.markForDeletion();
	}

}
