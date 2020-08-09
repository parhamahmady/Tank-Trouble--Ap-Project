package Model.Elements;

import Model.GameLogic.Collision;
import Model.GameLogic.GameSettings;
import Model.GameLogic.GameState;
import Model.MapComponents.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class UpgradeManager implements Serializable {
    private ArrayList<Upgrade> upgrades;// list of upgrades
    private GameSettings gameSettings;// settings of game
    private Random random;// random number
    private int result;// to save random number
    private Map map;
    private Collision collision;
    private GameState gameState;
    private double t1, t2;

    public UpgradeManager() {

    }

    /**
     * @param gameSettings settings of game
     */
    public UpgradeManager(GameSettings gameSettings, Map map, Collision collision) {
        this.gameSettings = gameSettings;
        this.map = map;
        this.collision = collision;
        random = new Random();

        t1 = t2 = 0;
    }

    /**
     * This method checks if the upgrade is expired returns it to its original
     * settings
     */
    private void expireChecker() {
        Iterator<Upgrade> it = upgrades.iterator();
        while (it.hasNext()) {
            Upgrade upgrade = it.next();
            if (upgrade.getActivatedTime() != 0) {
                if (upgrade.isExpired() == true) {
                    upgrade.reverse(gameSettings);
                    it.remove();
                }
            }
        }

    }

    /**
     * This method creates an upgrade randomly with delays
     *
     * @throws InterruptedException timeunit exception
     */
    private void UpgradeMaker() {
        t2 = System.currentTimeMillis() / 1000;
        if (t1 != 0 && (t2 - t1) < 15) {

            return;
        }
        System.out.println("Madking");
        result = random.nextInt(6 - 1) + 1;

        if (result == 1) {
            Shield shield = new Shield(10, 10, 20, 20, 1, "upgrade", 15);
            upgradeLocationSetter(shield);
            upgrades.add(shield);
        } else if (result == 2) {
            HealthBooster healthBooster = new HealthBooster(10, 10, 20, 20, 1, "upgrade", 10);
            upgradeLocationSetter(healthBooster);
            upgrades.add(healthBooster);
        } else if (result == 3) {
            PowerBooster powerBooster2 = new PowerBooster(10, 10, 20, 20, 5, "upgrade", 10, 2);
            upgradeLocationSetter(powerBooster2);
            upgrades.add(powerBooster2);
        } else if (result == 4) {
            PowerBooster powerBooster3 = new PowerBooster(10, 10, 20, 20, 5, "upgrade", 10, 3);
            upgradeLocationSetter(powerBooster3);
            upgrades.add(powerBooster3);
        } else if (result == 5) {
            Laser laser = new Laser(10, 10, 20, 20, 2, "upgrade", 10);
            upgradeLocationSetter(laser);
            upgrades.add(laser);
        }

        t1 = System.currentTimeMillis() / 1000;
    }

    /**
     * just calls expirechecker
     */
    public void updateState(ArrayList<Upgrade> upgrades, GameState gameState) {
        // System.out.println("Update");
        this.upgrades = upgrades;
        this.gameState = gameState;
        expireChecker();
        UpgradeMaker();

    }

    /**
     * Set a Random Location for upgrades.
     */
    private void upgradeLocationSetter(Upgrade upgrade) {
        while (true) {
            double x = Math.abs(Math.random()) * (map.getLength()[1] - map.getLength()[0] + 1) + map.getLength()[0];
            double y = Math.abs(Math.random()) * (map.getWidth()[1] - map.getWidth()[0] + 1) + map.getWidth()[0];
            upgrade.setX(x);
            upgrade.setY(y);
            if (collision.upgradeCollisionsWithInWall(upgrade, map.getIndestructibleWalls())
                    || collision.upgradeCollisionsWithWall(upgrade, map.getDestructibleWalls())) {
                continue;
            }
            if (collision.upgradeWithUpgradeCollison(upgrade, upgrades)) {// check collision with tanks
                continue;
            }
            if (collision.upgradeWithTankCollison(upgrade, gameState.getTanks())) {
                continue;
            }
            if (collision.prisonChecker(map, x, y)) {

            }
            break;
        }
    }

    public double getT1() {
        return t1;
    }

    public double getT2() {
        return t2;
    }

    public void setT1(double t1) {
        this.t1 = t1;
    }

    public void setT2(double t2) {
        this.t2 = t2;
    }
}
