package com.pong;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Player p1 = new Player(0, 160, 120, 15, true, 1);
        Player p2 = new Player(468, 160, 120, 15, false, 2);
        UI ui = new UI();
        ui.setP1(p1);
        ui.setP2(p2);
        ui.init();
        while (true) {
            p1 = new Player(0, 160, 120, 15, true, 1);
            p2 = new Player(468, 160, 120, 15, false, 2);
            System.out.println(p1.getPoints());
            ui.setP1(p1);
            ui.setP2(p2);
            ui.reinit();
        }
    }
}