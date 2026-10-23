package es.cristichi.obj;

import java.awt.Color;

public class Kaiju extends GameObject {
    private String species;
    private StatBlock statBlock;

    public Kaiju(String species, StatBlock statBlock, char textDisplay, Color colorDisplay) {
        super(textDisplay, colorDisplay);
        this.species = species;
        this.statBlock = statBlock;
    }

    @Override
    public boolean canBeReplacedBy(GameObject other) {
        return false;
    }

    @Override
    public boolean stopsRangedAttacks(GameObject other) {
        return true;
    }

    public String getSpecies() {
        return species;
    }

    public StatBlock getStatBlock() {
        return statBlock;
    }
}
