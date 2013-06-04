package NateQuest.Editor;

import java.util.ArrayList;

import org.newdawn.slick.Input;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import GameClasses.MouseManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.ActionItems.ActionCreationMenu;
import NateQuest.ActionItems.Actionable;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.DrawableLine;
import NateQuest.Drawables.DrawableWithText;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Crossheir;

public class InspectorDialogue extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2972342934936928962L;
	ArrayList<InspectorField> fields;
	InspectorEditor parentEditor;
	Drawable selectionBox;
	ActionCreationMenu actionCreationMenu;
	ArrayList<DrawableWithText> currentActionLinks;
	ArrayList<DrawableLine> currentActionLines;
	Entity target;
	Crossheir crossheir;

	public InspectorDialogue(GameScreen gs, InspectorEditor editor,
			Entity target) {
		super(gs);
		parentEditor = editor;
		fields = new ArrayList<InspectorField>();
		for (int i = 0; i < target.getClass().getFields().length; i++) {
			fields.add(new InspectorField(gs, target, target.getClass()
					.getFields()[i]));
		}
		saveThisEntity = false;
		selectionBox = new Drawable(this, new Sprite(
				CUtil.QuickImage("selectionbox")), LayerConstants.HUD);
		selectionBox.setPositionRounded(target.position.x, target.position.y);
		anchor();
		setLockedParentEditor(true);

		if (target instanceof Actionable) {
			actionCreationMenu = new ActionCreationMenu(parentScreen,
					(Actionable) target, this);
			makeActionInfos(target);
		}
		this.target = target;
	}

	private void setLockedParentEditor(boolean isLocked) {
		parentEditor.bIsSleeping = isLocked;
		parentEditor.setDrawableStatus(isLocked);
	}

	public void anchor() {
		for (int i = 0; i < fields.size(); i++) {
			fields.get(i).setPosition(200, 200 + (i * 25));
		}
	}

	public void update() {
		handleShifter();
		if (KeyManager.isKeyHit(Input.KEY_5)
				|| KeyManager.isKeyHit(Input.KEY_I)
				|| MouseManager.getUpDownRightMouseClick()) {
			setLockedParentEditor(false);
			this.markForDeletion();
		}

	}

	public void handleShifter() {
		getCrossHeir();
		if (target != null && KeyManager.isKeyDown(Input.KEY_4)) {
			target.setPosition(crossheir.position.x, crossheir.position.y);
			selectionBox.setPositionRounded(crossheir.position.x,
					crossheir.position.y);
		}
	}

	private void getCrossHeir() {
		if (crossheir == null) {
			crossheir = (Crossheir) parentEditor.parentScreen
					.findEntity(Crossheir.class);
		}
	}

	public void makeActionInfos(Entity target) {
		currentActionLinks = ((Actionable) target)
				.createTextListOfCurrentActions();
		currentActionLines = ((Actionable) target)
				.createLineListOfCurrentActions();
	}

	public void clearActionInfos() {
		if (currentActionLinks != null) {
			for (DrawableWithText dwt : currentActionLinks) {
				dwt.removeProperty();
			}
		}
		if (currentActionLines != null) {
			for (DrawableLine dls : currentActionLines) {
				dls.removeProperty();
			}
		}
	}

	@Override
	public void markForDeletion() {
		super.markForDeletion();
		for (InspectorField f : fields)
			f.markForDeletion();
		fields.clear();
		if (actionCreationMenu != null) {
			actionCreationMenu.markForDeletion();
		}
		clearActionInfos();
	}

	@Override
	public void removeEntity() {
		super.removeEntity();
		parentEditor.inspector = null;
		selectionBox.removeProperty();
		if (actionCreationMenu != null) {
			actionCreationMenu.removeEntity();
		}

	}

}
