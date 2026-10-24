package es.cristichi.obj;

import es.cristichi.obj.game.AttackType;

public abstract interface AttackerObject {    
    public abstract int getAttackDamage(AttackType type);
}
