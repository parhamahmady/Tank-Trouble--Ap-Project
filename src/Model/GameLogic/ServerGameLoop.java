package Model.GameLogic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import Model.Countrollers.MainCountroller;
import Model.Elements.HealthBooster;
import Model.Elements.Laser;
import Model.Elements.PowerBooster;
import Model.Elements.Shield;
import Model.Elements.Upgrade;
import Model.Elements.UpgradeManager;
import Model.GameLogic.PlayerTypes.HumanPlayer;
import Model.GameLogic.PlayerTypes.Player;
import Model.MapComponents.DestructibleWall;
import Model.MapComponents.Map;
import Model.MapComponents.MapFileLoader;
import Model.Network.Message;
import Model.Network.Server;
import Model.Network.ServerAction;

/**
 * Game loop for OnlineGames
 */
public class ServerGameLoop extends GameLoop implements Runnable {
    private Map map;
    private MainCountroller mainCountroller;
    private HashMap<String, ServerAction> users;
    private Server server;
    private JFrame gFrame;
    private double time;
    /**
     * The Counstructor
     * 
     * @param fps
     * @param gameSettings
     * @param mainCountroller
     */
    public ServerGameLoop(int fps, GameSettings gameSettings, Server server) {
        super(fps, gameSettings);
        this.server = server;
        mainCountroller = new MainCountroller();

        users = new HashMap<String, ServerAction>();

        MapFileLoader mapFileLoader = new MapFileLoader(mainCountroller.randomMapName());// load map randomly
        gFrame = new JFrame();
        gFrame.setSize(1366, 741);// seet for 15 inch monitor
        mapFileLoader.loadMap(gFrame, gameSettings);// makeMap
        map = mapFileLoader.getLoadedMap();
    }

    /**
     * Add Player to the Game
     * 
     * @param player
     * @param socket
     */
    public void addPlayer(Player player, ServerAction serverAction) {
        players.add(player);

        users.put(player.getName(), serverAction);
        loadGame(true);
    }

    /**
     * Load the Game and incase start the game
     */
    private void loadGame(boolean sendMessage) {
        gameState = new GameState(players, map, gameSettings.getGameName(), gameSettings);
        System.out.println("Hello");
        if (sendMessage) {
            Message message = new Message(1, gameState);// 1 means just updateYour Frame
            massenger(message);
        }
        if (gameSettings.getMinPlayer() == gameState.getPlayers().size()) {// checkthe number of players
            server.getAvailableGames().remove(gameSettings.getGameName());
            ThreadPool.init();
            ThreadPool.execute(this);
        }

    }

