package Game.Skills;

import Game.Entity.Entity;
import Game.Map.*;

public interface Effect {
	
	public void use(Entity user, Tile target);
}
