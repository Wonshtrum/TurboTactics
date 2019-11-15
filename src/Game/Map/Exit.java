package Game.Map;

public class Exit extends Tile {
	public Exit(int posx,int posy) {
		super(posx,posy);
	}

	@Override
	public String toString() {
		return "-1";
	}
}
