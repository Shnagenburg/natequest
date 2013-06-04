package GameClasses;

import java.applet.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Target;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import NateQuest.Debugger;
import NateQuest.ImageCreator;
import NateQuest.Gameplay.Camera;
import NateQuest.ImageCreator.DanColor;
import Utility.MathUtility;

// A drawable picture
public class Sprite implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4455710943517431384L;
	public String spriteName;
	protected boolean bHasDonePass;
	public Image mTexture;
	protected int iXpos;
	protected int iYpos;
	protected int iWidth;
	protected int iHeight;
	protected float fScale;
	protected float fAngle;
	public boolean bIsCameraed = true;
	public Camera camera;
	protected Color color;
	private Color flashColor;
	protected float fadeAlpha;
	private float flashTimer = 0;
	private float MAX_FLASH_TIMER = 0;
	protected boolean isFlashing = false;

	AffineTransform mAffineTransform;

	public Sprite(String name) {
		this(CUtil.QuickImage(name));
	}

	public Sprite(Image texture) {
		spriteName = texture.getName();
		bHasDonePass = false;
		mTexture = texture;

		// Extract and width and height of the picture
		iWidth = texture.getWidth();
		iHeight = texture.getHeight();

		fScale = 1.0f;
		fAngle = 0.0f;

		// creating the AffineTransform instance
		mAffineTransform = new AffineTransform();

		mTexture.setCenterOfRotation(iWidth / 2, iHeight / 2);

		fadeAlpha = 1.0f;
		color = new org.newdawn.slick.Color(1.0f, 1.0f, 1.0f, fadeAlpha);
	}

	public void update() { // do nothing
		updateFlash();
	}

	private void updateFlash() {
		if (isFlashing) {
			flashTimer += CUtil.ElapsedTime;
			float percent = MathUtility.percent(flashTimer, MAX_FLASH_TIMER);
			color.r = flashColor.r * percent;
			color.g = flashColor.g * percent;
			color.b = flashColor.b * percent;
			if (flashTimer > MAX_FLASH_TIMER) {
				isFlashing = false;
			}
		}
	}

	public void draw(Graphics g) {
		int x = iXpos;
		int y = iYpos;
		float scale = fScale;
		if (bIsCameraed) {
			x *= camera.getZoom();
			y *= camera.getZoom();
			x += camera.xRoundedWithZoom;
			y += camera.yRoundedWithZoom;
			scale *= camera.getZoom();
		}

		// Culling
		// int xbound = (int)((iXpos - (iWidth * fScale) / 2) + (bIsCameraed ?
		// CUtil.CurrentGame.camera.xRounded : 0));
		// int ybound = (int)((iYpos - (iHeight * fScale) / 2) + (bIsCameraed ?
		// CUtil.CurrentGame.camera.yRounded : 0));
		if (shouldCull(x, y, scale)) {
			return;
		}

		mTexture.setRotation((int) fAngle);
		if (fadeAlpha == 1.0f && !isFlashing) {
			mTexture.draw(x - (iWidth * scale) / 2, y - (iHeight * scale) / 2,
					scale);
		} else {
			mTexture.draw(x - (iWidth * scale) / 2, y - (iHeight * scale) / 2,
					scale, color);

		}

	}

	protected boolean shouldCull(int x, int y, float scale) {
		if (x + ((iWidth * scale)) < 0
				|| x - ((iWidth * scale)) > CUtil.Dimensions.width
				|| y + ((iHeight * scale)) < 0
				|| y - ((iHeight * scale)) > CUtil.Dimensions.height) {
			return true;
		}
		return false;
	}

	public boolean willBeCulled() {
		Camera camera = CUtil.CurrentGame.camera;
		int x = iXpos;
		int y = iYpos;
		float scale = fScale;
		if (bIsCameraed) {
			x *= camera.getZoom();
			y *= camera.getZoom();
			x += camera.xRoundedWithZoom;
			y += camera.yRoundedWithZoom;
			scale *= camera.getZoom();
		}
		if (x + ((iWidth * scale)) < 0
				|| x - ((iWidth * scale)) > CUtil.Dimensions.width
				|| y + ((iHeight * scale)) < 0
				|| y - ((iHeight * scale)) > CUtil.Dimensions.height) {
			return true;
		}
		return false;
	}

	public int getX() {
		return iXpos;
	}

	public int getY() {
		return iYpos;
	}

	public int getWidth() {
		return iWidth;
	}

	public int getHeight() {
		return iHeight;
	}

	public float getScale() {
		return fScale;
	}

	public float getRotation() {
		return fAngle;
	}

	public void setRotation(float value) {
		fAngle = value;

		if (bIsCameraed) {
			// Camera camera = CUtil.CurrentGame.camera;
			// mTexture.setCenterOfRotation(iWidth * camera.getZoom()/ 2,
			// iHeight * camera.getZoom() / 2);
		}
		// mTexture.setRotation(fAngle);
	}

	public void setPosition(int x, int y) {
		iXpos = x;
		iYpos = y;
	}

	public void setPositionRounded(float x, float y) {
		iXpos = Math.round(x);
		iYpos = Math.round(y);
	}

	public boolean getHasDonePass() {
		return bHasDonePass;
	}

	public void setScale(float scale) {
		fScale = scale;
		mTexture.setCenterOfRotation(iWidth * fScale / 2, iHeight * fScale / 2);
	}

	public void setAlpha(float newAlpha) {
		fadeAlpha = newAlpha;
		color.a = newAlpha;
	}

	public void flashColor(Color flashColor, float duration) {
		isFlashing = true;
		this.flashColor = flashColor;
		color = new Color(0, 0, 0);
		flashTimer = 0;
		MAX_FLASH_TIMER = duration;
	}

	// Do these two sprites intersect?
	public boolean intersect(Sprite sprite) {
		if (iXpos - (fScale * iWidth / 2) < sprite.getX()
				+ (sprite.getScale() * sprite.getWidth() / 2)
				&& iXpos + (fScale * iWidth / 2) > sprite.getX()
						- (sprite.getScale() * sprite.getWidth() / 2)
				&&

				iYpos - (fScale * iHeight / 2) < sprite.getY()
						+ (sprite.getScale() * sprite.getHeight() / 2)
				&& iYpos + (fScale * iHeight / 2) > sprite.getY()
						- (sprite.getScale() * sprite.getHeight() / 2)) {
			return true;
		}
		return false;
	}

	// is this point inside the sprite?
	public boolean isInsideSprite(float x, float y) {
		if (iXpos - (fScale * iWidth / 2) < x
				&& iXpos + (fScale * iWidth / 2) > x &&

				iYpos - (fScale * iHeight / 2) < y
				&& iYpos + (fScale * iHeight / 2) > y) {
			return true;
		}
		return false;
	}

	public boolean isWithinRange(Sprite sprite, float minDistance) {
		float xDist = iXpos - sprite.getX();
		float yDist = iYpos - sprite.getY();

		float distSquared = (xDist * xDist) + (yDist * yDist);
		if (distSquared < minDistance * minDistance) {
			return true;
		}
		return false;
	}

	// This version just tells you if the center point is within range
	// returns -1 if it didnt hit anything
	// 0 - right, 1 - top, 2 - left, 3 - bottom
	public int pointIntersect(Sprite sprite) {
		if (

		iXpos < sprite.getX() + (sprite.getScale() * sprite.getWidth() / 2)
				&& iXpos > sprite.getX()
						- (sprite.getScale() * sprite.getWidth() / 2)
				&&

				iYpos < sprite.getY()
						+ (sprite.getScale() * sprite.getHeight() / 2)
				&& iYpos > sprite.getY()
						- (sprite.getScale() * sprite.getHeight() / 2)) {
			// Which face is it closest to?
			int distToRight = Math.abs(iXpos
					- (sprite.getX() + (int) (sprite.getScale()
							* sprite.getWidth() / 2)));
			int distToLeft = Math.abs(iXpos
					- (sprite.getX() - (int) (sprite.getScale()
							* sprite.getWidth() / 2)));
			int distToTop = Math.abs(iYpos
					- (sprite.getY() - (int) (sprite.getScale()
							* sprite.getWidth() / 2)));
			int distToBottom = Math.abs(iYpos
					- (sprite.getY() + (int) (sprite.getScale()
							* sprite.getWidth() / 2)));
			int max = distToRight;

			if (max < distToLeft)
				max = distToLeft;
			if (max < distToTop)
				max = distToTop;
			if (max < distToBottom) {
				// max = distToBottom;
				return 3;
			}

			if (max == distToRight)
				return 0;
			else if (max == distToTop)
				return 1;
			else
				return 2;

		}
		return -1;
	}

	public float getTop() {
		return iYpos - ((iHeight / 2) * fScale);
	}

	public float getBottom() {
		return iYpos + ((iHeight / 2) * fScale);
	}

	public float getLeft() {
		return iXpos - ((iWidth / 2) * fScale);
	}

	public float getRight() {
		return iXpos + ((iWidth / 2) * fScale);
	}

	public float getScaledWidth() {
		return iWidth * fScale;
	}

	public float getScaledHeight() {
		return iHeight * fScale;
	}

	public void setTexture(Image texture) {
		mTexture = texture;
	}

	public void setFrame(int frame) {
	}

	// do nothing
	public void lock(int frame) {

	}

	// do nothing
	public void unlock() {
	}

	public String toString() {
		return "" + this.getClass().getSimpleName() + " x: " + iXpos + " y: "
				+ iYpos + " width: " + iWidth + " height " + iHeight;
	}

	private void readObject(java.io.ObjectInputStream in) {

		try {
			spriteName = (String) in.readObject();
			bHasDonePass = in.readBoolean();
			iXpos = in.readInt();
			iYpos = in.readInt();
			iWidth = in.readInt();
			iHeight = in.readInt();
			fScale = in.readFloat();
			fAngle = in.readFloat();
			bIsCameraed = in.readBoolean();
			fadeAlpha = in.readFloat();
			color = new Color(1, 1, 1, fadeAlpha);
			camera = (Camera) in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Null indicates it was a generated sprite, we need to remake it.
		if (spriteName.charAt(0) == '!') {
			try {
				int tx = in.readInt();
				int ty = in.readInt();
				int r = in.readInt();
				int g = in.readInt();
				int b = in.readInt();
				int a = in.readInt();
				mTexture = ImageCreator.createImage(tx, ty, r, g, b, a);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (spriteName.charAt(0) == '#') {
			try {
				int tx = in.readInt();
				int ty = in.readInt();
				int set = in.readInt();
				mTexture = ImageCreator.createButton(tx, ty, set);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			mTexture = CUtil.ResourcePool.getImageByName(spriteName);
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) {
		try {
			out.writeObject(spriteName);
			out.writeBoolean(bHasDonePass);
			out.writeInt(iXpos);
			out.writeInt(iYpos);
			out.writeInt(iWidth);
			out.writeInt(iHeight);
			out.writeFloat(fScale);
			out.writeFloat(fAngle);
			out.writeBoolean(bIsCameraed);
			out.writeFloat(fadeAlpha);
			out.writeObject(camera);

			// this means it was a generated sprite
			if (spriteName.charAt(0) == '!') {
				out.writeInt(mTexture.getWidth());
				out.writeInt(mTexture.getHeight());
				out.writeInt(mTexture.getColor(0, 0).getRed());
				out.writeInt(mTexture.getColor(0, 0).getBlue());
				out.writeInt(mTexture.getColor(0, 0).getGreen());
				out.writeInt(mTexture.getColor(0, 0).getAlpha());
			}
			// this means it was a generated button
			if (spriteName.charAt(0) == '#') {
				out.writeInt(mTexture.getWidth());
				out.writeInt(mTexture.getHeight());
				String s = mTexture.getName();
				int set = Integer.parseInt("" + s.charAt(s.length() - 1));
				out.writeInt(set);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
