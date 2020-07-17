package com.pong;

public class Vector {

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector normalize(double speed) {
        if (x == 0 && y == 0) {
            throw new ArithmeticException("can´t normalize 0 zero vector");
        } else {
            double n = getNorm();
            x = speed * x / n;
            y = speed * y / n;
        }
        return this;
    }

    public Vector normalize() {
        if (x == 0 && y == 0) {
            throw new ArithmeticException("can´t normalize 0 zero vector");
        } else {
            double n = getNorm();
            x = x / n;
            y = y / n;
        }
        return this;
    }

    private double getNorm() {
        return Math.sqrt(x * x + y * y);
    }
}