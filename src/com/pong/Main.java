package com.pong;

public class Main {

    public static void main(String[] args) {
        Player p1 = new Player(0, 160, 120, 15, true,1);
        Player p2 = new Player(468, 160, 120, 15, false,2);
        UI ui = new UI();
        ui.setP1(p1);
        ui.setP2(p2);
        ui.init();
    }
}