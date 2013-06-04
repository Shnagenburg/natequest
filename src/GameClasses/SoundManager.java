package GameClasses;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import NateQuest.Projectiles.EnemyProjectiles.SonicWave;

public class SoundManager {

	public static Sound batdead;
	public static Sound crabdead;
	public static Sound enemydamage1;
	public static Sound enemydamage2;
	public static Sound enemydamage3;
	public static Sound grapplechink;
	public static Sound heartgun;
	public static Sound hookshotgun;
	public static Sound knightdead;
	public static Sound lizarddead;
	public static Sound natedamage;
	public static Sound natedead;
	public static Sound orcdead1;
	public static Sound orcdead2;
	public static Sound rocketgun;
	public static Sound shotgun;
	public static Sound warpin;
	public static Sound wizarddead;

	public static Sound meow;
	public static Sound bark;
	public static Music theme;
	public static Music battle;
	public static Music town;
	private static boolean musicIsOn = true;
	private static boolean soundIsOn = true;

	public static void init() {
		System.out.println("About to load sounds");
		try {
			batdead = new Sound("sounds/batdead.ogg");
			crabdead = new Sound("sounds/crabdead.ogg");
			enemydamage1 = new Sound("sounds/enemydamage1.ogg");
			enemydamage2 = new Sound("sounds/enemydamage2.ogg");
			enemydamage3 = new Sound("sounds/enemydamage3.ogg");
			grapplechink = new Sound("sounds/grapplechink.ogg");
			heartgun = new Sound("sounds/heartgun.ogg");
			hookshotgun = new Sound("sounds/hookshotgun.ogg");
			knightdead = new Sound("sounds/knightdead.ogg");
			lizarddead = new Sound("sounds/lizarddead.ogg");
			natedamage = new Sound("sounds/natedamage.ogg");
			natedead = new Sound("sounds/natedead.ogg");
			orcdead1 = new Sound("sounds/orcdead1.ogg");
			orcdead2 = new Sound("sounds/orcdead2.ogg");
			rocketgun = new Sound("sounds/rocketgun.ogg");
			shotgun = new Sound("sounds/shotgun.ogg");
			warpin = new Sound("sounds/wizarddead.ogg");
			wizarddead = new Sound("sounds/wizarddead.ogg");

			meow = new Sound("sounds/meow1.ogg");
			theme = new Music("sounds/Click on the Cat.ogg");
			bark = new Sound("sounds/bark1.ogg");
			battle = new Music("sounds/battle.ogg");
			town = new Music("sounds/town.ogg");
		} catch (SlickException e) {

			System.out.println("we couldnt find the sound!");
			e.printStackTrace();
		}
		System.out.println("done loading sounds");
	}

	public static void setMusicIsOn(boolean isOn) {
		musicIsOn = isOn;
		if (!musicIsOn) {
			theme.stop();
			town.stop();
		}
	}

	public static boolean getMusicIsOn() {
		return musicIsOn;
	}

	public static void setSFXIsOn(boolean isOn) {
		soundIsOn = isOn;
	}

	public static boolean getSFXisOn() {
		return soundIsOn;
	}

	public static void playSound(Sound sound) {
		playSound(sound, 1, 1);
	}

	public static void playSound(Sound sound, float pitch, float volume) {
		if (soundIsOn) {
			sound.play(pitch, volume);
		}
	}

	public static void playMusic(Music music) {
		playMusic(music, 1);
	}

	public static void playMusic(Music music, float speed) {
		if (musicIsOn) {
			music.loop(speed, 1);
		}
	}

}
