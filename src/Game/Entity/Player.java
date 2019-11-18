package Game.Entity;

import java.io.IOException;

import javax.websocket.Session;

import Game.Map.Map;

public class Player extends Entity {
	private Session session;
	private String name;

	public Player (String name, Session session) {
		super(null);
		this.session=session;
		this.setId(session.getId());
		this.name=name;
	}

	public Session getSession() {
		return session;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPa(int pa) {
		this.pa = pa;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message.replace("#", "\""));
	}

	@Override
	public String toString() {
		return "P"+this.id;
	}
	public String fullData() {
		return "#P"+this.id+"#:{"+super.toString()+"}";
	}
}