package es.cristichi;

import java.awt.Color;

import es.cristichi.obj.Building;
import es.cristichi.obj.Kaiju;
import es.cristichi.obj.Player;
import es.cristichi.obj.StatBlock;
import es.cristichi.obj.game.Board;
import es.cristichi.obj.game.Game;
import es.cristichi.ui.KaijuGameWindow;
import es.cristichi.util.Position;

public class Main {
    public static void main(String[] args) {
        System.out.println("Loading");

        Kaiju fastKaiju = new Kaiju("test", new StatBlock(2, 1, 1), 'T', Color.YELLOW);
        Kaiju strongKaiju = new Kaiju("test", new StatBlock(1, 2, 1), 'T', Color.RED);
        //Kaiju mageKaiju = new Kaiju("test", new StatBlock(1, 1, 2), 'T', Color.BLUE);

        Board board = new Board.Builder(5, 5)
            .addSpawnPoint(new Position(0, 0))
            .addSpawnPoint(new Position(4, 0))
            .addSpawnPoint(new Position(0, 4))
            .addSpawnPoint(new Position(4, 4))
            .addObject(new Position(1, 1), new Building())
            .addObject(new Position(1, 3), new Building())
            .addObject(new Position(3, 1), new Building())
            .addObject(new Position(3, 3), new Building())
            .build();
        
        Player[] players = new Player[]{
            new Player("Cristichi", 3, false, 'C', fastKaiju),
            new Player("Bot 1", 3, true, '1', strongKaiju),
            new Player("Bot 2", 3, true, '2', fastKaiju),
        };

        Game game = new Game(board, players);
        
        System.out.println(game.toString());
        KaijuGameWindow window = new KaijuGameWindow(game);
        
        System.out.println(game.toString());

        window.setSize(600, 500);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        window.startGame();
    }
}