package Model.Elements;

import Model.GameLogic.GameSettings;
import Model.MapComponents.MapComponent;

import java.io.Serializable;

public abstract class Upgrade extends MapComponent {
    // to save current time of system
    private double activatedTime;
    // Most activity time of upgrade
    private double maxTime;
    private Tank tankOwner;

    public Upgrade(double x, double y, double length, double width, int health, String imageName, int maxTime) {
        super(x, y, length, width, health, imageName);
        this.maxTime = maxTime;
        activatedTime = 0;
    }

    /**
     * Specifies activatedTime by using system current time
     */
    public void start() {
        activatedTime = System.currentTimeMillis() / 1000;
    }

    /**
     * getter method
     * 
     * @return Tank
     */
    public Tank getTankOwner() {
        return tankOwner;
    }

    /**
     * setter method
     * 
     * @param tankOwner type of tank
     */
    public void setTankOwner(Tank tankOwner) {
        this.tankOwner = tankOwner;
    }

    /**
     *
     * @return true if upgrade has expired.
     */
    public boolean isExpired() {
        if ((System.currentTimeMillis() / 1000) - activatedTime >= maxTime) {
            System.out.println("Deprecated");
            return true;
        }
        return false;
    }

    public double getActivatedTime() {
        return activatedTime;
    }

    public double getMaxTime() {
        return maxTime;
    }

    /**
     * this method Returns to the original settings after using the amplifier
     * 
     * @param gameSettings Basic game settings
     */
    public abstract void reverse(GameSettings gameSettings);

    public abstract void action() throws InterruptedException;

}
