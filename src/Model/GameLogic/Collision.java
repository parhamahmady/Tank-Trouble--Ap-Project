package Model.GameLogic;

import Model.Elements.Bullet;
import Model.Elements.Tank;
import Model.Elements.Upgrade;
import Model.MapComponents.*;
import Model.MapComponents.Map;

import java.util.*;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

/**
 * A class that check the Collisions
 * 
 * @return return true if a collision happen
 */
public class Collision implements Serializable {
    /**
     * Check the Tank Collision with Indestructablewalls
     */
    public boolean tankCollisionsWithInWall(Tank tank, ArrayList<IndestructibleWall> walls) {
        Rectangle tankRec = new Rectangle((int) tank.getX2(), (int) tank.getY2(), (int) tank.getLength(),
                (int) tank.getWidth());
        for (IndestructibleWall wall : walls) {

            Rectangle wallRec = new Rectangle((int) wall.getX(), (int) wall.getY(), (int) wall.getLength(),
                    (int) wall.getWidth());
            if (tankRec.intersects(wallRec)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check the Tank Collision with Destructablewalls
     */
    public boolean tankCollisionsWithWall(Tank tank, ArrayList<DestructibleWall> walls) {
        Rectangle tankRec = new Rectangle((int) tank.getX2(), (int) tank.getY2(), (int) tank.getLength(),
                (int) tank.getWidth());
        for (DestructibleWall wall : walls) {
            if (wall.getHeath() > 0) {// check the health of wall to check activity
                Rectangle wallRec = new Rectangle((int) wall.getX(), (int) wall.getY(), (int) wall.getLength(),
                        (int) wall.getWidth());
                if (tankRec.intersects(wallRec)) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Check Collison of my tank
     * 
     * @param myTank
     * @param tanks
     * @return true if the colliosn aqured
     */
    public boolean tankWithTankCollison(Tank myTank, ArrayList<Tank> tanks) {

        Rectangle myTankRec = new Rectangle((int) myTank.getX2(), (int) myTank.getY2(), (int) myTank.getLength(),
                (int) myTank.getWidth());

        int index = tanks.indexOf(myTank);
        int i = 0;
        for (Tank tank : tanks) {
            if (i == index) {// to prevent comparing same tanks
                i++;
                continue;
            }
            Rectangle tankRec = new Rectangle((int) tank.getX2(), (int) tank.getY2(), (int) tank.getLength(),
                    (int) tank.getWidth());

            if (myTankRec.intersects(tankRec)) {
                return true;
            }
            i++;
        }
        return false;
    }

    /**
     * Check the collision and is case of collision set new angle and locations
     * 
     * @param bullet
     * @param walls
     * @return ture if the bullet hit an Indestructable wall
     */
    public boolean bulletWithInWallCollison(Bullet bullet, ArrayList<IndestructibleWall> walls) {
        Shape bull = new Ellipse2D.Double(bullet.getX2(), bullet.getY2(), 10, 10);// bigger than bullet size to make
                                                                                  // sure the collision happen
        for (IndestructibleWall wall : walls) {

            Rectangle wallRec = new Rectangle((int) wall.getX(), (int) wall.getY(), (int) wall.getLength(),
                    (int) wall.getWidth());

            if (bull.intersects(wallRec)) {// check the collision

                bullet.setX2(bullet.getX1());// reverse locations
                bullet.setY2(bullet.getY1());

                if (bull.getBounds().getMinX() < wallRec.getMinX() || bull.getBounds().getMaxX() > wallRec.getMaxX()) {// Left
                                                                                                                       // Or
                                                                                                                       // Right

                    bullet.setAngle2((Math.PI - bullet.getAngle2()));// change the angle of bullet
                }
                if (bull.getBounds().getMaxY() > wallRec.getMaxY() || bull.getBounds().getMinY() < wallRec.getMinY()) {// Bottom
                                                                                                                       // or
                                                                                                                       // Up
                    bullet.setAngle2((+-1 * bullet.getAngle2()));// change the angle of bullet

                }

                return true;
            }
        }
        return false;
    }

    /**
     * Check the collision of bullet and the Destructables walls in case of
     * encounter the wall will get damaged and in case of destroying wall , bullet
     * destroy too
     * 
     * @param bullet
     * @param walls
     * @return true if hit the wall
     */
    public boolean bulletWithWallCollison(Bullet bullet, ArrayList<DestructibleWall> walls) {
        Shape bull = new Ellipse2D.Double(bullet.getX2(), bullet.getY2(), 10, 10);// bigger than bullet size to make
                                                                                  // sure the collision happen
        for (DestructibleWall wall : walls) {

            Rectangle wallRec = new Rectangle((int) wall.getX(), (int) wall.getY(), (int) wall.getLength(),
                    (int) wall.getWidth());

            if (bull.intersects(wallRec)) {// check the collision

                if (wall.getHeath() <= 0) {// check the activity of wall
                    continue;
                }

                wall.setHealth(wall.getHeath() - bullet.getPower());// change the health of wall
                if (wall.getHeath() <= 0) {
                    bullet.setX2(-1000);// Destroy the Bullet
                    bullet.setY2(-1000);
                    continue;
                }

                bullet.setX2(bullet.getX1());// reverse locations
                bullet.setY2(bullet.getY1());

                if (bull.getBounds().getMinX() < wallRec.getMinX() || bull.getBounds().getMaxX() > wallRec.getMaxX()) {// Left
                                                                                                                       // Or
                                                                                                                       // Right

                    bullet.setAngle2((Math.PI - bullet.getAngle2()));// change the angle of bullet
                }
                if (bull.getBounds().getMaxY() > wallRec.getMaxY() || bull.getBounds().getMinY() < wallRec.getMinY()) {// Bottom
                                                                                                                       // or
                                                                                                                       // Up
                    bullet.setAngle2((+-1 * bullet.getAngle2()));// change the angle of bullet

                }

                return true;
            }
        }
        return false;
    }

    /**
     * If the bullet hit a tank will destroy itself and damage tank health
     * 
     * @param bullet
     * @param tanks
     * @return true if bullet hit a tank
     */
    public boolean bulletWithWTanks(Bullet bullet, ArrayList<Tank> tanks) {
        Shape bull = new Ellipse2D.Double(bullet.getX2(), bullet.getY2(), 10, 10);// bigger than bullet size to make
                                                                                  // sure the collision happen
        for (Tank tank : tanks) {

            Rectangle tankRec = new Rectangle((int) tank.getX2(), (int) tank.getY2(), (int) tank.getLength(),
                    (int) tank.getWidth());

            if (tank.getHeath() <= 0) {// check the activity of wall
                continue;
            }
            if (bull.intersects(tankRec)) {// check the collision
                if (tank.hasSheild()) {
                    bullet.setX2(bullet.getX1());// reverse locations
                    bullet.setY2(bullet.getY1());
                    if (bull.getBounds().getMinX() < tankRec.getMinX()
                            || bull.getBounds().getMaxX() > tankRec.getMaxX()) {// Left
                        // Or
                        // Right

                        bullet.setAngle2((Math.PI - bullet.getAngle2()));// change the angle of bullet
                    }
                    if (bull.getBounds().getMaxY() > tankRec.getMaxY()
                            || bull.getBounds().getMinY() < tankRec.getMinY()) {// Bottom
                        // or
                        // Up
                        bullet.setAngle2((+-1 * bullet.getAngle2()));// change the angle of bullet

                    }
                    continue;
                }
                tank.setHealth(tank.getHealth() - bullet.getPower());// change the health of wall

                bullet.setX2(-1000);// Destroy the Bullet
                bullet.setY2(-1000);

                if (tank.getHeath() <= 0) {// destroy Tank
                    tank.setX2((-2000));
                    tank.setY2((-2000));
                    continue;
                }

                return true;
            }
        }
        return false;
    }

    ////////////////////////

    /**
     * Check the upgrade Collision with Indestructablewalls
     */
    public boolean upgradeCollisionsWithInWall(Upgrade upgrade, ArrayList<IndestructibleWall> walls) {
        Rectangle upgradeRec = new Rectangle((int) upgrade.getX(), (int) upgrade.getY(), (int) upgrade.getLength(),
                (int) upgrade.getWidth());
        for (IndestructibleWall wall : walls) {
            Rectangle wallRec = new Rectangle((int) wall.getX(), (int) wall.getY(), (int) wall.getLength(),
                    (int) wall.getWidth());
            if (upgradeRec.intersects(wallRec)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check the upgrade Collision with Destructablewalls
     */
    public boolean upgradeCollisionsWithWall(Upgrade upgrade, ArrayList<DestructibleWall> walls) {
        Rectangle upgradeRec = new Rectangle((int) upgrade.getX(), (int) upgrade.getY(), (int) upgrade.getLength(),
                (int) upgrade.getWidth());
        for (DestructibleWall wall : walls) {
            if (wall.getHeath() > 0) {// check the health of wall to check activity
                Rectangle wallRec = new Rectangle((int) wall.getX(), (int) wall.getY(), (int) wall.getLength(),
                        (int) wall.getWidth());
                if (upgradeRec.intersects(wallRec)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check the upgrade Collison with upgrade
     *
     * @param upgrade
     * @param upgrades
     * @return true if the colliosn aqured
     */
    public boolean upgradeWithUpgradeCollison(Upgrade upgrade, ArrayList<Upgrade> upgrades) {
        Rectangle upgradeRec = new Rectangle((int) upgrade.getX(), (int) upgrade.getY(), (int) upgrade.getLength(),
                (int) upgrade.getWidth());
        int index = upgrades.indexOf(upgrade);
        int i = 0;
        for (Upgrade upgrade1 : upgrades) {
            if (i == index) {
                i++;
                continue;
            }
            Rectangle upRec = new Rectangle((int) upgrade1.getX(), (int) upgrade1.getY(), (int) upgrade1.getLength(),
                    (int) upgrade1.getWidth());
            if (upgradeRec.intersects(upRec)) {
                return true;
            }
            i++;
        }
        return false;
    }

    /**
     * Check Collison of upgrade with tank
     * 
     * @param upgrade
     * @param tanks
     * @return
     */
    public boolean upgradeWithTankCollison(Upgrade upgrade, ArrayList<Tank> tanks) {
        Rectangle upgrdaeRec = new Rectangle((int) upgrade.getX(), (int) upgrade.getY(), (int) upgrade.getLength(),
                (int) upgrade.getWidth());
        for (Tank tank : tanks) {
            Rectangle tankRec = new Rectangle((int) tank.getX2(), (int) tank.getY2(), (int) tank.getLength(),
                    (int) tank.getWidth());
            if (upgrdaeRec.intersects(tankRec)) {
                return true;
            }
        }
        return false;
    }

    public Upgrade tankWithUpgradesCollision(Tank tank, ArrayList<Upgrade> upgrades) {
        Rectangle tankRec = new Rectangle((int) tank.getX2(), (int) tank.getY2(), (int) tank.getLength(),
                (int) tank.getWidth());
        for (Upgrade upgrade : upgrades) {
            if (upgrade.getActivatedTime() != 0) {
                continue;
            }
            Rectangle upgrdaeRec = new Rectangle((int) upgrade.getX(), (int) upgrade.getY(), (int) upgrade.getLength(),
                    (int) upgrade.getWidth());
            if (tankRec.intersects(upgrdaeRec)) {
                if (tank.gethasUpgrade()) {// if had upgrade Before
                    System.out.println("HasUpgrade");
                    return null;
                }
                return upgrade;
            }
        }
        return null;
    }

    /**
     * Check the Element Not Locate in a prision
     * 
     * @param map
     * @param x
     * @param y
     */
    public boolean prisonChecker(Map map, double x, double y) {
        int r;// map's columns and rows
        r = map.getRow();

        int w, l;// length and width of images;
        boolean horizental, vertical;
        horizental = vertical = false;
        if (r <= 13) {// set images lenght and width acording to rows
            w = l = 50;
        } else {
            w = l = 40;
        }
        double X = x - map.getLength()[0]; // finding the plate of Tank
        double Y = y - map.getWidth()[0];
        X /= l;
        Y /= w;
        int absoloutX = (int) X;
        int absoloutY = (int) Y;

        for (int i = absoloutX - 2; i < map.getColumn() && i < 2 + absoloutX; i++) {// Vertical
            boolean up, down;
            up = down = false;
            if (i < 0) {
                continue;
            }
            for (int j = (int) absoloutY - 3; j < map.getRow() && j < absoloutY + 3; j++) {
                if (j < 0) {
                    continue;
                }
                if (i == absoloutX && j == absoloutY) {
                    continue;
                }
                if (map.getMapPattern()[j][i] != 1) {
                    continue;
                }
                if (j < absoloutY) {

                    up = true;
                } else {

                    down = true;
                }
            }
            if (up && down) {// if both sides were closed
                vertical = true;
            } else
                vertical = false;
        }

        for (int i = (int) absoloutY - 3; i < map.getRow() && i < absoloutY + 3; i++) {// Horizaental
            boolean left, right;
            left = right = false;
            if (i < 0) {
                continue;
            }
            for (int j = (int) absoloutX - 2; j < map.getColumn() && j < absoloutX + 2; j++) {
                if (j < 0) {
                    continue;
                }
                if (i == absoloutX && j == absoloutY) {
                    continue;
                }
                if (map.getMapPattern()[i][j] != 1) {
                    continue;
                }
                if (j < absoloutX) {

                    left = true;
                } else {

                    right = true;
                }
            }
            if (right && left) {// if both sides were closed
                horizental = true;
            } else
                horizental = false;
        }
        if (vertical && horizental) {
            System.out.println("Prison");
            return true;
        }
        return false;
    }
}