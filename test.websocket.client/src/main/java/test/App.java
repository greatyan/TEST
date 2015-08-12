package test;

import java.net.URI;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) throws Exception {
		System.out.println("Hello World!");
		Client client = new Client(new URI("ws://localhost:8080/test/wssocket"));
		try {
			client.sendMessage("abcdefg");
			client.sendMessage("echo");
			client.sendMessage("another message");
		} finally {
			client.close();
		}

	}
}
