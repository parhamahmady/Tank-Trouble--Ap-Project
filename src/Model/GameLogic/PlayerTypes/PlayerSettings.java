package Model.GameLogic.PlayerTypes;

/**
 * Class for each player settings and personalization
 */
public class PlayerSettings {
    private String tankType;
    private int fireKey;// enum of Player OwnFireKey
    private int teamNumber;

    /**
     * Counstructor for online and team game;
     * 
     * @param tT tankType
     * @param fK FireKey
     * @param tN Team Number
     */
    public PlayerSettings(String tT, int fK, int tN) {
        tankType = tT;
        fireKey = fK;
        teamNumber = tN;
    }

    /**
     * Counstructor for Offline mode or None team Game
     * 
     * @param tT tankType
     * @param fK FireKey
     */
    public PlayerSettings(String tT, int fK) {
        tankType = tT;
        fireKey = fK;
        teamNumber = 0;
    }

    public int getFireKey() {
        return fireKey;
    }

    public String getTankType() {
        return tankType;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }
}