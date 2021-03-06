package turbo.game.map;

import turbo.game.Game;
import turbo.game.entity.Entity;
import turbo.game.entity.Goblin;
import turbo.game.entity.Mob;
import turbo.game.entity.Player;
import turbo.util.Couple;
import turbo.util.Tools;
import turbo.util.Triplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Map {
    private int width;
    private int height;
    private int floor;
    private Tile[][] tiles;
    public TurnManager sortedEntityOrder;

    public Map(int width, int height, int floor, List<Player> players, Game game) {
        super();
        this.width = width;
        this.height = height;
        this.floor = floor;
        this.tiles = new Tile[width][height];

        ArrayList<Entity> entityOrder = new ArrayList<Entity>();
        //Spawn players
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (Tools.randInt(0, 5) == 0) {
                    tiles[x][y] = new Wall(x, y);
                } else {
                    tiles[x][y] = new Air(x, y);
                }
            }
        }
        for (int i = 0; i < players.size(); i++) {
            switch (i) {
                case 0:
                    place(1, 1, players.get(0));
                    entityOrder.add(players.get(0));
                    break;
                case 1:
                    place(0, 1, players.get(1));
                    entityOrder.add(players.get(1));
                    break;
                case 2:
                    place(1, 0, players.get(2));
                    entityOrder.add(players.get(2));
                    break;
                case 3:
                    place(0, 0, players.get(3));
                    entityOrder.add(players.get(3));
                    break;
                default:
                    break;
            }
        }
        Mob monstreTest = Goblin.generate(this);
        place(width - 2, height - 2, monstreTest);
        entityOrder.add(monstreTest);
        place(width - 1, height - 1, new Exit(0, 0));
        this.sortedEntityOrder = new TurnManager(entityOrder, game);

    }

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

    public void move(Entity target, int x, int y) {
        this.place(target.posX, target.posY, new Air(0, 0));
        this.place(x, y, target);
    }


    public boolean inBound(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public Tile checkTile(int x, int y) {
        return tiles[x][y];
    }

    public Couple<HashMap<Triplet<Integer, Integer, Integer>, Triplet<Integer, Integer, Integer>>, Triplet<Integer, Integer, Integer>> range(int x, int y, int gx, int gy, int pamax, boolean goalEmpty) {
        HashSet<Couple<Integer, Integer>> visited = new HashSet<>();
        HashSet<Triplet<Integer, Integer, Integer>> parents = new HashSet<>();
        parents.add(new Triplet<Integer, Integer, Integer>(x, y, 0));
        HashSet<Triplet<Integer, Integer, Integer>> newParents = new HashSet<>();
        HashMap<Triplet<Integer, Integer, Integer>, Triplet<Integer, Integer, Integer>> tree = new HashMap<>();
        for (int pa = 0; pa < pamax && parents.size() > 0; pa++) {
            newParents.clear();
            for (Triplet<Integer, Integer, Integer> node : parents) {
                x = node.x;
                y = node.y;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        int a = x + i;
                        int b = y + j;
                        if (i != j && -i != j && inBound(x + i, y + j) && !visited.stream().anyMatch(t -> t.x == a && t.y == b)) {
                            boolean empty = tiles[x + i][y + j] instanceof Air;
                            Triplet<Integer, Integer, Integer> newNode = node;
                            if (empty) {
                                visited.add(new Couple<Integer, Integer>(x + i, y + j));
                                newNode = new Triplet<Integer, Integer, Integer>(x + i, y + j, pa + 1);
                                newParents.add(newNode);
                                tree.put(newNode, node);
                            }
                            if (x + i == gx && y + j == gy && (empty || !goalEmpty)) {
                                return new Couple<HashMap<Triplet<Integer, Integer, Integer>, Triplet<Integer, Integer, Integer>>, Triplet<Integer, Integer, Integer>>(tree, newNode);
                            }
                        }
                    }
                }
            }
            parents.addAll(newParents);
        }
        return null;
    }

    private boolean unfoldPos(int x, int y, int dx, double dy, boolean reverse, boolean flipx, boolean flipy) {
        int posX = dx;
        int posY = (int) (Math.floor(dy));
        if (reverse) {
            int tmp = posX;
            posX = posY;
            posY = tmp;
        }
        if (flipx) {
            posY = -posY;
        }
        if (flipy) {
            posX = -posX;
        }
        return (!(this.checkTile(posX, posY) instanceof Air));
    }

    private boolean straightLine(int x, int y, int dx, int dy, boolean reverse, boolean flipX, boolean flipY) {
        double coef = dy / dx;
        double posY = 0.5 + 0.5 * coef;
        double totalPosY = posY;
        for (int posX = 1; posX < dx; posX++) {
            if (unfoldPos(x, y, posX, totalPosY, reverse, flipX, flipY)) {
                return false;
            }
            posY += coef;
            totalPosY += coef;
            if (posY >= 1) {
                if (posY == 1) {
                    if (unfoldPos(x, y, posX, totalPosY, reverse, flipX, flipY) && (unfoldPos(x, y, posX + 1, totalPosY - 1, reverse, flipX, flipY))) {
                        return false;
                    }
                } else {
                    if (unfoldPos(x, y, posX, totalPosY, reverse, flipX, flipY)) {
                        return false;
                    }
                }
                posY--;
            }
        }
        return true;
    }


    public boolean straightLine(int x, int y, int gx, int gy, int range) {
        if (Math.abs(gx - x) + Math.abs(gy - y) > range) {
            boolean flipX = false;
            boolean flipY = false;
            boolean reverse = false;
            int dx = gx - x;
            int dy = gy - y;
            if (dx < 0) {
                flipX = true;
                dx *= -1;
            }
            if (dy < 0) {
                flipY = true;
                dy *= -1;
            }
            if (dy > dx) {
                reverse = true;
                int tmp = dx;
                dx = dy;
                dy = tmp;
            }
            return straightLine(x, y, dx, dy, reverse, flipX, flipY);
        }
        return false;
    }

    public ArrayList<Couple<Integer, Integer>> path(int x, int y, int gx, int gy, int pamax, boolean goalEmpty) {
        Couple<HashMap<Triplet<Integer, Integer, Integer>, Triplet<Integer, Integer, Integer>>, Triplet<Integer, Integer, Integer>> res = this.range(x, y, gx, gy, pamax, goalEmpty);
        if (res != null) {
            HashMap<Triplet<Integer, Integer, Integer>, Triplet<Integer, Integer, Integer>> tree = res.x;
            Triplet<Integer, Integer, Integer> pos = res.y;
            return Tools.tracePath(tree, pos);
        } else {
            return null;
        }
    }

    public String toString() {
        String res = "{#w#:" + width + ",#h#:" + height + ",#map#:[";
        for (int x = 0; x < this.width; x++) {
            res += "[";
            for (int y = 0; y < height; y++) {
                if (tiles[x][y] instanceof Entity) {
                    res += "#" + tiles[x][y] + "#";
                } else {
                    res += tiles[x][y];
                }
                if (y < height - 1) {
                    res += ",";
                }
            }
            res += "]";
            if (x < width - 1) {
                res += ",";
            }
        }
        res += "]}";
        return res;
    }
}