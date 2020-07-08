package turbo.game;

import turbo.repository.Repositories;
import turbo.game.entity.Entity;
import turbo.game.entity.Player;
import turbo.game.map.Map;
import turbo.util.Couple;
import turbo.util.Stats;
import turbo.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    private HashMap<String, Player> players;
    private Map map;
    private final String idGame;

    public Game(String idGame) {
        this.idGame = idGame;
        this.players = new HashMap<String, Player>();
    }

    public void receive(String id, String data) {
        System.out.println(data);
        try {
            String cmd = data.substring(0, data.indexOf(":"));
            String[] args = data.substring(data.indexOf(":") + 1).split(",");
            String argsString = "";
            for (String arg : args) {
                argsString += " " + arg;
            }
            System.out.println(cmd + "->" + argsString);
            Player player = players.get(id);
            switch (cmd) {
                case "move":
                    int gx = Integer.parseInt(args[0]);
                    int gy = Integer.parseInt(args[1]);
                    if (map.sortedEntityOrder.isTurn(player)) {
                        ArrayList<Couple<Integer, Integer>> path = this.map.path(player.posX, player.posY, gx, gy, player.getStat(Stats.pa), true);
                        if (path != null) {
                            int pa = path.size();
                            player.move(gx, gy, pa);
                            this.broadcast("move", "[#" + player + "#," + player.getStat(Stats.pa) + "," + Tools.pathToString(path) + "]");
                        }
                    }
                    break;
                case "attack":
                    if (map.sortedEntityOrder.isTurn(player)) {
                        int aimPosX = Integer.parseInt(args[0]);
                        int aimPosY = Integer.parseInt(args[1]);
                        if (!((player.posX == aimPosX) && (player.posY == aimPosY))) {
                            if (map.straightLine(player.posX, player.posY, aimPosX, aimPosY, player.getEquipment().Weapon.getRange())) {
                                //player.dealDamage(player.getEquipment().Weapon.getDmgFlat(),(Entity)map.checkTile(aimPosX, aimPosY));
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
            if (map.sortedEntityOrder.isTurn(player) && (cmd.equals("endTurn")/* || player.getStat(Stats.pa) == 0*/)) {
                this.broadcast("end", "#" + player + "#");
                try {
                    map.sortedEntityOrder.next();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Manager.getInstance().removePlayer(id);
        }
    }

    public void addPlayer(Player player) {
        this.players.put(player.getSession().getId(), player);
        this.broadcast(player.getName() + " joined");
        this.trySend(player, "me", "#" + player + "#");
        this.start();
    }

    public void removePlayer(String id) {
        Player player = players.get(id);
        Repositories.updateScore(player.getName(), player.getStat(Stats.xp));
        player.die();
        this.players.remove(id);
        this.broadcast(player.getName() + " left");
        this.broadcast("rm", "#" + player + "#");
    }

    private String sendEntities() {
        String res = "{";
        List<Entity> entityList = this.map.sortedEntityOrder.entities;
        int length = entityList.size();
        System.out.println("length: " + length);
        for (int i = 0; i < length; i++) {
            res += entityList.get(i).fullData();
            if (i < length - 1) {
                res += ",";
            }
        }
        res += "}";
        return res;
    }

    public void start() {
        this.map = new Map(8, 8, 0, new ArrayList<>(players.values()), this);
        for (Player player : players.values()) {
            player.setStat(Stats.pa, player.getStat(Stats.paMax));
            player.setMap(this.map);
        }
        this.broadcast("map", this.map.toString());
        this.broadcast("entities", this.sendEntities());
    }

    public String typedData(String type, String data) {
        return "{#type#:#" + type + "#,#data#:" + data + "}";
    }

    public void trySend(Player player, String data) {
        this.trySend(player, "msg", "#" + data + "#");
    }

    public void trySend(Player player, String type, String data) {
        try {
            player.sendMessage(typedData(type, data));
        } catch (Exception e) {
            Manager.getInstance().removePlayer(player.getSessionId());
        }
    }

    public void broadcast(String data) {
        this.broadcast("msg", "#" + data + "#");
    }

    public void broadcast(Couple<String, String> data) {
        this.broadcast(data.x, data.y);
    }

    public void broadcast(String type, String data) {
        List<Player> playersList = new ArrayList<>(this.players.values());
        for (Player player : playersList) {
            this.trySend(player, type, data);
        }
    }
}