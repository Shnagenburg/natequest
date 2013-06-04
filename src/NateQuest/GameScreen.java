package NateQuest;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import javax.crypto.spec.PSource;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import GameClasses.ScreenShifter.ShiftType;
import NateQuest.ActionItems.ActionLink;
import NateQuest.ActionItems.Door;
import NateQuest.ActionItems.FlipSwitch;
import NateQuest.ActionItems.LevelTransition;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.DrawableWithText;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Drawables.SpriteMapped;
import NateQuest.Editor.Editor;
import NateQuest.Editor.EditorController;
import NateQuest.Editor.EditorToggler;
import NateQuest.Editor.InspectorField;
import NateQuest.Editor.LevelSelector;
import NateQuest.Editor.ThingEditor;
import NateQuest.Editor.InspectorDialogue;
import NateQuest.Enemies.Enemy;
import NateQuest.Enemies.EnemySpawner;
import NateQuest.Enemies.GiantCrab;
import NateQuest.Enemies.Trooper;
import NateQuest.Enemies.Wizard;
import NateQuest.Gameplay.Camera;
import NateQuest.Gameplay.Crossheir;
import NateQuest.Gameplay.Hero;
import NateQuest.HUD.HealthBar;
import NateQuest.Tiling.ChunkMap;
import NateQuest.Tiling.TilePainter;
import NateQuest.Weapons.Armaments;
import NateQuest.Weapons.Gun;
import Screens.Screen;
import UserInterface.DanButton;

public class GameScreen extends Screen {

	GiantCrab crab;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8353566991399606347L;
	public int idCounter = 0;
	public ArrayList<Entity> entities;
	public ArrayList<Entity> collidables;
	public ArrayList<Entity> badguys;
	public ArrayList<Entity> items;
	public ArrayList<Drawable> drawables;
	public ArrayList<Enemy> enemies;
	public ArrayList<Entity> stuffToDelete;
	public Crossheir crossheir;
	public ChunkMap map;
	public ChunkMap lowerlay;
	public ChunkMap overlay;
	public Camera camera;
	public Hero hero;
	public ArrayList<LevelTransition> levelTransitions;
	public String levelName = "NEEDS NAME";
	public EditorController editorController, backupEditorController;
	public ArrayList<Drawable> nonSavedDrawables;

	public GameScreen() {
		init();
		new Hero(this);
		new Crossheir(this);
		new Armaments(this);
	}

	private void init() {
		entities = new ArrayList<Entity>();
		collidables = new ArrayList<Entity>();
		badguys = new ArrayList<Entity>();
		items = new ArrayList<Entity>();
		stuffToDelete = new ArrayList<Entity>();
		drawables = new ArrayList<Drawable>();
		enemies = new ArrayList<Enemy>();
		camera = new Camera(this);
		levelTransitions = new ArrayList<LevelTransition>();
	}

	public GameScreen(LevelParameters params) {
		init();

		map = new ChunkMap(this, params.width, params.height,
				LayerConstants.TILEMAP);
		camera.setMaxes(map);

		new Hero(this);
		new Armaments(this);
		new Crossheir(this);
		EditorToggler.enableEditor(this);
	}

	public GameScreen(boolean custom) {
		// CUtil.GameHandle.levels.add(this);
		entities = new ArrayList<Entity>();
		collidables = new ArrayList<Entity>();
		badguys = new ArrayList<Entity>();
		items = new ArrayList<Entity>();
		stuffToDelete = new ArrayList<Entity>();
		drawables = new ArrayList<Drawable>();
		enemies = new ArrayList<Enemy>();
		camera = new Camera(this);
		levelTransitions = new ArrayList<LevelTransition>();

		// Wall w = new Wall(this, new Vector2f(400, 200));

		map = new ChunkMap(this);
		// overlay = new ChunkMap(this, 5);
		// lowerlay = new ChunkMap(this, 2);
		// new Wizard(this, null, new Vector2f(400, 400));
		FlipSwitch flip = new FlipSwitch(this);
		flip.setPositionRounded(new Vector2f(200, 300));

		// flip = new FlipSwitch(this);
		// flip.setPositionRounded(new Vector2f(400, 300));
		Door door = new Door(this);
		flip.addActionLink(new ActionLink(flip, 0, door, 0));
		flip.addActionLink(new ActionLink(flip, 1, door, 1));

		camera.setMaxes(map);
		// new DialogueWindow(this, new Vector2f(200, 200));

		new Hero(this);
		new Armaments(this);
		new Crossheir(this);
		Trooper t = new Trooper(this);
		t.setPosition(300, 300);
		t.health = 200;
		// GiantCrab c = new GiantCrab(this);
		// c.setPosition(400, 400);
		// editorController = new EditorController(this);
		// setUpTransitions();

		// crab = new GiantCrab(this, null);
		// crab.setPosition(300, 400);

		// new DialogueWindow(this, new Vector2f(400, 400), new Rectangle(200,
		// 50));
	}

