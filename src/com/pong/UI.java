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
    private boolean p1isMoveableUP;
    private boolean p1isMoveableDown;
    private boolean p2isMoveableUP;
    private boolean p2isMoveableDown;
    private boolean wisPressed;
    private boolean sisPressed;
    private boolean arrowUPisPressed;
    private boolean arrowDownisPressed;
    private boolean isP1Unhittable;
    private boolean isP2Unhittable;
    private String winMessage;
    private JFrame frame;
    private boolean running;
    private boolean isGameFinished;
    private int bestOf;

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
            g2d.drawString(winMessage, 60, 200);
            resetGame();
        }

        repaint();
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public void init() {
        final int width = 500;
        final int height = 500;
        frame = new JFrame();
        frame.setTitle("Pong" + "P1: " + p1.getPoints() + " P2: " + p2.getPoints());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        setBackground(Color.BLACK);
        frame.addKeyListener(this);
        frame.setLocationRelativeTo(null);
        setSize(new Dimension(width, height));
        frame.add(this);
        frame.setVisible(true);
        enablePlayerMovement();
        ball = new Ball(230, 200, 20, 20, new Vector(-1, 0.1));
        winMessage = "";
        running = true;
        bestOf = 5;


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
        Thread keyControllerThread = new Thread(keyController);
        keyControllerThread.start();


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

        Thread collisionBallThread = new Thread(collisionBall);
        collisionBallThread.start();
    }

    private synchronized void update() {

        checkWin();

        ball.move();
        isP1Unhittable = false;
        isP2Unhittable = false;

        if (collidesWithPlayer(p1)) {
            System.out.println(collisionLocation(p1) + " coordinate of p1");
            ball.getVelocity().setX(-ball.getVelocity().getX());
        }

        if (collidesWithPlayer(p2)) {
            System.out.println(collisionLocation(p2) + " coordinate of p2");
            ball.getVelocity().setX(-ball.getVelocity().getX());
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
        if (p1.getPoints() == bestOf && p2.getPoints() < bestOf) {
            winMessage = "Spieler " + p1.getPlayerNumber() + " gewinnt !";
            frame.setTitle("Pong" + "P1: " + p1.getPoints() + " P2: " + p2.getPoints());
            isGameFinished = true;

        }
        if (p2.getPoints() == bestOf && p1.getPoints() < bestOf) {
            winMessage = "Spieler " + p2.getPlayerNumber() + " gewinnt !";
            frame.setTitle("Pong" + "P1: " + p1.getPoints() + " P2: " + p2.getPoints());
            isGameFinished = true;
        }

        if (p1.getPoints() == bestOf && p2.getPoints() == bestOf) {
            winMessage = "Unentschieden";
            resetGame();
            isGameFinished = true;
        }

    }

    private boolean collidesWithPlayer(Player player) {
        if (isP1Unhittable || isP2Unhittable) {
            return false;
        }

        if (player.isLeftPlayer()) {
            if (ball.getX() <= player.getX() + player.getWidth()) {

                if (ball.getY() + (ball.getHeight() / 2) >= player.getY() && ball.getY() - (ball.getHeight() / 2) <= player.getY() + player.getHeight()) {

                    return true;
                } else {
                    isP1Unhittable = true;

                }
            }
            // is right player
        } else {
            if (ball.getX() >= player.getX() - player.getWidth()) {

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
        ball.setX(230);
        ball.setY(200);
        isP1Unhittable = false;
        isP2Unhittable = false;
    }

    private void resetGame() {
        p1.setPoints(0);
        p2.setPoints(0);
        frame.setTitle("Pong" + "P1: " + p1.getPoints() + " P2: " + p2.getPoints());
        resetBall();
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