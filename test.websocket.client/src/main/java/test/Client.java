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

	public Client() throws Exception {
		this.client = new WebSocketClient();
	}

	public void connect(URI endpointURI) throws Exception {
		if (isConnected()) {
			throw new IOException("has connected");
		}
		this.client.start();
		this.client.connect(this, endpointURI);
	}

	public boolean isConnected() {
		return userSession != null;
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
		System.out.println("socket connected.");
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
		System.out.println("send:" + message);
		this.userSession.getRemote().sendString(message);
	}

}
