package Model.MapComponents;

import java.io.Serializable;

public class MapComponent implements Serializable {
    // length in coordinate
    private double x;
    // width in coordinate
    private double y;
    // length of map component
    private double length;
    // width of map component
    private double width;
    // health of component
    private int health;
    // picture of map component
    private String imageName;

    /**
     * For Component who has Health
     * 
     * @param x             length in coordinate
     * @param y             width in coordinate
     * @param length        length of map component
     * @param width         width of map component
     * @param health        health of component
     * @param bufferedImage picture of map component.
     */
    public MapComponent(double x, double y, double length, double width, int health, String imageName) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        this.health = health;
        this.imageName = imageName;
    }

    /**
     * For Component who dont has Health
     * 
     * @param x             length in coordinate
     * @param y             width in coordinate
     * @param length        length of map component
     * @param width         width of map component
     * @param bufferedImage picture of map component.
     */
    public MapComponent(double x, double y, double length, double width, String imageName) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        this.health = -100;
        this.imageName = imageName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public int getHeath() {
        return health;
    }

    public String getImageName() {
        return imageName;
    }

    public void setX(double x1) {
        x = x1;
    }

    public void setY(double y1) {
        y = y1;
    }

    public void setLength(double length1) {
        length = length1;
    }

    public void setWidth(double width1) {
        width = width1;
    }

    public void setHealth(int health1) {
        health = health1;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
