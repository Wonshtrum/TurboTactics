package Game.Entity;

import Game.Map.Map;

public class Mob extends Entity {
	static int nextId = 0;
	
	public Mob(int hpMax, int mpMax, int paMax, int armor, int initiative, int level, int xp, int intel, int str, int dext, int gold, Map map) {
		super(""+nextId,hpMax, mpMax, paMax, armor, initiative,level, xp, intel, str, dext, gold, map);
		nextId++;
	}
	
	@Override
	public String toString() {
		return "M";
	}
	
	public void play() {
		System.out.println("Come over here!");
	}
	
	public static Mob generate(int floor) {
		return null;
	}
}
