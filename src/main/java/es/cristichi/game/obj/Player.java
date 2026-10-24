package es.cristichi.game.obj;

import java.awt.Color;

import es.cristichi.game.GameObject;

public class Player extends Kaiju {
    private String nickname;
    private boolean cpu;

    public Player(String nickname, boolean cpu, String species, StatBlock statBlock, String textDisplay, Color colorDisplay) {
        super(species, statBlock, textDisplay, colorDisplay);

        this.nickname = nickname;
        this.cpu = cpu;
    }

    public Player(String nickname, boolean cpu, String textDisplay, Kaiju kaiju){
        this(nickname, cpu, kaiju.getSpecies(), kaiju.getStatBlock(), textDisplay, kaiju.getColorDisplay());
    }

    public Player(String nickname, boolean cpu, Kaiju kaiju){
        this(nickname, cpu, kaiju.getSpecies(), kaiju.getStatBlock(), kaiju.getTextDisplay(), kaiju.getColorDisplay());
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
