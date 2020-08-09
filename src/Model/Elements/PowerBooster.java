package Model.Elements;

import Model.GameLogic.GameSettings;

import java.awt.image.BufferedImage;

public class PowerBooster extends Upgrade {
    private int powerBooster;// Two or three times as much

    /**
     * 
     * @param x
     * @param y
     * @param length
     * @param width
     * @param health
     * @param imageName
     * @param powerBooster the powerBosster stenght
     */
    public PowerBooster(double x, double y, double length, double width, int health, String imageName, int maxTime,
            int powerBooster) {
        super(x, y, length, width, health, imageName, maxTime);
        this.powerBooster = powerBooster;
    }

    /**
     * Bullet power doubles or triples.
     */
    @Override
    public void action() {

        getTankOwner().getBullet().setPower((powerBooster) * getTankOwner().getBullet().getPower());
    }

    /**
     * Returns to the original settings after using the amplifier
     * 
     * @param gameSettings Basic game settings
     *
     */
    @Override
    public void reverse(GameSettings gameSettings) {
        getTankOwner().getBullet().setPower(gameSettings.getBulletPower());
        getTankOwner().setHasUpgrade(false);

    }

    public int getPowerBooster() {
        return powerBooster;
    }
    
}
