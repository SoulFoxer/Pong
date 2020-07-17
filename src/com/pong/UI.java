package com.pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.awt.event.KeyEvent.*;

public class UI extends JPanel implements KeyListener {

    private Player p1;
    private Player p2;
    private Ball ball;
    private volatile boolean p1isMoveableUP;
    private volatile boolean p1isMoveableDown;
    private volatile boolean p2isMoveableUP;
    private volatile boolean p2isMoveableDown;
    private volatile boolean wisPressed;
    private volatile boolean sisPressed;
    private volatile boolean arrowUPisPressed;
    private volatile boolean arrowDownisPressed;
    private volatile boolean isP1Unhittable;
    private volatile boolean isP2Unhittable;
    private volatile boolean running;
    private volatile boolean isGameFinished;
    private int bestOf;
    private double maxSpeed;
    private String winMessage;
    private JFrame frame;
    private Thread keyControllerThread;                                     // having a refenrence to controll it from whereever in the code
    private Thread collisionBallThread;                                     // having a refenrence to controll it from whereever in the code

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isGameFinished) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(p1.getX(), p1.getY(), p1.getWidth(), p1.getHeight());
            g2d.setColor(Color.WHITE);
            g2d.fillRect(p2.getX(), p2.getY(), p2.getWidth(), p2.getHeight());
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) ball.getX(), (int) ball.getY(), (int) ball.getWidth(), (int) ball.getHeight());
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(234, 0, 234, 500);
        } else {
            g2d.setFont(new Font("TimesRoman", Font.BOLD, 40));
            g2d.setColor(new Color(255, 120, 0));
            int l = (int) g2d.getFontMetrics().getStringBounds(winMessage, g2d).getWidth(); // getting width of the winMessage
            g2d.drawString(winMessage, (frame.getWidth() / 2 - l / 2) - 15, 200);
            running = false;
        }
        repaint();
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public void init() throws InterruptedException {
        final int width = 500;
        final int height = 500;
        frame = new JFrame();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        setBackground(Color.BLACK);
        frame.addKeyListener(this);
        frame.setLocationRelativeTo(null);
        setSize(new Dimension(width, height));
        frame.add(this);
        frame.setVisible(true);
        bestOf = 4;
        maxSpeed = 2.74f;
        reinit();
    }

    public void reinit() throws InterruptedException {
        ball = new Ball(230, 200, 20, 20, new Vector(-1.0, 0.1));
        winMessage = "";
        frame.setTitle("Pong" + "P1: " + p1.getPoints() + " P2: " + p2.getPoints());
        p1isMoveableUP = false;
        p1isMoveableDown = false;
        p2isMoveableUP = false;
        p2isMoveableDown = false;
        wisPressed = false;
        sisPressed = false;
        arrowUPisPressed = false;
        arrowDownisPressed = false;
        isP1Unhittable = false;
        isP2Unhittable = false;
        isGameFinished = false;
        running = true;

        enablePlayerMovement();

        Runnable keyController = () -> {
            while (running) {
                if (wisPressed) {
                    checkScreenCollission();
                    if (p1isMoveableUP) {
                        p1.setY(p1.getY() - 5);
                    }
                }
                if (sisPressed) {
                    checkScreenCollission();
                    if (p1isMoveableDown) {
                        p1.setY(p1.getY() + 5);
                    }
                }
                if (arrowUPisPressed) {
                    checkScreenCollission();
                    if (p2isMoveableUP) {
                        p2.setY(p2.getY() - 5);
                    }
                }
                if (arrowDownisPressed) {
                    checkScreenCollission();
                    if (p2isMoveableDown) {
                        p2.setY(p2.getY() + 5);
                    }
                }
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable collisionBall = () -> {
            while (running) {
                update();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        collisionBallThread = new Thread(collisionBall);
        keyControllerThread = new Thread(keyController);
        keyControllerThread.start();
        collisionBallThread.start();
        collisionBallThread.join();                             // main process is waiting for the thread ( collisionBallThread ) to end  main thread will go on ...
        keyControllerThread.join();
        Thread.sleep(2000);
    }

    private double power(double base, int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("Exponent can´t be negative");
        }
        double res = 1.0;

        for (int i = 1; i <= exp; i++) {
            res = (res * base);
        }
        return res;
    }

    private synchronized void update() {
        checkWin();
        ball.move();
        isP1Unhittable = false;
        isP2Unhittable = false;

        if (collidesWithPlayer(p1)) {
            double maxDist = p1.getHeight() / 2 + ball.getWidth() / 2;
            double relativeDis = collisionLocation(p1) / maxDist;            // output will be something between 1 and -1
            relativeDis = power(relativeDis, 3);
            ball.getVelocity().setX(-ball.getVelocity().getX() + 0.2 * Math.abs(relativeDis));      // reverse direction and vector addition
            ball.getVelocity().setY(ball.getVelocity().getY() + relativeDis);
            ball.getVelocity().normalize(ball.getSpeed());
            if(ball.getSpeed() <maxSpeed){
                ball.setSpeed(ball.getSpeed() * 1.15555f);
                ball.getVelocity().normalize(ball.getSpeed());
            }
        }

        if (collidesWithPlayer(p2)) {
            double maxDist = p2.getHeight() / 2 + ball.getWidth() / 2;
            double relativeDis = collisionLocation(p2) / maxDist;            // output will be something between 1 and -1
            relativeDis = power(relativeDis, 3);
            ball.getVelocity().setX(-ball.getVelocity().getX() - 0.2 * Math.abs(relativeDis));    // reverse direction and vector addition
            ball.getVelocity().setY(ball.getVelocity().getY() + relativeDis);
            ball.getVelocity().normalize(ball.getSpeed());
            if(ball.getSpeed() <maxSpeed){
                ball.setSpeed(ball.getSpeed() * 1.15555f);
                ball.getVelocity().normalize(ball.getSpeed());
            }
        }

        // collision with up and down
        if ((ball.getY() + (ball.getHeight() / 2) >= 450) || ball.getY() - (ball.getHeight() / 2) <= 0) {
            ball.getVelocity().setY(-ball.getVelocity().getY());
        }

        // collision with the left side
        if (ball.getX() <= p1.getX()) {
            p2.setPoints(p2.getPoints() + 1);
            frame.setTitle("Pong" + "P1: " + p1.getPoints() + " P2: " + p2.getPoints());
            resetBall();
        }

        // collision with the right side
        if (ball.getX() >= p2.getX()) {
            p1.setPoints(p1.getPoints() + 1);
            frame.setTitle("Pong" + "P1: " + p1.getPoints() + " P2: " + p2.getPoints());
            resetBall();
        }
    }


    private void checkWin() {
        int pointLimit = (bestOf) / 2;
        if (p1.getPoints() == pointLimit + 1) {
            winMessage = "Spieler " + p1.getPlayerNumber() + " gewinnt !";
            frame.setTitle("Pong" + "P1: " + p1.getPoints() + " P2: " + p2.getPoints());
            isGameFinished = true;
        } else if (p2.getPoints() == pointLimit + 1) {
            winMessage = "Spieler " + p2.getPlayerNumber() + " gewinnt !";
            frame.setTitle("Pong" + "P1: " + p1.getPoints() + " P2: " + p2.getPoints());
            isGameFinished = true;
        } else if (p1.getPoints() + p2.getPoints() == bestOf) {
            winMessage = "Unentschieden";
            isGameFinished = true;
        }
    }

    private boolean collidesWithPlayer(Player player) {
        if (isP1Unhittable || isP2Unhittable) {
            return false;
        }

        if (player.isLeftPlayer()) {
            if (ball.getX() <= player.getX() + player.getWidth() && ball.getVelocity().getX() <= 0) {

                if (ball.getY() + (ball.getHeight() / 2) >= player.getY() && ball.getY() - (ball.getHeight() / 2) <= player.getY() + player.getHeight()) {
                    return true;
                } else {
                    isP1Unhittable = true;
                }
            }
            // is right player
        } else {
            if (ball.getX() >= player.getX() - player.getWidth() && ball.getVelocity().getX() >= 0) {

                if (ball.getY() + (ball.getHeight() / 2) >= player.getY() && ball.getY() - (ball.getHeight() / 2) <= player.getY() + player.getHeight()) {
                    return true;
                } else {
                    isP2Unhittable = true;
                }
            }
        }
        return false;
    }

    private double collisionLocation(Player player) {
        return ball.getY() - (player.getY() + (player.getHeight() / 2.0));
    }

    private void resetBall() {
        ball.setSpeed(1.0f);
        ball.getVelocity().normalize();
        ball.setX(230);
        ball.setY(200);
        isP1Unhittable = false;
        isP2Unhittable = false;
    }

    private void enablePlayerMovement() {
        p1isMoveableUP = true;
        p1isMoveableDown = true;
        p2isMoveableUP = true;
        p2isMoveableDown = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        enablePlayerMovement();
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case VK_W -> wisPressed = true;
            case VK_S -> sisPressed = true;
            case VK_UP -> arrowUPisPressed = true;
            case VK_DOWN -> arrowDownisPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case VK_W -> wisPressed = false;
            case VK_S -> sisPressed = false;
            case VK_UP -> arrowUPisPressed = false;
            case VK_DOWN -> arrowDownisPressed = false;
        }
    }

    private void checkScreenCollission() {
        if (p1.getY() <= 0) {
            p1isMoveableUP = false;
        }
        if (p1.getY() >= 340) {
            p1isMoveableDown = false;
        }
        if (p2.getY() <= 0) {
            p2isMoveableUP = false;
        }
        if (p2.getY() >= 340) {
            p2isMoveableDown = false;
        }
    }
}