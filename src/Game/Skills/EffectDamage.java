package Game.Skills;

import Game.Entity.Entity;
import Game.Map.Tile;

public class EffectDamage implements Effect{
	
	public int damage;
	public float CoefStrengh;
	public float CoefMagic;
	
	@Override
	public void use(Entity user, Tile target) {
		if(target instanceof Entity) {
			((Entity)target).takeDamage(Math.round(this.damage + ((Entity)user).getIntel()*this.CoefMagic+((Entity)user).getStr()*this.CoefStrengh));
		}
	}

	public EffectDamage(int damage, float coefStrengh, float coefMagic) {
		this.damage = damage;
		CoefStrengh = coefStrengh;
		CoefMagic = coefMagic;
	}
	
}
