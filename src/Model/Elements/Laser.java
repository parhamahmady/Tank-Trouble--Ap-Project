package Model.Elements;

import Model.GameLogic.GameSettings;

public class Laser extends Upgrade {

    public Laser(double x, double y, double length, double width, int health, String imageName, int MaxTime) {
        super(x, y, length, width, health, imageName, MaxTime);
    }

    @Override
    public void reverse(GameSettings gameSettings) {
        getTankOwner().getBullet().setPower(gameSettings.getBulletPower());
        getTankOwner().getBullet().setSpeed(2);
        getTankOwner().getBullet().setMaxTime(4);
        getTankOwner().setHasUpgrade(false);
        getTankOwner().getBullet().setImageName("OrdinaryBullet");
    }

    /**
     * @throws InterruptedException
     */
    @Override
    public void action() throws InterruptedException {// getTankOwner().getBullet().getSpeed()
        
        getTankOwner().getBullet().setSpeed(6);
        getTankOwner().getBullet().setPower(1000);
        getTankOwner().getBullet().setMaxTime(3);
        getTankOwner().getBullet().setImageName("Lazer");
    }
}
