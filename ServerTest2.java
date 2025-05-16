import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.Test;

class ServerTest2 {

	@Test
	void testCoordinatorinfo() throws IOException {
		// Create a mock Coordinator
	    Coordinator mockCoordinator = new Coordinator();
	    ServerSocket mockserverSocket = new ServerSocket();
	    Server server = new Server(mockserverSocket, mockCoordinator);
	    // Create a few mock ClientHandler objects
	    ClientHandler client1 = new ClientHandler("Amosh");
	    
	    // Test scenario when the client is the coordinator
	    mockCoordinator.addMember(client1);
	    server.addToclientHandlers(client1);
	    server.coordinatorinfo(mockCoordinator, client1);
	    
	    System.out.println("");
	    ClientHandler client2 = new ClientHandler("Jenny");
	    
	    // Test scenario when the client is not the coordinator
	    mockCoordinator.addMember(client2);
	    server.addToclientHandlers(client2);
	    server.coordinatorinfo(mockCoordinator, client2);
	    
	   

	}

	    
	    
}

	


