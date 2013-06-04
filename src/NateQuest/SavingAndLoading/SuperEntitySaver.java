package NateQuest.SavingAndLoading;

import java.awt.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import GameClasses.CUtil;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Tiling.ChunkMap;

public class SuperEntitySaver {

	public static void saveEntities(ArrayList<Entity> entities, String filename) {
		ArrayList<SavedEntity> savedEntities = new ArrayList<SavedEntity>();
		// Empty screen for entity params
		CUtil.ScreenGettingLoaded = new GameScreen();
		for (Entity e : entities) {
			savedEntities.add(new SavedEntity(e));
		}
		try {
			FileOutputStream saveFile = new FileOutputStream(filename + "-entz");
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(savedEntities);
			save.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public static ArrayList<SavedEntity> loadEntities(String filename) {
		ArrayList<SavedEntity> savedEntities = null;
		try {
			// FileInputStream saveFile = new FileInputStream(filename+"-entz");
			// ObjectInputStream save = new ObjectInputStream(saveFile);
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(filename + "-entz");
			ObjectInputStream save = new ObjectInputStream(is);
			savedEntities = (ArrayList<SavedEntity>) save.readObject();
			save.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		for (SavedEntity e : savedEntities) {
			e.craftEntity();
		}
		for (SavedEntity e : savedEntities) {
			e.attachActionLinks(CUtil.ScreenGettingLoaded);
		}
		return savedEntities;
	}

}
