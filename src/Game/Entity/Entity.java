package Game.Entity;

import java.util.ArrayList;

import Game.Buffs.Buff;
import Game.Items.Item;
import Game.Map.Air;
import Game.Map.Map;
import Game.Map.Tile;
import Game.Skills.Skill;

public abstract class Entity extends Tile {
	protected String id;
	protected int hp;
	protected int mp;
	protected int pa;
	protected int hpmax;
	protected int mpmax;
	protected int pamax;
	public int initiative;
	protected boolean alive;
	protected int lvl;
	protected int xp;
	protected int intel;
	protected int str;
	protected int gold;
	protected ArrayList<Item> inventory;
	protected ArrayList<Skill> skills;
	protected ArrayList<Buff> buffs;
	protected Map map;
	
	public Entity(Map map) {
		super(0,0);
		this.alive=true;
		this.lvl=0;
		this.xp=0;
		this.hpmax=10;
		this.mpmax=0;
		this.pamax=5;
		this.hp=hpmax;
		this.mp=mpmax;
		this.pa=pamax;
		this.initiative=0;
		this.intel=5;
		this.str=5;	
		this.gold=0;
		this.inventory= new ArrayList<Item>();
		this.skills= new ArrayList<Skill>();
		this.buffs= new ArrayList<Buff>();
		this.map= map;
	}
	
	
	public Entity(String id, int hp, int mp, int pa, int hpmax, int mpmax, int pamax, boolean alive, int lvl, int xp,
			int intel, int str, int gold, ArrayList<Item> inventory, ArrayList<Skill> skills,ArrayList<Buff> buffs, Map map) {
		super(0,0);
		this.id = id;
		this.hp = hp;
		this.mp = mp;
		this.pa = pa;
		this.hpmax = hpmax;
		this.mpmax = mpmax;
		this.pamax = pamax;
		this.alive = alive;
		this.lvl = lvl;
		this.xp = xp;
		this.intel = intel;
		this.str = str;
		this.gold = gold;
		this.inventory = inventory;
		this.skills = skills;
		this.buffs = buffs;
		this.map = map;
	}

	public String getId() {
		return id;
	}

	public boolean isAlive() {
		return alive;
	}

	public int getLvl() {
		return lvl;
	}

	public int getXp() {
		return xp;
	}

	public int getHpmax() {
		return hpmax;
	}

	public int getMpmax() {
		return mpmax;
	}

	public int getPamax() {
		return pamax;
	}

	public int getHp() {
		return hp;
	}

	public int getMp() {
		return mp;
	}

	public int getPa() {
		return pa;
	}

	public int getIntel() {
		return intel;
	}

	public int getStr() {
		return str;
	}

	public int getGold() {
		return gold;
	}

	public void setId(String newId) {
		id=newId;
	}
	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public ArrayList<Skill> getSkills() {
		return skills;
	}
	
	public void die() {
		alive=false;
		this.map.place(this.posX,this.posY, new Air(0,0));
		this.map.sortedEntityOrder.remove(this);
	}
	
	public void gainXp(int sum) {		
		int xpToUp=(int) (4/5*(Math.pow(lvl,3)+1));	
		xp+=sum;
		if (xp>= xpToUp) {
			lvl+=1;
			xp=xp-xpToUp;
			System.out.println("pop-up de gain de compétence");
		}
	}
	
	public void takeDamage(int dmg) {
		hp-=dmg;
		if (hp<=0) {
			die();
		}
	}
	
	public void dealDamage(int dmg, Entity e) {
		e.takeDamage(dmg);
		
	}
	
	public void getHealed(int heal) {
		if (hp+heal>hpmax) {
			hp=hpmax;
		} else {
			hp=hp+heal;
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
		this.pa -= pa;
		this.map.move(this, x, y);
	}
	
	public String toString() {
		int[] values = {posX, posY, lvl, xp, hpmax, mpmax, pamax, hp, mp, pa, intel, str, gold};
		String[] names = {"x", "y", "lvl", "xp", "hpmax", "mpmax", "pamax", "hp", "mp", "pa", "intel", "str", "gold"};
		int length = values.length;
		String res = "";
		for (int i=0 ; i<length ; i++) {
			res += "#"+names[i]+"#:"+values[i];
			if (i<length-1) {
				res += ",";
			}
		}
		return res;
	}
	
	public void getBuff(Buff buff) {
		this.buffs.add(buff);
	}
	
	public void removeBuff(Buff buff) {
		this.buffs.remove(buff);
	}
	
	public void beginTurn() {
		System.out.println("Début du tour " + this.id);
		//todo appliquer les buffs
	}
	
	public void endTurn() {
		this.pa=this.pamax;
	}
	
	
}