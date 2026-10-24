package es.cristichi.obj;

import java.awt.Color;

import es.cristichi.obj.game.AttackType;

public abstract class AttackerObject extends GameObject {

    public AttackerObject(char textDisplay, Color colorDisplay) {
        super(textDisplay, colorDisplay);
    }
    
    public abstract int getAttackDamage(AttackType type);
}
