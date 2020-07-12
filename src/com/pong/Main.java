package com.pong;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Player p1 = new Player(0, 160, 120, 15,true);
        Player p2 = new Player(468, 160, 120, 15,false);
        UI ui = new UI();
        ui.init();
        ui.setP1(p1);
        ui.setP2(p2);
    }
}