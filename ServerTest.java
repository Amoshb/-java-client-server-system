import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.Test;

class ServerTest {

	@Test
	void testBroadcastMessage() throws IOException {
	    // Create a mock server socket
	    ServerSocket mockServerSocket = new ServerSocket(0);
	    // Create a mock server
	    Server mockServer = new Server(mockServerSocket, new Coordinator());

	    // Create a few mock client handlers
	    ClientHandler client1 = new ClientHandler(new Socket(), mockServer, new Coordinator());
	    ClientHandler client2 = new ClientHandler(new Socket(), mockServer, new Coordinator());
	    ClientHandler client3 = new ClientHandler(new Socket(), mockServer, new Coordinator());

	    // Add the client handlers to the server
	    mockServer.getClientHandlers().add(client1);
	    mockServer.getClientHandlers().add(client2);
	    mockServer.getClientHandlers().add(client3);

	    // Send a message to the server
	    mockServer.broadcastMessage("Hello, everyone!", client1);

	    // Check that the message was received by the other clients
	    assertEquals("Hello, everyone!", client2.getMessages().get(0));
	    assertEquals("Hello, everyone!", client3.getMessages().get(0));
	}

	@Test
	void testSendPrivateMessage() {
		fail("Not yet implemented");
	}

	@Test
	void testSendlisttoclient() {
		fail("Not yet implemented");
	}

}
