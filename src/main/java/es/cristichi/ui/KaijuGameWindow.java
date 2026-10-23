package es.cristichi.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.cristichi.obj.game.Game;
import es.cristichi.obj.game.Board.Movement;

public class KaijuGameWindow extends JFrame{
    private final Thread gameThread;
    private Game game;

    private JLabel currentAction;
    private JPanel controls;
    private CardLayout controlCards;
    private BoardComponent boardComponent;

    public KaijuGameWindow(Game game){
        super("Kaiju Fight!");
        this.game = game;
                
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        currentAction = new JLabel("Loading");
        add(currentAction, BorderLayout.NORTH);

        boardComponent = new BoardComponent(this);
        add(boardComponent, BorderLayout.CENTER);

        controlCards = new CardLayout();
        controls = new JPanel(controlCards);
        add(controls, BorderLayout.SOUTH);

        JPanel noControls = new JPanel(new FlowLayout());
        JButton btnWaiting = new JButton("Waiting");
        btnWaiting.setEnabled(false);
        noControls.add(btnWaiting);
        controls.add(noControls, "nocontrols");

        JPanel acceptControls = new JPanel(new FlowLayout());
        JButton btnAccept = new JButton("OK");
        btnAccept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("OK");
                KaijuGameWindow.this.game.sendInput(btnAccept);
            }
        });
        acceptControls.add(btnAccept);
        controls.add(acceptControls, "accept");

        JPanel moveControls = new JPanel(new FlowLayout());
        for(Movement move : Movement.values()){
            JButton btnMove = new JButton(move.name());
            btnMove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(move);
                }
            });
            moveControls.add(btnMove);
        }
        controls.add(moveControls, "move");
        
        gameThread = this.game.readyGame(boardComponent);
    }

    public Game getGame() {
        return game;
    }

    public void updateTopText(String text){
        System.out.println("Updating text: "+text);
        currentAction.setText(text);
        boardComponent.revalidate();
        repaint();
    }

    public void removeControls(){
        controlCards.show(controls, "nocontrols");
    }

    public void showAcceptControls(){
        controlCards.show(controls, "accept");
    }


    public void showMoveControls(){
        controlCards.show(controls, "move");
    }

    public void startGame(){
        gameThread.start();
    }
}
