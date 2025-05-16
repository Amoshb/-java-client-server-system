import java.util.*;

public class Coordinator {
	// Set of all connected members
    private Set<ClientHandler> members = new HashSet<>();
    
 // The current coordinator
    private ClientHandler coordinator;

    public Coordinator() {
    }
    
    /**
     * Add a new member to the set of members
     * If there is no coordinator yet, make this member the coordinator
     * @param member ClientHandler to adds
     */
    public void addMember(ClientHandler member) {
    	if (!members.contains(member)) {
    		members.add(member);
    	
        
        if (coordinator == null) {
            coordinator = member;
            System.out.println("New coordinator: " + member.getClientUsername());
            
        } else {
            System.out.println("New member: " + member.getClientUsername());
        }
        }
    }
    
    /**
     * Change the current coordinator to the given new coordinator
     * @param newCoordinator New coordinator
     */
    public void setCoordinator(ClientHandler newCoordinator) {
        if (newCoordinator != coordinator) {
            coordinator = newCoordinator;
            System.out.println("Coordinator changed: " + coordinator.getClientUsername());
        }
    }
    
    
    /**
     * Remove a member from the set of members
     * If the removed member was the coordinator, find a new coordinator
     * @param member ClientHandler to remove
     */
    public void removeMember(ClientHandler member) {
    	if (members.contains(member)){
        members.remove(member);
        if (member == coordinator) {
        	System.out.println("Coordinator left: " + member.getClientUsername());
            if (!members.isEmpty()) {
            	setCoordinator(getNewCoordinator());
                //System.out.println("New coordinator: " + coordinator.getClientUsername());
            } else {
                coordinator = null;
                
            }
        } else {
            System.out.println("Member left: " + member.getClientUsername());
        }}
    }

    
    /**
     * Find a new coordinator based on the current set of members
     * @return New coordinator
     */
    private ClientHandler getNewCoordinator() {
        // Find any member who is connected to the server
        for (ClientHandler member : members) {
            if (member.getClientUsername() != null) {
                return member;
            }
        }

        // If no member is found, return the first member in the set
        return members.iterator().next();
    }

    
    /**
     * Broadcast a message to all members, except the sender
     * @param message Message to send
     * @param sender ClientHandler of the sender
     */
    public void broadcastMessage(String message, ClientHandler sender) {
        if (coordinator == sender) {
            System.out.println("[" + coordinator.getClientUsername() + "]: " + message);
        }

        for (ClientHandler member : members) {
            if (member != sender) {
                member.sendMessage(message);
            }
        }
    }
    
    /**
     * Get the name of the current coordinator
     * @return Coordinator name or null if there is no coordinator
     */
    public String getcoordinatorName() {
    	if (this.coordinator == null) {
    		
    	}else {
    	return coordinator.getClientUsername();
    	}
		return null;
    }
    

    /**
     * Get the set of all connected members
     * @return Set of ClientHandlers
     */
    public Set<ClientHandler> getallmembers() {
    	return this.members;
    }
    
    
    /**
     * Send a private message from the sender to the recipient
     * @param recipient Recipient's username
     * @param message Message to send
     * @param sender ClientHandler of the sender
     */
    public void sendPrivateMessage(String recipient, String message, ClientHandler sender) {
        ClientHandler recipientHandler = null;
        for (ClientHandler member : members) {
            if (member.getClientUsername().equals(recipient)) {
                recipientHandler = member;
                break;
            }
        }

        if (recipientHandler != null) {
            recipientHandler.sendMessage("PRIVATE: " + sender.getClientUsername() + "> " + message);
            sender.sendMessage("TO: " + recipient + " - PRIVATE: " + message);
        } else {
            System.out.println("ERROR: User " + recipient + " not found");
            sender.sendMessage("ERROR: User " + recipient + " not found");
        }
    }
}