	private void setUpTransitions() {
		LevelTransition l1 = new LevelTransition(this);
		l1.sourceID = 0;
		l1.targetID = 1;
		l1.targetName = "two.lvl";
		l1.setPosition(0f, 400f);
		LevelTransition l2 = new LevelTransition(this);
		l2.sourceID = 1;
		l2.targetID = 0;
		l2.setPosition(24 * 64 - 64, 1000f);
		l2.targetName = "one.lvl";

		LevelTransition l3 = new LevelTransition(this);
		l3.sourceID = 3;
		l3.targetID = 4;
		l3.targetName = "two.lvl";
		l3.setPosition(400f, 0f);
		LevelTransition l4 = new LevelTransition(this);
		l4.sourceID = 4;
		l4.targetID = 3;
		l4.setPosition(600f, 24 * 64 - 64);
		l4.targetName = "one.lvl";
	}

	public void update() {
		for (int i = entities.size() - 1; i >= 0; i--) {
			if (!entities.get(i).bIsSleeping)
				entities.get(i).update();
		}
		// Debugger.print("entities: " + entities.size());
		flush();
		EditorToggler.handleInput(this);
	}

	public void flush() {
		if (!stuffToDelete.isEmpty()) {
			for (Entity e : stuffToDelete)
				e.removeEntity();
		}
		stuffToDelete.clear();
	}

	public void draw(Graphics g) {
		for (int i = drawables.size() - 1; i >= 0; i--) {
			drawables.get(i).draw(g);
		}
		// Debugger.print("drawables: " + drawables.size());
	}

	public void attachParents() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).parentScreen = this;
		}
	}

	// Returns the first entity of the given type
	public Entity findEntity(Class c) {
		for (Entity e : entities) {
			if (e.getClass() == c) {
				return e;
			}
		}
		return null;
	}

	// Returns a list of entities of the given class
	public ArrayList<Entity> findEntities(Class c) {

		ArrayList<Entity> toReturn = new ArrayList<Entity>();
		for (Entity e : entities) {
			// if (e.getClass().isAssignableFrom(c)){
			if (c.isInstance(e)) {
				toReturn.add(e);
			}
		}
		if (toReturn.isEmpty()) {
			return null;
		} else {
			return toReturn;
		}
	}

	public Entity findEntity(String name) {
		for (Entity e : entities) {
			if (e.getName().equals(name))
				return e;
		}
		return null;
	}

	public LevelTransition findTransition(int id) {
		for (LevelTransition l : levelTransitions) {
			if (l.sourceID == id) {
				return l;
			}
		}
		// Debugger.print("The level transition outlet id: " + id +
		// " couldn't be found");
		// Debugger.print("" + levelTransitions.size());
		return null;
	}

	public void setSleepingAll(boolean b) {
		for (Entity e : entities)
			e.bIsSleeping = b;
	}

	public void createOverlay() {
		overlay = new ChunkMap(this, map.getWidth(), map.getHeight(),
				LayerConstants.TILEMAP_OVERLAY);
	}

	public void createLowerlay() {
		lowerlay = new ChunkMap(this, map.getWidth(), map.getHeight(),
				LayerConstants.TILEMAP_LOWERLAY);
	}

	public void printEntities() {
		Debugger.print("----printing ents in " + levelName + " ----");
		for (Entity e : entities) {
			Debugger.print(e.toString());
		}
		Debugger.print("done printing ents---");
	}

	public void printLevelTransitions() {
		Debugger.print("----printing transitions in " + levelName + " "
				+ toString() + "----");
		for (LevelTransition e : levelTransitions) {
			Debugger.print(e.toString());
		}
		Debugger.print("done printing ents---");
	}

	public void reblendMaps() {
		if (map != null) {
			map.reblend();
		}
		if (lowerlay != null) {
			lowerlay.reblend();
		}
		if (overlay != null) {
			overlay.reblend();
		}

	}
}
