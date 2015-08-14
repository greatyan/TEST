package test.websocket.tomcat;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws")
public class ServiceWebSocket

{
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		System.out.println("session opened");
		final RemoteEndpoint.Basic remote = session.getBasicRemote();
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			public void onMessage(String text) {
				System.out.println("recv:" + text + "send it back");
				try {
					remote.sendText(text);
				} catch (IOException ioe) {
					// handle send failure here
				}
			}
		});
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		System.out.println("session closed" + reason.getCloseCode() + "," + reason.getReasonPhrase());
	}

	@OnError
	public void onError(Session session, Throwable ex) {
		System.out.println("session error:" + ex.getLocalizedMessage());
		ex.printStackTrace();
	}
}
