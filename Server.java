import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

// Server class to manage multiple clients and broadcast messages
public class Server {
	// ServerSocket instance variable
	private ServerSocket serverSocket;
	private Coordinator coordinator;
	
	// Set to store all client handlers
	private Set<ClientHandler> clientHandlers = new HashSet<>();
	
	  /**
     * Constructs a new Server object with the given ServerSocket
     * @param serverSocket The ServerSocket used to listen for incoming client connections
     * @param coordinator The Coordinator object that will manage the clients
     */
    public Server(ServerSocket serverSocket, Coordinator coordinator) {
        this.serverSocket = serverSocket;
        this.coordinator = coordinator;
    }
	
	


	/**
     * Starts the server and listens for incoming client connections
     * @throws IOException If there is an error accepting a client connection
     */
	public void startServer() {
		try {
			while (!serverSocket.isClosed()) {
				
				Socket socket = serverSocket.accept();
				System.out.println("A new client has connected");
				ClientHandler clientHandler = new ClientHandler(socket, this, coordinator);
				Thread thread = new Thread(clientHandler);
				thread.start();

				if (!checkUsernameExist(clientHandler.getClientUsername())) {
					coordinator.addMember(clientHandler);
					addToclientHandlers(clientHandler);
					coordinatorinfo(coordinator, clientHandler);
				} else {
					clientHandler.sendMessage("UserName already exists please re-run and enter different UserName");
					clientHandler.ExitChat();
				}

			}
		} catch(IOException e) {
			// Handle exceptions
		}
	}
	public void addToclientHandlers(ClientHandler clientHandler) {
		clientHandlers.add(clientHandler);
	}
	public boolean checkUsernameExist(String username) {
		for (ClientHandler clientHandler : clientHandlers) {
			if (clientHandler.getClientUsername().equals(username)) {
				return true;
			}
		} return false;
	}
	public void coordinatorinfo(Coordinator coordiantor, ClientHandler clientHandler) {
		String Msg;
		// as soon as someone joins in they becomes coordinator and informs them that they are the coordinator
		if (coordinator.getcoordinatorName() == clientHandler.getClientUsername()){
			Msg =  "Your are coordinator";
			//clientHandler.sendMessage(Msg);
			
			// for testing uncomment print part below and comment out clientHandler.sendMessage(Msg)
			System.out.println(Msg);
		
		}else {
		 // Otherwise, inform all clients about the coordinator	
			Msg = coordinator.getcoordinatorName()+ " is coordinator";
			//clientHandler.sendMessage(Msg);	
			
			
			// for testing uncomment print part below and comment out clientHandler.sendMessage(Msg)
			System.out.println(Msg);
			
		}
		
	}
	
    /**
     * @param message The message to broadcast
     * @param sender The client handler of the sender
     */
	public void broadcastMessage(String message, ClientHandler sender) {
		for (ClientHandler clientHandler : clientHandlers) {
			clientHandler.sendMessage(message);
		}
	}
	
    /**
     * Sends a private message to a specific client
     * @param recipient The username of the recipient
     * @param message The message to send
     * @param sender The client handler of the sender
     */

	public void sendPrivateMessage(String recipient, String message, ClientHandler sender) {
		
		for (ClientHandler clientHandler : clientHandlers) {
			if (clientHandler.getClientUsername().equals(recipient)) {
				clientHandler.sendMessage("PRIVATE: " + sender.getClientUsername() + ": " + message);
				sender.sendMessage("TO: " + recipient + " - PRIVATE: " + message);
				break;
//		if (!clientHandlers.contains(recipient)) {
//				sender.sendMessage(recipient + " is not part of the Group");
//				break;
			}
		}
	}
	
	// sends list of client in the server to the requestor 
	public void sendlisttoclient(ClientHandler sender) {
		int id = 0;
		for (ClientHandler clientHandler: clientHandlers) {
			if (clientHandler.getClientUsername().equals(sender.getClientUsername())){
				Iterator<ClientHandler> members = coordinator.getallmembers().iterator();
				while (members.hasNext()) {
					ClientHandler client = members.next();
					sender.sendMessage("ID: "+id++ +", USER NAME: " + client.getClientUsername()+ ", IP: "+ client.getIp()+ ", PORT: " + client.getPort());
				}
			}
		}
	}

	
    /**
     * Removes a client handler from the set when the client disconnects
     * @param clientHandler The client handler to remove
     */
	public void removeClientHandler(ClientHandler clientHandler) {
		boolean iscoordinator = clientHandler.getClientUsername().equals(coordinator.getcoordinatorName());
		clientHandlers.remove(clientHandler);
		coordinator.removeMember(clientHandler);
		
		// Notify all clients about the leaving client 
		broadcastMessage("SERVER: "+ clientHandler.getClientUsername() + " has left the chat", clientHandler);
		
		// error handling if there is no member in the server
		if (coordinator.getcoordinatorName() == null){
		}else if (iscoordinator) {
			// notify all the members about new coordinator
			broadcastMessage("SERVER: "+ coordinator.getcoordinatorName() + " has becomer the coordinator", clientHandler);
		}
		
	}
	
	static String getTimestamp() {
	    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss", Locale.UK));
	}
	
	// Main method to start the server
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(2345);
		Coordinator coordinator = new Coordinator();
		Server server = new Server(serverSocket, coordinator);
		System.out.println("Server Running ....");
		server.startServer();
	}




	public Set<ClientHandler> getClientHandlers() {
		// TODO Auto-generated method stub
		return clientHandlers;
	}
}