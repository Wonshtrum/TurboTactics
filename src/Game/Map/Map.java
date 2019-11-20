package Game.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

import Game.Entity.Entity;
import Game.Entity.Mob;
import Game.Entity.Player;
import Game.Map.Exit;
import Utils.Couple;
import Utils.Tools;
import Utils.Triplet;

public class Map {
	private int width;
	private int height;
	private int floor;
	private Tile[][] tiles;
	public SortedList sortedEntityOrder;
	
	public Map(int width, int height, int floor, List<Player> players) {
		super();
		this.width = width;
		this.height = height;
		this.floor = floor;
		this.tiles = new Tile[width][height];

		ArrayList<Entity> entityOrder = new ArrayList<Entity>();
		//Spawn players
		for (int x=0 ; x<width ; x++) {
			for (int y=0 ; y<height ; y++) {
				if (Tools.randInt(0, 5) == 0) {
					tiles[x][y] = new Wall(x, y);
				}
				else {
					tiles[x][y] = new Air(x,y);
				}
			}
		}
		for (int i=0 ; i < players.size() ; i++) {
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
		Mob monstreTest = new Mob(this);
		place(width-2, height-2, monstreTest);
		entityOrder.add(monstreTest);
		place(width-1, height-1, new Exit(0, 0));
		this.sortedEntityOrder = new SortedList(entityOrder);
		
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
		return x>=0 && y>=0 && x<width && y<height;
	}
	
	public Tile checkTile(int x, int y) {
		return tiles[x][y];
	}
	
	public HashMap<Triplet<Integer, Integer, Integer>, Triplet<Integer, Integer, Integer>> paths(int x, int y) {
		Entity entity = (Entity) tiles[x][y];
		HashSet<Couple<Integer, Integer>> visited = new HashSet<>();
		HashSet<Triplet<Integer, Integer, Integer>> parents = new HashSet<>();
		parents.add(new Triplet<Integer, Integer, Integer>(x, y, 0));
		HashSet<Triplet<Integer, Integer, Integer>> newParents = new HashSet<>();
		HashMap<Triplet<Integer, Integer, Integer>, Triplet<Integer, Integer, Integer>> tree = new HashMap<>();
		for (int pa=0 ; pa<entity.getPa() && parents.size()>0 ; pa++) {
			newParents.clear();
			for (Triplet<Integer, Integer, Integer> node : parents) {
				x = node.x;
				y = node.y;
				for (int i=-1 ; i<2 ; i++) {
					for (int j=-1 ; j<2 ; j++) {
						int a = x+i;
						int b = y+j;
						if (i!=j && -i!=j && inBound(x+i, y+j) && tiles[x+i][y+j] instanceof Air && !visited.stream().anyMatch(t -> t.x == a && t.y == b)) {
							visited.add(new Couple<Integer, Integer>(x+i, y+j));
							Triplet<Integer, Integer, Integer> newNode = new Triplet<Integer, Integer, Integer>(x+i, y+j, pa+1);
							newParents.add(newNode);
							tree.put(newNode, node);
						}
					}
				}
			}
			parents.addAll(newParents);
		}
		return tree;
	}
		
	public String toString() {
		String res = "{#w#:"+width+",#h#:"+height+",#map#:[";
		for (int x=0 ; x<this.width ; x++) {
			res += "[";
			for (int y=0 ; y<height ; y++) {
				if (tiles[x][y] instanceof Entity) {
					res += "#"+tiles[x][y]+"#";
				} else {
					res += tiles[x][y];				
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