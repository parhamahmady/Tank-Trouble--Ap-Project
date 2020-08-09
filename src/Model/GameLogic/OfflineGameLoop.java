package Model.GameLogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import Model.Countrollers.MainCountroller;
import Model.GameLogic.PlayerTypes.Player;
import Model.MapComponents.Map;
import Model.MapComponents.MapFileLoader;
import View.GameFrame.*;

/**
 * Game loop for not local games;
 */
public class OfflineGameLoop extends GameLoop implements Runnable {

    private int numberOfGames;// for leagueMode
    private GameFrame gameFrame;
    private Map map;
    protected MainCountroller mainCountroller;

    public OfflineGameLoop(int fps, GameFrame gameFrame, GameSettings gameSettings, MainCountroller mainCountroller) {
        super(fps, gameSettings);
        this.gameFrame = gameFrame;
        this.mainCountroller = mainCountroller;

    }

    /**
     * Load Game Components and make ready to play
     * 
     * @param players
     * @param map
     */
    public void loadTheGame(ArrayList<Player> players, Map map) {
        this.map = map;
        this.players = players;
        gameState = new GameState(players, map, "SoloGame", gameSettings);
    }

    public void run() {
        startTime = System.currentTimeMillis();// time on minute
        boolean end = false;
        numberOfGames = gameSettings.getEndType() == 1 ? 1 : gameSettings.getNumberofLeagueGames();// set the number of
                                                                                                   // games
        System.out.println("Starts");
        int pcWin, humanWin;
        pcWin = humanWin = 0;
        for (int i = 0; i < numberOfGames; i++) {
            if (numberOfGames != 1) {
                gameState.setIsleague(true);
            }
            while (!end) {
                try {

                    long start = System.currentTimeMillis();

                    end = gameState.updateState();
                    gameFrame.render(gameState);

                    long delay = (1000 / fps) - (System.currentTimeMillis() - start);
                    if (delay > 0)
                        Thread.sleep(delay);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            gameFrame.render(gameState);
            try {
                Thread.sleep((long) 2000);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(gameFrame, "Error", "!!!!", JOptionPane.ERROR_MESSAGE);
            }
            if (numberOfGames != 1) {
                if (mainCountroller.winnerNameFinder(gameState).equals("Pc"))// find the winner of game
                {
                    pcWin++;
                } else
                    humanWin++;
                resetGame();
                loadTheGame(players, map);
                end = false;
            }
        }

        endTime = System.currentTimeMillis();

        double time = ((TimeUnit.MILLISECONDS.toSeconds((long) endTime))
                - TimeUnit.MILLISECONDS.toSeconds((long) startTime));
        System.out.println("Time" + time /60 );
        if (numberOfGames == 1) {
            mainCountroller.endSinglePlayerSGame(gameState, time/60);
            return;
        }
        mainCountroller.endSinglePlayerLGame(pcWin, humanWin, time/60);
    }

    /**
     * Reset the Game To start Again
     */
    private void resetGame() {
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {// reset players
            Player p = it.next();
            if (p.getMyTank().getHealth() > 0) {// add win number
                p.setWinNumber(p.getWinNumber() + 1);
            }
            p.getMyTank().setHealth(gameSettings.getTankMaxHealth());
        }
        MapFileLoader mapFileLoader = new MapFileLoader(mainCountroller.randomMapName());// reset map
        mapFileLoader.loadMap(gameFrame, gameSettings);
        map = mapFileLoader.getLoadedMap();
    }
}