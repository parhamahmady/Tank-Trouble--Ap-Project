package Model.MapComponents;

import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable {
   private int row, column;// row and column of map
   // list of indestructibleWalls
   private ArrayList<IndestructibleWall> indestructibleWalls;
   // list of destructibleWalls
   private ArrayList<DestructibleWall> destructibleWalls;
   // list of roads
   private ArrayList<Road> roads;

   private double[] length, width;// index 0 : start of map ; 1: end of map
   private int[][] mapPattern;

   /**
    * map constructor for initialization of variables.
    */
   public Map() {
      indestructibleWalls = new ArrayList<>();
      destructibleWalls = new ArrayList<>();
      roads = new ArrayList<>();
   }

   public ArrayList<IndestructibleWall> getIndestructibleWalls() {
      return indestructibleWalls;
   }

   public ArrayList<DestructibleWall> getDestructibleWalls() {
      return destructibleWalls;
   }

   public ArrayList<Road> getRoads() {
      return roads;
   }

   public int getColumn() {
      return column;
   }

   public int getRow() {
      return row;
   }

   public void setColumn(int column) {
      this.column = column;
   }

   public void setRow(int row) {
      this.row = row;
   }

   public void setLength(double[] length) {
      this.length = length;
   }

   public double[] getLength() {

      return length;
   }

   public void setWidth(double[] width) {
      this.width = width;
   }

   public double[] getWidth() {
      return width;
   }

   public void setDestructibleWalls(ArrayList<DestructibleWall> destructibleWalls) {
      this.destructibleWalls = destructibleWalls;
   }

   public void setIndestructibleWalls(ArrayList<IndestructibleWall> indestructibleWalls) {
      this.indestructibleWalls = indestructibleWalls;
   }

   public void setRoads(ArrayList<Road> roads) {
      this.roads = roads;
   }

   public void setMapPattern(int[][] mapPattern) {
      this.mapPattern = mapPattern;
   }

   public int[][] getMapPattern() {
      return mapPattern;
   }
}
