package GameClasses;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.*;
import org.newdawn.slick.font.effects.ColorEffect;

import Lighting.Light;
import NateQuest.Debugger;

//////////////////////////////////////////////////////////////////////////////
//*************************************************************************//
//
//					NOTE: WHEN USING THIS TEMPLATE YOU MUST GO
//					TO REFERENCED LIBRARIES, lwjgl.jar, PROPERTIES
//					NATIVE PATH, AND POINT TO PROJECT/lib
//
//*************************************************************************//
//////////////////////////////////////////////////////////////////////////////

public class GameHarness extends BasicGame {

	// static final int GAME_WIDTH = 800;
	// static final int GAME_HEIGHT = 480;
	static final int GAME_WIDTH = 1024;
	static final int GAME_HEIGHT = 720;
	static final int FPS = 60;

	public static int GameTime;
	long startTime, currTime, elapsedTime, drawTime;
	Game game;

	public GameHarness() {
		super("Nate Quest - Live the Legend");

	}

	public void restartGame() {
		game = new Game();
		CUtil.GameHandle = game;
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		CUtil.Harness = this;
		gc.setShowFPS(true);
		// locks mouse into screen
		gc.setMouseGrabbed(true);
		CUtil.SlickGameContainer = gc;

		// initDAVDES(gc);

		KeyManager.initialize();
		Font f = new Font("Verdana", Font.BOLD, 16);
		UnicodeFont ufont = new UnicodeFont(f);
		ufont.getEffects().add(new ColorEffect(java.awt.Color.white));
		ufont.addAsciiGlyphs();
		try {
			ufont.loadGlyphs();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Debugger.print("font!! " + ufont);
		CUtil.GameFont = ufont;
		SoundManager.init();

		// our timers
		startTime = System.currentTimeMillis();
		currTime = startTime;
		drawTime = 0;

		CUtil.Dimensions = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		game = new Game();
		CUtil.GameHandle = game;

		gc.setTargetFrameRate(FPS);
		gc.getGraphics().setBackground(new Color(25, 25, 10));
		// gc.getGraphics().setBackground(new Color(0, 0, 0));
		CUtil.GameGraphics = gc.getGraphics();

	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		GameTime = delta;
		CUtil.ElapsedTime = GameTime;

		// updateDAVDES(gc, delta);

		elapsedTime = System.currentTimeMillis() - currTime;
		currTime = System.currentTimeMillis();

		MouseManager.HandleSlickInput(gc.getInput());
		KeyManager.HandleSlickInput(gc.getInput());
		game.update();

	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		game.draw(g);
		// renderDAVDES(gc, g);
	}

	public static void main(String[] args) throws SlickException {
		// SoundManager.init();

		AppGameContainer app = new AppGameContainer(new GameHarness());

		app.setDisplayMode(GAME_WIDTH, GAME_HEIGHT, false);
		app.start();
	}

	// //////////////////
	// //////////////////
	// ////////////////
	// / DAVDES COOL LIGHTSTUFF
	// number of tiles in our simple horizontal sprite sheet
	public static final int TILE_COUNT = 5;

	// width/height of tile in pixels
	public static final int TILE_SIZE = 40;

	// size of alpha map (for use with sprite sheet)
	public static final int ALPHA_MAP_SIZE = 256;

	// space after tile, before next tile
	public static final int TILE_SPACING = 2;

	// the "sprite sheet" or texture atlas image
	private Image spriteSheet;

	// the sub-images of our sprite sheet
	private Image[] tileSprites;

	// our 2D map array
	private Image[][] tileMap;

	// map size in tiles
	private int mapWidth, mapHeight;

	// our alpha map image; just a feathered black circle on a transparent
	// background
	private Image alphaMap, player;
	private Image offscreen, lightsImage;
	private Graphics offscreenGraphics, lightsGraphics;

	private Random random = new Random();

	// our lights
	private List<Light> lights = new ArrayList<Light>();

	// a timer used for simple light scaling effect
	private long elapsed;

	// a shared instance of Color so we don't need to create a new one each
	// frame
	private Color sharedColor = new Color(1f, 1f, 1f, 1f);

	public void initDAVDES(GameContainer container) throws SlickException {
		// container.setVSync(true);
		// container.setTargetFrameRate(60);

		// To reduce texture binds, our alpha map and tilesheet will be in the
		// same texture
		// Most games will implement their own SpriteSheet class, but for
		// simplicity's sake:
		// map tiles are in a horizontal row starting at (0, 0)
		// alpha map is located below the tiles, at (0, TILE_SIZE+TILE_SPACING)

		// grab the alpha map
		alphaMap = new Image("images/lights/light1.png");

		// reset the lighting
		resetLights(container);

		// our offscreen image
		offscreen = new Image(container.getWidth(), container.getHeight());
		offscreenGraphics = offscreen.getGraphics();

		// our lights image
		lightsImage = new Image(container.getWidth(), container.getHeight());
		lightsGraphics = lightsImage.getGraphics();
	}

	public void resetLights(GameContainer container) {
		// clear the lights and add a new one with default scale
		lights.clear();
		lights.add(new Light(container.getInput().getMouseX(), container
				.getInput().getMouseY(), 1f));
	}

	public void renderDAVDES(GameContainer container, Graphics g)
			throws SlickException {
		// ---- First render our lights to a single image
		// technically we only need to do this whenever the lights change
		Graphics.setCurrent(lightsGraphics);

		// 1. clear the light image background to black
		lightsGraphics.setBackground(Color.black);
		lightsGraphics.clear();

		// 2. set up GL blending to avoid any transparency loss
		GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA,
				GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
				GL11.GL_ONE_MINUS_SRC_ALPHA);

		// 3. draw our lights... a feathered white circle on a transparent
		// background
		alphaMap.startUse();
		for (int i = 0; i < lights.size(); i++) {
			Light light = lights.get(i);
			light.tint.bind();
			alphaMap.drawEmbedded(light.x
					- (light.scale * alphaMap.getWidth() / 2f), light.y
					- (light.scale * alphaMap.getHeight() / 2f), light.scale
					* alphaMap.getWidth(), light.scale * alphaMap.getHeight());
		}
		alphaMap.endUse();

		// 4. reset the draw mode
		lightsGraphics.setDrawMode(Graphics.MODE_NORMAL);

		// 5. flush the light image graphics!!
		lightsGraphics.flush();

		// ---- Now we can start rendering to the screen
		Graphics.setCurrent(g);

		game.draw(g);

		// 2. Set up blend mode for masking
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_SRC_COLOR);

		// 3. Draw our light map
		lightsImage.draw();

		// ---- Reset the mode to normal before continuing!
		g.setDrawMode(Graphics.MODE_NORMAL);

		g.setColor(Color.white);
		g.drawString(
				"Mouse click to add a light (total count: " + lights.size()
						+ ")", 10, 25);
		g.drawString("Press R to randomize the map tiles", 10, 40);
		g.drawString("Press SPACE to reset the lights", 10, 55);
	}

	public void updateDAVDES(GameContainer container, int delta)
			throws SlickException {

		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			resetLights(container);
		}
		elapsed += delta;

		// update all lights to have them smoothly scale
		for (int i = 0; i < lights.size(); i++) {
			lights.get(i).update(elapsed / 1000f);
		}

		// the last-added light will be the one under the mouse
		if (lights.size() > 0) {
			Light l = lights.get(lights.size() - 1);
			l.x = container.getInput().getMouseX();
			l.y = container.getInput().getMouseY();
		}

	}

	// adds a new light
	public void mousePressed(int button, int x, int y) {
		float randSize = random.nextInt(15) * .1f;
		Color randColor = new Color(random.nextFloat(), random.nextFloat(),
				random.nextFloat());
		lights.add(new Light(x, y, randSize, randColor));
	}
}