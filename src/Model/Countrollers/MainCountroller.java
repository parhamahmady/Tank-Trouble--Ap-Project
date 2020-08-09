package Model.Countrollers;

import Model.FIO.*;
import Model.GameLogic.GameSettings;
import Model.GameLogic.PlayerTypes.*;
import Model.Network.Client;
import Model.GameLogic.*;
import Model.User.*;
import Model.MapComponents.*;
import View.GamePages.MultiPlayerPanel;
import View.GamePages.SettingMenuPanel;
import View.GamePages.SinglePlayerPanel;
import View.LogInPage.LoginPage;
import View.GameFrame.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This is the main countroller uses for user and game general settings
 */
public class MainCountroller {
    private Loader loader;// uses when want to read from file;
    private Saver saver;// uses when want to save to file
    private User user;// the logged in user
    private Client client;// socket that connect user to server
    private LoginPage loginPage;// page with options for user to login
    private SettingMenuPanel settingMenuPanel;
    private GameSettings gameSettings;
    private PlayerSettings playerSettings;
    private GameFrame gameFrame;

    public MainCountroller() {

    }

    /**
     * Counstructor
     *
     * @param loginPage will controlled
     */
    public MainCountroller(LoginPage loginPage) {
        this.loginPage = loginPage;

        loader = new Loader("Files");
        saver = new Saver("Files");
        gameSettings = new GameSettings(1, 30, 30, 30);

        try {/// load Saved user
            loginPage.getFrame().setVisible(false);
            Remember remember = (Remember) loader.loadObject("../Files/Remember/user/");
            System.out.println(remember.getPort());
            System.out.println(remember.getServerIP().substring(1));
            makeSocket(remember.getPort(), remember.getServerIP());
            signIn(remember.getUsername(), remember.getPassword(), true);// sign in the
            System.out.println("Remembered");
            loginPage.getFrame().setVisible(false);
            loginPage.getFrame().dispose();
            gameFrame = new GameFrame(this);

            // saved user
        } catch (Exception e) {
            loginPage = new LoginPage();
            loginPage.setController(this);
            loginPage.getFrame().setVisible(true);
            System.out.println("Nothing to load");

            try {
                if (client == null) {
                    return;
                }
                client.getSocket().close();

            } catch (Exception e2) {
                e2.printStackTrace();
            }
            client = null;
        }

    }

