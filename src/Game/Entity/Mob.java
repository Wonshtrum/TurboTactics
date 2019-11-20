package Game.Entity;

import Game.Map.Map;

public class Mob extends Entity {
	 public Mob (Map map) {
		 super(map);
	 }

	@Override
	public String toString() {
		return "M";
	}
	
	public void play() {
		System.out.println("Come over here!");
	}
}
