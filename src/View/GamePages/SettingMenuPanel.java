package View.GamePages;

import java.awt.Graphics;
// import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.*;
import Model.Countrollers.MainCountroller;
import Model.GameLogic.GameSettings;

import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.text.DecimalFormat;

/**
 * The Setting menu
 */
public class SettingMenuPanel extends JPanel implements ActionListener {
    private JLabel userNameLabel, winAPc, loseAPc, winAL, loseAL, tankType, settingLabel, minPlayed;
    private JComboBox tankHealth, bulletPower, inDestructiveWallHealth;
    private JButton back;
    private HashMap<String, BufferedImage> images;
    private JFrame frame;
    private MainCountroller mainCountroller;

    /**
     * The Counstructor
     */
    public SettingMenuPanel(HashMap<String, BufferedImage> images, JFrame frame, MainCountroller mainCountroller) {
        this.images = images;
        this.frame = frame;
        this.mainCountroller = mainCountroller;

        setLayout(null);

        settingLabel = new JLabel("GameSetting", SwingConstants.CENTER);
        settingLabel.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 30));
        settingLabel.setBounds(frame.getWidth() / 2, 20, 400, 40);
        settingLabel.setForeground(Color.LIGHT_GRAY);
        add(settingLabel);

        userNameLabel = new JLabel("UserName : " + mainCountroller.getUser().getUserName(), SwingConstants.LEFT);
        userNameLabel.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 20));
        userNameLabel.setBounds(100, 40, 500, 40);
        userNameLabel.setForeground(Color.lightGray);
        add(userNameLabel);

        winAPc = new JLabel("Win Against Pc : " + mainCountroller.getUser().getWinAndLost()[0], SwingConstants.LEFT);
        winAPc.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 20));
        winAPc.setBounds(100, 80, 500, 40);
        winAPc.setForeground(Color.lightGray);
        add(winAPc);

        loseAPc = new JLabel("Lost Against Pc : " + mainCountroller.getUser().getWinAndLost()[1], SwingConstants.LEFT);
        loseAPc.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 20));
        loseAPc.setBounds(100, 120, 500, 40);
        loseAPc.setForeground(Color.lightGray);
        add(loseAPc);

        winAL = new JLabel("Win at Local : " + mainCountroller.getUser().getWinAndLost()[2], SwingConstants.LEFT);
        winAL.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 20));
        winAL.setBounds(100, 160, 500, 40);
        winAL.setForeground(Color.lightGray);
        add(winAL);

        loseAL = new JLabel("Lost at Local : " + mainCountroller.getUser().getWinAndLost()[3], SwingConstants.LEFT);
        loseAL.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 20));
        loseAL.setBounds(100, 200, 500, 40);
        loseAL.setForeground(Color.lightGray);
        add(loseAL);

        tankType = new JLabel("TankType : " + mainCountroller.getUser().getTankType(), SwingConstants.LEFT);
        tankType.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 20));
        tankType.setBounds(100, 240, 500, 40);
        tankType.setForeground(Color.lightGray);
        add(tankType);

        minPlayed = new JLabel("MinPlayed(At this Server) : " + mainCountroller.getUser().getHourPlayed(),
                SwingConstants.LEFT);
        minPlayed.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 20));
        minPlayed.setBounds(100, 280, 500, 40);
        minPlayed.setForeground(Color.lightGray);
        add(minPlayed);

        back = new JButton("Back To MainMenu");
        back.setBounds(frame.getWidth() / 2, frame.getHeight() - 300, 300, 35);
        back.addActionListener(this);
        add(back);

        JLabel tHLabel = new JLabel("Tank Health");
        tHLabel.setBounds(1100, 60, 100, 20);
        tHLabel.setForeground(Color.lightGray);
        add(tHLabel);

        String[] temp = { "30", "40", "60", "80", "100", "120" };
        tankHealth = new JComboBox<String>(temp);
        tankHealth.setSelectedItem(Integer.toString(mainCountroller.getGameSettings().getTankMaxHealth()));
        tankHealth.setBounds(1200, 60, 50, 20);
        add(tankHealth);

        JLabel bPLabel = new JLabel("Bullet Power");
        bPLabel.setBounds(1100, 80, 100, 20);
        bPLabel.setForeground(Color.lightGray);
        add(bPLabel);
        String[] temp2 = { "10", "20", "30" };
        bulletPower = new JComboBox<String>(temp2);
        bulletPower.setSelectedItem(Integer.toString(mainCountroller.getGameSettings().getBulletPower()));
        bulletPower.setBounds(1200, 80, 50, 20);
        add(bulletPower);

        JLabel wHLabel = new JLabel("Wall Health");
        wHLabel.setBounds(1100, 100, 100, 20);
        wHLabel.setForeground(Color.lightGray);
        add(wHLabel);

        inDestructiveWallHealth = new JComboBox<String>(temp);
        inDestructiveWallHealth.setSelectedItem(Integer.toString(mainCountroller.getGameSettings().getWallHealth()));
        inDestructiveWallHealth.setBounds(1200, 100, 50, 20);
        add(inDestructiveWallHealth);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(images.get("setting"), 0, 0, frame.getWidth(), frame.getHeight(), null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(back)) {
            GameSettings temp = new GameSettings(0, Integer.parseInt((String) tankHealth.getSelectedItem()),
                    Integer.parseInt((String) bulletPower.getSelectedItem()),
                    Integer.parseInt((String) inDestructiveWallHealth.getSelectedItem()));
            mainCountroller.setGameSettings(temp);
            mainCountroller.changePage(frame, this, mainCountroller.getgameFrame().getMainMenuPanel());
        }
    }

}