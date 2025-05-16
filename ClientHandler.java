import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

//ClientHandler class to manage individual client connections and messages
public class ClientHandler implements Runnable {
	// ArrayList to store all client handlers
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	
	// Socket, BufferedReader, BufferedWriter, and Server instance variables
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String clientUsername;
	private String ip;
    private int port;
	private Server server;
	private Coordinator coordinator;
	private boolean coordinator1;
	
	 /**
     * Constructs a new ClientHandler object with the given socket and server
     * @param socket The socket used to connect to the client
     * @param server The server that this client handler is associated with
     * @throws IOException If there is an error initializing the BufferedReader or BufferedWriter
     */
	public ClientHandler(Socket socket, Server server, Coordinator coordinator) {
		try {
			this.socket = socket;
			this.server = server;
			this.coordinator = coordinator;
			this.setCoordinator1(false);
			this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			this.setClientUsername(bufferedReader.readLine());
			this.setIp(bufferedReader.readLine());
			this.setPort(Integer.parseInt(bufferedReader.readLine()));
			clientHandlers.add(this);
			server.broadcastMessage("SERVER: " + getClientUsername() + " has entered the chat!", this);
		} catch (IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
			
	}
	
    public ClientHandler(String client) {
    	setClientUsername(client);
		// TODO Auto-generated constructor stub
	}

	/**
     * Implements the Runnable interface to handle messages from individual clients
     */
	@Override
	public void run() {
		String messageFromClient;
		
		while (socket.isConnected()) {
			try {
				messageFromClient = bufferedReader.readLine();
				String[] messageParts = messageFromClient.split(" ", 2);
				//System.out.println(messageParts[1]);	
	
				if (messageParts.length > 1) {
				    String command = messageParts[0];
				    //System.out.println(command);
				    String message = messageParts[1];
				    if (command.equalsIgnoreCase("pm")) {
						String[] pmParts = message.split(" ", 2);
						server.sendPrivateMessage(pmParts[0], pmParts[1], this);
					}
				    else {
						server.broadcastMessage(messageFromClient, this);
					}
				} else {
					// If the message starts with ol then sends the list of members 
					if (messageParts[0].equalsIgnoreCase("ol")){
						server.sendlisttoclient(this);
					}
				    // handle error or ignore message
				}
				
			} catch (IOException e) {
				closeEverything(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
	}
	
	  /**
     * Sends a message to the individual client
     * @param message The message to send
     * @throws IOException If there is an error writing to the BufferedWriter
     */
	public void sendMessage(String message) {
		try {
			bufferedWriter.write("[" + Server.getTimestamp() + "] " + message);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}
	public void ExitChat() {
		closeEverything(socket, bufferedReader, bufferedWriter);
	}
	
    /**
     * Closes all resources when the client disconnects
     * @param socket The socket to close
     * @param bufferedReader The BufferedReader to close
     * @param bufferedWriter The BufferedWriter to close
     */
	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		server.removeClientHandler(this);
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (socket != null) {
				socket.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// Getter and Setter methods for clientUsername
	public String getClientUsername() {
		return clientUsername;
	}

	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}
    public void broadcastMessage(String message) {
        coordinator.broadcastMessage(message, this);
    }

    public void sendPrivateMessage(String recipient, String message) {
        coordinator.sendPrivateMessage(recipient, message, this);
    }
    public Coordinator getCoordinator() {
        return coordinator;
    }

	public boolean getCoordinator1() {
		return coordinator1;
	}

	public void setCoordinator1(boolean coordinator1) {
		this.coordinator1 = coordinator1;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Socket getSocket() {
		return socket;
	}
	public String getmessage() {
		return Client.getmessage();
	}

}