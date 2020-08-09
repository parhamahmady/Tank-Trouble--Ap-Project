package Model.MapComponents;

import java.util.Scanner;

import javax.swing.JFrame;

import Model.FIO.Loader;
import Model.GameLogic.GameSettings;

/**
 * This is a class that load a map from file and new it ;
 * 
 */
public class MapFileLoader {
    private Map loadedMap;// map that will loaded
    private Loader loader;// will load mapText file
    private int[][] mapPattern;// the pattern of Map ,show the map elements loacations

    public MapFileLoader(String mapName) {
        loadedMap = new Map();

        loader = new Loader("Filse");
        String pattern = "";// will init from file
        try {
            pattern = loader.loadMapFile(mapName);
        } catch (Exception e) {
            System.out.println("Cant load MapFile");
        }

        makeMapPattern(pattern);

    }

    /**
     * Load the map Pattern
     * 
     * @param patternString
     */
    private void makeMapPattern(String patternString) {
        loadColumnAndRow(patternString);// first load row and column
        mapPattern = new int[loadedMap.getRow()][loadedMap.getColumn()];
        Scanner tempScanner = new Scanner(patternString);

        for (int i = 0; i < loadedMap.getRow(); i++) {
            String temp = tempScanner.next();// load a row
            for (int j = 0; j < loadedMap.getColumn(); j++) {
                mapPattern[i][j] = Character.getNumericValue(temp.charAt(j));
            }
        }
        tempScanner.close();
        loadedMap.setMapPattern(mapPattern);
    }

    /**
     * Find map row and column from text file
     * 
     * @param petternString that loaded from file
     */
    private void loadColumnAndRow(String patternString) {
        Scanner tempScanner = new Scanner(patternString);
        int i = 0;// column
        int j = 1;// row
        int spaces = 0;// count spaces
        while (tempScanner.hasNext()) {
            String temp = tempScanner.next();
            if (i == 0) {
                i = temp.length();
            }
            spaces++;
        }
        j = ((patternString.length() - spaces) / i);// for removing spaces;
        tempScanner.close();
        loadedMap.setColumn(i);
        loadedMap.setRow(j);
    }

    /**
     * load map components according to Frame & GameSetiing and pattern
     */
    public void loadMap(JFrame frame, GameSettings gameSettings) {
        int c, r;// map's columns and rows
        c = loadedMap.getColumn();
        r = loadedMap.getRow();

        int w, l;// length and width of images;

        if (r <= 13) {// set images lenght and width acording to rows
            w = l = 50;
        } else {
            w = l = 40;
        }
        // calculate the lenght and width of mapp
        double[] mapLenght = new double[2];
        mapLenght[0] = (frame.getWidth() - (c * l)) / 2;
        mapLenght[1] = (frame.getWidth() - (c * l)) / 2 + ((loadedMap.getColumn() - 1) * l);
        loadedMap.setLength(mapLenght);

        double[] mapWidth = new double[2];

        mapWidth[0] = (frame.getHeight() - (r - 1) * w) / 2;
        mapWidth[1] = (frame.getHeight() - (r - 1) * w) / 2 + (loadedMap.getRow() - 1) * w;
        loadedMap.setWidth(mapWidth);

        // locate all plates

        for (int i = 0; i < loadedMap.getRow(); i++) {

            for (int j = 0; j < loadedMap.getColumn(); j++) {
                if (mapPattern[i][j] == 1) {// add IndestructibleWall
                    IndestructibleWall indestructibleWall = new IndestructibleWall(
                            (double) (frame.getWidth() - (c * l)) / 2 + (j * l),
                            (double) (frame.getHeight() - (r - 1) * w) / 2 + i * w, (double) l, (double) w,
                            "crateMetal");
                    loadedMap.getIndestructibleWalls().add(indestructibleWall);
                    continue;
                }
                if (mapPattern[i][j] == 2) {// add DestructibleWall
                    DestructibleWall destructibleWall = new DestructibleWall(
                            (double) (frame.getWidth() - (c * l)) / 2 + (j * l),
                            (double) (frame.getHeight() - (r - 1) * w) / 2 + i * w, (double) l, (double) w,
                            gameSettings.getWallHealth(), "crateWood");
                    loadedMap.getDestructibleWalls().add(destructibleWall);
                    continue;
                }
                if (mapPattern[i][j] == 0) {// add road
                    Road road = new Road((double) (frame.getWidth() - (c * l)) / 2 + (j * l),
                            (double) (frame.getHeight() - (r - 1) * w) / 2 + i * w, (double) l, (double) w,
                            roadNameMaker(i, j));
                    loadedMap.getRoads().add(road);
                    continue;
                }
            }
        }
    }

    /**
     * find the road Image name as its situation
     * 
     * @param i location of the road
     * @param j
     * @return the imagename of the road
     */
    private String roadNameMaker(int i, int j) {
        String roadname = "Grass_";
        boolean up, down, left, right;
        up = down = left = right = true;

        if (i - 1 < 0 || mapPattern[i - 1][j] == 1 || mapPattern[i - 1][j] == 2) {// for up side of road
            up = false;
        }
        if (i + 1 >= loadedMap.getRow() || mapPattern[i + 1][j] == 1 || mapPattern[i + 1][j] == 2) {// for down side of
                                                                                                    // road
            down = false;
        }
        if (j - 1 < 0 || mapPattern[i][j - 1] == 1 || mapPattern[i][j - 1] == 2) {// for left side of road
            left = false;
        }
        if (j + 1 >= loadedMap.getColumn() || mapPattern[i][j + 1] == 1 || mapPattern[i][j + 1] == 2) {// for right side
                                                                                                       // of road
            right = false;
        }
        if (up) {
            roadname += "U";
        }
        if (down) {
            roadname += "D";
        }
        if (left) {
            roadname += "L";
        }
        if (right) {
            roadname += "R";
        }
        return roadname;
    }

    public Map getLoadedMap() {
        return loadedMap;
    }
}