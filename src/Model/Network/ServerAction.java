package Model.Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import Model.GameLogic.GameSettings;
import Model.GameLogic.ServerGameLoop;

import Model.GameLogic.PlayerTypes.Player;
import Model.User.*;

/**
 * Manage Server Actions send and recive requests
 */
public class ServerAction implements Runnable {
    private Socket connection;
    private InputStream in;
    private OutputStream out;
    private Server server;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    /*
     * 
     * @param connection with client
     * 
     * @param server that getting managed
     */
    public ServerAction(Socket connection, Server server) {
        this.connection = connection;
        this.server = server;
        try {
            in = connection.getInputStream();// get connection IO
            out = connection.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * manage requests from client
     * 
     * commands indexes : 0 exit ; 1 signIn ; 2 signUp ; 3 signOut
     * 
     */
    @Override
    public void run() {

        try {
            do {

                byte[] buffer = new byte[2048];
                int read = in.read(buffer);
                String command = new String(buffer, 0, read);
                switch (command) {
                    case "0":
                        return;
                    case "1":
                        out.write("5".getBytes());// tells client im ready
                        System.out.println("LogedIn");
                        out.flush();
                        signIn();
                        return;
                    case "2":
                        out.write("5".getBytes());
                        System.out.println("Signed Up");
                        out.flush();
                        signUp();
                        return;
                    case "3":
                        out.write("5".getBytes());
                        System.out.println("Logged Out");
                        out.flush();
                        signOut();
                        return;
                    case "4":
                        out.write("5".getBytes());
                        System.out.println("AddWinOrLost");
                        out.flush();
                        addWinOrLost();
                        return;
                    case "5":
                        out.write("5".getBytes());
                        System.out.println("MakeGame");
                        makeGame();
                        out.flush();
                        return;
                    case "6":
                        out.write("5".getBytes());
                        System.out.println("JoinGame");
                        out.flush();
                        joinGame();
                        return;
                    case "7":
                        System.out.println("Get Available Games");
                        getAvailableGames();
                        out.flush();
                        break;
                    default:
                        System.out.println("333");
                        break;

                }
            } while (true);
        } catch (Exception e) {
            try {
                out.write("0".getBytes());

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    /**
     * Get user and pass from client check them send back the user
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    private void signIn() throws IOException, InterruptedException {

        byte[] buffer = new byte[2048];// made buffer
        int read = in.read(buffer);
        String username = new String(buffer, 0, read);// get the username

        if (!server.getSignedUsers().containsKey(username)) {// check the existance
            out.write("0".getBytes());// tell client abort the mission !
            out.flush();
            run();
            return;
        }

        if (server.getOnlineUsers().contains(username)) {// check if the user was online before

            out.write("0".getBytes());// tell client abort the mission !
            out.flush();
            run();
            return;
        }

        out.write("password?".getBytes());// request password
        read = in.read(buffer);// get password
        String password = new String(buffer, 0, read);
        if (!server.getSignedUsers().get(username).getPassword().equals(password)) {// check the password
            out.write("0".getBytes());// tell client abort the mission !
            out.flush();
            run();
            return;
        }
        out.write("oK".getBytes());

        User addeduser = server.getSignedUsers().get(username);// find user
        server.getOnlineUsers().add(addeduser.getUserName());// add to online users

        oos = new ObjectOutputStream(connection.getOutputStream());

        oos.writeObject(addeduser);// send it to client
        oos.flush();

        run();
    }

    /**
     * will signUp user in the server
     */
    private void signUp() throws InterruptedException, IOException {

        byte[] buffer = new byte[2048];// made buffer
        int read = in.read(buffer);
        String username = new String(buffer, 0, read);// get the username
        if (server.getSignedUsers().containsKey(username)) {// check the existance
            out.write("0".getBytes());
            out.flush();
            run();
            return;
        }
        out.write("password?".getBytes());// request password
        read = in.read(buffer);// get password
        String password = new String(buffer, 0, read);
        out.write("OK".getBytes());

        User addeduser = server.addUser(username, password);// add user
        oos = new ObjectOutputStream(connection.getOutputStream());

        server.getOnlineUsers().add(addeduser.getUserName());// add to online users
        oos.writeObject(addeduser);// send it to client
        oos.flush();

        run();
    }

    /**
     * Sign out requested user
     * 
     * @throws IOException
     */
    private void signOut() throws IOException {
        byte[] buffer = new byte[2048];// made buffer
        int read = in.read(buffer);
        String username = new String(buffer, 0, read);// get the username

        if (!server.getOnlineUsers().contains(username)) {// check the online user
            out.write("0".getBytes());
            run();
            return;
        }
        server.getOnlineUsers().remove(username);// remove from onlines
        out.write("OK".getBytes());// tell client
        out.flush();
        run();

    }

    private void addWinOrLost() throws IOException {
        byte[] buffer = new byte[2048];// made buffer
        int read = in.read(buffer);

        String username = new String(buffer, 0, read);// get the username

        if (!server.getOnlineUsers().contains(username)) {// check the online user
            out.write("0".getBytes());
            run();
            return;
        }
        out.write("index?".getBytes());// request the type of win or lost
        read = in.read(buffer);
        String index = new String(buffer, 0, read);

        out.write("time?".getBytes());// request duration of game
        read = in.read(buffer);
        String duration = new String(buffer, 0, read);

        server.addWinOrLose(username, Integer.parseInt(index), Double.parseDouble(duration));
        out.write("OK".getBytes());// tell client
        out.flush();
        run();

    }

    /**
     * Get a name and game Settings from client then make a game
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void makeGame() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[2048];// made buffer
        int read = in.read(buffer);
        String gameName = new String(buffer, 0, read);// get the gameName

        if (server.getRunningGames().containsKey(gameName)) {// check the name
            out.write("0".getBytes());
            out.flush();
            run();
            return;
        }

        out.write("OK".getBytes());// tell client
        out.flush();

        ois = new ObjectInputStream(connection.getInputStream());

        GameSettings gameSettings = (GameSettings) ois.readObject();
        server.getAvailableGames().add(gameName);// add to available games

        ServerGameLoop serverGameLoop = new ServerGameLoop(60, gameSettings, server);
        server.getRunningGames().put(gameName, serverGameLoop);// add to the runningGames

        run();
    }

    private void joinGame() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[2048];// made buffer
        int read = in.read(buffer);
        String gameName = new String(buffer, 0, read);// get the gameName

        if (!server.getAvailableGames().contains(gameName)) {// check the name
            out.write("0".getBytes());
            out.flush();
            run();
            return;
        }

        oos.writeObject(server.getRunningGames().get(gameName).getGameSettings());// send Setting

        if (ois == null) {
            ois = new ObjectInputStream(connection.getInputStream());
        }

        Player player = (Player) ois.readObject();// get player

        server.getRunningGames().get(gameName).addPlayer(player, this);// add to game
    }

    /**
     * Send Available Games For Client
     * 
     * @throws IOException
     */
    private void getAvailableGames() throws IOException {
        oos.writeObject(server.getAvailableGames());
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }
}