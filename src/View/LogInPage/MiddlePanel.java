package View.LogInPage;

import javax.swing.*;
import javax.swing.border.Border;

import Model.Countrollers.MainCountroller;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Set;
import java.awt.*;

public class MiddlePanel extends JPanel {
  private JTextField userName, password, portNumber;
  private JLabel login, pass, user, port, remember;
  private JButton submit;
  private JCheckBox checkBox;
  private JPanel panel1, panel2, panel3, panel4, buttomPanel, toppanel, panel4a;
  private Border border;
  private MainCountroller controller;
  private JComboBox<String> serverList;
  private HashMap<String, HashMap<String, Integer>> servers;

  public MiddlePanel() {
    border = BorderFactory.createLineBorder(Color.lightGray, 2);
    newComponent();
    addComponent();
    setPanel1();
    setPanel2();
    setPanel3();
    setPanel4();
    setColor();
    setSize(295, 680);
    setLocation(295, 0);
    setLayout(new GridLayout(7, 1));
    setBackground(Color.pink);
  }

  public void newComponent() {
    userName = new JTextField();
    login = new JLabel("Login", SwingConstants.CENTER);
    portNumber = new JTextField();
    Font font1 = new Font("SansSerif", Font.BOLD, 25);
    login.setFont(font1);
    login.setForeground(Color.gray);

    password = new JTextField();
    submit = new JButton();
    remember = new JLabel("Remember me!!:", SwingConstants.CENTER);
    checkBox = new JCheckBox();
    panel1 = new JPanel();
    panel2 = new JPanel();
    panel3 = new JPanel();
    panel4 = new JPanel();
    buttomPanel = new JPanel();
    toppanel = new JPanel();
    user = new JLabel("UserName:", SwingConstants.CENTER);
    pass = new JLabel("PassWord:", SwingConstants.CENTER);
    port = new JLabel("Server", SwingConstants.CENTER);
    loadServers();
    panel4a = new JPanel();
  }

  public void setPanel1() {
    Font font2 = new Font("SansSerif", Font.BOLD, 20);
    user.setFont(font2);

    panel1.setLayout(new GridLayout(2, 1));

    panel1.add(user);
    panel1.add(userName);
  }

  public void setPanel2() {
    Font font3 = new Font("SansSerif", Font.BOLD, 20);
    pass.setFont(font3);
    panel2.setLayout(new GridLayout(2, 1));
    // panel2.setBorder(border);
    panel2.add(pass);
    panel2.add(password);
  }

  public void setPanel3() {
    Font font4 = new Font("SansSerif", Font.BOLD, 20);
    serverList.setFont(font4);
    port.setFont(font4);
    panel3.setLayout(new GridLayout(2, 1));
    panel3.add(port);
    panel3.add(serverList);

  }

  public void setPanel4() {
    Font rememberfont = new Font("SansSerif", Font.BOLD, 15);
    remember.setFont(rememberfont);
    submit.setFont(rememberfont);
    submit.addActionListener(new SubmitAction());
    panel4a.setLayout(new GridLayout(1, 4));
    panel4a.add(new JPanel());
    panel4a.add(remember);
    panel4a.add(checkBox);
    panel4.setLayout(new GridLayout(2, 1));
    panel4.add(panel4a);
    panel4.add(submit);
    submit.setText("  Submit  ");

  }

  public void setColor() {
    toppanel.setBackground(Color.darkGray);
    pass.setForeground(Color.gray);
    submit.setForeground(Color.darkGray);
    remember.setForeground(Color.gray);
    user.setForeground(Color.pink);
    port.setForeground(Color.pink);
    remember.setBackground(Color.pink);
    submit.setBackground(Color.gray);
    buttomPanel.setBackground(Color.darkGray);

  }

  public void addComponent() {
    add(toppanel);
    add(login);
    add(panel1);
    add(panel2);
    add(panel3);
    add(panel4);
    add(buttomPanel);
    repaint();
    revalidate();
  }

  /**
   * SubmitAction for button action
   */
  private class SubmitAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource().equals(submit)) {
        int port = 0;
        String ip = "";
        Set<String> key = servers.get((String) serverList.getSelectedItem()).keySet();
        for (String string : key) {
          port = servers.get((String) serverList.getSelectedItem()).get(string);
          ip = string;
        }
        controller.submit(userName.getText(), password.getText(), Integer.toString(port), ip, checkBox.isSelected(),
            true);
      }

    }

  }

  public void setController(MainCountroller controller) {
    this.controller = controller;
  }

  private void loadServers() {
    try {
      MainCountroller controller2 = new MainCountroller();
      servers = controller2.loadServerList();
      serverList = new JComboBox<String>(controller2.findServernames(servers));

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
