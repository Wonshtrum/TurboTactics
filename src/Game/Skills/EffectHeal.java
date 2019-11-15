package Game.Skills;

import Game.Entity.Entity;
import Game.Map.Tile;

public class EffectHeal implements Effect{
	public int heal;
	public float CoefMagic;
	
	@Override
	public void use(Entity user, Tile target) {
		if(target instanceof Entity) {
			((Entity)target).getHealed((Math.round(this.heal + ((Entity)user).getIntel()*this.CoefMagic)));
		}
	}

	public EffectHeal(int heal, float coefMagic) {
		this.heal = heal;
		CoefMagic = coefMagic;
	}
	
}

