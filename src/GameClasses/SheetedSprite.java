package GameClasses;

import java.applet.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.Serializable;

import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;

import NateQuest.Gameplay.Camera;

public class SheetedSprite extends Sprite implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9021480815797152265L;
	boolean bIsLocked;
	// BufferedImage [] mTextures;
	Image[] mTextures;
	int iFrames;
	public int iFrameWidth;
	int iCurrentFrame;
	int iDelay;
	float fTimer;

	public SheetedSprite(Image texture, int frames, int msDelay) {
		super(texture);
		bHasDonePass = false;

		// Get our frames
		mTextures = new Image[frames];

		// We need to find the width of a frame.
		iFrames = frames;
		iFrameWidth = iWidth / frames;

		iDelay = msDelay;
		fTimer = 0;

		iCurrentFrame = 0;
		iWidth = iFrameWidth;

		mTexture.setCenterOfRotation(iWidth / 2, iHeight / 2);
	}

	// See if we need to go to the next frame in the animation
	public void update() {
		super.update();
		if (bIsLocked)
			return;

		fTimer += GameHarness.GameTime;
		if (fTimer > iDelay) {
			fTimer -= iDelay;
			if (iCurrentFrame == iFrames - 1) {
				bHasDonePass = true;
				iCurrentFrame = 0;
			} else {
				iCurrentFrame++;
			}
		}
	}

	@Override
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

		// culling
		if (x + ((iFrameWidth * scale)) < 0
				|| x - ((iFrameWidth * scale)) > CUtil.Dimensions.width
				|| y + ((iHeight * scale)) < 0
				|| y - ((iHeight * scale)) > CUtil.Dimensions.height) {
			return;
		}
		mTexture.setRotation(fAngle);

		if (fadeAlpha == 1.0f && !isFlashing) {
			mTexture.draw(x - (iFrameWidth * scale) / 2, y - (iHeight * scale)
					/ 2, x + (iFrameWidth * scale) / 2, y + (iHeight * scale)
					/ 2, iFrameWidth * iCurrentFrame, 0, iFrameWidth
					* (iCurrentFrame + 1), iHeight);
		} else {
			mTexture.draw(x - (iFrameWidth * scale) / 2, y - (iHeight * scale)
					/ 2, x + (iFrameWidth * scale) / 2, y + (iHeight * scale)
					/ 2, iFrameWidth * iCurrentFrame, 0, iFrameWidth
					* (iCurrentFrame + 1), iHeight, color);

		}

	}

	public void setFrame(int frame) {
		iCurrentFrame = frame;
	}

	public void lock(int frame) {
		bIsLocked = true;
		iCurrentFrame = frame;
	}

	public void unlock() {
		bIsLocked = false;
	}

	public void reset() {
		bHasDonePass = false;
		iCurrentFrame = 0;
		fTimer = 0;
	}
}
