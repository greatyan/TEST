package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) throws Exception {

		URI url = new URI("ws://localhost:8080/test/ws");
		Client client = new Client();
		System.out.println("type 'exit' to exit");
		while (true) {
			String line = readLine();
			if ("close".equals(line)) {
				client.close();
			} else if ("exit".equals(line)) {
				System.exit(0);
			} else if ("connect".equals(line)) {
				if (client.isConnected()) {
					System.out.println("alread connected");
				} else {
					client.connect(url);
				}
			} else {
				if (line != null && line.length() > 0) {
					if (client.isConnected()) {
						client.sendMessage(line);
					} else {
						System.out.println("client is not connected");
					}
				}
			}
		}
	}

	private static String readLine() throws IOException {
		return new BufferedReader(new InputStreamReader(System.in)).readLine();
	}
}
