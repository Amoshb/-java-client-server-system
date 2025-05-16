import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	// The socket used to connect to the serve
	private Socket socket;
	
	// BufferedReader to read messages from the server
	private BufferedReader bufferedReader;
	
	// BufferedWriter to send messages to the server
	private BufferedWriter	bufferedWriter;
	
	// The username of the client
	private String username;
	private String ip;
	private int port;
	static String message;
	 /**
     * Constructs a new Client object with the given socket and username
     * @param socket The socket used to connect to the server
     * @param username The username of the client
     * @throws IOException If there is an error initializing the BufferedReader or BufferedWriter
     */
	public Client(Socket socket, String username, String ip, int port) {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username;
			this.ip = ip;
			this.port = port;
			
		} catch (IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}
    public Client(Socket socket, String username) {
    	try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username;

			
		} catch (IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}
	
	/**
     * Sends a message to the server
     * @throws IOException If there is an error writing to the BufferedWriter
     */
	public void sendMessage() {
		try {
			// Send the username to the server
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
			// Send the IP and port to the server
			bufferedWriter.write(ip);
			bufferedWriter.newLine();
			bufferedWriter.write(Integer.toString(port));
			bufferedWriter.newLine();
			bufferedWriter.flush();

			
			try (// Listen for user input
			Scanner scanner = new Scanner(System.in)) {
				while (socket.isConnected()) {
					String messageToSend = scanner.nextLine();
					
					// If the message starts with "pm ", send it as a private message

					if (messageToSend.startsWith("!pm ")) {
						
					    try {
					        String[] pmMessage = messageToSend.split(" ", 3);
					        if (pmMessage.length < 3) {
					        	System.out.println("Error: PM command needs two parameters usernames and a message");
					        	bufferedWriter.newLine();
					        	bufferedWriter.flush();
					        }
					        bufferedWriter.write("pm " + pmMessage[1] + " " + pmMessage[2]);
					        bufferedWriter.newLine();
					        bufferedWriter.flush();
					    } catch (ArrayIndexOutOfBoundsException e) {
					        //System.err.println("Error splitting the message for the private message: " + e.getMessage());
					    } catch (IOException e) {
					        //System.err.println("Error writing the private message: " + e.getMessage());
					    }
						
					// If the message starts with "ol", sends the list of members
					}else if (messageToSend.startsWith("!ol")) {
						bufferedWriter.write("ol");
						bufferedWriter.newLine();
						bufferedWriter.flush();
					}else if (messageToSend.startsWith("!ex")) {
						System.exit(0);
					}else if (messageToSend.startsWith("!he")){
						DisplayMessage();
					}else {
						// Otherwise, send it as a regular message
						bufferedWriter.write(username + ": " + messageToSend);
						bufferedWriter.newLine();
						bufferedWriter.flush();
					}
				}
			}
		} catch (IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}
	/**
     * Listens for messages from the server
     */
	public void listenForMessage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String msgFromGroupChat;
				while (socket.isConnected()) {
					try {
						// Read a message from the server
						msgFromGroupChat = bufferedReader.readLine();
						message = msgFromGroupChat;
						
						System.out.println(msgFromGroupChat);
					} catch (IOException e) {
						closeEverything(socket, bufferedReader, bufferedWriter);
					}
				}
			}
		}).start();
	}
	public static String getmessage() {
		
		return message;
	}
	
	public static void DisplayMessage() {
		System.out.println("""
					Welcome to the chatting group!!
					
					This are the commands:
					
					!ol: To see the list of members.
					!pm: To pm existing user. 
						command pm need two parameter 
						pm [Username] [message] 
					!ex: exit the chat
					!he: to prompt this message again.
					 """);
	}
	/**
     * Closes the socket, BufferedReader, and BufferedWriter
     * @param socket The socket to close
     * @param bufferedReader The BufferedReader to close
     * @param bufferedWriter The BufferedWriter to close
     */
	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
	public static void main(String[] arg) throws UnknownHostException, IOException {

		
		try (Scanner scanner = new Scanner(System.in)) {
			DisplayMessage();
			System.out.println("Enter your username for the group chat: ");
			String username = scanner.nextLine();
			System.out.println("Enter your Ip Address ");
			String ip = scanner.nextLine();
			System.out.println("Enter your Port: ");
			int port = scanner.nextInt();
			Socket socket = new Socket("localhost", 2345);
			Client client = new Client(socket, username, ip, port);
			client.listenForMessage();
			client.sendMessage();
		}
		
		
		
	}
	public static void exit(int i) {
		System.exit(i);
		// TODO Auto-generated method stub
		
	}
}