package es.cristichi.obj;

import es.cristichi.util.Color;

public class Player extends Kaiju {
    private String nickname;
    private int lifes;
    private boolean cpu;

    public Player(String nickname, int lifes, boolean cpu, String species, StatBlock statBlock, char textDisplay, Color colorDisplay) {
        super(species, statBlock, textDisplay, colorDisplay);

        this.nickname = nickname;
        this.lifes = lifes;
        this.cpu = cpu;
    }

    public Player(String nickname, int lifes, boolean cpu, char textDisplay, Kaiju kaiju){
        this(nickname, lifes, cpu, kaiju.getSpecies(), kaiju.getStatBlock(), textDisplay, kaiju.getColorDisplay());
    }

    public Player(String nickname, int lifes, boolean cpu, Kaiju kaiju){
        this(nickname, lifes, cpu, kaiju.getSpecies(), kaiju.getStatBlock(), kaiju.getTextDisplay(), kaiju.getColorDisplay());
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isCpu() {
        return cpu;
    }

    public int getLifes() {
        return lifes;
    }

}
