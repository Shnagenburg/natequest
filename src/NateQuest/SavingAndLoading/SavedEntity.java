package NateQuest.SavingAndLoading;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import GameClasses.CUtil;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.ActionItems.ActionLink;
import NateQuest.ActionItems.Actionable;
import NateQuest.ActionItems.LevelTransition;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.SpriteMapped;
import NateQuest.Editor.EditorController;
import NateQuest.Enemies.Enemy;
import NateQuest.Gameplay.Camera;
import NateQuest.Gameplay.Crossheir;
import NateQuest.Gameplay.Hero;
import NateQuest.Tiling.ChunkMap;

public class SavedEntity implements Serializable {

	private static final long serialVersionUID = -7856674001685219609L;
	Class typeOfEntity;
	Entity unloadedEntity;
	Map<String, String> values;
	private float x = 0, y = 0;
	SavedEntityParams savedParams;
	ArrayList<SavedActionLink> savedActionLinks;

	public SavedEntity(Entity e) {
		typeOfEntity = e.getClass();
		unloadedEntity = e;
		savedParams = new SavedEntityParams(e);
		if (e instanceof Actionable) {
			saveActionLinks();
		}
	}

	private void saveActionLinks() {
		// Debugger.print(unloadedEntity + " is actionable");
		Actionable actionable = (Actionable) unloadedEntity;
		savedActionLinks = new ArrayList<SavedActionLink>();
		for (ArrayList<ActionLink> linkSet : actionable.links) {
			for (ActionLink link : linkSet) {
				savedActionLinks.add(new SavedActionLink(link));
			}
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) {

		try {
			out.writeObject(typeOfEntity);
			out.writeFloat(unloadedEntity.position.x);
			out.writeFloat(unloadedEntity.position.y);
			out.writeObject(savedParams);
			out.writeObject(savedActionLinks);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readObject(java.io.ObjectInputStream in) {
		try {
			typeOfEntity = (Class) in.readObject();
			x = in.readFloat();
			y = in.readFloat();
			savedParams = (SavedEntityParams) in.readObject();
			savedActionLinks = (ArrayList<SavedActionLink>) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Entity craftEntity() {
		Entity entity = null;
		try {
			entity = (Entity) typeOfEntity.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}

		entity.setPosition(x, y);
		savedParams.parameterize(entity);
		return entity;
	}

	public void attachActionLinks(GameScreen screen) {
		if (savedActionLinks != null) {
			for (SavedActionLink link : savedActionLinks) {
				link.reattachActionLink(screen);
			}
		}
	}

}
