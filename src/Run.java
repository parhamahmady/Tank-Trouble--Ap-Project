import Model.Countrollers.*;
import Model.Elements.UpgradeManager;
import Model.GameLogic.Collision;
import Model.GameLogic.GameState;

import View.LogInPage.*;
import View.GamePages.*;

/**
 * To run the Project
 */
public class Run {
    public static void main(String[] args) {
        // Load frame

        // mainPage.getFrame().setVisible(false);
        // SinglePlayerPanel o=new SinglePlayerPanel();
        UpgradeManager upgradeManager = new UpgradeManager();
        LoginPage loginPage = new LoginPage();
        MainCountroller mainCountroller = new MainCountroller(loginPage);
        // GameState s=new GameState();
        Collision coll=new Collision();
        loginPage.setController(mainCountroller);
        // try {
        // mainCountroller.makeSocket(1234);
        // mainCountroller.signIn("usernsad", "pass", false);
        // mainCountroller.signOut();
        // // // mainCountroller.signIn("user", "pass");
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }
}