package es.cristichi.kaiju.game.obj.carry.factory;

import java.awt.Color;

import es.cristichi.kaiju.game.obj.carry.Carryable;
import es.cristichi.kaiju.game.obj.carry.CarryableAdapter;
import es.cristichi.kaiju.game.obj.carry.CarryableFactory;

public class TestFactory extends CarryableFactory<Carryable>{
    public TestFactory(String name){
        super(name, "Random desc xdd");
    }

    @Override
    public Carryable create() {
        return new CarryableAdapter(name, desc, name, null, Color.BLUE);
    }
    
}
