package NateQuest.SavingAndLoading;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Tiling.ChunkMap;
import NateQuest.Tiling.SavedChunkMap;

public class SaverAndLoader implements Serializable {

	public static GameScreen loadWithNameInput() {
		System.out.println("Enter name to load: ");
		Scanner user_input = new Scanner(System.in);

		String name;
		name = user_input.next();
		return superLoad(name);
	}

	public static void saveWithNameInput(GameScreen screen) {
		System.out.println("Enter name to save this screen as: ");
		Scanner user_input = new Scanner(System.in);

		String name;
		name = user_input.next();
		screen.levelName = name;
		superSave(screen, name);
	}

	public static void saveMap(ChunkMap map, String filename) {
		filename += "-map";
		SavedChunkMap savedMap = new SavedChunkMap(map);
		try {
			FileOutputStream saveFile = new FileOutputStream(filename);
			ObjectOutputStream save = new ObjectOutputStream(saveFile);

			save.writeObject(savedMap);
			save.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	static private void loadMap(GameScreen parentScreen, String filename) {
		ChunkMap map = null;
		SavedChunkMap savedMap = null;
		try {

			ObjectInputStream save = getObjectInputStream(filename + "-map");
			savedMap = (SavedChunkMap) save.readObject();
			map = new ChunkMap(parentScreen, savedMap, LayerConstants.TILEMAP);
			parentScreen.map = map;
			parentScreen.camera.setMaxes(map);
			save.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		loadOverlay(filename, parentScreen);
		loadLowerlay(filename, parentScreen);

	}

	private static void loadOverlay(String filename, GameScreen parentScreen) {
		ChunkMap map = null;
		SavedChunkMap savedMap = null;
		try {
			ObjectInputStream save = getObjectInputStream(filename
					+ "overlay-map");
			savedMap = (SavedChunkMap) save.readObject();
			map = new ChunkMap(parentScreen, savedMap,
					LayerConstants.TILEMAP_OVERLAY);
			parentScreen.overlay = map;
			save.close();
		} catch (Exception exc) {
			// Debugger.print("No overlay for " + filename);
		}
	}

	private static void loadLowerlay(String filename, GameScreen parentScreen) {
		ChunkMap map = null;
		SavedChunkMap savedMap = null;
		try {
			ObjectInputStream save = getObjectInputStream(filename
					+ "lowerlay-map");
			savedMap = (SavedChunkMap) save.readObject();
			map = new ChunkMap(parentScreen, savedMap,
					LayerConstants.TILEMAP_LOWERLAY);
			parentScreen.lowerlay = map;
			save.close();
		} catch (Exception exc) {
			// Debugger.print("No overlay for " + filename);
		}
	}

	public static void superSave(GameScreen screen, String filename) {
		String fullDirectory = "src/" + CUtil.LevelPool.currentDirectory + "/"
				+ filename;
		ChunkMap map = screen.map;

		ArrayList<Entity> entities = new ArrayList<Entity>();
		for (Entity e : screen.entities) {
			if (e.saveThisEntity)
				entities.add(e);
		}

		try {
			saveMap(map, fullDirectory);
			if (screen.overlay != null)
				saveMap(screen.overlay, fullDirectory + "overlay");
			if (screen.lowerlay != null)
				saveMap(screen.lowerlay, fullDirectory + "lowerlay");
			SuperEntitySaver.saveEntities(entities, fullDirectory);
		} catch (Exception exc) {
			exc.printStackTrace(); // If there was an error, print the info.
		}
	}

	public static GameScreen superLoad(String filename) {
		String fullDirectory = CUtil.LevelPool.currentDirectory + "/"
				+ filename;
		GameScreen screen = new GameScreen();
		screen.levelName = filename;
		CUtil.ScreenGettingLoaded = screen;
		try {
			loadMap(screen, fullDirectory);
			SuperEntitySaver.loadEntities(fullDirectory); // Auto loads to
															// 'ScreenGettingLoading'
			screen.reblendMaps();
		} catch (Exception exc) {
			exc.printStackTrace(); // If there was an error, print the info.
			return null;
		}
		return screen;
	}

	private static ObjectInputStream getObjectInputStream(String name) {
		return getObjectInputStreamProd(name);
	}

	private static ObjectInputStream getObjectInputStreamProd(String name) {
		FileInputStream saveFile;
		ObjectInputStream save = null;
		try {

			File file = null;
			// InputStream is =
			// this.getClass().getResourceAsStream("yourpackage/mypackage/myfile.xml");
			// file = new
			// File(Thread.currentThread().getContextClassLoader().getResource(name).getFile());
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(name);

			// saveFile = new FileInputStream(file);
			save = new ObjectInputStream(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return save;
	}

	private static ObjectInputStream getObjectInputStreamDev(String name) {
		FileInputStream saveFile;
		ObjectInputStream save = null;
		try {
			saveFile = new FileInputStream(name);
			save = new ObjectInputStream(saveFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return save;
	}

}
