package Model.Elements;

import Model.GameLogic.GameSettings;

public class HealthBooster extends Upgrade {
    public HealthBooster(double x, double y, double length, double width, int health, String imageName, int maxTime) {
        super(x, y, length, width, health, imageName, maxTime);
    }

    @Override
    public void reverse(GameSettings gameSettings) {
        getTankOwner().setHasUpgrade(false);
    }

    /**
     * Ten percent added to the previous life of the tank
     *
     */
    @Override
    public void action() {
        
        getTankOwner().setHealth(getTankOwner().getHealth() + 10);

    }
}
