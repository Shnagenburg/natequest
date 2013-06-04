package GameClasses;

import java.applet.Applet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import NateQuest.Debugger;
import NateQuest.ImageCreator;
import NateQuest.Collidables.Collidable;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.Tiling.Tiles.Tile.BasicType;
import NateQuest.Tiling.Tiles.WallTile;

public class ResourcePool {
	public final String[] blendedImages = { "grass2", "redrug", "sand",
			"stone", "crackedstone" };
	ArrayList<Image> mTextures;
	HashMap<String, Image> mTextureMap;

	public ResourcePool() {
		System.out.println("starting image loader");

		// First we need to get the names of all the images in the images folder
		mTextureMap = new HashMap<String, Image>();

		System.out.println();

		// use one or the other
		// addDirectoryOfImages("src/images");
		bruteForceLoadImages();

		Image horzfade = ImageCreator.createHorizFadedScreenTransition();
		mTextureMap.put(horzfade.getName(), horzfade);

		Image vertfade = ImageCreator.createVerticalFadedScreenTransition();
		mTextureMap.put(vertfade.getName(), vertfade);

	}

	public boolean isBlendedImage(String s) {
		// for (String str : blendedImages)
		// if (str.equals(s))
		return true;
		// return false;
	}

	private void craftBlendSet(String s) {
		Image up = ImageCreator.createBlurTop(mTextureMap.get(s));
		Image corner = ImageCreator.createBlurCorner(mTextureMap.get(s));
		Image inverted = ImageCreator.createBlurCornerInverted(mTextureMap
				.get(s));

		mTextureMap.put(up.getName(), up);
		mTextureMap.put(corner.getName(), corner);
		mTextureMap.put(inverted.getName(), inverted);
	}

