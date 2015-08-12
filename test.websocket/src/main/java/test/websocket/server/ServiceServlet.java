package test.websocket.server;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebServlet("/wssocket")
public class ServiceServlet extends WebSocketServlet {
	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(ServiceWebSocket.class);
	}
}
