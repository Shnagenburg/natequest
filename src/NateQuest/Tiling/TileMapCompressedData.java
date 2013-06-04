package NateQuest.Tiling;

import java.io.IOException;
import java.io.Serializable;

import NateQuest.Tiling.Tiles.Tile;
import NateQuest.Tiling.Tiles.WallTile;

public class TileMapCompressedData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6410258568670133191L;
	TileCompressionData[][] tileCompressedMap;
	boolean isNulled = true;

	public TileMapCompressedData(TileMap map) {
		tileCompressedMap = new TileCompressionData[map.tiles.length][map.tiles[0].length];
		for (int i = 0; i < tileCompressedMap.length; i++) {
			for (int j = 0; j < tileCompressedMap[0].length; j++) {

				if (map.tiles[i][j] != null
						&& map.tiles[i][j].getClass() == WallTile.class)
					tileCompressedMap[i][j] = new TileCompressionData(
							(WallTile) map.tiles[i][j], true);
				else
					tileCompressedMap[i][j] = new TileCompressionData(
							map.tiles[i][j]);

				if (!tileCompressedMap[i][j].isNulled) { // We check for the
															// first non-null
															// tile, thus
															// meaning we
					isNulled = false; // need to save the chunk
				}
			}
		}
	}

	public Tile[][] getTileMap(TileMap parent) {
		Tile[][] tiles = new Tile[tileCompressedMap.length][tileCompressedMap[0].length];
		parent.tiles = tiles;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j] = makeTileFromData(parent, i, j);
			}
		}
		return tiles;
	}

	public Tile makeTileFromData(TileMap parent, int i, int j) {
		if (tileCompressedMap[i][j] == null || tileCompressedMap[i][j].isNulled) {
			return null;
		} else if (tileCompressedMap[i][j].tileByte < TileCompressionData.BASIC_TYPES) {
			return new Tile(parent, null, i, j, tileCompressedMap[i][j]);
		} else {
			return new WallTile(parent, null, i, j, tileCompressedMap[i][j]);
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) {
		try {
			// out.writeObject(tileCompressedMap);

			out.writeInt(tileCompressedMap.length);
			out.writeInt(tileCompressedMap[0].length);
			out.writeBoolean(isNulled);
			if (!isNulled) {
				for (int i = 0; i < tileCompressedMap.length; i++) {
					for (int j = 0; j < tileCompressedMap[0].length; j++) {
						TileCompressionData data = tileCompressedMap[i][j];
						out.writeBoolean(data.isNulled);
						if (!data.isNulled) {
							out.writeBoolean(data.isBlended);
							out.writeByte(data.tileByte);
							out.writeByte(data.rotateByte);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readObject(java.io.ObjectInputStream in) {
		try {
			// tileCompressedMap = (TileCompressionData[][]) in.readObject();
			int width = in.readInt();
			int height = in.readInt();
			isNulled = in.readBoolean();
			tileCompressedMap = new TileCompressionData[width][height];
			if (!isNulled) {
				for (int i = 0; i < tileCompressedMap.length; i++) {
					for (int j = 0; j < tileCompressedMap[0].length; j++) {
						boolean isNulled = in.readBoolean();
						boolean isBlended = false;
						byte tileByte = (byte) 255;
						byte rotateByte = 0;
						if (!isNulled) {
							isBlended = in.readBoolean();
							tileByte = in.readByte();
							rotateByte = in.readByte();
						}
						TileCompressionData data = new TileCompressionData(
								isNulled, isBlended, tileByte, rotateByte);
						tileCompressedMap[i][j] = data;

					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
