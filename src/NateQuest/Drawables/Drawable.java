package NateQuest.Drawables;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.Property;

public class Drawable extends Property implements Serializable,
		Comparable<Drawable> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7615981025553782621L;
	public Sprite mSprite;
	int iPriority;
	public boolean bIgnore = false;

	public Drawable(Entity parentEntity) {
		super(parentEntity);
		iPriority = 0;
		addThisToDrawables();
	}

	public Drawable(Entity parentEntity, Sprite sprite, int priority) {
		super(parentEntity);
		mSprite = sprite;
		if (mSprite != null)
			mSprite.camera = parentEntity.parentScreen.camera;
		iPriority = priority;
		addThisToDrawables();
	}

	public void addThisToDrawables() {
		if (parentEntity == null || parentEntity.parentScreen == null
				|| parentEntity.parentScreen.drawables == null) {
			// Debugger.print("xxxxxxxxxxxxxxxxxxxpe: " + parentEntity +
			// parentEntity.parentScreen);
			parentEntity.parentScreen = CUtil.ScreenGettingLoaded;
			// Debugger.print("xxxxxxxxxxxxxxxxxxxpe: " + parentEntity +
			// parentEntity.parentScreen);
		}
		// Debugger.print("waga");
		// Debugger.print("waga2: " + parentEntity);
		// Debugger.print("waga3: " + parentEntity.parentScreen);
		// Debugger.print("waga4: " + parentEntity.parentScreen.drawables);
		for (int i = 0; i < parentEntity.parentScreen.drawables.size(); i++) {
			if (parentEntity.parentScreen.drawables.get(i).iPriority <= iPriority) {
				parentEntity.parentScreen.drawables.add(i, this);
				return;
			}
		}
		parentEntity.parentScreen.drawables.add(this);
	}

	public void update() {
		mSprite.update();
	}

	public void draw(Graphics g) {
		if (bIgnore)
			return;
		mSprite.draw(g);
	}

	public void removeProperty() {
		parentEntity.parentScreen.drawables.remove(this);
		super.removeProperty();
	}

	public void setPosition(int x, int y) {
		mSprite.setPosition(x, y);
	}

	public void setPositionRounded(float x, float y) {
		mSprite.setPositionRounded(x, y);
	}

	public void setRotation(float angle) {
		mSprite.setRotation(angle);
	}

	public float getRotation() {
		return mSprite.getRotation();
	}

	public void resetCamera() {
		mSprite.camera = parentEntity.parentScreen.camera;
	}

	public void setIsCamerad(boolean iscam) {
		if (mSprite != null) {
			mSprite.bIsCameraed = iscam;
		}
	}

	public void flashSprite(Color color, float duration) {
		if (mSprite != null) {
			mSprite.flashColor(color, duration);
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) {
		try {
			out.writeBoolean(getSaveThisProperity());
			if (getSaveThisProperity()) {
				out.writeObject(mSprite);
				out.writeInt(iPriority);
				out.writeBoolean(bIgnore);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void readObject(java.io.ObjectInputStream in) {
		try {

			setSaveThisProperty(in.readBoolean());
			if (getSaveThisProperity()) {
				mSprite = (Sprite) in.readObject();
				iPriority = in.readInt();
				bIgnore = in.readBoolean();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(Drawable o) {
		if (o.iPriority < iPriority) {
			return -1;
		} else if (o.iPriority > iPriority) {
			return 1;
		}
		return 0;
	}

}
