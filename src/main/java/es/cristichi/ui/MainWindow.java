package es.cristichi.ui;

import javax.swing.JFrame;

import es.cristichi.obj.game.Game;

public class MainWindow extends JFrame{
    private final Thread gameThread;

    public MainWindow(Game game){
        super("Kaiju Fight!");
                
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BoardComponent boardComponent = new BoardComponent(game.getBoard());
        add(boardComponent);
        
        gameThread = game.readyGame(boardComponent);
    }

    public void startGame(){
        gameThread.start();
    }
}
