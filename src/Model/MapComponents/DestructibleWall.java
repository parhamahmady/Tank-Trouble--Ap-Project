package Model.MapComponents;

public class DestructibleWall extends MapComponent {
    /**
     *
     * @param x             length in coordinate
     * @param y             width in coordinate
     * @param length        length of map component
     * @param width         width of map component
     * @param health        health of component(0 or 1)
     * @param bufferedImage picture of map component.
     */
    public DestructibleWall(double x, double y, double length, double width, int health, String imageName) {
        super(x, y, length, width, health, imageName);
    }
}
