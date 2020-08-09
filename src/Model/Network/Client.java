package Model.Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;

import Model.Countrollers.MainCountroller;
import Model.GameLogic.GameSettings;
import Model.GameLogic.PlayerTypes.HumanPlayer;
import Model.GameLogic.PlayerTypes.Player;
import Model.GameLogic.PlayerTypes.PlayerSettings;
import Model.User.User;

public class Client {
    private Socket socket;
    private User user;// user that use this socket
    private InputStream in;
    private OutputStream out;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    /**
     * Counstructor
     * 
     * @param serverPort
     */
    public Client(int serverPort, String ip) throws IOException {
        socket = new Socket(ip, serverPort);
        in = socket.getInputStream();
        out = socket.getOutputStream();

    }

    /**
     * Sign in to server
     * 
     * @param username
     * @param password
     * @throws IOException
     */
    public User signIn(String username, String password) throws IOException, ClassNotFoundException {
        String[] message = new String[3];// make message
        byte[] buffer = new byte[2048];
        message[0] = "1";// the sign in code (for Server)
        message[1] = username;
        message[2] = password;
        for (String string : message) {
            out.write(string.getBytes());// send message
            int read = in.read(buffer);
            String awnser = new String(buffer, 0, read);
            if (awnser.equals("0")) {// check the awnser

                return null;
            }
        }
        ois = new ObjectInputStream(socket.getInputStream());
        user = (User) ois.readObject();// get the final user

        return user;
    }

    /**
     * sign up a user with input info at server
     * 
     * @param username
     * @param password
     * @return the added user
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public User signUp(String username, String password) throws IOException, ClassNotFoundException {
        String[] message = new String[3];// make message
        byte[] buffer = new byte[2048];
        message[0] = "2";// signUp code for server
        message[1] = username;
        message[2] = password;

        for (String string : message) {
            out.write(string.getBytes());// send message
            int read = in.read(buffer);
            String awnser = new String(buffer, 0, read);
            // System.out.println(awnser);
            if (awnser.equals("0")) {// check the awnser
                // System.out.println("already exists");
                return null;
            }
        }
        ois = new ObjectInputStream(socket.getInputStream());
        user = (User) ois.readObject();// get the final user

        return user;
    }

    public boolean signOut(String username) throws IOException {
        String[] message = new String[2];// make message
        byte[] buffer = new byte[2048];
        message[0] = "3";// signOut code for server
        message[1] = username;

        for (String string : message) {
            out.write(string.getBytes());// send message
            int read = in.read(buffer);
            String awnser = new String(buffer, 0, read);
            if (awnser.equals("0")) {// check the awnser
                return false;// if couldnt signOut
            }
        }
        user = null;
        return true;
    }

    /**
     * 
     * @return socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Tell the server to add win or lose to the user
     */
    public void addWinOrLose(String username, int index, double gameTime) throws IOException {
        String[] message = new String[4];// make message
        byte[] buffer = new byte[2048];
        message[0] = "4";// signOut code for server
        message[1] = username;
        message[2] = Integer.toString(index);
        message[3] = Double.toString(gameTime);
        for (String string : message) {
            out.write(string.getBytes());// send message
            int read = in.read(buffer);
            String awnser = new String(buffer, 0, read);
            if (awnser.equals("0")) {// check the awnser
                return;// if couldnt signOut
            }
        }

        return;
    }

    /**
     * Make a game at server
     * 
     * @param gameSettings
     * @return true if server made a game
     * @throws IOException
     */
    public boolean makeGame(GameSettings gameSettings) throws IOException {
        String[] message = new String[2];// make message
        byte[] buffer = new byte[2048];
        message[0] = "5";// makeGame code for server
        message[1] = gameSettings.getGameName();
        for (String string : message) {
            out.write(string.getBytes());// send message
            int read = in.read(buffer);
            String awnser = new String(buffer, 0, read);
            if (awnser.equals("0")) {// check the awnser
                return false;// if couldnt make
            }
        }
        if (oos == null) {
            oos = new ObjectOutputStream(socket.getOutputStream());// send game settings
        }
        oos.writeObject(gameSettings);
        return true;
    }

    /**
     * Join User to the Game
     * 
     * @param playerSettings
     * @param gameName       name of the Game
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Player joinGame(PlayerSettings playerSettings, String gameName, MainCountroller mainCountroller)
            throws IOException, ClassNotFoundException {
        String[] message = new String[2];// make message
        byte[] buffer = new byte[2048];
        message[0] = "6";// joinGame code for server
        message[1] = gameName;
        int i = 0;
        for (String string : message) {
            out.write(string.getBytes());// send message
            if (i > 0) {
                continue;
            }
            int read = in.read(buffer);
            String awnser = new String(buffer, 0, read);

            if (awnser.equals("0")) {// check the awnser
                return null;// if couldnt join
            }
            i++;
        }

        if (ois == null) {
            ois = new ObjectInputStream(socket.getInputStream());
        }
        GameSettings gameSettings = (GameSettings) ois.readObject();// get gameSettings
        mainCountroller.setGameSettings(gameSettings);
        if (gameSettings.getTeamOrSolo() == 2) {// was solo
            playerSettings.setTeamNumber(0);
        }
        Player player = new HumanPlayer(user.getUserName(), gameSettings, playerSettings);// make player

        if (oos == null) {
            oos = new ObjectOutputStream(socket.getOutputStream());// Send Player
        }
        oos.writeObject(player);

        return player;

    }

    /**
     * 
     * @return Available Games
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public HashSet<String> getAvailableGames() throws IOException, ClassNotFoundException {
        out.write("7".getBytes());
        HashSet<String> games = (HashSet<String>) ois.readObject();
        return games;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public OutputStream getOut() {
        return out;
    }

    public InputStream getIn() {
        return in;
    }
}