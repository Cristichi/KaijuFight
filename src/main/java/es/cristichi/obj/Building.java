package es.cristichi.obj;

import java.awt.Color;

import es.cristichi.obj.game.AttackResult;
import es.cristichi.obj.game.AttackType;

public class Building extends GameObject {

    public Building() {
        super('B', new Color(100, 100, 100));
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
        return false;
    }

    @Override
    public AttackResult attack(AttackType type, GameObject other) {
        throw new UnsupportedOperationException("Buildings can't attack.");
    }

}
