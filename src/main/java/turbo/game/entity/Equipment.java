package turbo.game.entity;

import turbo.game.item.Armor;
import turbo.game.item.Helmet;
import turbo.game.item.Weapon;

public class Equipment {
    public Helmet helmet;
    public Armor armor;
    public Weapon Weapon;

    public Equipment() {
        this.helmet = null;
        this.armor = null;
        this.Weapon = null;
    }

    public Equipment(Helmet helmet, Armor armor, Weapon Weapon) {
        super();
        this.helmet = helmet;
        this.armor = armor;
        this.Weapon = Weapon;
    }


}
