package Game;

public abstract class GameEntity {
	public int posX;
	public int posY;
	
	
	public GameEntity(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	
	public abstract String toString();
	
}
