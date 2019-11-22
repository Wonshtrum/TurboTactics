package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import Game.Entity.Player;
import Game.Map.Map;
import Utils.Couple;

public class Game {
	private HashMap<String, Player> players;
	private Map map;
	public Game() {
		this.players = new HashMap<String, Player>();
	}
	public void receive(String id, String data) {
		System.out.println(data);
		try {
			String cmd = data.substring(0, data.indexOf(":"));
			String[] args = data.substring(data.indexOf(":")+1).split(",");
			String argsString = "";
			for (String arg : args) {
				argsString += " "+arg;
			}
			System.out.println(cmd+"->"+argsString);
			Player player = players.get(id);
			switch(cmd) {
			case "move":
				int gx = Integer.parseInt(args[0]);
				int gy = Integer.parseInt(args[1]);
				if(map.sortedEntityOrder.isTurn(player)) {
					ArrayList<Couple<Integer, Integer>> path = this.map.path(player.posX, player.posY, gx, gy, player.getPa());
					if (path != null) {
						String result = "[";
						int pa = path.size();
						for (int i = 0 ; i<pa ; i++) {
							result += "["+path.get(i)+"]";
							if (i < pa-1) {
								result += ",";
							}
						}
						player.move(gx, gy, pa);
						this.broadcast("move", "[#"+player+"#,"+pa+","+result+"]]");
					}
				}	
				break;
			case "endTurn":
				if(map.sortedEntityOrder.isTurn(player)) {
					this.broadcast("end", "#"+player+"#");
					map.sortedEntityOrder.next();
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Manager.getInstance().removePlayer(id);
		}
	}
		
	public void addPlayer(Player player) {
		this.players.put(player.getId(), player);
		this.broadcast(player.getId()+" joined");
		this.trySend(player, "me", "#"+player+"#");
		this.start();
	}
	
	public void removePlayer(String id) {
		Player player = players.get(id);
		player.die();
		this.players.remove(id);
		this.broadcast(id+" left");
		this.broadcast("rm", "#"+player+"#");
	}
	
	private String sendPlayers() {
		String res = "{";
		List<Player> playersList = players.values().stream().collect(Collectors.toList());
		int length = playersList.size();
		for (int i=0 ; i<length ; i++) {
			res += playersList.get(i).fullData();
			if (i<length-1) {
				res += ",";
			}
		}
		res += "}";
		return res;
	}

	public void start() {
		this.map = new Map(8, 8, 0, players.values().stream().collect(Collectors.toList()));
		for (Player player : players.values()) {
			player.setPa(player.getPamax());
			player.setMap(this.map);
		}
		this.broadcast("start");
		this.broadcast("map", map.toString());
		this.broadcast("players", sendPlayers());
	}
	
	public String typedData(String type, String data) {
		return "{#type#:#"+type+"#,#data#:"+data+"}";
	}
	
	public void trySend(Player player, String data) {
		this.trySend(player, "msg", "#"+data+"#");
	}
	
	public void trySend(Player player, String type, String data) {
		try {
			player.sendMessage(typedData(type, data));
		} catch (Exception e) {
			Manager.getInstance().removePlayer(player.getId());
		}
	}
	
	public void broadcast(String data) {
		this.broadcast("msg", "#"+data+"#");
	}
	
	public void broadcast(String type, String data) {
		List<Player> playersList = this.players.values().stream().collect(Collectors.toList());
		for (Player player : playersList) {
			this.trySend(player, type, data);
		}
	}
}