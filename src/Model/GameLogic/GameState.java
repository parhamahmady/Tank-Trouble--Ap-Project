package Model.GameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import Model.Elements.Bullet;
import Model.Elements.Tank;
import Model.Elements.Upgrade;
import Model.Elements.UpgradeManager;
import Model.GameLogic.PlayerTypes.*;
import Model.MapComponents.Map;

public class GameState implements Serializable {
    private ArrayList<Tank> tanks;// all the tanks in the game
    private ArrayList<Bullet> bullets;// all bullets in the game
    private ArrayList<Player> players;// all players
    private ArrayList<Upgrade> upgrades;
    private Map map;
    private Collision collision;
    private boolean end;
    private boolean isleague;
    private boolean isOnline;
    private String gameName;
    private int minPlayer;
    private GameSettings gameSettings;
    private UpgradeManager upgradeManager;

    /**
     * Counstructor for Offline
     * 
     * @param players
     * @param map
     */
    public GameState(ArrayList<Player> players, Map map, String gameName, GameSettings gameSettings) {
        this.players = players;
        this.map = map;
        this.gameSettings = gameSettings;
        end = false;
        isleague = false;
        bullets = new ArrayList<Bullet>();
        upgrades = new ArrayList<Upgrade>();
        collision = new Collision();
        isOnline = false;
        minPlayer = 0;
        this.gameName = gameName;
        upgradeManager = new UpgradeManager(gameSettings, map, collision);

        updateTanks();
        tankLocationSetter();

        // System.out.println("Width Map (" + map.getWidth()[0] + "," +
        // map.getWidth()[1] + ")");
        // System.out.println("Lenght Map (" + map.getLength()[0] + "," +
        // map.getLength()[1] + ")");
        // Iterator<Tank> it = tanks.iterator();
        // while (it.hasNext()) {
        // Tank temp = it.next();
        // System.out.println(temp.getOwner() + " ( " + temp.getX2() + " , " +
        // temp.getY2() + " )");
        // }
    }

    /**
     * Counstructor for Online
     * 
     * @param players
     * @param map
     * @param n
     */
    public GameState(ArrayList<Player> players, Map map, String gameName, int minPlayer, GameSettings gameSettings) {
        this.players = players;
        this.map = map;
        this.gameName = gameName;
        this.minPlayer = minPlayer;
        this.gameSettings = gameSettings;
        upgrades = new ArrayList<Upgrade>();
        end = false;
        isleague = false;
        bullets = new ArrayList<Bullet>();
        collision = new Collision();
        isOnline = true;
        upgradeManager = new UpgradeManager(gameSettings, map, collision);
        updateTanks();

    }

    /**
     * Update the state of the Game(save changes)
     */
    public boolean updateState() {
        if (!isOnline) {
            for (Player player : players) {
                player.updatePlayerState();
            }
        }

        updateTanks();
        updateBullets();
        wallcollisionChecker();
        tankWithTankCollisionChecker();
        bulletCollisionChecker();
        tankWithUpgradesCollisionChecker();
        upgradeManager.updateState(upgrades, this);
        return end = endCheck();
    }

    /**
     * Update all tanks locations of each player
     */
    private void updateTanks() {
        tanks = new ArrayList<Tank>();// make new array
        for (Player player : players) {
            if (player.getMyTank().getHealth() > 0) {// check the Tank Health
                if (map.getRow() > 13) {// resize the tank imageSize
                    player.getMyTank().setLength(24);
                    player.getMyTank().setWidth(24);
                }
                tanks.add(player.getMyTank());

            }
        }
    }

    /**
     * Update all bullets locations
     */
    private void updateBullets() {
        bullets = new ArrayList<Bullet>();// make new array
        for (Player player : players) {// add each player's bullet
            ArrayList<Bullet> playerBullets = player.getMyBullets();
            for (Bullet bullet : playerBullets) {
                bullets.add(bullet);

            }
        }
    }

