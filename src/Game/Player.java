package Game;

import javax.websocket.Session;

public class Player extends Entity {
	private Session session;
	private String name;

		
	public Player (String name, Session session) {
		super();
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
	
	public void sendMessage(String message) {
		try {
			this.session.getAsyncRemote().sendText(message.replace("#", "\""));
		} catch (Exception e) {
			System.out.println("Illegal attempt ("+e+") for session "+this.getId());
		}
	}

	@Override
	public String toString() {
		return this.id;
	}
}