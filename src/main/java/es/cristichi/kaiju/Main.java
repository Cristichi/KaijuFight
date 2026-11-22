package es.cristichi.kaiju;

import java.awt.Color;

import es.cristichi.kaiju.game.Campaign;
import es.cristichi.kaiju.game.Game;
import es.cristichi.kaiju.game.io.ResourceException;
import es.cristichi.kaiju.game.obj.Board;
import es.cristichi.kaiju.game.obj.Building;
import es.cristichi.kaiju.game.obj.Kaiju;
import es.cristichi.kaiju.game.obj.Position;
import es.cristichi.kaiju.game.obj.carry.Banana;
import es.cristichi.kaiju.game.obj.carry.factory.TestFactory;

public class Main {
    public static void main(String[] args) {
        System.out.println("Kaiju game starting. Check the window.");
        
        Kaiju[] players;
        try {
            players = new Kaiju[]{
                new Kaiju("Cristichi", "gummy", 1, 1, 3, 3, "Cristichi",
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
            .addObject(new Position(3, 3), new Banana())
            .addSquareOfCopies(new Position(2, 0), new Position(4, 0), new Building(0))
            .addSquareOfCopies(new Position(0, 2), new Position(0, 4), new Building(0))
            .addSquareOfCopies(new Position(6, 2), new Position(6, 4), new Building(0))
            .addSquareOfCopies(new Position(2, 6), new Position(4, 6), new Building(0))
            .build();

        Board testBoard = new Board.Builder(3, 3)
            .addSpawnPoint(new Position(0, 0))
            .addSpawnPoint(new Position(2, 0))
            .addSpawnPoint(new Position(0, 2))
            .addSpawnPoint(new Position(2, 2))
            .build();

        Campaign myCampaña = new Campaign.Builder("My campaña", "Cristichi", players)
            .addGame(new Game(testBoard))
            .addGame(new Game(board))
            .addGame(new Game(board))
            .addReward(new TestFactory("Item 1"))
            .addReward(new TestFactory("Item 2"))
            .addReward(new TestFactory("Item 3"))
            .addReward(new TestFactory("Item 4"))
            .addReward(new TestFactory("Item 5"))
            .build();

        myCampaña.start();
    }
}