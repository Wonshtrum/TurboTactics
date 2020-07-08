package turbo.game.entity;

import turbo.game.map.Map;

import java.util.Random;

public class Goblin extends Mob {

    public Goblin(Map map) {
        super(10, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, map);
    }

    public Goblin(int hpMax, int mpMax, int paMax, int armor, int initiative, int level, int xp, int intel, int str, int dext, int gold, Map map) {
        super(hpMax, mpMax, paMax, armor, initiative, level, xp, intel, str, dext, gold, map);
    }

    public static Goblin generate(Map map) {
        Random random = new Random();
        int tmpRandom;
        tmpRandom = random.nextInt(3);
        int level = map.getFloor() + tmpRandom;
        tmpRandom = random.nextInt(level + 1);
        int xp = level * 5 + tmpRandom;
        tmpRandom = random.nextInt(1 + level + map.getFloor());
        int hpMax = 10 + (int) Math.round(tmpRandom * 1.5);
        int mpMax = 0;
        int paMax = 4;
        int intel = 0;
        int armor = 0;
        int initiative = 0;
        tmpRandom = random.nextInt(1 + level * level);
        int gold = tmpRandom;
        int strengh = 0;
        int dext = 0;
        if (level > 3) {
            hpMax += 3;
            paMax++;
            initiative++;
        }
        if (level > 6) {
            hpMax += 5;
            paMax++;
            strengh++;
            initiative++;
        }
        if (level > 9) {
            hpMax += 8;
            paMax++;
            strengh++;
            armor++;
            initiative++;
        }
        if (level > 20) {
            hpMax += 30;
            armor++;
            paMax += 3;
            strengh += 10;
            initiative += 10;
        }
        Goblin goblin = new Goblin(hpMax, mpMax, paMax, armor, initiative, level, xp, intel, strengh, dext, gold, map);
        return goblin;
    }

}
