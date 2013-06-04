package NateQuest.Story;

import java.io.Serializable;
import java.util.ArrayList;

import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Editor.EditorController;
import NateQuest.Gameplay.Hero;
import NateQuest.Weapons.Armaments;

// These are the entities that travel between levels, such as your hero, his guns, hp bar, editor controller
public class PersistantEntities implements Serializable {

	ArrayList<Entity> persistantEntities;
	Hero hero;

	public PersistantEntities(GameScreen startingLevel) {

		// hero = new Hero(startingLevel);
		persistantEntities = new ArrayList<Entity>();
		// persistantEntities.add(hero);
		// persistantEntities.add(new EditorController(startingLevel));
		// persistantEntities.add(new Armaments(startingLevel, hero));
		// persistantEntities.add(new Crossheir(startingLevel));

		// startingLevel.hero = (Hero) startingLevel.findEntity(Hero.class);
		// startingLevel.editorController = (EditorController)
		// startingLevel.findEntity(EditorController.class);
		// startingLevel.crossheir = (Crossheir)
		// startingLevel.findEntity(Crossheir.class);

		for (Entity e : persistantEntities) {
			e.saveThisEntity = false;
		}
	}

	public void attachToLevel(GameScreen level) {
		for (Entity e : persistantEntities) {
			e.attach(level);
		}
	}

	public void detachFromLevel(GameScreen level) {
		level.entities.removeAll(persistantEntities);
	}

}
