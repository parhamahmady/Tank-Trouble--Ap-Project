package Model.GameLogic.PlayerTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import Model.Elements.Bullet;
import Model.Elements.Tank;
import Model.GameLogic.GameSettings;

/**
 * Main class for player elements and actions
 */
public abstract class Player implements Serializable {
    private String name;
    private int teamNumber;// for local Game
    private Tank myTank;// player Tank;
    private ArrayList<Bullet> myBullets;// all fired bullets
    private int winNumber;
    public boolean fire, up, down, left, right;// action commands

    /**
     * Counstructor
     * 
     * @param name
     * @param gameSettings
     * @param playerSettings
     */
    public Player(String name, GameSettings gameSettings, PlayerSettings playerSettings) {
        this.name = name;// add name
        teamNumber = playerSettings.getTeamNumber();// addTeam number

        myTank = new Tank(name, gameSettings.getTankMaxHealth(), playerSettings.getTankType());// add tank
        Bullet tempbullet = new Bullet(name, "OrdinaryBullet", gameSettings.getBulletPower(), 2, 4);// add bullet

        myTank.setBullet(tempbullet);
        myBullets = new ArrayList<Bullet>();// new Bullets
    }

    /**
     * Update the player state
     */
    public abstract void updatePlayerState();

    protected void updateBulletsDirection() {
        Iterator<Bullet> it = myBullets.iterator();
        while (it.hasNext()) {

            Bullet temp = it.next();
            temp.directionCalculator();
            if (!temp.directionCalculator()) {

                it.remove();
            }
        }
    }
    public ArrayList<Bullet> getMyBullets() {
        return myBullets;
    }
    public Tank getMyTank(){
        return myTank;
    }
    public String getName(){
        return name;
    }
    public int getTeamNumber() {
        return teamNumber;
    }

    public int getWinNumber() {
        return winNumber;
    }

    public void setWinNumber(int winNumber) {
        this.winNumber = winNumber;
    }

    public void setMyTank(Tank myTank) {
        this.myTank = myTank;
    }

    public void setMyBullets(ArrayList<Bullet> myBullets) {
        this.myBullets = myBullets;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }
}