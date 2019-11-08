package Game;

import javax.websocket.Session;
import Utils.Tools;

public class Player {
	private Session session;
	public double x;
	public double y;
	private int size;
	private int speed;
	private int keys;
	private String id;
	public Player(Session session) {
		this.session = session;
		this.id = session.getId();
		this.x = Tools.randInt(500);
		this.y = Tools.randInt(500);
		this.size = (int)Math.pow(Tools.randFloat(1,2), 2);
		this.speed = 5;
		this.keys = 0;
	}
	public void control(int keys) {
		this.keys |= keys;
	}
	public void update() {
		if ((this.keys & 1) != 0) {
			this.y -= this.speed;
		}
		if ((this.keys & 2) != 0) {
			this.y += this.speed;
		}
		if ((this.keys & 4) != 0) {
			this.x += this.speed;
		}
		if ((this.keys & 8) != 0) {
			this.x -= this.speed;
		}
		this.keys = 0;
	}
	public String getId() {
		return id;
	}
	public int getSize() {
		return this.size*10;
	}
	public Session getSession() {
		return this.session;
	}
	public void sendMessage(String message) {
		try {
			this.session.getAsyncRemote().sendText(message.replace("#", "\""));
		} catch (Exception e) {
			System.out.println("Illegal call ("+e+") for "+this.id);
		}
	}
	@Override
	public String toString() {
		return this.id+":["+this.x+","+this.y+","+this.size+"]";
	}
}