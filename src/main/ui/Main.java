package ui;

import java.io.IOException;

/**
 * Starts a terminal instance of FRAMED
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // create and start the game
        FramedGame framed = new FramedGame();
        framed.startGame();
    }
}
