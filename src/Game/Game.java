package Game;

import java.util.HashMap;
import java.util.List;
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
			String cmd = data.substring(0, data.indexOf(":"));
			String args = data.substring(data.indexOf(":")+1);
			System.out.println(cmd+" "+args);
			Player player = players.get(id);
			if (cmd.equals("pos")) {
				boolean valid = this.map.pathes(player.posX, player.posY).stream().anyMatch(t -> (t.x+","+t.y).equals(args));
				player.sendMessage(typedData("msg", ""+valid));
			}
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
		Player player = players.get(id);
		this.map.place(player.posX, player.posY, null);
		players.remove(id);
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
		this.map = new Map(8,8,0,players.values().stream().collect(Collectors.toList()));
		this.broadcast("start");
		this.broadcast("map", map.toString());
		this.broadcast("players", sendPlayers());
	}
	public String typedData(String type, String data) {
		return "{#type#:#"+type+"#,#data#:"+data+"}";
	}
	public void broadcast(String data) {
		this.broadcast("msg", "#"+data+"#");
	}
	public void broadcast(String type, String data) {
		for (Player player : players.values()) {
			player.sendMessage(typedData(type, data));
		}
	}
}
