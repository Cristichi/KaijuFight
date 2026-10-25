package es.cristichi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import es.cristichi.game.Game;
import es.cristichi.game.io.Resources;
import es.cristichi.game.obj.Board;
import es.cristichi.game.obj.Building;
import es.cristichi.game.obj.Kaiju;
import es.cristichi.game.obj.Player;
import es.cristichi.game.obj.Position;
import es.cristichi.ui.KaijuGameWindow;

public class Main {
    public static void main(String[] args) {
        System.out.println("Kaiju game starting. Check the window.");

        BufferedImage mageImage = null;
        try {
            mageImage = Resources.loadImage("kaijus/gummy.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Kaiju fastKaiju = new Kaiju("test", 2, 1, 3, 3, "Spe", null, Color.YELLOW);
        Kaiju strongKaiju = new Kaiju("test", 1, 2, 3, 3, "Str", null, Color.RED);
        Kaiju mageKaiju = new Kaiju("test", 5, 1, 20, 3, "Mana", mageImage, Color.BLUE);

        Board board = new Board.Builder(5, 5)
            .addSpawnPoint(new Position(0, 0))
            .addSpawnPoint(new Position(4, 0))
            .addSpawnPoint(new Position(0, 4))
            .addSpawnPoint(new Position(4, 4))
            .addObject(new Position(1, 1), new Building(3))
            .addObject(new Position(1, 3), new Building(3))
            .addObject(new Position(3, 1), new Building(3))
            .addObject(new Position(3, 3), new Building(3))
            .build();
        
        Player[] players = new Player[]{
            new Player("Cristichi", false, "Cristichi el mejor del mundo mundial de los nueve Avernos", mageKaiju),
            new Player("Falso Cris", false, "NoCris", mageKaiju),
            new Player("Bot 1", true, "Bot1", strongKaiju),
            new Player("Bot 2", true, "Bot2", fastKaiju),
        };

        Game game = new Game(board, players);
        KaijuGameWindow window = new KaijuGameWindow(game);
        window.setSize(1000, 1000);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        window.startGame();
    }
}