package View.GamePages;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Model.Countrollers.MainCountroller;
import Model.User.User;

import java.awt.event.*;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.*;

// import Model.FIO.*;

public class MainMenuPanel extends JPanel {
    private HashMap<String, BufferedImage> images;// add game Images
    private JFrame frame;// the main fram
    private JButton single, multi, signOut, setting, exit;
    private JLabel gameLabel, welcomeLabel;// shows Game name
    private User user;
    private MainCountroller mainCountroller;
    private int x;

    /**
     * Counstructor
     * 
     * @param images
     */
    public MainMenuPanel(HashMap<String, BufferedImage> images, JFrame frame, User user,
            MainCountroller mainCountroller) {
        this.frame = frame;
        this.images = images;
        this.user = user;
        this.mainCountroller = mainCountroller;

        setLayout(null);

        setBounds(0, 0, frame.getWidth(), frame.getHeight());
        x = getWidth() - 300;
        System.out.println(x);

        welcomeLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        welcomeLabel.setBounds(x, 10, 230, 35);
        add(welcomeLabel);

        ButtonActions buttonActions = new ButtonActions();

        single = new JButton("Single Player");// for single button
        single.setBounds(x, 50, 230, 35);
        single.addMouseListener(buttonActions);
        single.addActionListener(buttonActions);
        add(single);

        multi = new JButton("Multi Player");// for multi butoon
        multi.setBounds(x, 90, 230, 35);
        multi.addMouseListener(buttonActions);
        multi.addActionListener(buttonActions);
        add(multi);

        setting = new JButton("Game Setting");// for setting
        setting.setBounds(x, 130, 230, 35);
        setting.addMouseListener(buttonActions);
        setting.addActionListener(buttonActions);
        add(setting);

        signOut = new JButton("Sign Out");// for sign out
        signOut.setBounds(x, 170, 230, 35);
        signOut.addMouseListener(buttonActions);
        signOut.addActionListener(buttonActions);
        add(signOut);

        exit = new JButton("Exit");
        exit.setBounds(x, 210, 230, 35);
        exit.addMouseListener(buttonActions);
        exit.addActionListener(buttonActions);
        add(exit);

        gameLabel = new JLabel("The Tank Trouble");
        gameLabel.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 40));
        gameLabel.setVerticalAlignment(SwingConstants.CENTER);
        gameLabel.setForeground(Color.RED);
        gameLabel.setBounds(x-800, 20, 400, 150);
        add(gameLabel);

        frame.revalidate();
        frame.repaint();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(images.get("main"), 0, 0, frame.getWidth(), frame.getHeight(), null);

    }

    /**
     * ButtonActions
     */
    private class ButtonActions extends MouseAdapter implements ActionListener {
        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            if (e.getSource().equals(single)) {
                single.setBounds(x - 10, 50, 250, 35);
            }

            if (e.getSource().equals(multi)) {
                multi.setBounds(x - 10, 90, 250, 35);
            }

            if (e.getSource().equals(setting)) {
                setting.setBounds(x - 10, 130, 250, 35);

            }

            if (e.getSource().equals(signOut)) {
                signOut.setBounds(x - 10, 170, 250, 35);

            }
            if (e.getSource().equals(exit)) {
                exit.setBounds(x - 10, 210, 250, 35);

            }
            repaint();
            revalidate();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseDragged(e);
            if (e.getSource().equals(single)) {
                single.setBounds(x, 50, 230, 35);
            }

            if (e.getSource().equals(multi)) {
                multi.setBounds(x, 90, 230, 35);
            }

            if (e.getSource().equals(setting)) {
                setting.setBounds(x, 130, 230, 35);

            }

            if (e.getSource().equals(signOut)) {
                signOut.setBounds(x, 170, 230, 35);
            }
            if (e.getSource().equals(exit)) {
                exit.setBounds(x, 210, 230, 35);

            }
            repaint();
            revalidate();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(signOut)) {
                try {
                    mainCountroller.signOut(true, true);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (e.getSource().equals(exit)) {
                try {
                    mainCountroller.signOut(false, true);
                    System.exit(0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource().equals(setting)) {
                mainCountroller.settingMenu();
            }
            if (e.getSource().equals(single)) {
                mainCountroller.singleGamePanel();
            }
            if (e.getSource().equals(multi)) {
                mainCountroller.multiGamePanel();
            }
        }
    }
}