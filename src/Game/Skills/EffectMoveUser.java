package Game.Skills;

import Game.Entity.Entity;
import Game.Map.Air;
import Game.Map.Tile;

public class EffectMoveUser implements Effect{
	
	public void use(Entity user, Tile target) {
		if(target instanceof Air) {
			user.move(target.posX, target.posY, 0);
		}
	}

}
