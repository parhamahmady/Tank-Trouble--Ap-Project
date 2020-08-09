package View.GameFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.util.*;
import java.awt.event.*;
import Model.Countrollers.MainCountroller;
import Model.Elements.Bullet;
import Model.Elements.Tank;
import Model.Elements.Upgrade;
import Model.FIO.Loader;
import Model.GameLogic.*;
import Model.GameLogic.PlayerTypes.Player;
import Model.MapComponents.DestructibleWall;
import Model.MapComponents.IndestructibleWall;
import Model.MapComponents.Map;
import Model.MapComponents.Road;
import View.GamePages.MainMenuPanel;

import java.awt.geom.AffineTransform;

public class GameFrame extends JFrame {

    private Loader loader;
    private BufferStrategy bufferStrategy;
    private HashMap<String, BufferedImage> images;;
    private MainMenuPanel mainMenuPanel;
    private Graphics2D graphics2d;
    private MainCountroller mainCountroller;

    /**
     * The Counstructor For the First Time
     * 
     * @param mainCountroller
     */
    public GameFrame(MainCountroller mainCountroller) {

        super("Tank");
        setExtendedState(JFrame.MAXIMIZED_BOTH);// set the game Maximum
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        setVisible(true);
        initBufferStrategy();
        try {
            loader = new Loader("Files");
            images = new HashMap<String, BufferedImage>();
            images = loader.loadGameImages();

        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("ReStart");
        this.mainCountroller = mainCountroller;
        mainMenuPanel = new MainMenuPanel(images, this, mainCountroller.getUser(), mainCountroller);
        add(mainMenuPanel);
        repaint();
        revalidate();

    }

    /**
     * This must be called once after the JFrame is shown: frame.setVisible(true);
     * and before any rendering is started.
     */
    public void initBufferStrategy() {
        // Triple-buffering
        createBufferStrategy(3);
        bufferStrategy = getBufferStrategy();

    }

    /**
     * Game rendering with triple-buffering using BufferStrategy.
     */
    public void render(GameState state) {
        // Render single frame
        do {
            // The following loop ensures that the contents of the drawing buffer
            // are consistent in case the underlying surface was recreated
            do {
                // Get a new graphics context every time through the loop
                // to make sure the strategy is validated
                Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
                try {
                    doRendering(graphics, state);
                } finally {
                    // Dispose the graphics
                    graphics.dispose();
                }
                // Repeat the rendering if the drawing buffer contents were restored
            } while (bufferStrategy.contentsRestored());

            // Display the buffer
            bufferStrategy.show();
            // Tell the system to do the drawing NOW;
            // otherwise it can take a few extra ms and will feel jerky!
            Toolkit.getDefaultToolkit().sync();

            // Repeat the rendering if the drawing buffer was lost
        } while (bufferStrategy.contentsLost());
    }

    /**
     * Rendering all game elements based on the game state.
     */
    private void doRendering(Graphics2D g, GameState gameState) {

        // Draw background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        graphics2d = g;

        drawPlayerStates(g, gameState);

        drawMap(g, gameState);

        drawUpgrades(g, gameState);

        drawTanks(g, gameState);

        drawBullets(g, gameState);

        if (gameState.isEnd()) {// check the end of the game to show the end messages
            showWinner(gameState);
            return;
        }
    }

    /**
     * Draw the Resault of game
     * 
     * @param g
     * @param gameState
     */
    private void drawPlayerStates(Graphics2D g, GameState gameState) {
        Iterator<Player> it = gameState.getPlayers().iterator();
        int i = 0;// index counter
        while (it.hasNext()) {
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(Font.BOLD).deriveFont(10.0f));
            Player player = it.next();

            if (player.getMyTank().getHealth() <= 0) {// check the activity of player
                continue;
            }
            g.drawImage(images.get(player.getMyTank().getImageName()), getWidth() - 150, 100 * (i) + 50, 50, 50, null);// draw
                                                                                                                       // tank
            String name = player.getName();
            g.drawString(name, getWidth() - 100, 100 * i + 50);
            String health = "Health : " + player.getMyTank().getHealth();
            g.drawString(health, getWidth() - 100, 100 * i + 60);

            String bullet = "Bullet : ";
            int bPower;
            bPower = player.getMyTank().getBullet().getPower();

            int normalbPower = mainCountroller.getGameSettings().getBulletPower();
            if (bPower == normalbPower) {
                bullet += "NL";
            }

            if (bPower == 2 * normalbPower) {
                bullet += "2P";
            }
            if (bPower == 3 * normalbPower) {
                bullet += "3P";
            }
            if (bPower == 1000) {
                bullet += "LAZER";
            }
            g.drawString(bullet, getWidth() - 100, 100 * i + 70);

            String team = "" + (player.getTeamNumber() > 0 ? "Team :" + player.getTeamNumber() : "");
            g.drawString(team, getWidth() - 100, 100 * (i) + 80);

            String sheild = "" + (player.getMyTank().hasSheild() ? "SHIELD" : "");
            g.drawString(sheild, getWidth() - 100, 100 * (i) + 90);

            if (gameState.isleague()) {// if the league Match
                String temp = player.getName() + " (wins) : " + player.getWinNumber();
                g.drawString(temp, 30, 80 + 10 * i);
            }
            i++;
        }

    }

