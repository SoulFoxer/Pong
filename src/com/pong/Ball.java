package com.pong;

public class Ball {

    private double x;
    private double y;
    private double width;
    private double height;
    private Vector velocity;

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Ball(double x, double y, double width, double height, Vector velocity) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocity = velocity.normalize();
    }

    public void move() {
        x += velocity.getX();
        y += velocity.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}