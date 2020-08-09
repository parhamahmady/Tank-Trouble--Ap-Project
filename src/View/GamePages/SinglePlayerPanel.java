package View.GamePages;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.*;
import Model.Countrollers.MainCountroller;
import Model.GameLogic.GameSettings;
import Model.GameLogic.PlayerTypes.PlayerSettings;

import java.awt.*;
import java.util.*;
import java.awt.image.*;

/**
 * SinglePlayerPanel
 */
public class SinglePlayerPanel extends JPanel implements ActionListener {

    protected JComboBox<String> tankHealth, bulletPower, inDestructiveWallHealth, endType, tankType, gameNumber;
    protected JButton back, play;
    protected HashMap<String, BufferedImage> images;
    protected JFrame frame;
    protected MainCountroller mainCountroller;
    protected int fireKey;
    protected JPanel panel;
    protected JLabel gN;// gameNumber Label
    protected JLabel tTLabel, tHLabel, bPLabel, wHLabel, eTLabel;

    public SinglePlayerPanel() {

    }

    /**
     * Counstructor
     * 
     * @param mainCountroller
     * @param frame
     * @param images
     */
    public SinglePlayerPanel(MainCountroller mainCountroller, JFrame frame, HashMap<String, BufferedImage> images) {
        this.images = images;
        this.mainCountroller = mainCountroller;
        this.frame = frame;

        setLayout(null);

        back = new JButton("Back To MainMenu");
        back.setBounds(frame.getWidth() / 2 - 500, frame.getHeight() - 300, 300, 35);
        // back.setBounds(1300, 300, 300, 35);
        back.addActionListener(this);
        add(back);

        play = new JButton("Start The Game");
        play.setBounds(frame.getWidth() / 2, frame.getHeight() - 300, 300, 35);
        // play.setBounds(1000, 300, 300, 35);
        play.addActionListener(this);
        add(play);

        tHLabel = new JLabel("Tank Health");
        tHLabel.setBounds(600, 60, 100, 20);
        tHLabel.setForeground(Color.RED);
        add(tHLabel);

        String[] temp = { "30", "40", "60", "80", "100", "120" };
        tankHealth = new JComboBox<String>(temp);
        tankHealth.setSelectedItem(Integer.toString(mainCountroller.getGameSettings().getTankMaxHealth()));
        tankHealth.setBounds(700, 60, 50, 20);
        add(tankHealth);

        bPLabel = new JLabel("Bullet Power");
        bPLabel.setBounds(600, 80, 100, 20);
        bPLabel.setForeground(Color.RED);
        add(bPLabel);
        String[] temp2 = { "10", "20", "30" };
        bulletPower = new JComboBox<String>(temp2);
        bulletPower.setSelectedItem(Integer.toString(mainCountroller.getGameSettings().getBulletPower()));
        bulletPower.setBounds(700, 80, 50, 20);
        add(bulletPower);

        wHLabel = new JLabel("Wall Health");
        wHLabel.setBounds(600, 100, 100, 20);
        wHLabel.setForeground(Color.RED);
        add(wHLabel);

        inDestructiveWallHealth = new JComboBox<String>(temp);
        inDestructiveWallHealth.setSelectedItem(Integer.toString(mainCountroller.getGameSettings().getWallHealth()));
        inDestructiveWallHealth.setBounds(700, 100, 50, 20);
        add(inDestructiveWallHealth);

        eTLabel = new JLabel("End Types");
        eTLabel.setBounds(600, 120, 100, 20);
        eTLabel.setForeground(Color.RED);
        add(eTLabel);
        String[] endTypes = { "Survivel", "League Match" };
        endType = new JComboBox<String>(endTypes);
        endType.setBounds(700, 120, 200, 20);
        endType.setSelectedIndex(0);
        add(endType);

        tTLabel = new JLabel("Tank Type");
        tTLabel.setBounds(600, 140, 100, 20);
        tTLabel.setForeground(Color.RED);
        add(tTLabel);
        String[] tTypes = { "tank_blue", "tank_green", "tank_red", "tank_sand" };
        tankType = new JComboBox<String>(tTypes);
        tankType.setBounds(700, 140, 200, 20);
        tankType.setSelectedIndex(2);
        add(tankType);

        gN = new JLabel("Number Of League Games");
        gN.setBounds(600, 160, 200, 20);
        add(gN);
        String[] gameNumbers = new String[19];
        for (int i = 2; i <= 20; i++) {
            gameNumbers[i - 2] = Integer.toString(i);
        }
        gameNumber = new JComboBox<String>(gameNumbers);
        gameNumber.setBounds(785, 160, 60, 20);
        add(gameNumber);
        panel = this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(images.get("single"), 0, 0, frame.getWidth(), frame.getHeight(), null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(back)) {
            // saveChanges();
            mainCountroller.changePage(frame, this, mainCountroller.getgameFrame().getMainMenuPanel());
        }
        if (e.getSource().equals(play)) {

            FireKeyRequestFrame fireKeyRequestFrame = new FireKeyRequestFrame();
        }
    }

    private void saveChanges() {
        int end = 1;
        String temp = (String) endType.getSelectedItem();
        if (temp.equals("League Match")) {
            end = 2;
        }
        PlayerSettings tempPlayerSettings = new PlayerSettings((String) tankType.getSelectedItem(), fireKey);

        GameSettings tempGameSettings = new GameSettings(end, Integer.parseInt((String) tankHealth.getSelectedItem()),
                Integer.parseInt((String) bulletPower.getSelectedItem()),
                Integer.parseInt((String) inDestructiveWallHealth.getSelectedItem()));
        if (endType.getSelectedItem().equals("League Match")) {
            tempGameSettings.setNumberofLeagueGames(Integer.parseInt((String) gameNumber.getSelectedItem()));
        }

        mainCountroller.setGameSettings(tempGameSettings);
        mainCountroller.setPlayerSettings(tempPlayerSettings);
    }

    /**
     * FireKeyRequestFrame
     */
    protected class FireKeyRequestFrame extends KeyAdapter {
        private JFrame frame2;

        public FireKeyRequestFrame() {
            frame2 = new JFrame();
            frame2.setLocationRelativeTo(mainCountroller.getgameFrame());
            frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame2.setResizable(false);
            frame2.setSize(500, 200);
            // frame2.setLayout(null);
            frame2.addKeyListener(this);
            JPanel panel2 = new JPanel();
            panel2.setLayout(null);
            JLabel label = new JLabel("Press the Key to Set your Fire Button");
            label.setBounds(100, 100, 400, 10);
            panel2.add(label);
            frame2.add(panel2);
            frame2.setVisible(true);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            fireKey = e.getKeyCode();
            saveChanges();
            mainCountroller.getgameFrame().remove(panel);
            mainCountroller.startSinglePlayerGame();

            frame2.dispose();
        }

        public JFrame getFrame2() {
            return frame2;
        }

    }
}