    /**
     * Check the End as Survivle Mode
     * 
     * @return true if just one tank left
     */
    private boolean endCheck() {
        Iterator<Player> it = players.iterator();
        int i = 0;// number of active tanks

        while (it.hasNext()) {
            Tank temp = it.next().getMyTank();
            if (temp.getHealth() > 0) {
                i++;
            }
        }
        if (i > 1) {
            it = players.iterator();
            int j = 0;
            int team = 0;
            while (it.hasNext()) {
                Player p = it.next();
                Tank temp = p.getMyTank();
                if (temp.getHealth() > 0) {
                    if (j != 0) {
                        if (team != p.getTeamNumber()) {
                            return false;
                        }
                    }
                    team = p.getTeamNumber();
                    j++;
                }
            }
            if (team == 0) {
                return false;
            }
            return true;
        }
        return true;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public ArrayList<Tank> getTanks() {
        return tanks;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Map getMap() {
        return map;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void setIsleague(boolean isleague) {
        this.isleague = isleague;
    }

    public boolean isleague() {
        return isleague;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public void setMinPlayer(int minPlayer) {
        this.minPlayer = minPlayer;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ArrayList<Upgrade> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(ArrayList<Upgrade> upgrades) {
        this.upgrades = upgrades;
    }

    public UpgradeManager getUpgradeManager() {
        return upgradeManager;
    }

    public Collision getCollision() {
        return collision;
    }

    public void setUpgradeManager(UpgradeManager upgradeManager) {
        this.upgradeManager = upgradeManager;
    }

    /**
     * Check all kind of Bullet Collisions
     */
    private void bulletCollisionChecker() {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            if (!collision.bulletWithInWallCollison(bullet, map.getIndestructibleWalls())
                    && !collision.bulletWithWallCollison(bullet, map.getDestructibleWalls())
                    && !collision.bulletWithWTanks(bullet, tanks)) {
                bullet.setX1(bullet.getX2());
                bullet.setY1(bullet.getY2());
                bullet.setAngle1(bullet.getAngle2());
            }

        }
    }

    /**
     * Check if a Tank hit a upgrade
     */
    private void tankWithUpgradesCollisionChecker() {
        Iterator<Tank> it = tanks.iterator();
        while (it.hasNext()) {
            Tank tank = it.next();
            Upgrade upgrade = collision.tankWithUpgradesCollision(tank, upgrades);
            if (upgrade == null) {
                tank.setAngle1(tank.getAngle2());
                tank.setX1(tank.getX2());
                tank.setY1(tank.getY2());
                continue;
            }
            try {
                upgrade.setX(-2000);
                upgrade.setY(-2000);
                tank.setHasUpgrade(true);
                upgrade.setTankOwner(tank);
                upgrade.start();
                upgrade.action();
                System.out.println("Upgrade Complete");
                tank.setAngle2(tank.getAngle1());
                tank.setX2(tank.getX1());
                tank.setY2(tank.getY1());
                System.out.println(tank.gethasUpgrade());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Check tank to tank collisions
     */
    private void tankWithTankCollisionChecker() {
        Iterator<Tank> it = tanks.iterator();
        while (it.hasNext()) {
            Tank tank = it.next();
            if (tank.getHealth() <= 0) {
                continue;
            }
            if (collision.tankWithTankCollison(tank, tanks)) {
                tank.setX2(tank.getX1());
                tank.setY2(tank.getY1());
                tank.setAngle2(tank.getAngle2());
            } else {
                tank.setX1(tank.getX2());
                tank.setY1(tank.getY2());
                tank.setAngle1(tank.getAngle2());
            }
        }
    }

    /**
     * Check tanks Collision with Walls
     */
    private void wallcollisionChecker() {
        Iterator<Tank> it = tanks.iterator();
        while (it.hasNext()) {// iterate through all tanks
            Tank tank = it.next();
            if (tank.getHeath() <= 0) {// check the activity of tank
                continue;
            }
            if (collision.tankCollisionsWithInWall(tank, map.getIndestructibleWalls())
                    || collision.tankCollisionsWithWall(tank, map.getDestructibleWalls())
                    || collision.tankWithTankCollison(tank, tanks)) {// check collision
                tank.setX2(tank.getX1());// reverse the Direction
                tank.setY2(tank.getY1());
                tank.setAngle2(tank.getAngle1());
            } else {
                tank.setX1(tank.getX2());
                tank.setY1(tank.getY2());
                tank.setAngle1(tank.getAngle2());
            }

        }
    }

    /**
     * Set a Random Location for Tanks; The Location is a suitable not bad
     */
    private void tankLocationSetter() {
        Iterator<Tank> it = tanks.iterator();
        while (it.hasNext()) {
            Tank tank = it.next();
            while (true) {// to make sure the location is suitable
                // System.out.println("Width Map (" + map.getWidth()[0] + "," +
                // map.getWidth()[1] + ")");
                // System.out.println("Lenght Map (" + map.getLength()[0] + "," +
                // map.getLength()[1] + ")");
                double x = Math.abs(Math.random()) * (map.getLength()[1] - map.getLength()[0] + 1) + map.getLength()[0];
                double y = Math.abs(Math.random()) * (map.getWidth()[1] - map.getWidth()[0] + 1) + map.getWidth()[0];
                // System.out.println("x of tank " + tank.getOwner() + " : " + x);
                // System.out.println("y of tank" + tank.getOwner() + " : " + y);
                tank.setX2(x);
                tank.setY2(y);
                tank.setX1(x);
                tank.setY1(y);
                tank.setAngle2(Math.abs(Math.random() * 10000) % 360);
                if (collision.tankCollisionsWithInWall(tank, map.getIndestructibleWalls())// check collision with wall
                        || collision.tankCollisionsWithWall(tank, map.getDestructibleWalls())) {
                    continue;
                }
                if (collision.tankWithTankCollison(tank, tanks)) {// check collision with tanks
                    continue;
                }
                if (collision.prisonChecker(map, tank.getX2(), tank.getY2())) {
                    continue;
                }
                break;
            }
            // System.out.println("Final x of tank " + tank.getOwner() + " : " +
            // tank.getX2());
            // System.out.println("Final y of tank" + tank.getOwner() + " : " +
            // tank.getY2());

        }
    }

}