package turbo.game.entity;

import turbo.game.map.Map;
import turbo.util.Couple;
import turbo.util.Stats;
import turbo.util.Tools;

import java.util.List;

public class Mob extends Entity {
    private static int nextId = 0;

    public Mob(int hpMax, int mpMax, int paMax, int armor, int initiative, int level, int xp, int intel, int str, int dext, int gold, Map map) {
        super("M" + nextId, hpMax, mpMax, paMax, armor, initiative, level, xp, intel, str, dext, gold, map);
        nextId++;
    }

    public Couple<String, String> play() {
        Entity player = this.map.sortedEntityOrder.entities.stream().filter(e -> e instanceof Player).findAny().orElse(null);
        List<Couple<Integer, Integer>> path = this.map.path(this.posX, this.posY, player.posX, player.posY, 1000, false);
        if (path != null && path.size() > 0) {
            int length = path.size();
            if (length > this.stats[Stats.pa]) {
                path = path.subList(length - this.stats[Stats.pa], length);
            }
            this.move(path.get(0).x, path.get(0).y, path.size());
            return new Couple<String, String>("move", "[#" + this + "#," + this.stats[Stats.paMax] + "," + Tools.pathToString(path) + "]");
        } else {
            return new Couple<String, String>("msg", "#Come over here!#");
        }
    }

    public static Mob generate(int floor) {
        return null;
    }
}
