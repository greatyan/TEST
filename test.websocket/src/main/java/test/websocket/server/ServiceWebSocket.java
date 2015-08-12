package test.websocket.server;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class ServiceWebSocket {
	private Session session;

	@OnWebSocketConnect
	public void onConnect(Session session) {
		this.session = session;
	}

	@OnWebSocketClose
	public void onClose(int status, String message) {
		this.session = null;
		System.out.println("closed:" + status + " " + message);
	}

	@OnWebSocketMessage
	public void onMessage(String message) throws IOException {
		if ("echo".equals(message)) {
			session.getRemote().sendString("echo");
			System.out.println("echo:" + message);
		} else {
			System.out.println("recv:" + message);
		}
	}

}
