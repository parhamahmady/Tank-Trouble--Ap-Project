package Model.Network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

import Model.FIO.Loader;
import Model.FIO.Saver;
import Model.GameLogic.ServerGameLoop;
import Model.GameLogic.ThreadPool;
import Model.User.User;

/**
 * The server that contains Games and users Then manage them
 */
public class Server {

    private ServerSocket welcomingSocket;
    private int port; // seprate servers
    private HashMap<String, User> signedUsers;
    private Loader loader;// for load from files
    private Saver saver;// to save to files
    private HashSet<String> onlineUsers;// users that are online at server
    private HashSet<String> availableGames;// games that users can join
    private HashMap<String, ServerGameLoop> runningGames;// game that are run;

    /**
     * The Counstructor
     * 
     * @param port of the server
     */
    public Server(int port) {
        this.port = port;// set the port
        loader = new Loader(Integer.toString(port) + "--Server");// make loader
        saver = new Saver(Integer.toString(port) + "--Server");// make saver

        onlineUsers = new HashSet<String>();
        runningGames = new HashMap<String, ServerGameLoop>();
        availableGames = new HashSet<String>();
        ThreadPool.init();
        try {

            signedUsers = loader.loadSavedUsers();// load saved Users
        } catch (Exception e) {
            System.out.println("Cant load saved users");
            signedUsers = new HashMap<String, User>();
        }

        try {
            ServerSocket welcomingSocket = new ServerSocket(port);// new the Server
            for (int i = 0; i > -1; i++) {
                Socket connectionSocket = welcomingSocket.accept();
                ServerAction temp = new ServerAction(connectionSocket, this);
                // serverActionsPool.execute(temp);
                ThreadPool.execute(temp);
            }
            welcomingSocket.close();
        } catch (Exception e) {
            System.out.println("Cant make Server" + port);
        }
    }

    public synchronized HashSet<String> getOnlineUsers() {
        return onlineUsers;
    }

    public synchronized Saver getSaver() {
        return saver;
    }

    public synchronized int getPort() {
        return port;
    }

    public synchronized HashMap<String, User> getSignedUsers() {
        return signedUsers;
    }

    public synchronized HashMap<String, ServerGameLoop> getRunningGames() {
        return runningGames;
    }

    public synchronized HashSet<String> getAvailableGames() {
        return availableGames;
    }

    /**
     * Close the serverSocket
     */
    public void shutDownServer() {
        try {
            welcomingSocket.close();

        } catch (Exception e) {
            System.out.println("Cant shutdown");
        }
    }

    /**
     * Add user to singed users
     * 
     * @param username
     * @param password
     */
    public synchronized User addUser(String userName, String password) {
        User tempUser = new User(userName, password);// make user
        signedUsers.put(userName, tempUser);// add
        try {
            saver.saveUser(tempUser);// save to Server files

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempUser;
    }

    /**
     * Add win or lose to the Signed User
     * 
     * @param userName
     * @param index
     */
    public synchronized void addWinOrLose(String userName, int index, double duration) {
        signedUsers.get(userName).addWinAndLost(index);
        signedUsers.get(userName).setHourPlayed(signedUsers.get(userName).getHourPlayed() + duration);
        try {
            saver.saveUser(signedUsers.get(userName));// save the changes

            signedUsers = loader.loadSavedUsers();// load saved Users

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}