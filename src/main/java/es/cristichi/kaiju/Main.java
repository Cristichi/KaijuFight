package es.cristichi.kaiju;

import java.awt.Color;

import es.cristichi.kaiju.game.Game;
import es.cristichi.kaiju.game.io.ResourceException;
import es.cristichi.kaiju.game.obj.Board;
import es.cristichi.kaiju.game.obj.Building;
import es.cristichi.kaiju.game.obj.Kaiju;
import es.cristichi.kaiju.game.obj.Position;
import es.cristichi.kaiju.ui.KaijuGameWindow;

public class Main {
    public static void main(String[] args) {
        System.out.println("Kaiju game starting. Check the window.");
        
        Kaiju[] players;
        try {
            players = new Kaiju[]{
                new Kaiju("Cristichi", "Gummy", 55, 1, 3, 3, "Cristichi", "kaijus/gummy.png", Color.BLUE, false),
                new Kaiju("NoCris", "Gummy", 1, 1, 3, 3, "Falso Cristichi", "kaijus/gummy.png", Color.BLUE, false),
                new Kaiju("Strong Bot", "strong", 1, 2, 1, 3, "Strengther", null, Color.RED, true),
                new Kaiju("Fast Bot", "fast", 2, 1, 1, 3, "Speeder", null, Color.YELLOW, true)
            };
        } catch (ResourceException e) {
            e.printStackTrace();
            return;
        }

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

        Game game = new Game(board, players);
        KaijuGameWindow window = new KaijuGameWindow(game);
        window.setSize(1600, 1000);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        window.startGame();
    }
}