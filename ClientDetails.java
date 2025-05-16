
public class ClientDetails {
    private final int id;
    private final String ipAddress;
    private final int port;
    private final String userName;

    public ClientDetails(int id, String ipAddress, int port, String userName) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        this.userName = userName;
    }

    // Getters
    public int getId() { return id; }
    public String getIpAddress() { return ipAddress; }
    public int getPort() { return port; }
    public String getUserName() { return userName; }
}
