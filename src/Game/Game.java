package Game;

import java.util.HashMap;

public class Game {
	private HashMap<String, Player> players;
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
		String map = "{#w#:4,#h#:4,#map#:[[0,0,0,0],[0,1,0,0],[0,0,1,0],[0,0,0,0]]}";
		this.broadcast("start");
		this.broadcast("map", map);
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
