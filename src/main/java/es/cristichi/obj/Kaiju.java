package es.cristichi.obj;

import java.awt.Color;

import es.cristichi.obj.game.AttackResult;
import es.cristichi.obj.game.AttackType;

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

    @Override
    public boolean canBeAttackedBy(AttackType type, GameObject other) {
        return true;
    }

    @Override
    public AttackResult attack(AttackType type, GameObject other) {
        return new AttackResult(0, statBlock.getDamage());
    }
}
