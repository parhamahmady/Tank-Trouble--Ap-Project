package Model.GameLogic;

import java.io.Serializable;

/**
 * Settings of a game is here
 */
public class GameSettings implements Serializable {
    private int teamOrSolo;// type of playing 1:team , 2:solo
    private int endType;// the end type of the game 1 :Survivel ; 2 : League;
    private int tankMaxHealth;// max of tanks health
    private int bulletPower;// min power of bullets
    private int wallHealth;// the max health of walls
    private int minPlayer;// min number of players for starting game
    private int numberofLeagueGames;
    private String gameName;// name of game in onlineMode

    /**
     * Counstructor for offline game
     * 
     * @param eT entType
     * @param tM tank Max Health
     * @param bP Bullet power
     * @param wH wall health
     */
    public GameSettings(int eT, int tM, int bP, int wH) {
        endType = eT;
        tankMaxHealth = tM;
        bulletPower = bP;
        wallHealth = wH;
        minPlayer = 2;
    }

    /**
     * Counstructor for online game
     * 
     * @param gH  name Of Game
     * @param eT  entType
     * @param tM  tank Max Health
     * @param bP  Bullet power
     * @param wH  wall health
     * @param mp  minPlayer
     * @param tOs team or Solo
     */
    public GameSettings(String gN, int eT, int tM, int bP, int wH, int mP, int tOS) {
        gameName = gN;
        endType = eT;
        tankMaxHealth = tM;
        bulletPower = bP;
        wallHealth = wH;
        minPlayer = 2;
        minPlayer = mP;
        teamOrSolo = tOS;

    }

    public int getBulletPower() {
        return bulletPower;
    }

    public int getEndType() {
        return endType;
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public int getTankMaxHealth() {
        return tankMaxHealth;
    }

    public int getTeamOrSolo() {
        return teamOrSolo;
    }

    public int getWallHealth() {
        return wallHealth;
    }

    public int getNumberofLeagueGames() {
        return numberofLeagueGames;
    }

    public void setNumberofLeagueGames(int numberofLeagueGames) {
        this.numberofLeagueGames = numberofLeagueGames;
    }

    public String getGameName() {
        return gameName;
    }
}