package es.cristichi.kaiju.game.obj.carry;

import java.awt.Color;

import es.cristichi.kaiju.game.obj.Kaiju;

public class Banana extends CarryableAdapter{

    public Banana() {
        super("Banana", "Speed +1", null, Color.YELLOW);
    }
    
    @Override
    public void onPickup(Kaiju kaiju) {
        System.out.println("You got the banana! Speed +1");
        kaiju.modifySpeed(1);
    }
}
