package NateQuest.Enemies;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.Sprite;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.Collidable.Section;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Drawables.MultiSprited;
import NateQuest.Projectiles.Projectile;
import NateQuest.VFX.DeathAnimation;

public class EnemySpawner extends Enemy {

	ArrayList<Enemy> spawnedEnemies;
	final int HP = 4;
	public int enemiesLeftToSpawn;
	public int enemiesFieldedAtOnce;
	float spawnRate;
	float timer = 0;

	public EnemySpawner() {
		this(CUtil.ScreenGettingLoaded);
	}

	public EnemySpawner(GameScreen gs) {
		super(gs);
		// TODO Auto-generated constructor stub
		spawnedEnemies = new ArrayList<Enemy>();
		Sprite s = new Sprite(CUtil.QuickImage("spawnerbase"));
		Sprite s2 = new Sprite(CUtil.QuickImage("spawntwist"));
		s.setPositionRounded(this.position.x, this.position.y);
		s2.setPositionRounded(this.position.x, this.position.y);

		MultiSprited ms = new MultiSprited(this, s2, LayerConstants.WALLS);
		ms.addSprite(s);
		drawable = ms;

		collidable = new Collidable(this);
		// collidable.matchRect(s.getX(), s.getY(), s.iFrameWidth,
		// s.getHeight());
		collidable.matchSprite(s);
		collidable.mask = CollidableMasks.SPAWNER;

		enemiesLeftToSpawn = 5;
		enemiesFieldedAtOnce = 3;
		spawnRate = 1000;
		health = 4;
	}

	public void update() {
		collidable.matchSprite(drawable.mSprite, Section.BOTTOM_HALF);

		if (enemiesLeftToSpawn <= 0)
			return;

		timer += CUtil.ElapsedTime;
		if (timer > spawnRate) {
			timer = 0;
			if (enemiesFieldedAtOnce > spawnedEnemies.size()) {
				spawnEnemy();
				enemiesLeftToSpawn--;
			}
		}
	}

	public void spawnEnemy() {
		Trooper troop = new Trooper(parentScreen);
		troop.setPosition(position.x, position.y);
		spawnedEnemies.add(troop);
	}

	public void takeDamage(Projectile p) {
		health--;
		if (health == 0) {
			enemiesLeftToSpawn = 0;
			this.markForDeletion();
		}
	}

}
