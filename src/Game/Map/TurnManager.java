package Game.Map;

import java.util.ArrayList;

import Game.Game;
import Game.Entity.Entity;
import Game.Entity.Mob;
import Utils.Stats;

public class TurnManager {
	public ArrayList<Entity> entities;
	private Entity entityTurn;
	private Game game;
	
	public TurnManager(ArrayList<Entity> entityOrder, Game game) {
		this.entities=entityOrder;
		this.sort();
		this.entityTurn=entities.get(0);
		this.game = game;
	}
	
	public void sort() {
		entities.sort((e1, e2)->e1.getStat(Stats.initiative)-e2.getStat(Stats.initiative));		
	}
	
	public int size() {
		return this.entities.size();
	}
	
	public void add(Entity entity) {
		this.entities.add(entity);
		this.sort();
	}
	
	public void remove(Entity entity) {
		if (this.entityTurn==entity) {
			next();
		}
		this.entities.remove(entity);
	}
	
	public void next() {
		int index = this.entities.indexOf(this.entityTurn);
		this.entityTurn.endTurn();
		if (index==this.size()-1) {
			this.entityTurn=this.entities.get(0);
		} else {
			this.entityTurn=this.entities.get(index+1);
		}
		this.entityTurn.beginTurn();
		if (this.entityTurn instanceof Mob) {
			((Mob)this.entityTurn).play();
			this.next();
		}
	}
	
	public boolean isTurn(Entity entity) {
		return entity.equals(entityTurn);
	}

}
