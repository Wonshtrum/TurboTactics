package Game;

import java.util.HashMap;
import javax.websocket.Session;

public class Manager {
	private HashMap<Integer, Game> games;
	private HashMap<String, Game> playerToGame;
	private HashMap<String, Session> sessions;
	private static Manager instance = new Manager();
	private Manager() {
		this.games = new HashMap<Integer, Game>();
		this.playerToGame = new HashMap<String, Game>();
		this.sessions = new HashMap<String, Session>();
		this.games.put(0, new Game());
	}
	
	public static Manager getInstance() {
		return instance;
	}
	
	public void addPlayer(Session session, int idGame) {
		sessions.put(session.getId(), session);
		Game game = games.get(idGame);
		game.addPlayer(new Player("JeanRandom", session));
		playerToGame.put(session.getId(), game);
	}

	public void removePlayer(String id) {
		try {
			System.out.println("Try close "+id);
			sessions.get(id).close();
			playerToGame.get(id).removePlayer(id);
			playerToGame.remove(id);
		} catch (Exception e) {}
	}

	public void dispatch(String id, String data) {
		try {
			playerToGame.get(id).receive(id, data);
		} catch (Exception e) {}
	}
}