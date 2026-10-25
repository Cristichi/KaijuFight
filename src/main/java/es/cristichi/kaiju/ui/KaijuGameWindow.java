package es.cristichi.kaiju.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.cristichi.kaiju.game.Game;
import es.cristichi.kaiju.game.obj.AttackInfo;
import es.cristichi.kaiju.game.obj.AttackType;
import es.cristichi.kaiju.game.obj.Player;
import es.cristichi.kaiju.game.obj.Board.Movement;

public class KaijuGameWindow extends JFrame{
    private final Thread gameThread;
    private Game game;

    private JLabel currentAction;
    private JPanel controls;
    private CardLayout controlCards;
    private BoardComponent boardComponent;
    private JPanel attackControls;

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
        {
            JPanel noControls = new JPanel(new FlowLayout());
            JLabel lblWaiting = new JLabel("Waiting");
            noControls.add(lblWaiting);
            controls.add(noControls, "nocontrols");
        }
        {
            JPanel acceptControls = new JPanel(new BorderLayout());
            JButton btnAccept = new JButton("OK");
            btnAccept.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(btnAccept);
                }
            });
            acceptControls.add(btnAccept, BorderLayout.CENTER);
            controls.add(acceptControls, "accept");
        }
        {
            JPanel moveControls = new JPanel(new FlowLayout());
            JPanel centeredMoveControls = new JPanel(new GridLayout(2, 3));

            JButton btnStay = new JButton("Stay");
            btnStay.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(null);
                }
            });
            JButton btnUp = new JButton("↑");
            btnUp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(Movement.UP);
                }
            });
            JButton btnLeft = new JButton("←");
            btnLeft.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(Movement.LEFT);
                }
            });
            JButton btnDown = new JButton("↓");
            btnDown.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(Movement.DOWN);
                }
            });
            JButton btnRight = new JButton("→");
            btnRight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(Movement.RIGHT);
                }
            });
            
            centeredMoveControls.add(btnStay);
            centeredMoveControls.add(btnUp);
            centeredMoveControls.add(new JPanel());
            centeredMoveControls.add(btnLeft);
            centeredMoveControls.add(btnDown);
            centeredMoveControls.add(btnRight);

            moveControls.add(centeredMoveControls);
            controls.add(moveControls, "move");
        }
        {
            attackControls = new JPanel(new FlowLayout());
            controls.add(attackControls, "attack");
        }
        
        gameThread = this.game.readyGame(boardComponent);
    }

    public Game getGame() {
        return game;
    }

    public void updateTopText(String text){
        currentAction.setText(text);
        boardComponent.revalidate();
        repaint();
    }

    public void removeControls(){
        controlCards.show(controls, "nocontrols");
        attackControls.removeAll();
    }

    public void showAcceptControls(){
        controlCards.show(controls, "accept");
    }


    public void showMoveControls(){
        controlCards.show(controls, "move");
    }

    public void showAttackControls(Player attacker, Collection<AttackInfo> possibleAttacks){
        attackControls.removeAll();
        for (AttackInfo info : possibleAttacks) {
            JButton atk = new JButton("[%s] attack %s".formatted(info.type().equals(AttackType.MELEE)?"Melee":"Ranged", info.objective().getName()));
            atk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(info);
                }
            });
            atk.setEnabled(info.type().equals(AttackType.MELEE) || attacker.getMana()>=1);
            attackControls.add(atk);
        }
        JButton btnNoAttack = new JButton("Don't Attack");
        btnNoAttack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KaijuGameWindow.this.game.sendInput(new AttackInfo(AttackType.NO_ATTACK, null));
            }
        });
        attackControls.add(btnNoAttack);
        controlCards.show(controls, "attack");
    }

    public void startGame(){
        gameThread.start();
    }

}
