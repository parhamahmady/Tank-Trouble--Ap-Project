package Model.Elements;

public class Bullet extends MovingElements {
    private int power, speed;
    private int maxTime;// the life of bullet;
    private long creatTime;// the time that the bullet created

    /**
     * Counstructor
     * 
     * @param x         first location of bullet
     * @param y
     * @param angle
     * @param owner
     * @param imageName the type of bullet (laser or ordinary)
     * @param power     power of destroying
     * @param speed     speed of bullet
     */
    public Bullet(double x, double y, double angle, String owner, String imageName, int power, int speed, int maxTime) {
        super(x, y, angle, owner, imageName, 30, 30, -100);// becuas dont have health put health -100;
        this.maxTime = maxTime;
        creatTime = System.currentTimeMillis();
        this.speed = speed;
        this.power = power;
    }

    /**
     * Counstructor
     * 
     * @param owner
     * @param type  the type of bullet (laser or ordinary)
     * @param power power of destroying
     * @param speed speed of bullet
     */
    public Bullet(String owner, String imageName, int power, int speed, int maxTime) {
        super(0.0, 0.0, 0.0, owner, imageName, 30, 30, -100);
        this.maxTime = maxTime;
        creatTime = System.currentTimeMillis();
        this.speed = speed;
        this.power = power;
    }

    /**
     * Calculate the next direction of bullet
     * 
     * @return true if the life of bullet was valid
     */
    public boolean directionCalculator() {
        if (timer()) {// check the life of bullet
            return false;
        }

        x2 += speed * Math.cos(angle2);
        y2 += speed * Math.sin(angle2);

        return true;
    }

    /**
     * 
     * @return true if the life of bullet ran out
     */
    private boolean timer() {
        long currentTime = System.currentTimeMillis();

        if (((double) currentTime - (double) creatTime) / 1000 >= (double) maxTime) {

            return true;
        }
        return false;
    }

    public int getSpeed() {
        return speed;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }
}