	// Inspects a directory and adds all pictures to the list.
	// Will also recurse into other directories
	private void addDirectoryOfImages(String directory) {
		// System.out.println("trying to create file object..." + directory);

		File folder = new File(directory);
		// System.out.println("Directory: " + folder.getName() + " can read? " +
		// folder.exists() );
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				// load the file we found
				addImageWithFilename(directory + "/" + listOfFiles[i].getName());
				// System.out.println( "found " + listOfFiles[i].getName() ) ;

				String str = directory.substring(4);
				System.out.println("addImageWithFilename( \"" + str + "/"
						+ listOfFiles[i].getName() + "\");");
			} else if (listOfFiles[i].isDirectory()) {
				// recurse into the directory we found
				// System.out.println("heading into Directory " +
				// listOfFiles[i].getName());
				addDirectoryOfImages(directory + "/" + listOfFiles[i].getName());
			}
		}

	}

	// Add picture into the list
	private void addImageWithFilename(String filename) {
		Image image;

		try {
			if (!(filename.contains(".png") || filename.contains(".jpg")))
				return;
			image = new Image(filename);

			// wipe off the directory
			int last = filename.lastIndexOf("/");
			filename = filename.substring(last + 1, filename.length());

			// Get rid of file extension
			filename = filename.replace(".png", "");
			filename = filename.replace(".jpg", "");

			// just in case, ha ha ha
			filename = filename.toLowerCase();
			image.setName(filename);
			mTextureMap.put(filename, image);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Given the name of the picture, find its image
	public Image getImageByName(String filename) {
		Image image = null;
		if (filename == null)
			System.out.println("Asking for a null image.");

		image = mTextureMap.get(filename);

		if (image != null)
			return image;

		System.out.println("The image asked for: " + filename
				+ " couldn't be found.");
		for (String s : Tile.tileImageMap) {
			if (filename.contains(s) && filename.contains("BLEND")) {
				Debugger.print("Asking for a blended image for the first time, and its on the list. Crafting image...");
				craftBlendSet(s);

				image = mTextureMap.get(filename);

				if (image != null) {
					Debugger.print("Done crafting");
					return image;
				}
			}
		}
		for (String s : WallTile.wallImageMap) {
			if (filename.contains(s) && filename.contains("BLEND")) {
				Debugger.print("Asking for a blended image for the first time, and its on the list. Crafting image...");
				craftBlendSet(s);

				image = mTextureMap.get(filename);

				if (image != null) {
					Debugger.print("Done crafting");
					return image;
				}
			}
		}

		return null;
	}

	public Collection<Image> getTextures() {
		return mTextureMap.values();
	}

	public boolean imageExists(String name) {
		Image image = mTextureMap.get(name);
		if (image != null)
			return true;
		return false;
	}

	// for the web
	private void bruteForceLoadImages() {
		// EXAMPLE, MUST BE UPDATED
		addImageWithFilename("images/characters/dude.png");
		addImageWithFilename("images/characters/gun1.png");
		addImageWithFilename("images/characters/nate4frames.png");
		addImageWithFilename("images/characters/natehead.png");
		addImageWithFilename("images/characters/orc.png");
		addImageWithFilename("images/characters/orcarms.png");
		addImageWithFilename("images/characters/orcgun1.png");
		addImageWithFilename("images/characters/orchead.png");
		addImageWithFilename("images/characters/puchgun.png");
		addImageWithFilename("images/characters/Thumbs.db");
		addImageWithFilename("images/characters/wizard.png");
		addImageWithFilename("images/characters/wizarddeath.png");
		addImageWithFilename("images/enemies/bat4frames.png");
		addImageWithFilename("images/enemies/bat4frames2.png");
		addImageWithFilename("images/enemies/clawone.png");
		addImageWithFilename("images/enemies/clawtwo.png");
		addImageWithFilename("images/enemies/crabcarp.png");
		addImageWithFilename("images/enemies/knight2frames.png");
		addImageWithFilename("images/enemies/legsone.png");
		addImageWithFilename("images/enemies/legstwo.png");
		addImageWithFilename("images/enemies/lizard4frames.png");
		addImageWithFilename("images/enemies/orcheadred.png");
		addImageWithFilename("images/enemies/redlizard4frames.png");
		addImageWithFilename("images/enviroment/crate.png");
		addImageWithFilename("images/enviroment/door.png");
		addImageWithFilename("images/enviroment/door4wip.png");
		addImageWithFilename("images/enviroment/Thumbs.db");
		addImageWithFilename("images/equippedguns/heartgun1.png");
		addImageWithFilename("images/equippedguns/hookshot1.png");
		addImageWithFilename("images/equippedguns/magnetguneqp1.png");
		addImageWithFilename("images/equippedguns/rocketlauncher1.png");
		addImageWithFilename("images/equippedguns/shotguneqp1.png");
		addImageWithFilename("images/equippedguns/sinwaveeqp1.png");
		addImageWithFilename("images/help/12toswapweapons.png");
		addImageWithFilename("images/help/enemieswontrespawn.png");
		addImageWithFilename("images/help/etointeract.png");
		addImageWithFilename("images/help/lmbtofire.png");
		addImageWithFilename("images/help/respawnatcheckpoints.png");
		addImageWithFilename("images/help/rightclickfor.png");
		addImageWithFilename("images/help/wasdtomove.png");
		addImageWithFilename("images/icons/arrowicon.png");
		addImageWithFilename("images/icons/billboard.png");
		addImageWithFilename("images/icons/hookicon.png");
		addImageWithFilename("images/icons/magneticon.png");
		addImageWithFilename("images/icons/rocketicon.png");
		addImageWithFilename("images/icons/selectionbox.png");
		addImageWithFilename("images/icons/shotgunicon.png");
		addImageWithFilename("images/icons/warpcontrol.png");
		addImageWithFilename("images/icons/warpicon.png");
		addImageWithFilename("images/lights/light1.png");
		addImageWithFilename("images/misc/cavespawner.png");
		addImageWithFilename("images/misc/checkpoint.png");
		addImageWithFilename("images/misc/circleicon.png");
		addImageWithFilename("images/misc/crossheir.png");
		addImageWithFilename("images/misc/eraseicon.png");
		addImageWithFilename("images/misc/lever.png");
		addImageWithFilename("images/misc/nate.png");
		addImageWithFilename("images/misc/penicon.png");
		addImageWithFilename("images/misc/picture.png");
		addImageWithFilename("images/misc/redblock.png");
		addImageWithFilename("images/misc/redblocksmall.png");
		addImageWithFilename("images/misc/spawnerbase.png");
		addImageWithFilename("images/misc/spawntwist.png");
		addImageWithFilename("images/misc/squareicon.png");
		addImageWithFilename("images/pickups/hookshotpickup.png");
		addImageWithFilename("images/pickups/lasergunpickup.png");
		addImageWithFilename("images/pickups/magnetpickup.png");
		addImageWithFilename("images/pickups/rocketlauncherpickup.png");
		addImageWithFilename("images/pickups/shotgunpickup.png");
		addImageWithFilename("images/pickups/sinewavepickup.png");
		addImageWithFilename("images/projectiles/blueball.png");
		addImageWithFilename("images/projectiles/bubble.png");
		addImageWithFilename("images/projectiles/fireball.png");
		addImageWithFilename("images/projectiles/heart.png");
		addImageWithFilename("images/projectiles/hook.png");
		addImageWithFilename("images/projectiles/lizardfireball.png");
		addImageWithFilename("images/projectiles/magnet.png");
		addImageWithFilename("images/projectiles/pellet.png");
		addImageWithFilename("images/projectiles/redstar.png");
		addImageWithFilename("images/projectiles/rocket.png");
		addImageWithFilename("images/projectiles/sonic2frames.png");
		addImageWithFilename("images/projectiles/Thumbs.db");
		addImageWithFilename("images/story/noimage.png");
		addImageWithFilename("images/story/puchie.png");
		addImageWithFilename("images/story/titlescreen.png");
		addImageWithFilename("images/story/titletext2.png");
		addImageWithFilename("images/Thumbs.db");
		addImageWithFilename("images/tiles/basic/bluemarbel.png");
		addImageWithFilename("images/tiles/basic/blueporcelain.png");
		addImageWithFilename("images/tiles/basic/blueroof.png");
		addImageWithFilename("images/tiles/basic/crackedstonegems.png");
		addImageWithFilename("images/tiles/basic/gemfloor.png");
		addImageWithFilename("images/tiles/basic/grass3.png");
		addImageWithFilename("images/tiles/basic/greenporcelain.png");
		addImageWithFilename("images/tiles/basic/greymarble.png");
		addImageWithFilename("images/tiles/basic/molten1.png");
		addImageWithFilename("images/tiles/basic/orangerug.png");
		addImageWithFilename("images/tiles/basic/pinkcracked.png");
		addImageWithFilename("images/tiles/basic/porcelain1.png");
		addImageWithFilename("images/tiles/basic/purplerug.png");
		addImageWithFilename("images/tiles/basic/redrug2.png");
		addImageWithFilename("images/tiles/basic/sand2.png");
		addImageWithFilename("images/tiles/basic/sandfadecorner.png");
		addImageWithFilename("images/tiles/basic/sandfadeedge.png");
		addImageWithFilename("images/tiles/basic/wood.png");
		addImageWithFilename("images/tiles/brick2.png");
		addImageWithFilename("images/tiles/crackedstone.png");
		addImageWithFilename("images/tiles/dunwall2.png");
		addImageWithFilename("images/tiles/glass.png");
		addImageWithFilename("images/tiles/grass/grassblendsandcorner.png");
		addImageWithFilename("images/tiles/grass/grassblendsandfour.png");
		addImageWithFilename("images/tiles/grass/grassblendsandinversecorner.png");
		addImageWithFilename("images/tiles/grass/grassblendsandthree.png");
		addImageWithFilename("images/tiles/grass/grassblendsandtop.png");
		addImageWithFilename("images/tiles/grass/grassblurcorner.png");
		addImageWithFilename("images/tiles/grass/grassblurcornerinverted.png");
		addImageWithFilename("images/tiles/grass/grassblurtop.png");
		addImageWithFilename("images/tiles/grass/grasscorner.png");
		addImageWithFilename("images/tiles/grass/grassedge.png");
		addImageWithFilename("images/tiles/grass2.png");
		addImageWithFilename("images/tiles/redrug.png");
		addImageWithFilename("images/tiles/road.png");
		addImageWithFilename("images/tiles/roadt.png");
		addImageWithFilename("images/tiles/roadturn.png");
		addImageWithFilename("images/tiles/sand.png");
		addImageWithFilename("images/tiles/stone.png");
		addImageWithFilename("images/tiles/walls/barwall.png");
		addImageWithFilename("images/tiles/walls/dunwall.png");
		addImageWithFilename("images/tiles/walls/greywall.png");
		addImageWithFilename("images/tiles/walls/ironbars.png");
		addImageWithFilename("images/tiles/walls/trussblack.png");
		addImageWithFilename("images/tiles/walls/woodwall.png");
		addImageWithFilename("images/tiles/water3frames.png");
		addImageWithFilename("images/tiles/xroad.png");
		addImageWithFilename("images/VFX/smoke3frames.png");
	}
}
