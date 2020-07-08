package turbo.game.map;

public abstract class Tile {
    public int posX;
    public int posY;

    public Tile(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    abstract public String toString();
}
