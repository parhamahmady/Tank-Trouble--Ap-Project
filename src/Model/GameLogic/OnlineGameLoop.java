package Model.GameLogic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import Model.Countrollers.MainCountroller;
import Model.Elements.Upgrade;
import Model.GameLogic.PlayerTypes.HumanPlayer;
import Model.GameLogic.PlayerTypes.Player;
import Model.Network.Client;
import Model.Network.Message;
import View.GameFrame.GameFrame;

/**
 * Gameloop for client on Online Game
 */
public class OnlineGameLoop implements Runnable {
    private HumanPlayer player;
    private GameFrame gameFrame;
    private GameState gameState;
    private Client client;
    private MainCountroller mainCountroller;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean end, pause;
    private Message message;// message that the Server sends
    private boolean resume;

    public OnlineGameLoop(Player inPlayer, GameFrame gameFrame, MainCountroller mainCountroller, Client client) {

        this.gameFrame = gameFrame;
        this.mainCountroller = mainCountroller;
        this.client = client;
        player = (HumanPlayer) inPlayer;
        this.gameFrame.addKeyListener(player.getKeyActions());// set the key listener

        ois = client.getOis();
        oos = client.getOos();
        pause = false;
        resume = true;
    }

    @Override
    public void run() {
        try {

            while (resume) {
                message = (Message) ois.readObject();

                messageAnelyzer();
                if (!pause && resume) {

                    gameFrame.render(gameState);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Analyze the Header of message to manage Game
     * 
     * @param message sent from server
     * @throws IOException
     */
    private void messageAnelyzer() throws IOException {
        if (message.getHeader() == 1) {// befote Start
            Message message2 = new Message(1, "OK");
            oos.writeObject(message2);
            gameState = ((GameState) message.getBody());
            pause = true;
            RenderFrameWhilePause renderFrameWhilePause = new RenderFrameWhilePause();
            ThreadPool.execute(renderFrameWhilePause);
            return;
        }
        if (message.getHeader() == 2) {// while game

            gameState = ((GameState) message.getBody());
            pause = false;
            updatePlayerFields();

            HumanPlayer tempPlayer = makePlayerReadytoSend();// make ready

            Message message2 = new Message(2, tempPlayer);
            oos.writeObject(message2);// send

        }
        if (message.getHeader() == 3) {// end
            resume = false;
            gameState = ((GameState) message.getBody());
            pause = true;
            RenderFrameWhilePause renderFrameWhilePause = new RenderFrameWhilePause();
            ThreadPool.execute(renderFrameWhilePause);

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(gameFrame, "Error", "!!!!", JOptionPane.ERROR_MESSAGE);
            }

            pause = false;

            client.addWinOrLose(player.getName(), (int) message.getBody2(), (double) message.getBody3());// add Win or
                                                                                                         // Lost and
                                                                                                         // time

            mainCountroller.getUser().addWinAndLost((int) message.getBody2());

            mainCountroller.getUser()
                    .setHourPlayed(mainCountroller.getUser().getHourPlayed() + (double) message.getBody3());
            mainCountroller.getgameFrame().removeAll();
            mainCountroller.getgameFrame().dispose();
            GameFrame gameFrame = new GameFrame(mainCountroller);
            mainCountroller.setGameFrame(gameFrame);
            gameFrame.setVisible(true);

            return;
        }
        if (message.getHeader() == 100) {// emergancy
            resume = false;
            JOptionPane.showMessageDialog(gameFrame, "Error", "!!!!", JOptionPane.ERROR_MESSAGE);
            Message message2 = new Message(1, "OK");
            oos.writeObject(message2);
            // back to main menu
            mainCountroller.getgameFrame().add(mainCountroller.getgameFrame().getMainMenuPanel());
            mainCountroller.getgameFrame().repaint();
        }
    }

    /**
     * Update Players field by GameState
     */
    private void updatePlayerFields() {
        Iterator<Player> it = gameState.getPlayers().iterator();

        while (it.hasNext()) {
            Player player2 = it.next();
            if (player2.getName().equals(player.getName())) {
                player.setMyTank(player2.getMyTank());
                player.setMyBullets(player2.getMyBullets());
                // System.out.println("1 " + player2.getMyBullets().size());
                player.setWinNumber(player2.getWinNumber());
                player.updatePlayerState();
                return;
            }
        }

    }

    /**
     * Render Frame While Game is Paused
     */
    private class RenderFrameWhilePause implements Runnable {
        @Override
        public void run() {
            while (pause) {
                // System.out.println("Paused");
                gameFrame.render(gameState);
            }

        }

    }

    private HumanPlayer makePlayerReadytoSend() {
        HumanPlayer tempPlayer = new HumanPlayer(player.getName(), mainCountroller.getGameSettings(),
                mainCountroller.getPlayerSettings());
        tempPlayer.up = player.up;
        tempPlayer.down = player.down;
        tempPlayer.right = player.right;
        tempPlayer.left = player.left;
        tempPlayer.fire = player.fire;
        tempPlayer.setT1(player.getT1());
        tempPlayer.setMyBullets(player.getMyBullets());
        tempPlayer.setMyTank(player.getMyTank());
        tempPlayer.setWinNumber(player.getWinNumber());

        return tempPlayer;
    }
}