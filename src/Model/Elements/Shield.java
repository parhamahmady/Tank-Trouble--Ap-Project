package Model.Elements;

import Model.GameLogic.GameSettings;

import java.util.concurrent.TimeUnit;

public class Shield extends Upgrade {
    /**
     * Counstructor
     * 
     * @param x
     * @param y
     * @param length
     * @param width
     * @param health
     * @param imageName
     * @param maxTime
     */
    public Shield(double x, double y, double length, double width, int health, String imageName, int maxTime) {
        super(x, y, length, width, health, imageName, maxTime);
    }

    @Override
    public void reverse(GameSettings gameSettings) {
        getTankOwner().setHasShield(false);
        getTankOwner().setHasUpgrade(false);

    }

    /**
     * resistant to bullets and lasers for 15 seconds.
     */
    @Override
    public void action() throws InterruptedException {
        
        getTankOwner().setHasShield(true);
    }
}
