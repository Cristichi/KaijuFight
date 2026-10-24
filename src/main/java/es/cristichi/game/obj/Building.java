package es.cristichi.game.obj;

import java.awt.Color;

import es.cristichi.game.GameObject;

public class Building extends GameObject {

    public Building() {
        super("Building", new Color(100, 100, 100));
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
    public AttackResult receiveAttack(AttackType type, int quantity) {
        return new AttackResult(0, 0);
    }

}
