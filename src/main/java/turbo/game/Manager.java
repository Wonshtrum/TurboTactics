package turbo.game;

import org.springframework.web.socket.WebSocketSession;
import turbo.game.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import turbo.util.Tools;

public class Manager {
    private HashMap<String, Game> games;
    private HashMap<String, Game> playerToGame;
    private HashMap<String, WebSocketSession> sessions;
    private static final Manager instance = new Manager();

    private Manager() {
        this.games = new HashMap<String, Game>();
        this.playerToGame = new HashMap<String, Game>();
        this.sessions = new HashMap<String, WebSocketSession>();
    }

    public static Manager getInstance() {
        return instance;
    }

    public boolean existGame(String idGame) {
        return this.games.containsKey(idGame);
    }

    public void addPlayer(WebSocketSession session) throws IOException {
        String idGame = session.getUri().getPath().split("socket/")[1];
        if (!this.existGame(idGame)) {
            session.close();
        } else {
            this.sessions.put(session.getId(), session);
            Game game = this.games.get(idGame);
            game.addPlayer(new Player((String)session.getAttributes().get("name"), session));
            this.playerToGame.put(session.getId(), game);
        }
    }

    public void removePlayer(String id) {
        try {
            this.playerToGame.get(id).removePlayer(id);
            this.playerToGame.remove(id);
            System.out.println("closed " + id);
        } catch (Exception e) {
            System.out.println("can't close " + id);
        }
    }

    public void dispatch(String id, String data) {
        try {
            this.playerToGame.get(id).receive(id, data);
        } catch (Exception e) {
            System.out.println("can't dispatch to " + id);
        }
    }

    public String createGame() {
        String idGame;
        do {
            idGame = Tools.generateRandomString(8);
        } while (this.existGame(idGame));
        this.games.put(idGame, new Game(idGame));
        return idGame;
    }
}