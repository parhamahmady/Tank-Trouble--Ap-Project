package Model.GameLogic;

import java.util.*;
import Model.GameLogic.PlayerTypes.*;
import Model.Countrollers.*;

public class GameLoop {
    protected int fps;// frameRate
    protected int numberOfGames;// for leagueMode
    protected GameState gameState;
    protected GameSettings gameSettings;
    protected ArrayList<Player> players;
    protected double startTime, endTime;// to find the duration of game

    public GameLoop(int fps, GameSettings gameSettings) {
        this.fps = fps;
        this.gameSettings = gameSettings;
        players = new ArrayList<Player>();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public GameState getGameState() {
        return gameState;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

}