    /**
     * Draw All mapComponents acording to GameState
     * 
     * @param g
     * @param gameState
     */
    private void drawMap(Graphics2D g, GameState gameState) {
        Map map = gameState.getMap();

        String gameName = gameState.getGameName();
        g.drawString(gameName, 30, 50);

        String minPlayer = (gameState.getMinPlayer() == 0 ? ""
                : " Min Player : " + Integer.toString(gameState.getMinPlayer()));
        g.drawString(minPlayer, 30, 60);

        ArrayList<Road> roads = map.getRoads();
        for (Road road : roads) {// load roads
            g.drawImage(images.get(road.getImageName()), (int) road.getX(), (int) road.getY(), (int) road.getLength(),
                    (int) road.getWidth(), null);
        }

        ArrayList<DestructibleWall> destructibleWalls = map.getDestructibleWalls();
        for (DestructibleWall destructibleWall : destructibleWalls) {

            if (destructibleWall.getHeath() > 0) {// check health of wall
                g.drawImage(images.get(destructibleWall.getImageName()), (int) destructibleWall.getX(),
                        (int) destructibleWall.getY(), (int) destructibleWall.getLength(),
                        (int) destructibleWall.getWidth(), null);
            } else {
                g.drawImage(images.get("Grass_"), (int) destructibleWall.getX(), (int) destructibleWall.getY(),
                        (int) destructibleWall.getLength(), (int) destructibleWall.getWidth(), null);
            }
        }

        ArrayList<IndestructibleWall> indestructibleWalls = map.getIndestructibleWalls();
        for (IndestructibleWall indestructibleWall : indestructibleWalls) {

            g.drawImage(images.get(indestructibleWall.getImageName()), (int) indestructibleWall.getX(),
                    (int) indestructibleWall.getY(), (int) indestructibleWall.getLength(),
                    (int) indestructibleWall.getWidth(), null);

        }

    }

    /**
     * Draw not activated Upgrades
     * 
     * @param g
     * @param gameState
     */
    private void drawUpgrades(Graphics2D g, GameState gameState) {
        ArrayList<Upgrade> upgrades = gameState.getUpgrades();
        for (Upgrade upgrade : upgrades) {
            if (upgrade.getActivatedTime() == 0) {
                g.drawImage(images.get(upgrade.getImageName()), (int) upgrade.getX(), (int) upgrade.getY(),
                        (int) upgrade.getLength(), (int) upgrade.getWidth(), null);
            }
        }
    }

    /**
     * Draw all tanks of game State
     * 
     * @param gameState
     */
    private void drawTanks(Graphics2D g, GameState gameState) {
        ArrayList<Tank> tanks = gameState.getTanks();

        for (Tank tank : tanks) {
            BufferedImage image = images.get(tank.getImageName());
            AffineTransform curruntTransform = g.getTransform();// save Current Transform
            AffineTransform tankTrans = g.getTransform();// make a new transform for tank
            tankTrans.rotate(tank.getAngle2(), tank.getX2() + tank.getLength() / 2, tank.getY2() + tank.getWidth() / 2);

            g.setTransform(tankTrans);
            // System.out.println(tank.getWidth());
            g.drawImage(image, (int) tank.getX2(), (int) tank.getY2(), (int) tank.getLength(), (int) tank.getWidth(),
                    null);// draw Tank

            g.setTransform(curruntTransform);// reset the transform
        }
    }

    /**
     * Draw all bullets
     * 
     * @param g
     * @param gameState
     */
    private void drawBullets(Graphics2D g, GameState gameState) {
        ArrayList<Bullet> bullets = gameState.getBullets();

        for (Bullet bullet : bullets) {

            if (bullet.getImageName().contains("Lazer")) {// set the color of the bullet according to its type
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLACK);
            }
            g.fillOval((int) bullet.getX2(), (int) bullet.getY2(), 6, 6);

        }
    }

    /**
     * Draw a message to show winner
     * 
     * @param gameState
     */
    public void showWinner(GameState gameState) {

        graphics2d.setColor(Color.WHITE);
        graphics2d.setFont(graphics2d.getFont().deriveFont(Font.BOLD).deriveFont(20.0f));

        String text = "Player " + winnerNameFinder(gameState) + "Wins !!!!!";
        if (winnerNameFinder(gameState).isEmpty()) {
            graphics2d.setFont(graphics2d.getFont().deriveFont(Font.BOLD).deriveFont(40.0f));
            int strWidth = graphics2d.getFontMetrics().stringWidth(text);
            graphics2d.drawString("This is a Draw ", (getWidth() - strWidth) / 2, getHeight() / 2);
            return;
        }
        graphics2d.setFont(graphics2d.getFont().deriveFont(Font.BOLD).deriveFont(40.0f));
        int strWidth = graphics2d.getFontMetrics().stringWidth(text);
        graphics2d.drawString(text, (getWidth() - strWidth) / 2, getHeight() / 2);
    }

    /**
     * Find the name of the winner in the game
     * 
     * @param gameState
     * @return
     */
    private String winnerNameFinder(GameState gameState) {
        String name = "";
        Iterator<Player> it = gameState.getPlayers().iterator();
        while (it.hasNext()) {
            Player temp = it.next();
            if (temp.getMyTank().getHealth() <= 0) {
                continue;
            }
            name += temp.getName() + " "
                    + (temp.getTeamNumber() > 0 ? "(team : " + Integer.toString(temp.getTeamNumber()) + ")" : "");
        }
        return name;
    }

    public HashMap<String, BufferedImage> getImages() {
        return images;
    }

    public void setImages(HashMap<String, BufferedImage> images) {
        this.images = images;
    }

    public MainMenuPanel getMainMenuPanel() {
        return mainMenuPanel;
    }

    public BufferStrategy getBuffer() {
        return bufferStrategy;
    }

    /**
     * InnerGameFrame
     */
    public class InnerGameFrame extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            super.keyPressed(e);
            System.out.println("adasd");
        }

    }
}
