package es.cristichi.kaiju.game.obj.carry;

import es.cristichi.kaiju.game.obj.AttackResult;
import es.cristichi.kaiju.game.obj.AttackType;
import es.cristichi.kaiju.game.obj.AttackerObject;
import es.cristichi.kaiju.game.obj.Kaiju;

public interface Carryable {
    String getName();
    String getDesc();

    void onPickup(Kaiju kaiju);
    void onStartGame(Kaiju kaiju);
    AttackResult onDamageCalc(AttackType type, AttackerObject attacker, Kaiju defender, int damageToAttacker, int damageToDefender);
    void onReceiveDamage(Kaiju kaiju);
    void onMove(Kaiju kaiju);
}
