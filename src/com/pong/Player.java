package com.pong;


public class Player {

    private int x;
    private int y;
    private int height;
    private int width;
    private boolean isLeftPlayer;

    public Player(int x, int y, int height, int width, boolean isLeftPlayer) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.isLeftPlayer = isLeftPlayer;
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