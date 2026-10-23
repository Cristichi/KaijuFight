package es.cristichi;

import es.cristichi.obj.Board;
import es.cristichi.obj.Game;
import es.cristichi.obj.GameObject;
import es.cristichi.obj.Kaiju;
import es.cristichi.obj.Player;
import es.cristichi.obj.StatBlock;
import es.cristichi.util.Color;
import es.cristichi.util.Position;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting");

        Kaiju testKaiju = new Kaiju("test", new StatBlock(1, 1, 1), 'T', null);

        Board board = new Board.Builder(3, 3)
            .addSpawnPoint(new Position(0, 0))
            .addSpawnPoint(new Position(2, 0))
            .addSpawnPoint(new Position(0, 2))
            .addSpawnPoint(new Position(2, 2))
            .addObject(new Position(1, 1), new GameObject('X', new Color(0, 0, 0)){

                @Override
                public boolean canBeReplacedBy(GameObject other) {
                    return false;
                }
                
            })
            .build();
        
        Player[] players = new Player[]{
            new Player("Cristichi", 3, false, 'C', testKaiju),
            new Player("Bot 1", 3, true, '1', testKaiju),
            new Player("Bot 2", 3, true, '2', testKaiju),
        };

        Game game = new Game(board, players);
        
        System.out.println(game.toString());
        Thread gameThread = game.readyGame();
        System.out.println(game.toString());

        gameThread.start();

        System.out.println("Closing");
    }
}