package NateQuest.Editor;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.DrawableWithText;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Crossheir;
import NateQuest.Story.DialogueWindow;
import NateQuest.Tiling.TilePainter;
import NateQuest.Tiling.Tiles.Tile;
import UserInterface.DanMenu;
import UserInterface.DanMenu.Filters;
import UserInterface.DanMenu.Layout;
import UserInterface.DanMenu.Size;

public class EditorController extends Entity {

	final float CAM_SPEED = 10.0f;
	public TilePainter painter;
	public ThingEditor editor;
	public InspectorEditor inspectEditor;
	public LevelSelector levelSelector;
	public DrawableWithText label;
	public ArrayList<Editor> editors;
	public DanMenu editorSelector;

	public EditorController(GameScreen gs) {
		super(gs);
		painter = new TilePainter(gs);
		painter.bIsSleeping = true;
		painter.setDrawableStatus(true);
		editor = new ThingEditor(gs);
		editor.bIsSleeping = true;
		editor.setDrawableStatus(true);
		inspectEditor = new InspectorEditor(gs);
		inspectEditor.bIsSleeping = true;
		inspectEditor.setDrawableStatus(true);
		levelSelector = new LevelSelector(gs);
		levelSelector.bIsSleeping = true;
		levelSelector.setDrawableStatus(true);
		editors = new ArrayList<Editor>();
		editors.add(painter);
		editors.add(inspectEditor);
		editors.add(editor);
		editors.add(levelSelector);
		createEditorMenu();
		this.bIsSleeping = false;

		// label = new DialogueWindow(gs, new Vector2f(
		// (float)(CUtil.Dimensions.getWidth() - 200),
		// (float)(CUtil.Dimensions.getHeight() - 100)), new Rectangle(100,
		// 25));
		label = new DrawableWithText(this, null, LayerConstants.DEBUG, "");
		label.setPosition(800, 600);
		label.setText(gs.levelName);

		for (Editor ed : editors) {
			ed.parentController = this;
		}
		saveThisEntity = false;
	}

	public void removeEditorMenu() {
		if (editorSelector != null) {
			editorSelector.markForDeletion();
			editorSelector = null;
		}
	}

	public void createEditorMenu() {
		editorSelector = new DanMenu(parentScreen, editors, Filters.NONE,
				Layout.HORIZONTAL, Size.NORMAL, new Point(400, 50));

	}

	public void deactivateAllEditors() {
		for (Editor e : editors) {
			e.setActive(false);
		}
	}

	public void update() {
		// label.setMessage(parentScreen.levelName);
		if (KeyManager.isKeyHit(Input.KEY_T) && !editor.isPlayingLevel) {
			enableEditorCameraSettings();
			deactivateAllEditors();
			editor.setActive(true);
		} else if (KeyManager.isKeyHit(Input.KEY_Y) && !editor.isPlayingLevel) {
			enableEditorCameraSettings();
			deactivateAllEditors();
			painter.setActive(true);
		} else if (KeyManager.isKeyHit(Input.KEY_R) && !editor.isPlayingLevel) {
			enableEditorCameraSettings();
			deactivateAllEditors();
			levelSelector.setActive(true);
		} else if (KeyManager.isKeyHit(Input.KEY_I) && !editor.isPlayingLevel) {
			enableEditorCameraSettings();
			deactivateAllEditors();
			inspectEditor.setActive(true);
		}
		handleCamPan();
		handleSelectionMenu();
		label.setText(parentScreen.levelName);
		// checkTile();
	}

	private void checkTile() {
		if (KeyManager.isKeyDown(Input.KEY_W)) {
			Crossheir cross = (Crossheir) parentScreen
					.findEntity(Crossheir.class);
			Tile t = parentScreen.map.getTileFromPoint(cross.position.x,
					cross.position.y);
			Debugger.print(t.tileSprite.spriteName + " " + t);
		}
	}

	private void handleCamPan() {
		if (!editor.isPlayingLevel) {
			if (KeyManager.isKeyDown(Input.KEY_W)) {
				parentScreen.camera.shiftY(-CAM_SPEED);
			}
			if (KeyManager.isKeyDown(Input.KEY_S)) {
				parentScreen.camera.shiftY(CAM_SPEED);
			}
			if (KeyManager.isKeyDown(Input.KEY_A)) {
				parentScreen.camera.shiftX(-CAM_SPEED);
			}
			if (KeyManager.isKeyDown(Input.KEY_D)) {
				parentScreen.camera.shiftX(CAM_SPEED);
			}
			if (KeyManager.isKeyDown(Input.KEY_0)) {
				parentScreen.camera.jumpToPosition(0, 0);
			}
		}
	}

	private void handleSelectionMenu() {
		if (editorSelector != null && editorSelector.hasMadeSelection()
				&& !editor.isPlayingLevel) {
			Editor e = (Editor) editorSelector.getSelection();
			deactivateAllEditors();
			e.setActive(true);
		}
	}

	private void enableEditorCameraSettings() {
		parentScreen.camera.bIgnoreMaxes = true;
		parentScreen.camera.setPainterBounds();
	}

	public void resetTilePainter() {
		painter.markForDeletion();
		painter = new TilePainter(parentScreen);
		painter.bIsSleeping = true;
		painter.setDrawableStatus(true);
	}

	@Override
	public void markForDeletion() {
		super.markForDeletion();
		for (Editor ed : editors) {
			ed.markForDeletion();
		}
		deactivateAllEditors();
		editorSelector.markForDeletion();
	}

}
