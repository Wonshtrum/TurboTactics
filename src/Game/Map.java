package Game;

import java.util.List;

public class Map {
	
	private int width;
	private int height;
	private int floor;
	private Tile[][] tiles;
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
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
	
	public Map(int width, int height, int floor, List<Player> players) {
		super();
		this.width = width;
		this.height = height;
		this.floor = floor;
		this.tiles = new Tile[width][height];
		//Spawn players
		for (int x=0 ; x<width ; x++) {
			for (int y=0 ; y<height ; y++) {
				tiles[x][y] = new Wall(x, y);
			}			
		}
		for (int i=0 ; i < players.size() ; i++) {
			switch (i) {
				case 0:
					tiles[1][1] = players.get(0);
					break;
				case 1:
					tiles[0][1] = players.get(0);
					break;
				case 2:
					tiles[1][0] = players.get(0);
					break;
				case 3:
					tiles[0][0] = players.get(0);
					break;
				default:
					break;
			}
		}
		tiles[width-1][height-1] = new Exit(height,width);
	}
	
	public String toString() {
		String res = "[";
		for (int x=0 ; x<width ; x++) {
			res += "[";
			for (int y=0 ; y<height ; y++) {
				res += tiles[x][y]+", ";
			}
			res += "],\n";
		}
		res += "]";
		return res;
	}
}