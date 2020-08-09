package Model.GameLogic.PlayerTypes;

import Model.Elements.Bullet;
import Model.GameLogic.GameSettings;
import Model.GameLogic.ThreadPool;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ComputerPlayer extends Player {
    private Random random;
    private int odd, even;
    private double m1, m2, t1, t2;
    private int start;

    /**
     * Counstructor
     *
     * @param name
     * @param gameSettings
     * @param playerSettings
     */
    public ComputerPlayer(String name, GameSettings gameSettings, PlayerSettings playerSettings) {
        super(name, gameSettings, playerSettings);

        random = new Random();
        fire = up = down = left = right = false;
        start = 0;
    }

    /**
     * This function generates four random numbers and determines the next move
     * according to them
     */
    @Override
    public void updatePlayerState() {
        if (start == 0) {
            ThreadPool.execute(new Gun());
            start++;
        }
        odd = even = 0;
        for (int i = 1; i <= 4; i++) {
            int temp = (random.nextInt(100 - 1) + 1);

            if (temp % 2 == 0) {
                even++;
            } else if (temp % 2 != 0) {
                odd++;
            }
        }
        m2 = System.currentTimeMillis() / (double) 1000;

        if (m1 != 0 && (m2 - m1 < 0.15)) {
            getMyTank().directionCalculator(up, down, left, right);
            updateBulletsDirection();
            return;

        }
        if (even == 4 || odd == 4) {
            up = right = true;
            left = down = false;
        } else if (even == 1) {
            up = left = true;
            right = down = false;
        } else if (even == 2) {
            down = right = true;
            up = left = false;
        } else if (even == 3) {
            down = left = true;
            up = right = false;
        }

        m1 = System.currentTimeMillis() / (double) 1000;
        getMyTank().directionCalculator(up, down, left, right);
        updateBulletsDirection();
    }

    /**
     * Gun for Shooting
     */
    public class Gun implements Runnable {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(2);

            } catch (Exception e) {
                e.printStackTrace();
            }
            while (true) {
                int temp = (random.nextInt(10));
                if (temp % 2 == 0) {
                    fire = true;
                }
                if (fire) {

                    t2 = (double) System.currentTimeMillis() / (double) 1000;

                    if (t1 != 0 && (t2 - t1 < 2)) {
                        continue;
                    }
                    Bullet newBullet = new Bullet(getMyTank().getBullet().getX2(), getMyTank().getBullet().getY2(),
                            getMyTank().getBullet().getAngle2(), getName(), getMyTank().getBullet().getImageName(),
                            getMyTank().getBullet().getPower(), getMyTank().getBullet().getSpeed(),
                            getMyTank().getBullet().getMaxTime());
                    getMyBullets().add(newBullet);
                    t1 = (double) System.currentTimeMillis() / (double) 1000;
                    fire = false;

                }
            }

        }
    }
}
