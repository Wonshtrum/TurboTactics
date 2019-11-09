package Game;

import java.util.List;

import Utils.Tools;

public class Map {
	private int width;
	private int height;
	private int floor;
	private Tile[][] tiles;
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getFloor() {
		return floor;
	}
	
	public void setFloor(int floor) {
		this.floor = floor;
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}
	
	public void place(int x, int y, Tile tile) {
		if (tile != null) {
			tile.posX = x;
			tile.posY = y;
		}
		tiles[x][y] = tile;
	}
	
	public Map(int width, int height, int floor, List<Player> players) {
		super();
		this.width = width;
		this.height = height;
		this.floor = floor;
		this.tiles = new Tile[width][height];
		//Spawn players
		for (int x=0 ; x<width ; x++) {
			for (int y=0 ; y<height ; y++) {
				if (Tools.randInt(0, 5) == 0) {
					tiles[x][y] = new Wall(x, y);
				}
			}
		}
		for (int i=0 ; i < players.size() ; i++) {
			switch (i) {
				case 0:
					place(1, 1, players.get(0));
					break;
				case 1:
					place(0, 1, players.get(1));
					break;
				case 2:
					place(1, 0, players.get(2));
					break;
				case 3:
					place(0, 0, players.get(3));
					break;
				default:
					break;
			}
		}
		tiles[width-1][height-1] = new Exit(height,width);
	}
	
	public String toString() {
		String res = "{#w#:"+width+",#h#:"+height+",#map#:[";
		for (int x=0 ; x<width ; x++) {
			res += "[";
			for (int y=0 ; y<height ; y++) {
				if (tiles[x][y] != null) {
					if (tiles[x][y] instanceof Entity) {
						res += "#"+tiles[x][y]+"#";
					} else {
						res += tiles[x][y];
					}
				} else {
					res += 0;
				}
				if (y<height-1) {
					res+=",";
				}
			}
			res += "]";
			if (x<width-1) {
				res+=",";
			}
		}
		res += "]}";
		return res;
	}
}