    /**
     * SignUp
     * 
     * @param user
     * @param pass
     * @param remember if the user wanted to save last user
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean signUp(String user, String pass, Boolean remember) throws IOException, ClassNotFoundException {
        this.user = client.signUp(user, pass);
        if (this.user == null) {
            System.out.println("cant make user");
            return false;
        }
        if (remember) {// if should save
            Remember saveLogInUser = new Remember(user, pass, client.getSocket().getPort(),
                    client.getSocket().getLocalAddress().toString().substring(1));// get ready to save
            saveSignedIn(saveLogInUser);// save

        } else {
            saver.cleanUp("../Files/Remember/user/");// clean last saved user
        }
        return true;
    }

    /**
     * Sign in
     * 
     * @param user
     * @param pass
     * @param remmember if the user wanted to save last user
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean signIn(String user, String pass, Boolean remember) throws IOException, ClassNotFoundException {
        this.user = client.signIn(user, pass);
        if (this.user == null) {
            // JOptionPane.showMessageDialog(gameFrame, "Info Not Valid", "!!!!",
            // JOptionPane.ERROR_MESSAGE);
            System.out.println("info not valid");
            return false;
        }
        if (remember) {// if should save
            Remember saveLogInUser = new Remember(user, pass, client.getSocket().getPort(),
                    client.getSocket().getLocalAddress().toString().substring(1));// get ready to save
            saveSignedIn(saveLogInUser);// save

        } else {
            saver.cleanUp("../Files/Remember/user/");// clean last saved user
        }
        return true;

    }

    /**
     * SignOut the loggedIn user
     * 
     * @throws IOException
     */
    public void signOut(boolean delete, boolean showLoginPage) throws IOException {
        if (!client.signOut(user.getUserName())) {
            JOptionPane.showMessageDialog(gameFrame, "Cant SignOut", "!!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (delete) {
            saver.cleanUp("../Files/Remember/user/");
        }
        user = null;
        // gameFrame.getFrame().setVisible(false);
        client.getSocket().close();
        client = null;
        if (showLoginPage) {
            loginPage.getFrame().setVisible(true);
            if (gameFrame != null) {
                gameFrame.dispose();
            }
        } else {
            if (loginPage != null) {
                loginPage.getFrame().dispose();

            }
        }

    }

    /**
     * Make ready to get in to the Groove !
     * 
     * @param port of server
     * @throws IOException if coudnt connect to server
     */
    public void makeSocket(int port, String ip) throws IOException {
        client = new Client(port, ip);
    }

    /**
     * Save signedIn user
     * 
     * @throws IOException
     */
    private void saveSignedIn(Remember remember) throws IOException {
        File rememberFolder = new File("../Files/Remember");
        if (!rememberFolder.exists()) {// check the folder existance
            rememberFolder.mkdir();
        }
        saver.saveObject(remember, "../Files/Remember/user/");
    }

    /**
     * For submit button in LogIn Page
     * 
     * @param username
     * @param passWord
     * @param port
     * @param remember
     */
    public void submit(String username, String passWord, String port, String ip, boolean remember,
            boolean showMainMenu) {
        try {
            if (port.isEmpty()) {
                return;
            }
            makeSocket(Integer.parseInt(port), ip);

            if (signIn(username, passWord, remember)) {// check if could sign In
                System.out.println("SignedIn");
                if (showMainMenu) {
                    loginPage.getFrame().setVisible(false);
                    gameFrame = new GameFrame(this);
                }
                if (loginPage != null) {
                    loginPage.getFrame().dispose();// close the login page
                }
                return;
            }
            if (signUp(username, passWord, remember)) {
                System.out.println("SignedUp");
                if (showMainMenu) {
                    loginPage.getFrame().setVisible(false);
                    gameFrame = new GameFrame(this);
                }
                if (loginPage != null) {
                    loginPage.getFrame().dispose();// close the login page
                }
                return;
            }
            client.getSocket().close();
            client = null;
            System.out.println("Cant logIn");
            JOptionPane.showMessageDialog(gameFrame, "Cant Get In to the Groove", "!!!!", JOptionPane.ERROR_MESSAGE);
            if (gameFrame != null) {
                gameFrame.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gameFrame, "Error", "!!!!", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * The Setting Menu after Log in
     */
    public void settingMenu() {
        settingMenuPanel = new SettingMenuPanel(gameFrame.getImages(), gameFrame, this);

        changePage(gameFrame, gameFrame.getMainMenuPanel(), settingMenuPanel);

    }

    /**
     * Show SIngle Player Menu
     */
    public void singleGamePanel() {
        SinglePlayerPanel singlePlayerPanel = new SinglePlayerPanel(this, gameFrame, gameFrame.getImages());
        changePage(gameFrame, gameFrame.getMainMenuPanel(), singlePlayerPanel);
    }

    /**
     * Show MultiPlayer Panel
     */
    public void multiGamePanel() {
        MultiPlayerPanel multiPlayerPanel = new MultiPlayerPanel(gameFrame, this, gameFrame.getImages());
        changePage(gameFrame, gameFrame.getMainMenuPanel(), multiPlayerPanel);
    }

    /**
     * Replace panel2 on panel 1 at frame
     * 
     * @param frame
     * @param panel1
     * @param panel2
     */
    public void changePage(JFrame frame, JPanel panel1, JPanel panel2) {
        frame.remove(panel1);
        frame.add(panel2);
        frame.repaint();
        frame.revalidate();
    }

    /**
     * Make a game at Server
     * 
     * @return true if server made the game
     * @throws IOException
     */
    public boolean makeMultiPlayerGame() throws IOException {
        boolean canMake = client.makeGame(gameSettings);
        if (canMake) {
            System.out.println("Made");
            if (loginPage != null) {
                loginPage.getFrame().dispose();// close the login page
            }
            joinMultiPlayerGame(gameSettings.getGameName());
        }
        return canMake;
    }

    /**
     * Join a MultiPlayerGame at server
     * 
     * @param gameName is the name of the game we want to join
     */
    public void joinMultiPlayerGame(String gameName) {
        try {
            Player player = client.joinGame(playerSettings, gameName, this);
            if (player == null) {// check if coudlnt join
                JOptionPane.showMessageDialog(gameFrame, "Cant Join", "!!!!", JOptionPane.ERROR_MESSAGE);
                gameFrame.add(gameFrame.getMainMenuPanel());
                gameFrame.repaint();
                return;
            }
            System.out.println("Joined");
            if (loginPage != null) {
                loginPage.getFrame().dispose();// close the login page
            }
            OnlineGameLoop onlineGameLoop = new OnlineGameLoop(player, gameFrame, this, client);
            ThreadPool.execute(onlineGameLoop);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gameFrame, "Cant Join", "!!!!", JOptionPane.ERROR_MESSAGE);
            gameFrame.add(gameFrame.getMainMenuPanel());
            gameFrame.repaint();
        }

    }

    /**
     * Make a game based on Saved Settings This is an Offline Game Between Pc and
     * the Player
     */
    public void startSinglePlayerGame() {
        loginPage.getFrame().dispose();// close the login page

        ArrayList<Player> players = new ArrayList<Player>(2);
        Player player1 = new HumanPlayer(user.getUserName(), gameSettings, playerSettings);// make human player
        players.add(player1);
        HumanPlayer p1 = (HumanPlayer) player1;
        gameFrame.addKeyListener(p1.getKeyActions());// add key listener

        ComputerPlayer pc = new ComputerPlayer("Pc", gameSettings, new PlayerSettings("tank_dark", KeyEvent.VK_ESCAPE));// make
                                                                                                                        // ComputerPlayer
        pc.setTeamNumber(-200);
        players.add(pc);

        OfflineGameLoop gameLoop = new OfflineGameLoop(40, gameFrame, gameSettings, this);

        MapFileLoader mapFileLoader = new MapFileLoader(randomMapName());
        mapFileLoader.loadMap(gameFrame, gameSettings);
        gameLoop.loadTheGame(players, mapFileLoader.getLoadedMap());

        ThreadPool.init();
        ThreadPool.execute(gameLoop);

    }

    /**
     * Make a Map name Randomly
     * 
     * @param gameFrame
     * @param gameSettings
     * @return
     */
    public String randomMapName() {
        int numberOfMaps = 0;

        try {
            Path path = Paths.get("./View/MapFiles/");
            DirectoryStream<Path> files = Files.newDirectoryStream(path);// load the mapFiles folder
            for (Path path2 : files) {// find the number of maps
                path2.getNameCount();
                numberOfMaps++;
            }

            // return mapFileLoader.getLoadedMap();
        } catch (Exception e) {
            System.out.println("CantLoad map");
        }
        int temp = (int) Math.abs(Math.random() * (numberOfMaps + 1 - 2 + 1) + 1);// make a random number
        return (Integer.toString(temp) + ".txt");

    }

    /**
     * Check the result of the Survivel game to add to player's win or lose Vs pc
     * 
     * @param gameState
     */
    public void endSinglePlayerSGame(GameState gameState, double gameTime) {
        gameFrame.removeAll();
        gameFrame.dispose();// close the game frame
        gameFrame = new GameFrame(this);

        String name = winnerNameFinder(gameState);
        if (name.isBlank()) {// the draw situation
            user.setHourPlayed(user.getHourPlayed() + gameTime);
            return;
        }
        try {
            if (name.equals(user.getUserName())) {
                user.addWinAndLost(0);
                user.setHourPlayed(user.getHourPlayed() + gameTime);
                client.addWinOrLose(name, 0, gameTime);
            } else {
                user.addWinAndLost(1);
                user.setHourPlayed(user.getHourPlayed() + gameTime);
                client.addWinOrLose(user.getUserName(), 1, gameTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void endSinglePlayerLGame(int pcWin, int HumanWin, double gameTime) {
        gameFrame.removeAll();
        gameFrame.dispose();// close the game frame
        gameFrame = new GameFrame(this);

        if (pcWin == HumanWin) {// draw
            user.setHourPlayed(user.getHourPlayed() + gameTime);
            return;
        }
        try {
            if (HumanWin > pcWin) {
                user.addWinAndLost(0);
                user.setHourPlayed(user.getHourPlayed() + gameTime);
                client.addWinOrLose(user.getUserName(), 0, gameTime);
            } else {
                user.addWinAndLost(1);
                user.setHourPlayed(user.getHourPlayed() + gameTime);
                client.addWinOrLose(user.getUserName(), 1, gameTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Find the winner of the game
     * 
     * @param gameState
     * @return
     */
    public String winnerNameFinder(GameState gameState) {
        String name = "";
        Iterator<Player> it = gameState.getPlayers().iterator();
        while (it.hasNext()) {
            Player temp = it.next();
            if (temp.getMyTank().getHealth() <= 0) {
                continue;
            }
            name += temp.getName();
        }
        return name;
    }

    /**
     * 
     * @return servers
     * @throws FileNotFoundException
     */
    public HashMap<String, HashMap<String, Integer>> loadServerList() throws FileNotFoundException {
        if (loader == null) {
            loader = new Loader("Files");
        }
        return loader.loadServerList();
    }

    /**
     * 
     * @param servers
     * @return servers Name
     */
    public String[] findServernames(HashMap<String, HashMap<String, Integer>> servers) {
        String[] sName = new String[servers.size()];
        Set<String> set = servers.keySet();
        int i = 0;
        for (String string : set) {
            sName[i] = string;
            i++;
        }
        return sName;
    }

    public User getUser() {
        return user;
    }

    public GameFrame getgameFrame() {
        return gameFrame;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public void setPlayerSettings(PlayerSettings playerSettings) {
        this.playerSettings = playerSettings;
    }

    public PlayerSettings getPlayerSettings() {
        return playerSettings;
    }

    public Client getClient() {
        return client;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }
}
