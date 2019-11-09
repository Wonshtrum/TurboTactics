package Game;

import java.util.ArrayList;

public abstract class Entity extends Tile {
	protected String id;
	protected int hp;
	protected int mp;
	protected int pa;
	protected int hpmax;
	protected int mpmax;
	protected int pamax;	
	protected boolean alive;
	protected int lvl;
	protected int xp;
	protected int intel;
	protected int str;
	protected int gold;
	protected ArrayList<Item> inventory;
	protected ArrayList<Skill> skills;
	
	public Entity() {
		super(0,0);
		alive=true;
		lvl=0;
		xp=0;
		hpmax=10;
		mpmax=0;
		pamax=5;
		hp=hpmax;
		mp=mpmax;
		pa=pamax;
		intel=5;
		str=5;	
		gold=0;
		inventory= new ArrayList<Item>();
		skills= new ArrayList<Skill>();
	}
	
	
	public Entity(String id, int hp, int mp, int pa, int hpmax, int mpmax, int pamax, boolean alive, int lvl, int xp,
			int intel, int str, int gold, ArrayList<Item> inventory, ArrayList<Skill> skills) {
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
	
	public void DealDamage(int dmg, Entity e) {
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
	
	public void Move (Map map, int x, int y) {
		if (map.getTiles()[x][y] instanceof Wall) return;
		//TODO
	}
	
	public String toString() {
		int[] values = {lvl, xp, hpmax, mpmax, pamax, hp, mp, pa, intel, str, gold};
		String[] names = {"lvl", "xp", "hpmax", "mpmax", "pamax", "hp", "mp", "pa", "intel", "str", "gold"};
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
}