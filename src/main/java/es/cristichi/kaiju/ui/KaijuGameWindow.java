package es.cristichi.kaiju.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import es.cristichi.kaiju.game.Game;
import es.cristichi.kaiju.game.obj.AttackInfo;
import es.cristichi.kaiju.game.obj.AttackType;
import es.cristichi.kaiju.game.obj.Kaiju;
import es.cristichi.kaiju.game.obj.Board.Movement;

public class KaijuGameWindow extends JFrame{
    private final Thread gameThread;
    private Game game;

    private JLabel currentAction;
    private JPanel controls;
    private CardLayout controlCards;
    private BoardComponent boardComponent;
    private JPanel attackControls;

    public KaijuGameWindow(Game game, String title){
        super(title);
        this.game = game;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        currentAction = new JLabel("Loading");
        currentAction.setHorizontalAlignment(JLabel.CENTER);
        add(currentAction, BorderLayout.NORTH);

        boardComponent = new BoardComponent(this);
        add(boardComponent, BorderLayout.CENTER);

        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);

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
                    try {
                        KaijuGameWindow.this.game.sendInput(btnAccept);
                    }catch (IllegalStateException _){
                    }
                }
            });
            KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
            KeyStroke spaceKey = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false);
            controls.getInputMap().put(enterKey, "ACCEPT");
            controls.getInputMap().put(spaceKey, "ACCEPT");
            controls.getActionMap().put("ACCEPT", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent evt){
                    btnAccept.doClick();
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
            KeyStroke backKey = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, false);
            KeyStroke spaceKey = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false);
            controls.getInputMap().put(backKey, "STAY");
            controls.getInputMap().put(spaceKey, "STAY");
            controls.getActionMap().put("STAY", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent evt){
                    btnStay.doClick();
                }
            });
            centeredMoveControls.add(btnStay);

            JButton btnUp = new JButton("↑");
            btnUp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(Movement.UP);
                }
            });
            KeyStroke upKey = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false);
            KeyStroke wKey = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false);
            controls.getInputMap().put(upKey, "UP");
            controls.getInputMap().put(wKey, "UP");
            controls.getActionMap().put("UP", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent evt){
                    btnUp.doClick();
                }
            });
            centeredMoveControls.add(btnUp);

            centeredMoveControls.add(new JPanel());

            JButton btnLeft = new JButton("←");
            btnLeft.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(Movement.LEFT);
                }
            });
            KeyStroke leftKey = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false);
            KeyStroke aKey = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false);
            controls.getInputMap().put(leftKey, "LEFT");
            controls.getInputMap().put(aKey, "LEFT");
            controls.getActionMap().put("LEFT", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent evt){
                    btnLeft.doClick();
                }
            });
            centeredMoveControls.add(btnLeft);

            JButton btnDown = new JButton("↓");
            btnDown.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(Movement.DOWN);
                }
            });
            KeyStroke downKey = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false);
            KeyStroke sKey = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false);
            controls.getInputMap().put(downKey, "DOWN");
            controls.getInputMap().put(sKey, "DOWN");
            controls.getActionMap().put("DOWN", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent evt){
                    btnDown.doClick();
                }
            });
            centeredMoveControls.add(btnDown);

            JButton btnRight = new JButton("→");
            btnRight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KaijuGameWindow.this.game.sendInput(Movement.RIGHT);
                }
            });
            KeyStroke rightKey = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false);
            KeyStroke dKey = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false);
            controls.getInputMap().put(rightKey, "RIGHT");
            controls.getInputMap().put(dKey, "RIGHT");
            controls.getActionMap().put("RIGHT", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent evt){
                    btnRight.doClick();
                }
            });
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

    public void showAttackControls(Kaiju attacker, Collection<AttackInfo> possibleAttacks){
        attackControls.removeAll();
        for (AttackInfo info : possibleAttacks) {
            JButton atk = new JButton("[%s] attack %s (%d,%d)"
                .formatted(info.type().equals(AttackType.MELEE)?"Melee":"Ranged",
                info.objGameObject().getName(),
                info.objPos().row(),
                info.objPos().col())
            );
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
                KaijuGameWindow.this.game.sendInput(new AttackInfo(AttackType.NO_ATTACK, null, null));
            }
        });
        attackControls.add(btnNoAttack);
        controlCards.show(controls, "attack");
        btnNoAttack.grabFocus();
    }

    public void startGame(){
        gameThread.start();
    }

}
