package es.cristichi.kaiju.game.obj.carry.factory;

import es.cristichi.kaiju.game.obj.carry.Banana;
import es.cristichi.kaiju.game.obj.carry.CarryableFactory;

public class BananaFactory implements CarryableFactory<Banana> {

    @Override
    public Banana create() {
        return new Banana();
    }

    @Override
    public String getDisplayName() {
        return "Banana";
    }

    @Override
    public String getDesc() {
        return "It grants Speed +1";
    }
    
}
