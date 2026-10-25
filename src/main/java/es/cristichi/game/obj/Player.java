package es.cristichi.game.obj;

import java.awt.Color;
import java.awt.image.BufferedImage;

import es.cristichi.game.GameObject;

public class Player extends Kaiju {
    private String nickname;
    private boolean cpu;

    public Player(String nickname, boolean cpu, String species, int speed, int damage, int mana, int health, String textDisplay, BufferedImage image, Color colorDisplay) {
        super(species, speed, damage, mana, health, textDisplay, image, colorDisplay);

        this.nickname = nickname;
        this.cpu = cpu;
    }

    public Player(String nickname, boolean cpu, String textDisplay, Kaiju kaiju){
        this(nickname, cpu, kaiju.getSpecies(), kaiju.getSpeed(), kaiju.getDamage(), kaiju.getMana(), kaiju.getHealth(), textDisplay, kaiju.getImage(), kaiju.getColorDisplay());
    }

    public Player(String nickname, boolean cpu, Kaiju kaiju){
        this(nickname, cpu, kaiju.getSpecies(), kaiju.getSpeed(), kaiju.getDamage(), kaiju.getMana(), kaiju.getHealth(), kaiju.getTextDisplay(), kaiju.getImage(), kaiju.getColorDisplay());
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isCpu() {
        return cpu;
    }

    @Override
    public boolean canBeReplacedBy(GameObject other) {
        if (other instanceof Player){
            return false;
        }
        return super.canBeReplacedBy(other);
    }

}
