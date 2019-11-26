package Game.Map;

import java.util.ArrayList;
import Game.Entity.Entity;
import Game.Entity.Mob;
import Utils.Stats;

public class SortedList {
	public ArrayList<Entity> sortedEntityOrder;
	private Entity entityTurn;
	
	public SortedList(ArrayList<Entity> entityOrder) {
		this.sortedEntityOrder=entityOrder;
		this.sort();
		this.entityTurn=sortedEntityOrder.get(0);
	}
	
	public void sort() {
		sortedEntityOrder.sort((e1, e2)->e1.getStat(Stats.initiative)-e2.getStat(Stats.initiative));		
	}
	
	public int size() {
		return this.sortedEntityOrder.size();
	}
	
	public void add(Entity entity) {
		this.sortedEntityOrder.add(entity);
		this.sort();
	}
	
	public void remove(Entity entity) {
		if (this.entityTurn==entity) {
			next();
		}
		this.sortedEntityOrder.remove(entity);
	}
	
	public void next() {
		int index = this.sortedEntityOrder.indexOf(this.entityTurn);
		this.entityTurn.endTurn();
		if (index==this.size()-1) {
			this.entityTurn=this.sortedEntityOrder.get(0);
		} else {
			this.entityTurn=this.sortedEntityOrder.get(index+1);
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
