package es.cristichi.kaiju.game.obj.carry;

import java.awt.Color;
import java.awt.image.BufferedImage;

import es.cristichi.kaiju.game.GameObject;
import es.cristichi.kaiju.game.obj.AttackResult;
import es.cristichi.kaiju.game.obj.AttackType;
import es.cristichi.kaiju.game.obj.AttackerObject;
import es.cristichi.kaiju.game.obj.Kaiju;

public class CarryableAdapter extends GameObject implements Carryable {
    public CarryableAdapter(String name, String textDisplay, BufferedImage image, Color colorDisplay) {
        super(name, textDisplay, image, colorDisplay);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void onPickup(Kaiju kaiju) {
    }

    @Override
    public void onStartGame(Kaiju kaiju) {
    }

    @Override
    public AttackResult onDamageCalc(AttackType type, AttackerObject attacker, Kaiju defender, int damageToAttacker,
            int damageToDefender) {
        return new AttackResult(damageToAttacker, damageToDefender);
    }

    @Override
    public void onReceiveDamage(Kaiju kaiju) {
    }

    @Override
    public void onMove(Kaiju kaiju) {
    }

    @Override
    public boolean canBeReplacedBy(GameObject other) {
        return other instanceof Kaiju;
    }

    @Override
    public void beforeGettingReplacedBy(GameObject other) {
        if (other instanceof Kaiju kaiju){
            kaiju.giveItem(this);
        }
    }

    @Override
    public boolean canBeAttackedBy(AttackType type, GameObject other) {
        return false;
    }

    @Override
    public AttackResult receiveAttack(AttackType type, AttackerObject attacker, int quantity) {
        throw new UnsupportedOperationException("I can't be attacked!");
    }

    @Override
    public boolean stopsRangedAttacks(GameObject other) {
        return false;
    }
    
}
