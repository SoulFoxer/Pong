package com.pong;


public class Player {

    private int x;
    private int y;
    private int height;
    private int width;
    private String name;

    public Player(int x, int y, int height, int width,String name) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}