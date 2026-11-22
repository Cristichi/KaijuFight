package es.cristichi.kaiju.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.cristichi.kaiju.game.obj.carry.Carryable;
import es.cristichi.kaiju.game.obj.carry.CarryableFactory;

public class RewardWindow extends JFrame{
    protected Semaphore windowSem;
    protected CarryableFactory<? extends Carryable> chosen;
    
    public RewardWindow(){
        this(new LinkedList<>());
    }

    public RewardWindow(Collection<CarryableFactory<? extends Carryable>> choices){
        super("Rewarding time!");
        windowSem = new Semaphore(0);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        JLabel textDisplay = new JLabel("Hover over an item to see what it does!");
        textDisplay.setHorizontalAlignment(JLabel.CENTER);
        getContentPane().add(textDisplay, BorderLayout.NORTH);

        JPanel choicePanel = new JPanel(new FlowLayout());
        getContentPane().add(choicePanel, BorderLayout.CENTER);

        for (CarryableFactory<? extends Carryable> choiceType : choices) {
            JButton choiceBtn = new JButton(choiceType.getName());
            choiceBtn.setToolTipText(choiceType.getDesc());
            choiceBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    chosen = choiceType;
                    RewardWindow.this.dispose();
                    windowSem.release();
                }
            });
            choicePanel.add(choiceBtn);
        }

        JButton noChoiceBtn = new JButton("Skip");
        noChoiceBtn.setToolTipText("You don't carry a reward with you.");
        noChoiceBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RewardWindow.this.dispose();
                windowSem.release();
            }
        });
        choicePanel.add(noChoiceBtn);
    }

    public CarryableFactory<? extends Carryable> waitForChosen(){
        try {
            windowSem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return chosen;
    }
}
