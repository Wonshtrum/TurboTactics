package Game.Map;

public class Wall extends Tile{
	public Wall(int posx, int posy) {
		super(posx,posy);
	}
	
	@Override
	public String toString() {
		return "1";
	}
}