    /**
     * Send Messages to all clients and recive their awnsers
     * 
     * @param massage will send to clients
     */
    private ArrayList<Player> massenger(Message massage) {
        Iterator<Player> it = players.iterator();
        ArrayList<Player> newPlayerStates = new ArrayList<Player>();
        try {
            while (it.hasNext()) {
                Player player = it.next();
                ObjectOutputStream oos = users.get(player.getName()).getOos();// getting in and Out
                ObjectInputStream ois = users.get(player.getName()).getOis();

                oos.writeObject(massage);

                // System.out.println("1000A");

                Player awnser = messageAnalyzer((Message) ois.readObject());
                if (awnser != null) {
                    newPlayerStates.add(awnser);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newPlayerStates;
    }

    private Player messageAnalyzer(Message message) {
        if (message.getHeader() == 1) {
            System.out.println(message.getBody());
        }
        if (message.getHeader() == 2) {

            return (HumanPlayer) message.getBody();
        }
        return null;
    }

    @Override
    public void run() {

        startTime = System.currentTimeMillis();

        numberOfGames = gameSettings.getEndType() == 1 ? 1 : gameSettings.getNumberofLeagueGames();// set the number of
        // games
        boolean end = false;
        Message message;
        GameState gs = gameState;
        Map map2 = map;
        for (int i = 0; i < numberOfGames; i++) {
            System.out.println("Starts Game Number " + i);

            if (numberOfGames != 1) {
                gameState.setIsleague(true);
            }

            while (!end) {

                long start = System.currentTimeMillis();

                message = new Message(2, gs);// 2 means game is running and give me your States

                map2 = makeMapreadyToSend(); // Make State Readty to Send

                ArrayList<Player> playersStates = massenger(message);
                gs = new GameState(playersStates, map2, gameSettings.getGameName(), gameSettings.getMinPlayer(),
                        gameSettings);
                gs.setIsleague(gameState.isleague());
                gs.setUpgrades(makeUpgradeReadyToSend(gameState));
                gs.getUpgradeManager().setT1(gameState.getUpgradeManager().getT1());
                gs.getUpgradeManager().setT2(gameState.getUpgradeManager().getT2());
                UpgradeManager uPM = new UpgradeManager(gameSettings, map2, gs.getCollision());
                uPM.setT1(gameState.getUpgradeManager().getT1());
                uPM.setT2(gameState.getUpgradeManager().getT2());
                gs.setUpgradeManager(uPM);
                gs.setEnd(gameState.isEnd());

                System.out.println(gameState.getUpgrades().size());

                end = gs.updateState();

                gameState = gs;
                players = gs.getPlayers();
                map = map2;

                long delay = (1000 / fps) - (System.currentTimeMillis() - start);
                try {
                    if (delay > 0)
                        Thread.sleep(delay);
                } catch (Exception e) {
                    Message emergancy = new Message(100, gameState);
                    massenger(emergancy);
                    e.printStackTrace();
                }

            }
            message = new Message(1, gs);
            try {
                Thread.sleep((long) 2000);

            } catch (Exception e) {
                Message emergancy = new Message(100, gameState);
                massenger(emergancy);
            }
            if (numberOfGames != 1 && i != numberOfGames - 1) {
                resetGame();
                System.out.println("Reset");

                end = false;
            }
        }

        ////////////////////// Game End

        System.out.println("EndMatch");
        endTime = System.currentTimeMillis();
        time = ((TimeUnit.MILLISECONDS.toSeconds((long) endTime))
                - TimeUnit.MILLISECONDS.toSeconds((long) startTime)) ;

        try {

            playerWinCheck(false);
            sendResault(findWinnersNames(), gs);
            System.out.println("Winner Found End");

        } catch (Exception e) {
            Message emergancy = new Message(100, gameState);
            massenger(emergancy);
            e.printStackTrace();
        }
        server.getRunningGames().remove(gameSettings.getGameName());
    }

    /**
     * Reset the Game To start Again
     */
    private void resetGame() {
        playerWinCheck(true);
        MapFileLoader mapFileLoader = new MapFileLoader(mainCountroller.randomMapName());// reset map
        mapFileLoader.loadMap(gFrame, gameSettings);
        map = mapFileLoader.getLoadedMap();
        gameState = new GameState(players, map, gameSettings.getGameName(), gameSettings.getMinPlayer(), gameSettings);
        Iterator<Player> it = players.iterator();

        while (it.hasNext()) {// reset players
            Player p = it.next();
            System.out.println("Player Score " + p.getName() + " : " + p.getWinNumber());
        }
    }

    /**
     * Check If player Wins
     */
    private void playerWinCheck(boolean resetPlayer) {
        Iterator<Player> it = players.iterator();
        int winnerteam = 0;
        while (it.hasNext()) {// reset players
            Player p = it.next();
            if (p.getMyTank().getHealth() > 0) {// add win number
                System.out.println("Alive " + p.getName());
                p.setWinNumber(p.getWinNumber() + 1);
                if (gameSettings.getTeamOrSolo() == 1) {// if teamMode
                    winnerteam = p.getTeamNumber();
                }
            }
            if (gameSettings.getTeamOrSolo() != 1 && resetPlayer) { // if SoloMode
                System.out.println("SoloMode");
                p.getMyTank().setHealth(gameSettings.getTankMaxHealth());
            }
        }
        if (gameSettings.getTeamOrSolo() == 1) {
            it = players.iterator();
            while (it.hasNext()) {
                Player p = it.next();
                if (p.getMyTank().getHealth() <= 0 && p.getTeamNumber() == winnerteam) {// add win number
                    System.out.println("Winner Team " + p.getName());
                    p.setWinNumber(p.getWinNumber() + 1);

                }
                if (resetPlayer) {
                    p.getMyTank().setHealth(gameSettings.getTankMaxHealth());
                }

            }
        }

    }

    /**
     * Make Map Ready to Send
     * 
     * @return map
     */
    private Map makeMapreadyToSend() {
        Map map2 = new Map();// make Map ready to Send
        map2.setColumn(map.getColumn());
        map2.setRow(map.getRow());
        map2.setIndestructibleWalls(map.getIndestructibleWalls());
        map2.setRoads(map.getRoads());
        map2.setLength(map.getLength());
        map2.setWidth(map.getWidth());
        map2.setMapPattern(map.getMapPattern());
        ArrayList<DestructibleWall> wall = map.getDestructibleWalls();
        for (DestructibleWall dw : wall) {

            DestructibleWall wall2 = new DestructibleWall(dw.getX(), dw.getY(), dw.getLength(), dw.getWidth(),
                    dw.getHeath(), dw.getImageName());
            map2.getDestructibleWalls().add(wall2);

        }
        return map2;
    }

    private ArrayList<Upgrade> makeUpgradeReadyToSend(GameState gs) {
        ArrayList<Upgrade> upgrades = new ArrayList<Upgrade>();
        ArrayList<Upgrade> old = gs.getUpgrades();
        for (Upgrade upgrade : old) {
            if (upgrade instanceof HealthBooster) {
                HealthBooster temp = new HealthBooster(upgrade.getX(), upgrade.getY(), 20, 20, upgrade.getHeath(),
                        "upgrade", (int) upgrade.getMaxTime());
                temp.setTankOwner(upgrade.getTankOwner());
                upgrades.add(temp);
                continue;
            }
            if (upgrade instanceof PowerBooster) {
                PowerBooster t = (PowerBooster) upgrade;
                PowerBooster temp = new PowerBooster(upgrade.getX(), upgrade.getY(), 20, 20, upgrade.getHeath(),
                        "upgrade", (int) upgrade.getMaxTime(), t.getPowerBooster());
                temp.setTankOwner(upgrade.getTankOwner());

                upgrades.add(temp);
                continue;
            }
            if (upgrade instanceof Shield) {

                Shield temp = new Shield(upgrade.getX(), upgrade.getY(), 20, 20, upgrade.getHeath(), "upgrade",
                        (int) upgrade.getMaxTime());
                temp.setTankOwner(upgrade.getTankOwner());
                upgrades.add(temp);
                continue;
            }
            if (upgrade instanceof Laser) {

                Laser temp = new Laser(upgrade.getX(), upgrade.getY(), 20, 20, upgrade.getHeath(), "upgrade",
                        (int) upgrade.getMaxTime());
                temp.setTankOwner(upgrade.getTankOwner());
                upgrades.add(temp);
            }
        }
        return upgrades;
    }

    /**
     * Find All Winners Name
     * 
     * @return names Of Winners
     */
    private HashSet<String> findWinnersNames() {
        ArrayList<Integer> winNumbers = new ArrayList<Integer>();
        HashSet<String> winnersName = new HashSet<String>();
        Iterator<Player> it = players.iterator();

        for (int i = 0; i < players.size(); i++) {
            winNumbers.add(i);
        }

        int i = 0;
        System.out.println(players.size());
        System.out.println("ArraySize" + winNumbers.size());

        while (it.hasNext()) {// Iterate Thorugh all players
            Player p = it.next();

            winNumbers.set(i, p.getWinNumber());// add WinNumber
            i++;
        }

        Collections.sort(winNumbers);// sort winNumbers

        it = players.iterator();
        while (it.hasNext()) {// Iterate Thorugh all players

            Player p = it.next();
            if (p.getWinNumber() == winNumbers.get(winNumbers.size() - 1)) {
                winnersName.add(p.getName());
            }
        }
        return winnersName;
    }

    /**
     * Send all Players The Resault of the Game
     * 
     * @param winnersName
     * @param gameState
     * @throws IOException
     */
    private void sendResault(HashSet<String> winnersName, GameState gameState) throws IOException {
        Iterator<Player> it = players.iterator();
        Message message;
        while (it.hasNext()) {
            Player p = it.next();
            if (winnersName.contains(p.getName())) {
                System.out.println("Winner Player Score " + p.getName() + " : " + p.getWinNumber());

                message = new Message(3, gameState, 2, time/60);
            } else {
                System.out.println("Loser Player Score " + p.getName() + " : " + p.getWinNumber());
                message = new Message(3, gameState, 3, time/60);

            }
            ObjectOutputStream oos = users.get(p.getName()).getOos();
            oos.writeObject(message);
            oos.flush();

            ThreadPool.execute(users.get(p.getName()));
        }
    }
}