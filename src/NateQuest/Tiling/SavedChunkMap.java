package NateQuest.Tiling;

import java.io.IOException;
import java.io.Serializable;

import GameClasses.CUtil;
import NateQuest.Debugger;
import NateQuest.GameScreen;

public class SavedChunkMap implements Serializable {

	public int overlay = 0;
	public int width;
	public int height;
	public TileMapCompressedData[][] mapData;

	public SavedChunkMap(ChunkMap map) {
		overlay = map.getOverlay();
		width = map.getWidth();
		height = map.getHeight();
		mapData = new TileMapCompressedData[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				mapData[i][j] = new TileMapCompressedData(map.getChunks()[i][j]);

	}

	private void writeObject(java.io.ObjectOutputStream out) {
		try {
			out.writeInt(width);
			out.writeInt(height);
			for (int i = 0; i < mapData.length; i++) {
				for (int j = 0; j < mapData[0].length; j++) {
					out.writeObject(mapData[i][j]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readObject(java.io.ObjectInputStream in) {
		try {
			width = in.readInt();
			height = in.readInt();
			mapData = new TileMapCompressedData[width][height];
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					mapData[i][j] = (TileMapCompressedData) in.readObject();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void craftAndAttachChunkMap(GameScreen parentScreen) {

	}
}
