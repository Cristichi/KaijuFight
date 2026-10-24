package es.cristichi.obj;

import java.awt.Color;

import es.cristichi.obj.game.AttackResult;
import es.cristichi.obj.game.AttackType;

public class Kaiju extends GameObject implements AttackerObject {
    private String species;
    private StatBlock statBlock;
    private int health;

    public Kaiju(String species, StatBlock statBlock, String textDisplay, Color colorDisplay) {
        super(textDisplay, colorDisplay);
        this.species = species;
        this.statBlock = statBlock;
        health = statBlock.getMaxHealth();
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

    public int getHealth() {
        return health;
    }

    public boolean receiveDamage(int quantity){
        health-=quantity;
        if (health<=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeAttackedBy(AttackType type, GameObject other) {
        return true;
    }

    @Override
    public AttackResult receiveAttack(AttackType type, int quantity) {
        receiveDamage(quantity);
        return new AttackResult(0, quantity);
    }

    @Override
    public int getAttackDamage(AttackType type) {
        return statBlock.getDamage();
    }
}
