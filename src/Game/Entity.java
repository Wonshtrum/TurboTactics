package Game;

import java.util.ArrayList;

public abstract class Entity extends Tile {
	private String id;
	private int hp;
	private int mp;
	private int pa;
	private int hpmax;
	private int mpmax;
	private int pamax;	
	private boolean alive;
	private int lvl;
	private int xp;
	private int intel;
	private int str;
	private int gold;
	private ArrayList<Item> inventory;
	private ArrayList<Skill> skills;
	
	public Entity() {
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

}
