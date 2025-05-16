import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class CoordinatorTest {

	@Test
	void testAddMember() throws IOException {
		System.out.println("");
		System.out.println("\"System: AddMember and assign Coordinator to 1st Client\"");
		System.out.println("");
		// Create a mock Coordinator
	    Coordinator mockCoordinator = new Coordinator();

	    // Create a few mock ClientHandler objects
	    ClientHandler client1 = new ClientHandler("Amosh");
	    ClientHandler client2 = new ClientHandler("Jenny");

	    // Test adding the first client
	    mockCoordinator.addMember(client1);
	    assertEquals(1, mockCoordinator.getallmembers().size());

	    // Test adding the second client
	    mockCoordinator.addMember(client2);
	    assertEquals(2, mockCoordinator.getallmembers().size());

	    // Test adding the same client again
	    mockCoordinator.addMember(client1);
	    assertEquals(2, mockCoordinator.getallmembers().size());
	}

	@Test
	void testRemoveMember() {
		System.out.println("");
		System.out.println("\"System: testRemoveMember and Cooridnator change\"");
		System.out.println("");
	    // Create a mock Coordinator
	    Coordinator mockCoordinator = new Coordinator();

	    // Create a few mock ClientHandler objects
	    ClientHandler client1 = new ClientHandler("Amosh");
	    ClientHandler client2 = new ClientHandler("Jenny");

	    // Add the clients to the coordinator
	    mockCoordinator.addMember(client1);
	    mockCoordinator.addMember(client2);
	    System.out.println("");
	    // Test removing the first client
	    mockCoordinator.removeMember(client1);
	    assertEquals(1, mockCoordinator.getallmembers().size());

	    // Test removing the second client
	    mockCoordinator.removeMember(client2);
	    assertEquals(0, mockCoordinator.getallmembers().size());

	    // Test removing the first client again
	    mockCoordinator.removeMember(client1);
	    assertEquals(0, mockCoordinator.getallmembers().size());
	}
	}

