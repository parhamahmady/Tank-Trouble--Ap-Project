package View.GamePages;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import Model.Countrollers.MainCountroller;
import Model.Network.Client;

/**
 * The page for MultiPlaying settings
 */
public class MultiPlayerPanel extends JPanel implements ActionListener {
    private JComboBox<String> serverChoose;// combo box for chossing server
    private JButton makeGameButton, joinGameButton, back;// two buttons of page
    private JFrame frame;// the gameFrame
    private MainCountroller mainCountroller;
    private HashMap<String, BufferedImage> images;
    private HashMap<String, HashMap<String, Integer>> serverList;

    public MultiPlayerPanel(JFrame frame, MainCountroller mainCountroller, HashMap<String, BufferedImage> images) {
        this.frame = frame;
        this.mainCountroller = mainCountroller;
        this.images = images;

        setLayout(null);

        JLabel sCLabel = new JLabel("Choose The Server !", SwingConstants.CENTER);// label for comboBox
        sCLabel.setForeground(Color.RED);
        sCLabel.setBounds(frame.getWidth() / 2 - 200, 100, 200, 40);
        add(sCLabel);
        try {
            serverList = mainCountroller.loadServerList();
            String[] servers = mainCountroller.findServernames(serverList);
            serverChoose = new JComboBox<String>(servers);
            serverChoose.setBounds(frame.getWidth() / 2, 100, 300, 30);
            serverChoose.addActionListener(this);
            add(serverChoose);
        } catch (Exception e) {
            e.printStackTrace();
        }
        makeGameButton = new JButton("Make new Game");
        makeGameButton.setBounds(frame.getWidth() / 2 - 100, 250, 200, 30);
        makeGameButton.addActionListener(this);
        add(makeGameButton);

        joinGameButton = new JButton("Join a Game");
        joinGameButton.setBounds(frame.getWidth() / 2 - 100, 300, 200, 30);
        joinGameButton.addActionListener(this);
        add(joinGameButton);

        back = new JButton("Back to MainMenu");
        back.setBounds(frame.getWidth() / 3, 500, 200, 30);
        back.addActionListener(this);
        add(back);
    }

    /**
     * Draw Background Image
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(images.get("multi"), 0, 0, frame.getWidth(), frame.getHeight(), null);

    }

    /**
     * Actions of Buttons
     * 
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(makeGameButton)) {
            connectTOSelecredServer();
            MakeGamePanel makeGamePanel = new MakeGamePanel(mainCountroller, frame, images);
            mainCountroller.changePage(frame, this, makeGamePanel);
        }
        if (e.getSource().equals(joinGameButton)) {
            connectTOSelecredServer();
            JoinGamePanel joinGamePanel = new JoinGamePanel(mainCountroller, frame, mainCountroller.getClient());
            mainCountroller.changePage(frame, this, joinGamePanel);
        }
        if (e.getSource().equals(back)) {
            mainCountroller.changePage(frame, this, mainCountroller.getgameFrame().getMainMenuPanel());
        }
    }

    private void connectTOSelecredServer() {
        String ip, port;
        ip = "";
        port = "";
        Set<String> key = serverList.get((String) serverChoose.getSelectedItem()).keySet();
        for (String string : key) {
            port = Integer.toString(serverList.get((String) serverChoose.getSelectedItem()).get(string));
            ip = string;
        }
        if (mainCountroller.getClient().getSocket().getPort() == Integer.parseInt(port)) {
            System.out.println("Same");
            return;
        }
        String username = mainCountroller.getUser().getUserName();
        String passWord = mainCountroller.getUser().getPassword();
        String ip2 = mainCountroller.getClient().getSocket().getLocalAddress().toString().substring(1);
        String port2 = Integer.toString(mainCountroller.getClient().getSocket().getPort());
        try {

            mainCountroller.signOut(false, false);// first SignOut
            mainCountroller.submit(username, passWord, port, ip, false, false);// signIn to the Next

        } catch (Exception e) {
            mainCountroller.submit(username, passWord, port2, ip2, false, true);
        }

    }
}