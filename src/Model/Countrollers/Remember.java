package Model.Countrollers;

import java.io.Serializable;

/**
 * Remember contains the info of last user and port
 */
public class Remember implements Serializable {
    private String username;
    private String password;
    private Integer port;
    private String serverIP;
    

    /**
     * 
     * @param username of last user
     * @param password of last user
     * @param port     of last server
     */
    public Remember(String username, String password, int port, String serverIP) {
        this.username = username;
        this.password = password;
        this.port = port;
        this.serverIP = serverIP;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getServerIP() {
        return serverIP;
    }
}