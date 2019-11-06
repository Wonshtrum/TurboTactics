package WebSockets;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import Game.Manager;

@ServerEndpoint("/socket")
public class WebSocket {
    @OnOpen
    public void open(Session session) {
    	System.out.println("open "+session);
    	//TODO party number
    	Manager.getInstance().addPlayer(session, 0);
	}
	
	@OnClose
    public void close(Session session) {
    	System.out.println("close "+session);
    	Manager.getInstance().removePlayer(session.getId());
	}
	
	@OnError
    public void onError(Throwable error) {
		error.printStackTrace();
	}
	
	@OnMessage
    public void handleMessage(String message, Session session) {
		Manager.getInstance().dispatch(session.getId(), message);
	}
}