package es.cristichi;

import java.util.HashMap;

import es.cristichi.obj.Board;
import es.cristichi.obj.Game;
import es.cristichi.obj.GameObject;
import es.cristichi.obj.Kaiju;
import es.cristichi.util.Color;
import es.cristichi.util.Position;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting");

        Board board = new Board.Builder(3, 3)
            .addSpawnPoint(new Position(0, 0))
            .addSpawnPoint(new Position(2, 0))
            .addSpawnPoint(new Position(0, 2))
            .addSpawnPoint(new Position(2, 2))
            .addObject(new Position(1, 1), new GameObject('X', new Color(0, 0, 0)))
            .build();
        
        HashMap<String, Kaiju> players = new HashMap<>(2);
        players.put("Cristichi", new Kaiju('C', new Color(100, 0, 115)));
        players.put("Bot 1", new Kaiju('1', new Color(100, 100, 100)));
        players.put("Bot 2", new Kaiju('2', new Color(100, 100, 100)));

        Game game = new Game(board, players);
        
        System.out.println(game.toString());

        game.start();
        
        System.out.println(game.toString());

        System.out.println("Closing");
    }
}