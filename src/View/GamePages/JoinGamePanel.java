package View.GamePages;

import java.awt.Graphics;
import java.util.HashSet;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import Model.Countrollers.MainCountroller;
import Model.GameLogic.PlayerTypes.PlayerSettings;
import Model.Network.Client;
import java.awt.*;
import java.awt.event.*;

public class JoinGamePanel extends SinglePlayerPanel {
    private HashSet<String> availableGames;
    private Client client;
    private JComboBox<String> games, teamNumber;
    private int count = 0;

    public JoinGamePanel() {

    }

    public JoinGamePanel(MainCountroller mainCountroller, JFrame frame, Client client) {
        super(mainCountroller, frame, mainCountroller.getgameFrame().getImages());
        availableGames = new HashSet<String>();
        this.client = client;
        getAvailableGames();
        remove(tankHealth);
        remove(inDestructiveWallHealth);
        remove(bulletPower);
        remove(endType);
        remove(gameNumber);
        remove(tHLabel);
        remove(bPLabel);
        remove(wHLabel);
        remove(eTLabel);
        remove(gN);
        if (availableGames.size() == 0) {
            remove(play);
        }
        JLabel aGLabel = new JLabel("AvailableGames", SwingConstants.CENTER);
        aGLabel.setBounds(600, 120, 200, 20);
        aGLabel.setForeground(Color.RED);
        add(aGLabel);
        String[] aGamesName = new String[availableGames.size()];
        int i = 0;
        for (String string : availableGames) {
            aGamesName[i] = string;
            i++;
        }
        games = new JComboBox<String>(aGamesName);
        games.setBounds(785, 120, 200, 20);
        add(games);

        JLabel teamNumberLabel = new JLabel("Your Team Number");
        teamNumberLabel.setForeground(Color.RED);
        teamNumberLabel.setBounds(600, 160, 200, 20);
        add(teamNumberLabel);
        String[] teams = new String[6];
        for (int j = 0; j < teams.length; j++) {
            teams[j] = Integer.toString(j + 1);
        }
        teamNumber = new JComboBox<String>(teams);
        teamNumber.setBounds(785, 160, 100, 20);
        add(teamNumber);

    }

    /**
     * Get AvailableGamse From Server
     */
    private void getAvailableGames() {
        try {
            availableGames = client.getAvailableGames();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(images.get("multi"), 0, 0, frame.getWidth(), frame.getHeight(), null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(play)) {
            if (count == 0) {// to make sure only one frame will make
                FireKeyRequestFrame fireKeyRequestFrame = new FireKeyRequestFrame();
                fireKeyRequestFrame.getFrame2().setVisible(false);
                fireKeyRequestFrame.getFrame2().removeKeyListener(fireKeyRequestFrame);
                fireKeyRequestFrame.getFrame2().addKeyListener(new Key(fireKeyRequestFrame.getFrame2()));
                fireKeyRequestFrame.getFrame2().setVisible(true);
                count++;
            }

        }
        if (e.getSource().equals(back)) {
            super.actionPerformed(e);
        }
    }

    /**
     * Save Settings
     */
    private void saveChanges() {
        PlayerSettings playerSettings = new PlayerSettings((String) tankType.getSelectedItem(), fireKey,
                Integer.parseInt((String) teamNumber.getSelectedItem()));
        mainCountroller.setPlayerSettings(playerSettings);

    }

    /**
     * Tell Controller To ad player To the Game
     */
    private void joinSelectedGame() {
        saveChanges();
        frame.remove(this);
        mainCountroller.joinMultiPlayerGame((String) games.getSelectedItem());
    }

    /**
     * key listener fot fire Chooser
     */
    private class Key extends KeyAdapter {
        private JFrame frame;
        // private static boolean isBuild = false;// for singleTon

        public Key(JFrame frame) {
            this.frame = frame;
            // isBuild = true;
        }

        @Override
        public void keyPressed(KeyEvent e) {

            super.keyPressed(e);
            fireKey = e.getKeyCode();
            joinSelectedGame();
            frame.dispose();
        }

    }
}