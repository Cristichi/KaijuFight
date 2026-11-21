package es.cristichi.kaiju;

import java.awt.Color;

import es.cristichi.kaiju.game.Game;
import es.cristichi.kaiju.game.io.ResourceException;
import es.cristichi.kaiju.game.obj.Board;
import es.cristichi.kaiju.game.obj.Building;
import es.cristichi.kaiju.game.obj.Kaiju;
import es.cristichi.kaiju.game.obj.Position;
import es.cristichi.kaiju.game.obj.carry.CarryableAdapter;
import es.cristichi.kaiju.ui.KaijuGameWindow;

public class Main {
    public static void main(String[] args) {
        System.out.println("Kaiju game starting. Check the window.");
        
        Kaiju[] players;
        try {
            players = new Kaiju[]{
                new Kaiju("Cristichi", "gummy", 4, 1, 3, 3, "Cristichi",
                    "kaijus/gummy.png", Color.MAGENTA, null, false),
                new Kaiju("Snail", "snail", 1, 1, 3, 3, "Random Snail",
                    "kaijus/snail.png", Color.BLUE, null, true),
                new Kaiju("Strong Bot", "strong", 1, 2, 1, 3, "Strengther",
                    "kaijus/robot.png", Color.RED, null, true),
                new Kaiju("Fast Bot", "fast", 2, 1, 1, 3, "Speeder",
                    "kaijus/robot.png", Color.YELLOW, null, true)
            };
        } catch (ResourceException e) {
            e.printStackTrace();
            return;
        }

        Board board = new Board.Builder(7, 7)
            .addSpawnPoint(new Position(0, 0))
            .addSpawnPoint(new Position(6, 0))
            .addSpawnPoint(new Position(0, 6))
            .addSpawnPoint(new Position(6, 6))
            .addObject(new Position(2, 2), new Building(3))
            .addObject(new Position(2, 4), new Building(3))
            .addObject(new Position(4, 2), new Building(3))
            .addObject(new Position(4, 4), new Building(3))
            .addObject(new Position(3, 3), new CarryableAdapter("banana", "Banana", null, Color.YELLOW){
                @Override
                public void onPickup(Kaiju kaiju) {
                    System.out.println("You got the banana! Speed +1");
                    kaiju.modifySpeed(1);
                }
            })
            .addSquareOfCopies(new Position(2, 0), new Position(4, 0), new Building(0))
            .addSquareOfCopies(new Position(0, 2), new Position(0, 4), new Building(0))
            .addSquareOfCopies(new Position(6, 2), new Position(6, 4), new Building(0))
            .addSquareOfCopies(new Position(2, 6), new Position(4, 6), new Building(0))
            .build();

        Game game = new Game(board, players);
        KaijuGameWindow window = new KaijuGameWindow(game);
        window.setSize(1600, 1000);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        window.startGame();
    }
}