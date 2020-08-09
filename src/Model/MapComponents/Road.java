package Model.MapComponents;

public class Road extends MapComponent {
    /**
     *
     * @param x             length in coordinate
     * @param y             width in coordinate
     * @param length        length of map component
     * @param width         width of map component
     * @param health        health of component
     * @param bufferedImage picture of map component.
     */
    public Road(double x, double y, double length, double width, String imageName) {
        super(x, y, length, width, imageName);
    }
}
