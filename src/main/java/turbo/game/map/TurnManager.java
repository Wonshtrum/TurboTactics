package turbo.game.map;

import turbo.game.Game;
import turbo.game.entity.Entity;
import turbo.game.entity.Mob;
import turbo.game.entity.Player;
import turbo.util.Stats;

import java.util.ArrayList;
import java.util.Comparator;

public class TurnManager {
    public ArrayList<Entity> entities;
    private Entity entityTurn;
    private Game game;

    public TurnManager(ArrayList<Entity> entityOrder, Game game) {
        this.entities = entityOrder;
        this.sort();
        this.entityTurn = entities.get(0);
        this.game = game;
    }

    public void sort() {
        entities.sort(Comparator.comparingInt(e -> e.getStat(Stats.initiative)));
    }

    public int size() {
        return this.entities.size();
    }

    public void add(Entity entity) {
        this.entities.add(entity);
        this.sort();
    }

    public void remove(Entity entity) {
        if (this.entityTurn == entity) {
            next();
        }
        this.entities.remove(entity);
    }

    public void next() {
        int index = this.entities.indexOf(this.entityTurn);
        this.entityTurn.endTurn();
        if (index == this.size() - 1) {
            this.entityTurn = this.entities.get(0);
        } else {
            this.entityTurn = this.entities.get(index + 1);
        }
        this.entityTurn.beginTurn();
        if (this.entityTurn instanceof Player) {
            this.game.broadcast("Turn of " + ((Player)this.entityTurn).getName());
        } else {
            this.game.broadcast("Turn of " + this.entityTurn);
        }
        if (this.entityTurn instanceof Mob) {
            this.game.broadcast(((Mob) this.entityTurn).play());
            this.next();
        }
    }

    public boolean isTurn(Entity entity) {
        return entity.equals(entityTurn);
    }

}
