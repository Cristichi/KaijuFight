package es.cristichi.game.obj;

import java.awt.Color;

import es.cristichi.game.GameObject;

public class Building extends GameObject {
    private static final Color MANAED_COLOR = new Color(100, 100, 200);
    private static final Color EMPTIED_COLOR = new Color(100, 100, 200);
    private int charges;

    public Building(int charges) {
        super("Building", charges>0?MANAED_COLOR:EMPTIED_COLOR);
        this.charges = charges;
    }

    @Override
    public boolean canBeReplacedBy(GameObject other) {
        return false;
    }

    @Override
    public boolean stopsRangedAttacks(GameObject other) {
        return true;
    }

    @Override
    public boolean canBeAttackedBy(AttackType type, GameObject other) {
        return type.equals(AttackType.MELEE);
    }

    @Override
    public AttackResult receiveAttack(AttackType type, AttackerObject attacker, int quantity) {
        if (charges>0 && attacker instanceof ManaUserObject manaUser){
            charges--;
            manaUser.modifyMana(1);
        }
        colorDisplay = charges>0?MANAED_COLOR:EMPTIED_COLOR;
        return new AttackResult(0, 0);
    }

}
