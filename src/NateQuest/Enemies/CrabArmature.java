package NateQuest.Enemies;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.ActionItems.LevelTransition;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Drawables.SpriteMapped;
import NateQuest.Editor.EditorController;
import NateQuest.Gameplay.Camera;
import NateQuest.Gameplay.Crossheir;
import NateQuest.Gameplay.Hero;
import NateQuest.Tiling.ChunkMap;
import Utility.UtilityOscillator;
import Utility.UtilityTimer;

public class CrabArmature {

	UtilityOscillator oscillator;
	UtilityTimer timer;
	Drawable drawable;
	Vector2f offset;
	float rotation;
	GiantCrab parentCrab;
	Vector2f position;

	public CrabArmature(GiantCrab parentCrab, Vector2f offset, String imagename) {
		drawable = new Drawable(parentCrab, new Sprite(imagename),
				LayerConstants.CHARACTERS);
		this.offset = offset;
		this.parentCrab = parentCrab;
		position = new Vector2f();
	}

	public void setTimeAndOscillator() {
	};

	public void setPositionFromParent() {
		float newX = offset.x + parentCrab.position.x;
		float newY = offset.y + parentCrab.position.y;
		drawable.setPositionRounded(newX, newY);
		position.x = newX;
		position.y = newY;
	}

	public void setRotation(float newRotation) {
		float angle = parentCrab.rotation;
		rotation = newRotation;
		drawable.setRotation(angle + rotation);
		angle = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float newOffsetX = offset.x * cos - offset.y * sin;
		float newOffsetY = offset.x * sin + offset.y * cos;
		float newX = (float) (parentCrab.position.x + newOffsetX);
		float newY = (float) (parentCrab.position.y + newOffsetY);
		drawable.setPositionRounded(newX, newY);
		position.x = newX;
		position.y = newY;
	}

	public void update() {
	}

	public float getTotalRotation() {
		return drawable.mSprite.getRotation();
	}

	public void removeArmature() {
		drawable.removeProperty();
	}

}
