package Model.GameLogic.PlayerTypes;

import java.awt.event.*;
import java.io.Serializable;

import Model.Elements.Bullet;
import Model.GameLogic.GameSettings;
// import sun.tools.serialver.resources.serialver;

/**
 * Class for a human player that controls humans actions
 */
public class HumanPlayer extends Player implements Serializable {
    private transient KeyActions keyActions;// for controling tank and firing bullets
    private int fireButton;
    private double t1, t2;// for measuring bullet per second

    /**
     * Counstructor
     * 
     * @param name
     * @param gameSettings
     * @param playerSettings
     */
    public HumanPlayer(String name, GameSettings gameSettings, PlayerSettings playerSettings) {
        super(name, gameSettings, playerSettings);
        keyActions = new KeyActions();
        fireButton = playerSettings.getFireKey();

        fire = up = down = left = right = false;

    }

    @Override
    public void updatePlayerState() {
        
        if (getMyTank().getHealth() <= 0) {// check the activity of Tank
            return;
        }
        getMyTank().directionCalculator(up, down, left, right);
        updateBulletsDirection();

        if (fire) {
            t2 = (double) System.currentTimeMillis() / (double) 1000;
            
            if (t1 != 0 && (t2 - t1 < 0.5)) {
                
                return;// Control the Bullet Rate
            }
            
            Bullet newBullet = new Bullet(getMyTank().getBullet().getX2(), getMyTank().getBullet().getY2(),
                    getMyTank().getBullet().getAngle2(), getName(), getMyTank().getBullet().getImageName(),
                    getMyTank().getBullet().getPower(), getMyTank().getBullet().getSpeed(),
                    getMyTank().getBullet().getMaxTime());
            getMyBullets().add(newBullet);
            t1 = (double) System.currentTimeMillis() / (double) 1000;
        }
    }

    /**
     * KeyActions
     */
    public class KeyActions extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent k) {
            // System.out.println("fire");

            switch (k.getKeyCode()) {

                case KeyEvent.VK_UP:
                    up = true;
                    break;
                case KeyEvent.VK_DOWN:
                    down = true;
                    break;
                case KeyEvent.VK_LEFT:
                    left = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    right = true;
                    break;

            }
            if (k.getKeyCode() == fireButton) {// for fireButton
                fire = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent k) {
            switch (k.getKeyCode()) {
                case KeyEvent.VK_UP:
                    up = false;
                    break;
                case KeyEvent.VK_DOWN:
                    down = false;
                    break;
                case KeyEvent.VK_LEFT:
                    left = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    right = false;
                    break;

            }
            if (k.getKeyCode() == fireButton) {// for fireButton
                fire = false;
            }
        }

    }

    public KeyActions getKeyActions() {
        return keyActions;
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