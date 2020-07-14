package com.pong;


public class Player {

    private int x;
    private int y;
    private final int height;
    private final int width;
    private final boolean isLeftPlayer;
    private final int playerNumber;
    private volatile int points;

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Player(int x, int y, int height, int width, boolean isLeftPlayer, int playerNumber) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.isLeftPlayer = isLeftPlayer;
        this.playerNumber = playerNumber;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isLeftPlayer() {
        return isLeftPlayer;
    }
}