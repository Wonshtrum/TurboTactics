package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import Game.Entity.Player;
import Game.Map.Air;
import Game.Map.Map;
import Utils.Couple;
import Utils.Tools;
import Utils.Triplet;

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
			String args = data.substring(data.indexOf(":")+1);
			System.out.println(cmd+"->"+args);
			Player player = players.get(id);
			if (cmd.equals("move")) {
				HashMap<Triplet<Integer, Integer, Integer>, Triplet<Integer, Integer, Integer>> tree = this.map.paths(player.posX, player.posY);
				Triplet<Integer, Integer, Integer> pos = tree.keySet().stream().filter(t -> (t.x+","+t.y).equals(args)).findAny().orElse(null);
				if (pos != null) {
					ArrayList<Couple<Integer, Integer>> path = Tools.tracePath(tree, pos);
					String result = "[";
					for (int i = 0 ; i<pos.z ; i++) {
						result += "["+path.get(i)+"]";
						if (i < pos.z-1) {
							result += ",";
						}
					}
					player.move(pos.x, pos.y, pos.z);
					this.broadcast("move", "[#P"+id+"#,"+pos.z+","+result+"]]");
				}
			}
		} catch (Exception e) {
			Manager.getInstance().removePlayer(id);
		}
	}
		
	public void addPlayer(Player player) {
		System.out.println("added: "+player.getId());
		this.players.put(player.getId(), player);
		this.broadcast(player.getId()+" joined");
		this.trySend(player, "me", "#P"+player.getId()+"#");
		if (this.players.size() >= 2) {
			this.start();
		}
	}
	
	public void removePlayer(String id) {
		Player player = players.get(id);
		this.map.place(player.posX, player.posY, new Air(0,0));
		this.players.remove(id);
		this.broadcast(id+" left");
		this.broadcast("players", sendPlayers());
		this.broadcast("map", map.toString());
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