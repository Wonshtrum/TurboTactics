package turbo.game.entity;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import turbo.game.map.Map;

import java.io.IOException;

public class Player extends Entity {
    private WebSocketSession session;
    private String name;

    public Player(String name, WebSocketSession session) {
        //String id, int hpMax, int mpMax, int paMax, int armor, int initiative, int level, int xp, int intel, int str, int dext, int gold, Map map
        super(session.getId(), 20, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, null);
        this.session = session;
        this.setId("P" + session.getId());
        this.name = name;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public String getSessionId() {
        return session.getId();
    }

    public String getName() {
        return name;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void sendMessage(String message) throws IOException {
        this.session.sendMessage(new TextMessage(message.replace("#", "\"")));
    }
}