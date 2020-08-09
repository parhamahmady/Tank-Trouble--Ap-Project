package View.GamePages;

import java.awt.Graphics;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.Rectangle;
import Model.Countrollers.MainCountroller;
import Model.GameLogic.GameSettings;
import Model.GameLogic.PlayerTypes.PlayerSettings;

/**
 * MakeGamePanel
 */
public class MakeGamePanel extends SinglePlayerPanel {
    private JTextField gameName;
    private JComboBox<String> teamOrSolo, minSoloPlayer, minTeamPlayer, teamNumber;
    private int count = 0;

    public MakeGamePanel() {

    }

    /**
     * Counstructor
     * 
     * @param mainCountroller
     * @param frame
     * @param images
     */
    public MakeGamePanel(MainCountroller mainCountroller, JFrame frame, HashMap<String, BufferedImage> images) {
        super(mainCountroller, frame, images);

        JLabel gameSettingsLabel = new JLabel("Game Settings", SwingConstants.CENTER);
        gameSettingsLabel.setForeground(Color.RED);
        gameSettingsLabel.setBounds(650, 20, 200, 20);
        add(gameSettingsLabel);

        JLabel gameNameLabel = new JLabel("GameName");
        gameNameLabel.setForeground(Color.RED);
        gameNameLabel.setBounds(600, 40, 100, 20);
        add(gameNameLabel);
        gameName = new JTextField("GameName");
        gameName.setBounds(700, 40, 100, 20);
        add(gameName);

        gN.setBounds(600, 140, 200, 20);
        gN.setForeground(Color.RED);
        gameNumber.setBounds(785, 140, 60, 20);

        JLabel teamOSLabel = new JLabel("Team Or Solo");
        teamOSLabel.setForeground(Color.RED);
        teamOSLabel.setBounds(600, 160, 150, 20);
        add(teamOSLabel);
        String[] tOS = { "TeamGame", "SoloGame" };
        teamOrSolo = new JComboBox<String>(tOS);
        teamOrSolo.setBounds(700, 160, 150, 20);
        add(teamOrSolo);

        JLabel mSP = new JLabel("Number of Player (Solo)");
        mSP.setBounds(600, 180, 200, 20);
        mSP.setForeground(Color.RED);
        add(mSP);
        String[] minSpNumbers = new String[5];
        for (int i = 0; i < minSpNumbers.length; i++) {
            minSpNumbers[i] = Integer.toString(i + 2);
        }
        minSoloPlayer = new JComboBox<String>(minSpNumbers);
        minSoloPlayer.setBounds(785, 180, 100, 20);
        add(minSoloPlayer);

        JLabel mTP = new JLabel("Number of Player (Team)");
        mTP.setBounds(600, 200, 200, 20);
        mTP.setForeground(Color.RED);
        add(mTP);
        String[] minTpNumbers = new String[3];
        for (int i = 0; i < minTpNumbers.length; i++) {
            minTpNumbers[i] = Integer.toString(2 * i + 2);
        }
        minTeamPlayer = new JComboBox<String>(minTpNumbers);
        minTeamPlayer.setBounds(785, 200, 100, 20);
        add(minTeamPlayer);

        JLabel playerSettingsLabel = new JLabel("Player Settings", SwingConstants.CENTER);
        playerSettingsLabel.setForeground(Color.blue);
        playerSettingsLabel.setBounds(650, 220, 200, 20);
        add(playerSettingsLabel);

        tankType.setBounds(700, 240, 100, 20);
        tTLabel.setBounds(600, 240, 100, 20);

        JLabel teamNumberLabel = new JLabel("Your Team Number");
        teamNumberLabel.setForeground(Color.RED);
        teamNumberLabel.setBounds(600, 260, 200, 20);
        add(teamNumberLabel);
        String[] teams = new String[6];
        for (int i = 0; i < teams.length; i++) {
            teams[i] = Integer.toString(i + 1);
        }
        teamNumber = new JComboBox<String>(teams);
        teamNumber.setBounds(785, 260, 100, 20);
        add(teamNumber);

        play.removeAll();
        play.addActionListener(this);

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
        int end = 1;
        String temp = (String) endType.getSelectedItem();
        if (temp.equals("League Match")) {
            end = 2;
        }
        int mp = Integer.parseInt((String) minSoloPlayer.getSelectedItem());
        int tOS = 2;
        if (teamOrSolo.getSelectedItem().equals("TeamGame")) {
            mp = Integer.parseInt((String) minTeamPlayer.getSelectedItem());
            tOS = 1;
        }
        GameSettings gameSettings = new GameSettings(gameName.getText(), end,
                Integer.parseInt((String) tankHealth.getSelectedItem()),
                Integer.parseInt((String) bulletPower.getSelectedItem()),
                Integer.parseInt((String) inDestructiveWallHealth.getSelectedItem()), mp, tOS);

        if (end == 2) {
            gameSettings.setNumberofLeagueGames(Integer.parseInt((String) gameNumber.getSelectedItem()));
        }
        mainCountroller.setGameSettings(gameSettings);

    }

    /**
     * tell mainCountroller to make game ; if coudlnt make stay at this page
     */
    private void startMultiPlayer() {
        saveChanges();
        try {
            if (mainCountroller.makeMultiPlayerGame()) {
                frame.remove(this);
                return;
            } else
                JOptionPane.showMessageDialog(frame, "Info not Valid", "!!!!", JOptionPane.ERROR_MESSAGE);
            mainCountroller.getgameFrame().add(mainCountroller.getgameFrame().getMainMenuPanel());
            mainCountroller.getgameFrame().repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Cant Make", "!!!!", JOptionPane.ERROR_MESSAGE);
            mainCountroller.getgameFrame().add(mainCountroller.getgameFrame().getMainMenuPanel());
            mainCountroller.getgameFrame().repaint();

        }

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
            startMultiPlayer();
            frame.dispose();
        }

    }
}