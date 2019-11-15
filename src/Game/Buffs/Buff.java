package Game.Buffs;

import java.util.ArrayList;

import Game.Entity.Entity;
import Game.Map.Tile;
import Game.Skills.Effect;

public class Buff implements Effect{

	public ArrayList<Effect> effects;
	public int duration;
	
	public Buff(ArrayList<Effect> effects,int duration) {
		this.effects=effects;
		this.duration=duration;
	}

	@Override
	public void use(Entity user, Tile target) {	
		if (target instanceof Entity) {
			((Entity)target).getBuff(this);
		}
	}
	
	public void apply(Entity target) {
		for(Effect effect : effects) {
			effect.use(target, target);
		}
		duration--;
		if (duration==0) {
			target.removeBuff(this);
		}
	}

}
