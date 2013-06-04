package NateQuest.Tiling;

import java.io.IOException;
import java.io.Serializable;

import NateQuest.Debugger;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.Tiling.Tiles.Tile.BasicType;
import NateQuest.Tiling.Tiles.WallTile.WallType;
import NateQuest.Tiling.Tiles.WallTile;

public class TileCompressionData implements Serializable {

	/**
	 * 
	 */
	public final static byte BASIC_TYPES = 50;
	private static final long serialVersionUID = -3485913583341817722L;
	boolean isNulled;
	public boolean isBlended;
	byte tileByte;
	byte rotateByte;

	// used when loaded
	public TileCompressionData(boolean isNulled, boolean isBlended,
			byte tileByte, byte rotateByte) {
		this.isNulled = isNulled;
		this.isBlended = isBlended;
		this.tileByte = tileByte;
		this.rotateByte = rotateByte;
	}

	// Used when saving
	public TileCompressionData(Tile t) {
		if (t == null) {
			isNulled = true;
		} else {
			isNulled = false;
			isBlended = t.isBlended;
			tileByte = (byte) t.type.ordinal();
			rotateByte = byteFromRotation(t.tileSprite.getRotation());
		}
	}

	// I wish I knew why it needs to do this boolean, its not sending WallTiles
	// here, its casting them to tiles.
	public TileCompressionData(WallTile t, boolean isWall) {
		if (t == null) {
			isNulled = true;
		} else {
			isNulled = false;
			isBlended = t.isBlended;
			tileByte = (byte) (t.wallType.ordinal() + BASIC_TYPES);
			rotateByte = byteFromRotation(t.tileSprite.getRotation());
		}
	}

	public float getRotation() {
		return rotationFromByte(rotateByte);
	}

	private float rotationFromByte(byte theByte) {
		float rotation = theByte * 90f;
		return rotation;
	}

	private byte byteFromRotation(float rotation) {
		byte b = (byte) (rotation / 90f);
		return b;
	}

	// For the byte, the first n is the number of types of basic types,
	// But if its more than that, then it must be of a different type (wall)
	public BasicType getType() {
		if (tileByte < Tile.BasicType.values().length) {
			return BasicType.values()[(int) tileByte];
		} else {
			return BasicType.values()[(int) tileByte]; // TODO
		}
	}

	public WallType getWallType() {
		return WallType.values()[(int) tileByte - BASIC_TYPES];
	}

	private void writeObject(java.io.ObjectOutputStream out) {
		try {
			out.writeBoolean(isNulled);
			if (!isNulled) {
				out.writeBoolean(isBlended);
				out.writeByte(tileByte);
				out.writeByte(rotateByte);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readObject(java.io.ObjectInputStream in) {
		try {
			isNulled = in.readBoolean();
			if (!isNulled) {
				isBlended = in.readBoolean();
				tileByte = in.readByte();
				rotateByte = in.readByte();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
