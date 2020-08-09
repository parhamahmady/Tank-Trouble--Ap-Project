package View.LogInPage;

import javax.swing.*;

import Model.Countrollers.MainCountroller;

import java.awt.*;

public class LoginPage {
    private LeftPanel leftPanel;
    private RightPanel rightPanel;
    private MiddlePanel middlePanel;
    private MainCountroller controller;// controler of MVC
    private JFrame frame;

    public LoginPage() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        newComponent();
        addComponent();
        frame.setLayout(new GridLayout(1, 3));
        frame.setSize(1400, 750);
        frame.setBackground(Color.pink);
        frame.setVisible(true);
    }

    public void newComponent() {
        leftPanel = new LeftPanel();
        rightPanel = new RightPanel();
        middlePanel = new MiddlePanel();

    }

    public void addComponent() {
        frame.add(leftPanel);
        frame.add(middlePanel);
        frame.add(rightPanel);
        frame.revalidate();
        frame.repaint();
    }

    public MiddlePanel getMiddlePanel() {
        return middlePanel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setController(MainCountroller controller) {
        this.controller = controller;
        middlePanel.setController(controller);
    }
}
