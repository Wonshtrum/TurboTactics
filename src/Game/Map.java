package Game;


public class Map {
	private int width;
	private int height;
	private int floor;
	private GameEntity[][] tiles;
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
	public GameEntity[][] getTiles() {
		return tiles;
	}
	public void setTiles(GameEntity[][] tiles) {
		this.tiles = tiles;
	}
	public Map(int width, int height, int floor, Player[] players) {
		super();
		this.width = width;
		this.height = height;
		this.floor = floor;
		tiles[height][width] = new Exit(height,width);
		
		for (int i = 0; i < players.length; i++) {
			if (i<3) {
				tiles[0][i] = players[i];
			}
			if (i==3) {
				tiles[1][0] = players[i];
			}
			
		}
		
		
	}
	

}
