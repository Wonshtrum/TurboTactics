package turbo.game.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeaponGenerator {

    static final List<Weapon> baseWeapon = new ArrayList<Weapon>() {
        private static final long serialVersionUID = -5980386674576906791L;

        {
            //name, rank, dmgFlat, dmgRand, range, strMod, hpMod, intMod, manaMod, apMod
            add(new Weapon("Sword", 0, 5, 2, 1, 0, 0, 0, 0, 0));
            add(new Weapon("Axe", 0, 6, 0, 1, 0, 0, 0, 0, 0));
            add(new Weapon("Spear", 0, 4, 2, 2, 0, 0, 0, 0, 0));
            add(new Weapon("Warhammer", 0, 5, 0, 1, 2, 0, 0, 0, 0));
            add(new Weapon("Staff", 0, 3, 2, 1, 0, 0, 5, 0, 0));
            add(new Weapon("Wand", 0, 2, 2, 1, 0, 0, 3, 5, 0));
            add(new Weapon("Orb", 0, 1, 2, 1, 0, 0, 2, 10, 0));
            add(new Weapon("Grimoire", 0, 0, 2, 1, 0, 0, 5, 5, 0));
        }
    };

    static final Enchant commonPrefix = new Enchant("", 0, 0, 0, 0, 0, 0, 0, 0);

    static final List<Enchant> rarePrefix = new ArrayList<Enchant>() {
        private static final long serialVersionUID = -5980386674576906791L;

        {
            //Enchant(enchantName, wType, flatMod, randMod, strMod, hpMod, intMod, manaMod, apMod)
            add(new Enchant("Sharp", 1, 4, 1, 0, 0, 0, 0, 0));
            add(new Enchant("Twisted", 1, 2, 4, 0, 0, 0, 0, 0));
            add(new Enchant("Strong", 1, 0, 0, 2, 5, 0, 0, 0));
            add(new Enchant("Mindfull", 2, 0, 0, 0, 0, 2, 5, 0));
            add(new Enchant("Magical", 2, 0, 0, 0, 0, 4, 0, 0));
            add(new Enchant("Mana", 2, 0, 0, 0, 0, 0, 10, 0));
        }
    };

    static final List<Enchant> epicPrefix = new ArrayList<Enchant>() {
        private static final long serialVersionUID = -5980386674576906791L;

        {
            //Enchant(enchantName, wType, flatMod, randMod, strMod, hpMod, intMod, manaMod, apMod)
            add(new Enchant("Sharper", 1, 8, 2, 0, 0, 0, 0, 0));
            add(new Enchant("Lucky", 1, 4, 10, 0, 0, 0, 0, 0));
            add(new Enchant("Stronger", 1, 0, 0, 4, 10, 0, 0, 0));
            add(new Enchant("Smart", 2, 0, 0, 0, 0, 4, 10, 0));
            add(new Enchant("Enchanted", 2, 0, 0, 0, 0, 8, 0, 0));
            add(new Enchant("Infused", 2, 0, 0, 0, 0, 0, 20, 0));
        }
    };

    static final List<Enchant> legendaryPrefix = new ArrayList<Enchant>() {
        private static final long serialVersionUID = -5980386674576906791L;

        {
            //Enchant(enchantName, wType, flatMod, randMod, strMod, hpMod, intMod, manaMod, apMod)
            add(new Enchant("Swift", 0, 4, 0, 0, 0, 0, 0, 1));
            add(new Enchant("Balanced", 0, 6, 6, 6, 10, 6, 10, 0));
            add(new Enchant("Vorpal", 1, 14, 8, 0, 0, 0, 0, 0));
            add(new Enchant("Fateful", 1, 8, 16, 0, 0, 0, 0, 0));
            add(new Enchant("Mighty", 1, 6, 0, 10, 20, 0, 0, 0));
            add(new Enchant("Enlightened", 2, 0, 0, 0, 0, 10, 30, 0));
            add(new Enchant("Divine", 2, 0, 0, 0, 0, 20, 0, 0));
            add(new Enchant("Overflowing", 2, 0, 0, 0, 0, 0, 60, 0));
        }
    };

    static final Enchant cheatPrefix = new Enchant("Overpowered", 0, 999, 999, 999, 999, 999, 999, 999);

    private static Enchant generateEnchant(int wType, int rank) {
        Enchant ench = null;
        if (rank == 0 || rank == -1) {
            ench = commonPrefix;
        } else if (rank == 1) {
            Random rand = new Random();
            do {
                ench = rarePrefix.get(rand.nextInt(rarePrefix.size()));
            } while (ench.getwType() != 0 && ench.getwType() != wType);
        } else if (rank == 2) {
            Random rand = new Random();
            do {
                ench = epicPrefix.get(rand.nextInt(epicPrefix.size()));
            } while (ench.getwType() != 0 && ench.getwType() != wType);
        } else if (rank == 3 || rank == 4) {
            Random rand = new Random();
            do {
                ench = legendaryPrefix.get(rand.nextInt(legendaryPrefix.size()));
            } while (ench.getwType() != 0 && ench.getwType() != wType);
        } else if (rank == 5) {
            ench = cheatPrefix;
        }
        return ench;
    }

    public static Weapon generateWeapon() {
        int rank = 0;
        Random rand = new Random();
        int res = rand.nextInt(100);
        if (res >= 97) {
            rank = 4;
        } else if (res >= 90) {
            rank = 3;
        } else if (res >= 75) {
            rank = 2;
        } else if (res >= 50) {
            rank = 1;
        }
        return generateWeapon(rank);
    }

    public static Weapon generateWeapon(String wType) {
        int rank = 0;
        Random rand = new Random();
        int res = rand.nextInt(100);
        if (res >= 97) {
            rank = 4;
        } else if (res >= 90) {
            rank = 3;
        } else if (res >= 75) {
            rank = 2;
        } else if (res >= 50) {
            rank = 1;
        }
        return generateWeapon(wType, rank);
    }

    public static Weapon generateWeapon(int rank) {
        String wType = null;
        Random rand = new Random();
        int res = rand.nextInt(8);
        switch (res) {
            case 0:
                wType = "Sword";
                break;
            case 1:
                wType = "Axe";
                break;
            case 2:
                wType = "Spear";
                break;
            case 3:
                wType = "Warhammer";
                break;
            case 4:
                wType = "Staff";
                break;
            case 5:
                wType = "Wand";
                break;
            case 6:
                wType = "Orb";
                break;
            case 7:
                wType = "Grimoire";
                break;
        }
        return generateWeapon(wType, rank);
    }

    public static Weapon generateWeapon(String wType, int rank) {
        Enchant ench1 = null;
        Enchant ench2 = null;
        String name = null;
        Weapon wBase = null;

        for (Weapon weapon : baseWeapon) {
            if (weapon.getName().equals(wType)) {
                wBase = weapon;
            }
        }

        if (wType.equals("Sword") || wType.equals("Axe") || wType.equals("Spear") || wType.equals("Warhammer")) {
            ench1 = generateEnchant(1, rank);
            ench2 = generateEnchant(1, rank - 1);
        } else {
            ench1 = generateEnchant(2, rank);
            ench2 = generateEnchant(2, rank - 1);
        }

        if (rank == 5) {
            ench2 = commonPrefix;
        }

        if (ench1.getEnchantName().equals("")) {
            name = wType;
        } else if (ench2.getEnchantName().equals("")) {
            name = ench1.getEnchantName() + " " + wType;
        } else {
            name = ench1.getEnchantName() + " " + ench2.getEnchantName() + " " + wType;
        }

        int flatDmg = wBase.getDmgFlat() + ench1.getFlatMod() + ench2.getFlatMod();
        int randDmg = wBase.getDmgRand() + ench1.getRandMod() + ench2.getRandMod();
        int range = wBase.getRange();
        int strMod = wBase.getStrMod() + ench1.getStrMod() + ench2.getStrMod();
        int hpMod = wBase.getHpMod() + ench1.getHpMod() + ench2.getHpMod();
        int intMod = wBase.getIntMod() + ench1.getIntMod() + ench2.getIntMod();
        int manaMod = wBase.getManaMod() + ench1.getManaMod() + ench2.getManaMod();
        int apMod = wBase.getApMod() + ench1.getApMod() + ench2.getApMod();

        return new Weapon(name, rank, flatDmg, randDmg, range, strMod, hpMod, intMod, manaMod, apMod);
    }
}
