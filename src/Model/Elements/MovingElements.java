package Model.Elements;

import Model.MapComponents.MapComponent;

/**
 * The Main class for moving Objects
 */
public abstract class MovingElements extends MapComponent {
    protected double x1, x2, y1, y2;// the locations of the element at time 1 & 2;
    protected double angle1, angle2;// angle (teta)
    private String owner;// the owner of the element(Player)
    // private String type;// the type of element for pic will used

    /**
     * Counstructor for elements who has Health
     * 
     * @param x     first location of element
     * @param y
     * @param angle
     */
    public MovingElements(Double x, Double y, double angle, String owner, String imageName, double width,
            double length,int health) {
        super(x, y, length, width, health, imageName);
        x1 = x2 = x;
        y1 = y2 = y;
        angle1 = angle2 = angle;
        this.owner = owner;
    }

    // /**
    //  * Counstructor
    //  * 
    //  */
    // public MovingElements(String owner, String imageName,he) {
    //        super(0, 0, 0, 0, 0, imageName);
    //     this.owner = owner;

    // }

    public String getOwner() {
        return owner;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getAngle1() {
        return angle1;
    }

    public double getAngle2() {
        return angle2;
    }

    public double getY1() {
        return y1;
    }

    public double getY2() {
        return y2;
    }

    public void setAngle1(double angle) {
        angle1 = angle;
    }

    public void setAngle2(double angle2) {
        this.angle2 = angle2;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }
}