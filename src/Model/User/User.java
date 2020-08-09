package Model.User;

import java.io.Serializable;

/**
 * Every player in this game must creat their user acount this is the class that
 * contain their info;
 */
public class User implements Serializable {
    private final String userName, password;// these are unChangble
    private double hourPlayed;// number if hours that the player played this game
    private int[] winAndLost; // number of win and lost index: 0:Vs pc win 1:Vs pv lost
                              // 2:Vs user win 3:Vs user lost
    private String tankType;// type of User Tank

    /**
     * The Counstructor
     * 
     * @param userName
     * @param password
     */
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        hourPlayed = 0;
        winAndLost = new int[4];
        tankType = "Normal";
    }

    /**
     * 
     * @return array of win and lost
     */
    public int[] getWinAndLost() {
        return winAndLost;
    }

    /**
     * ++ the number of index
     * 
     * @param index specify the type of Win or lost
     */
    public void addWinAndLost(int index) {
        winAndLost[index]++;
    }

    /**
     * Set the Win and lost Not Add
     * 
     * @param index specify the type of win or lost
     * @param value the value of .....
     */
    public void setWinAndLost(int index, int value) {
        winAndLost[index] = value;
    }

    /**
     * 
     * @return Type of Tank
     */
    public String getTankType() {
        return tankType;
    }

    public void setTankType(String tankType) {
        this.tankType = tankType;
    }

    /**
     * 
     * @param addition is the time that will add
     */
    public void setHourPlayed(double addition) {
        hourPlayed += addition;
    }

    /**
     * 
     * @return HourPlayed
     */
    public double getHourPlayed() {
        return hourPlayed;
    }

    /**
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @return userName
     */
    public String getUserName() {
        return userName;
    }
}