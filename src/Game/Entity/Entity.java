package Game.Entity;

import java.util.ArrayList;

import Game.Items.Item;
import Game.Map.Air;
import Game.Map.Map;
import Game.Map.Tile;
import Game.Skills.Skill;
import Utils.Stats;

public abstract class Entity extends Tile {
	protected String id;		
	protected int[] stats;
	protected boolean alive;
	protected ArrayList<Item> inventory;
	protected Equipment equipment;
	protected ArrayList<Skill> skills;
	protected Map map;
	
	
	public Entity(String id, int hp, int mp, int pa, int hpMax, int mpMax, int paMax, int armor, int initiative, int level, int xp,
			int intel, int str, int dext, int gold, boolean alive, ArrayList<Item> inventory, Equipment equipment, ArrayList<Skill> skills, Map map) {
		super(0,0);
		this.id = id;
		/* hp, mp, pa, hpMax, mp, pa, hpMax, mpMax, paMax, armor, initiative, level, xp, intel, str, dext, gold */
		this.stats = new int[] {hp, mp, pa,hpMax, mpMax, paMax, armor, initiative, level, xp, intel, str, dext, gold};
		this.equipment = equipment;
		this.inventory = inventory;
		this.skills = skills;
		this.map = map;
	}
	
	public Entity(String id, int hpMax, int mpMax, int paMax, int armor, int initiative, int level, int xp, int intel, int str, int dext, int gold, Map map) {
		super(0,0);
		this.id = id;
		/* hp, mp, pa, hpMax, mp, pa, hpMax, mpMax, paMax, armor, initiative, level, xp, intel, str, dext, gold */
		this.stats = new int[] {hpMax, mpMax, paMax, hpMax, mpMax, paMax, armor, initiative, level, xp, intel, str, dext, gold};
		this.inventory = new ArrayList<Item>();
		this.equipment = new Equipment();
		this.skills = new ArrayList<Skill>();
		this.map = map;
	}

	public String getId() {
		return this.id;
	}

	public boolean isAlive() {
		return this.alive;
	}

	public int getStat(int key) {
		return this.stats[key];
	}

	public void setStat(int key, int value) {
		this.stats[key] = value;
	}

	public void setId(String newId) {
		id=newId;
	}
	public ArrayList<Item> getInventory() {
		return this.inventory;
	}
	
	public Equipment getEquipment() {
		return this.equipment;
	}

	public ArrayList<Skill> getSkills() {
		return this.skills;
	}
	
	public Map getMap() {
		return this.map;
	}
	
	public void die() {
		alive=false;
		this.map.place(this.posX,this.posY, new Air(0,0));
		this.map.sortedEntityOrder.remove(this);
	}
	
	public void gainXp(int xpGained) {		
		int xpToUp=(int) (4/5*(Math.pow(this.stats[Stats.level],3)+1));	
		this.stats[Stats.xp]+=xpGained;
		if (this.stats[Stats.xp]>= xpToUp) {
			this.stats[Stats.level]+=1;
			this.stats[Stats.xp]-=xpToUp;
			System.out.println("pop-up de gain de compétence");
		}
	}
	
	public void pickUp(Item item) {
		if (inventory.size()<10) {
			inventory.add(item);
		}
		else {
			System.out.println("inventaire deja plein");
		}
	}
	
	public void move (int x, int y, int pa) {
		this.stats[Stats.pa] -= pa;
		this.map.move(this, x, y);
	}
	
	@Override
	public String toString() {
		return this.id;
	}

	public String mapAttribute() {
		String[] names = {"hp", "mp", "pa", "hpMax", "mpMax", "paMax", "armor", "initiative", "level", "xp", "intel", "str", "dext", "gold"};
		int length = this.stats.length;
		String res = "#x#:"+posX+",#y#:"+posY+",";
		for (int i=0 ; i<length ; i++) {
			res += "#"+names[i]+"#:"+this.stats[i];
			if (i<length-1) {
				res += ",";
			}
		}
		return res;
	}
	
	public String fullData() {
		return "#"+this+"#:{"+this.mapAttribute()+"}";
	}
	
	
	public void beginTurn() {
		System.out.println("Starting Turn" + this.id);
		//todo appliquer les buffs
	}
	
	public void endTurn() {
		this.stats[Stats.pa]=this.stats[Stats.paMax];
	}
}