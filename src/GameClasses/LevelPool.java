package GameClasses;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.ImageCreator;
import NateQuest.SavingAndLoading.SaverAndLoader;

public class LevelPool {

	// load all the .lvls in a directory
	private HashMap<String, GameScreen> levelMap;
	private ArrayList<GameScreen> levelList;
	public String currentDirectory;

	public LevelPool(String directoryOfWorld) {
		System.out.println("starting level loader");
		CUtil.LevelPool = this;
		currentDirectory = directoryOfWorld;

		// First we need to get the names of all the images in the images folder
		levelMap = new HashMap<String, GameScreen>();
		levelList = new ArrayList<GameScreen>();

		System.out.println();

		// use one or the other
		// addDirectoryOfLevels(directoryOfWorld);
		bruteForceLoadLevels();

		Debugger.print("Ending level loader");
	}

	// Inspects a directory and adds all pictures to the list.
	// Will also recurse into other directories
	private void addDirectoryOfLevels(String directory) {
		// System.out.println("trying to create file object..." + directory);

		File folder = new File(directory);
		// System.out.println("Directory: " + folder.getName() + " can read? " +
		// folder.exists() );

		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				// load the file we found
				addLevelWithFilename( /* directory + "/" + */listOfFiles[i]
						.getName());
				// System.out.println( "found " + listOfFiles[i].getName() ) ;

				String str = directory.substring(4);
			} else if (listOfFiles[i].isDirectory()) {
				// recurse into the directory we found
				// System.out.println("heading into Directory " +
				// listOfFiles[i].getName());
				addLevelWithFilename(directory + "/" + listOfFiles[i].getName());
			}
		}

	}

	// Add picture into the list
	private void addLevelWithFilename(String filename) {
		if (filename.contains("temp.sav"))
			return;
		GameScreen gs;

		filename = filename.replace("-entz", "");
		filename = filename.replace("-map", "");
		filename = filename.replace("overlay", "");
		filename = filename.replace("lowerlay", "");
		int last = filename.lastIndexOf("/");
		String levelname = filename.substring(last + 1, filename.length());
		levelname = levelname.toLowerCase();

		if (!levelMap.containsKey(levelname)) {
			// Debugger.print("Loading " + filename);
			gs = SaverAndLoader.superLoad(filename);
			if (gs == null)
				return;

			System.out.println("addLevelWithFilename(\"" + filename + "\");");
			levelMap.put(levelname, gs);
			GameScreen markedToDelete = null;
			for (GameScreen level : levelList) {
				if (level.levelName.equals(gs.levelName)) {
					markedToDelete = level;
					break;
				}
			}
			if (markedToDelete != null) {
				levelList.remove(markedToDelete);
			}
			levelList.add(gs); // REVISIT needs to delete on refesh
		}
	}

	// Given the name of the level, find it
	public GameScreen getLevelByName(String filename) {

		GameScreen gs = null;
		if (filename == null)
			System.out.println("Asking for a null level.");

		gs = levelMap.get(filename);

		if (gs != null)
			return gs;

		System.out.println("The level asked for: " + filename
				+ " couldn't be found.");

		return null;
	}

	public ArrayList<GameScreen> getLevels() {
		return levelList;
	}

	public HashMap<String, GameScreen> getLevelMap() {
		return levelMap;
	}

	public void printLevels() {
		Debugger.print("Map: ");
		for (GameScreen screen : levelMap.values()) {
			Debugger.peek(screen, screen.levelName);
		}
		Debugger.print("List: ");
		for (GameScreen screen : levelList) {
			Debugger.peek(screen, screen.levelName);
		}
	}

	private void bruteForceLoadLevels() {
		addLevelWithFilename("a-1");
		addLevelWithFilename("a-2");
		addLevelWithFilename("a-3");
		addLevelWithFilename("a-4");
		addLevelWithFilename("a-5");
		addLevelWithFilename("a-6");
		addLevelWithFilename("b-1");
		addLevelWithFilename("b-2");
		addLevelWithFilename("b-3");
		addLevelWithFilename("b-4");
		addLevelWithFilename("b-5");
		addLevelWithFilename("b-6");
		addLevelWithFilename("b-7");
		addLevelWithFilename("c-1");
		addLevelWithFilename("c-2");
		addLevelWithFilename("c-3");
		addLevelWithFilename("c-4");
		addLevelWithFilename("c-5");
		addLevelWithFilename("d-1");
		addLevelWithFilename("d-2");
		addLevelWithFilename("d-3");
		addLevelWithFilename("d-4");
		addLevelWithFilename("d-5");
		addLevelWithFilename("default_map");
		addLevelWithFilename("e-1");
		addLevelWithFilename("e-2");
		addLevelWithFilename("file.sav");
		addLevelWithFilename("three");
		addLevelWithFilename("two");
		addLevelWithFilename("xxx");
	}

}
