package Game;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Game {
	private HashMap<String, Player> players;
	private Map map;
	public Game() {
		this.players = new HashMap<String, Player>();
	}
	public void receive(String id, String data) {
		System.out.println(data);
		try {
			//this.players.get(id).control(Integer.parseInt(data));
		} catch (Exception e) {
			Manager.getInstance().removePlayer(id);
		}
	}
	public void addPlayer(Player player) {
		this.players.put(player.getId(), player);
		this.broadcast(player.getId()+" joined");
		if (this.players.size() >= 2) {
			this.start();
		}
	}
	public void removePlayer(String id) {
		players.remove(id);
		this.broadcast(id+" left");
	}
	public void start() {
		this.map = new Map(8,8,0,players.values().stream().collect(Collectors.toList()));
		this.broadcast("start");
		this.broadcast("map", map.toString());
	}
	public void broadcast(String data) {
		this.broadcast("msg", "#"+data+"#");
	}
	public void broadcast(String type, String data) {
		for (Player player : players.values()) {
			player.sendMessage("{#type#:#"+type+"#,#data#:"+data+"}");
		}
	}
}
