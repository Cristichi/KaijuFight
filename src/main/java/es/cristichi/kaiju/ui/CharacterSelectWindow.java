package es.cristichi.kaiju.ui;

import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import es.cristichi.kaiju.game.io.ResourceException;
import es.cristichi.kaiju.game.obj.Kaiju;

public class CharacterSelectWindow extends JFrame {
    protected Kaiju character;
    protected Semaphore characterChosen;

    protected JPanel errorPanel;
    protected Archetype archetype;
    
    public CharacterSelectWindow(String title) throws ResourceException {
        super(title);
        this.characterChosen = new Semaphore(0);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(10, 1, 0, 20));
        mainPanel.setBorder(new EmptyBorder(2, 4, 2, 4));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel nickPanel = new JPanel(new GridLayout(2, 1));
        nickPanel.add(new JLabel("Nickname:"));
        JTextField txtNick = new JTextField();
        nickPanel.add(txtNick);
        mainPanel.add(nickPanel);

        JPanel archPanel = new JPanel(new GridLayout(3, 1));
        archPanel.add(new JLabel("Species:"));
        JComboBox<Archetype> archBox = new JComboBox<>();
        for (Archetype archetype : Archetype.values()){
            archBox.addItem(archetype);
        }

        JLabel archStats = new JLabel();

        archBox.addActionListener((e) -> {
            Object selObj = archBox.getSelectedItem();
            if (selObj instanceof Archetype selArch){
                archStats.setText(selArch.getStats());
                archetype = selArch;

                archPanel.invalidate();
                archPanel.revalidate();
                archPanel.repaint();
            }
        });
        archBox.setSelectedIndex(0);

        archPanel.add(archBox);
        archPanel.add(archStats);
        mainPanel.add(archPanel);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        errorPanel = new JPanel(new BorderLayout());
        JButton submit = new JButton("Play with that!");
        submit.addActionListener((event) -> {
            bottomPanel.remove(errorPanel);
            errorPanel.removeAll();

            String nick = txtNick.getText().trim();
            if (nick.isBlank()){
                bottomPanel.add(errorPanel, BorderLayout.NORTH);
                JLabel errorTxt = new JLabel("Put something on your nick! Try something fancy.");
                errorTxt.setForeground(Color.RED);
                errorPanel.add(errorTxt);
            } else{
                try {
                    character = new Kaiju(nick, archetype.getSpeciesName(), archetype.getSpeed(), archetype.getDamage(),
                        archetype.getMana(), archetype.getHealth(), archetype.getResourceImgPath(), Color.WHITE, null, false);
                    characterChosen.release();
                    dispose();
                } catch (ResourceException e) {
                    e.printStackTrace();
                }
            }
            
            invalidate();
            revalidate();
            repaint();
        });
        bottomPanel.add(submit, BorderLayout.SOUTH);
    }

    public Kaiju waitForCharacter() {
        try {
            characterChosen.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return character;
    }

    private static enum Archetype {
        SNAIL("Snail", 1, 1, 3, 3, "kaijus/snail.png"),
        STRONG("Strong", 1, 2, 0, 3, "kaijus/robot.png"),
        ZOOMIES("Zoomies", 2, 1, 1, 3, "kaijus/zoomies.png"),
        ;
        private final String speciesName, resourceImgPath;
        private final int speed, damage, mana, health;

        private Archetype(String speciesName, int speed, int damage, int mana, int health, String resourceImgPath) {
            this.speciesName = speciesName;
            this.speed = speed;
            this.damage = damage;
            this.mana = mana;
            this.health = health;
            this.resourceImgPath = resourceImgPath;
        }

        public String getSpeciesName() {
            return speciesName;
        }

        public String getResourceImgPath() {
            return resourceImgPath;
        }

        public int getSpeed() {
            return speed;
        }

        public int getDamage() {
            return damage;
        }

        public int getMana() {
            return mana;
        }

        public int getHealth() {
            return health;
        }

        public String getStats(){
            return "Health %d, Damage %d, Speed %d, Mana %d".formatted(health, damage, speed, mana);
        }

        @Override
        public String toString() {
            return "%s (%s)".formatted(speciesName, getStats());
        }
    }
}
