package test;

import java.io.IOException;
import java.net.URI;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;

@WebSocket
public class Client {
	private WebSocketClient client;
	private Session userSession = null;

	public Client(URI endpointURI) {

		this.client = new WebSocketClient();
		try {
			this.client.start();
			this.client.connect(this, endpointURI);
			System.out.printf("Connecting to : %s%n", endpointURI);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void close() throws Exception {
		if (userSession != null) {
			userSession.close();
		}
		userSession = null;
		this.client.stop();
	}

	@OnWebSocketConnect
	public void onOpen(Session userSession) {
		System.out.println("wsocked opened");
		this.userSession = userSession;
	}

	@OnWebSocketClose
	public void onClose(Session userSession, int statusCode, String reason) {
		System.out.println("wsocked closed:" + statusCode + " " + reason);
		this.userSession = null;
	}

	@OnWebSocketMessage
	public void onMessage(String message) {
		System.out.println("recv:" + message);
	}

	public void sendMessage(String message) throws IOException {
		this.userSession.getRemote().sendString(message);
	}

}
