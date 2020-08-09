package Model.MapComponents;



public class IndestructibleWall extends MapComponent {
    /**
     *
     * @param x             length in coordinate
     * @param y             width in coordinate
     * @param length        length of map component
     * @param width         width of map component
     * @param bufferedImage picture of map component.
     */
    public IndestructibleWall(double x, double y, double length, double width, String imageName) {
        super(x, y, length, width, imageName);
    }
}
