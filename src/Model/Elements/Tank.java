package Model.Elements;

import java.io.Serializable;

public class Tank extends MovingElements implements Serializable {
    private int health;// the health of tank;
    private Bullet bullet;
    private boolean hasUpgrade;
    private boolean hasShield;// if the tank has shield

    /**
     * Counstructor
     * 
     * @param x
     * @param y
     * @param angle
     * @param owner
     * @param health    //the Maximum size of health
     * @param imageName
     */
    public Tank(double x, double y, double angle, String owner, int health, String imageName) {
        super(x, y, angle, owner, imageName, 100, 100, health);
        this.health = health;
        hasShield = false;
        hasUpgrade = false;
    }

    /**
     * Counstructor
     * 
     * @param health //the Maximum size of health
     * @param type
     */
    public Tank(String owner, int health, String imageName) {
        super(10.0, 10.0, 10.0, owner, imageName, 38, 38, health);

        this.health = health;
        hasShield = false;
    }

    /**
     * Calculate the next direction of tank by user inputs
     * 
     * @param up    upButton of keyboard
     * @param down
     * @param left
     * @param right
     */
    public void directionCalculator(boolean up, boolean down, boolean left, boolean right) {

        bullet.setX2(getX2() + getLength() / 2 + 40 * Math.cos(angle2));// update the location of bullets
        bullet.setY2(getY2() + getWidth() / 2 + 40 * Math.sin(angle2));
        bullet.setAngle2(getAngle2());
        // setX1(getX2());// put this location on past location in case of collisions
        // will used
        // setY1(getY2());// put this location on past location in case of collisions
        // will used
        // setAngle1(getAngle2());
        if (right) {
            angle2 += Math.toRadians(10);
            if (angle2 > 2 * Math.PI) {
                angle2 = 0;
            }
            angle2 = Math.abs(angle2);

        }
        if (left) {
            setX1(getX2());// put this location on past location in case of collisions will used
            // setY1(getY2());// put this location on past location in case of collisions
            // will used

            // setAngle1(getAngle2());
            angle2 -= Math.toRadians(10);
            if (angle2 < 0) {
                angle2 = 2 * Math.PI - (angle2 + Math.toRadians(10));
            }
        }
        if (down) {
            x2 += -(10 * Math.cos(angle2));
            y2 += -(10 * Math.sin(angle2));

        }
        if (up) {
            x2 += 10 * Math.cos(angle2);
            y2 += 10 * Math.sin(angle2);

        }

        // if (angle!=0) {
        // System.out.println("angle : "+angle +" cos : "+Math.cos(angle)+" sin :
        // "+Math.sin(angle) );
        // }
    }

    public boolean hasSheild() {
        return hasShield;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public void setHasUpgrade(boolean hasUpgrade) {
        this.hasUpgrade = hasUpgrade;
    }

    public boolean gethasUpgrade() {
        return hasUpgrade;
    }
}