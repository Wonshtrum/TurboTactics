package Game.Map;

import java.util.ArrayList;

import Game.Game;
import Game.Entity.Entity;
import Game.Entity.Mob;
import Utils.Couple;

public class TurnManager {
	public ArrayList<Entity> entities;
	private Entity entityTurn;
	private Game game;
	
	public TurnManager(ArrayList<Entity> entities, Game game) {
		this.game = game;
		this.entities=entities;
		this.sort();
		this.entityTurn=entities.get(0);
	}
	
	public void sort() {
		entities.sort((e1, e2)->e1.initiative-e2.initiative);		
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
			this.next();
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
		this.game.broadcast("Turn of "+this.entityTurn);
		this.entityTurn.beginTurn();
		if (this.entityTurn instanceof Mob) {
			Couple<String, String> res = ((Mob)this.entityTurn).play();
			game.broadcast(res.x, res.y);
			this.next();
		}
		//TODO delete this!
		if (this.entityTurn.getXp() > 0) {
			this.next();
		}
	}
	
	public boolean isTurn(Entity entity) {
		return entity.equals(entityTurn);
	}

}
