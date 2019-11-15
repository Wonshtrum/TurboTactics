package Game.Entity;

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
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	public void sendMessage(String message) {
		try {
			this.session.getBasicRemote().sendText(message.replace("#", "\""));
		} catch (Exception e) {
			System.out.println("Illegal call ("+e+") for session "+this.getId());
		}
	}

	@Override
	public String toString() {
		return "P"+this.id;
	}
	public String fullData() {
		return "#P"+this.id+"#:{"+super.toString()+"}";
	}
}