package turbo.game.skill;

import turbo.game.entity.Entity;
import turbo.game.map.Tile;

public abstract class Skill {

    public int range;
    public int cooldown;
    public int PACost;
    public int manaCost;
    public boolean affectedByWall;
    public boolean diagonal;

    public String description;


    public void use(Entity user, Tile target) {

    }

}