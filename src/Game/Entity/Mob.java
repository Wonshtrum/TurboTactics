package Game.Entity;

import java.util.List;

import Game.Map.Map;
import Utils.Couple;
import Utils.Tools;

public class Mob extends Entity {
	static int mobId = 0;
	
	public Mob (Map map) {
		 super(map);
		 this.id = "M"+mobId;
		 pa = pamax = 2;
	}
	
	public Couple<String, String> play() {
		Entity player = this.map.sortedEntityOrder.entities.stream().filter(e -> e instanceof Player).findAny().orElse(null);
		List<Couple<Integer, Integer>> path = this.map.path(this.posX, this.posY, player.posX, player.posY, 1000, false);
		if (path != null && path.size() > 0) {
			int length = path.size();
			if (length > this.pa) {
				path = path.subList(length-this.pa, length);
			}
			this.move(path.get(0).x, path.get(0).y, path.size());
			return new Couple<String, String>("move", "[#"+this+"#,0,"+Tools.pathToString(path)+"]");
		} else {
			return new Couple<String, String>("msg", "#Come over here!#");
		}
	}
}
