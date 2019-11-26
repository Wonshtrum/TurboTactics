package Game.Entity;

import java.io.IOException;

import javax.websocket.Session;

import Game.Map.Map;

public class Player extends Entity {
	private Session session;
	private String name;

	public Player (String name, Session session) {
		//String id, int hpMax, int mpMax, int paMax, int armor, int initiative, int level, int xp, int intel, int str, int dext, int gold, Map map
		super(session.getId(), 20, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0,null);
		this.session=session;
		this.setId("P"+session.getId());
		this.name=name;
	}

	public Session getSession() {
		return session;
	}
	
	public String getName() {
		return name;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message.replace("#", "\""));
	}
}