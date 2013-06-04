package NateQuest.Editor;

import java.awt.Point;

import org.newdawn.slick.Input;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import GameClasses.MouseManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Crossheir;
import UserInterface.DanButton;
import UserInterface.DanMenu;
import UserInterface.DanMenu.Filters;
import UserInterface.DanMenu.Layout;
import UserInterface.DanMenu.Size;

public class InspectorEditor extends Editor {

	InspectorDialogue inspector;
	DanMenu entityMenu;
	Drawable selectionBox;
	Crossheir crossheir;

	public InspectorEditor(GameScreen gs) {
		super(gs);
		selectionBox = new Drawable(this, new Sprite(
				CUtil.QuickImage("selectionbox")), LayerConstants.HUD);
		selectionBox.bIgnore = true;
		crossheir = gs.crossheir;
	}

	public void update() {
		handleInspect();
		handleEntityMenu();
	}

	private void handleInspect() {
		Entity target = null;
		if (MouseManager.getUpDownLeftMouseClick()) {
			for (Entity e : parentScreen.entities) {
				if (e.collidable != null
						&& e.collidable.isColliding(crossheir.position)
						&& !(e instanceof DanButton)) {
					Debugger.print("Inspecting...: " + e.toString());
					target = e;
				}
			}
		}
		if (target != null) {
			if (inspector != null)
				inspector.markForDeletion();
			inspector = new InspectorDialogue(parentScreen, this, target);
		}
	}

	private void handleEntityMenu() {
		if (KeyManager.isKeyHit(Input.KEY_4)) {
			if (entityMenu != null)
				entityMenu.markForDeletion();
			entityMenu = new DanMenu(parentScreen, parentScreen.entities,
					Filters.INSPECTOR, Layout.VERTICAL, Size.THIN, new Point(
							DanMenu.THIN_BUTTON_WIDTH / 2, 50));
		}
		if (entityMenu != null && entityMenu.hasMadeSelection()) {
			Entity e = (Entity) entityMenu.getSelection();
			inspector = new InspectorDialogue(parentScreen, this, e);
			entityMenu.markForDeletion();
			entityMenu = null;
		}
		if (entityMenu != null && entityMenu.isHoveringOverOption()) {
			Entity e = (Entity) entityMenu.getHoverSelection();
			selectionBox.bIgnore = false;
			selectionBox.setPositionRounded(e.position.x, e.position.y);
		} else {
			selectionBox.bIgnore = true;
		}
	}

	public void clearInspector() {
		if (inspector != null)
			inspector.markForDeletion();
		inspector = null;
		if (entityMenu != null)
			entityMenu.markForDeletion();
		entityMenu = null;
	}

	public void deactivateEditor() {
		clearInspector();
	}

	@Override
	public void setActive(boolean isActive) {
		super.setActive(isActive);
		selectionBox.bIgnore = !isActive;

		if (inspector != null)
			inspector.markForDeletion();
		inspector = null;
		if (entityMenu != null)
			entityMenu.markForDeletion();
		entityMenu = null;
	}

	public void setDrawableStatus(boolean isInvis) {
		selectionBox.bIgnore = isInvis;
	}

	public String toString() {
		return "Inspector";
	}

}
