package es.cristichi.kaiju.game.obj.carry.factory;

import es.cristichi.kaiju.game.obj.carry.Banana;
import es.cristichi.kaiju.game.obj.carry.CarryableFactory;

public class BananaFactory extends CarryableFactory<Banana> {

    public BananaFactory() {
        super("Banana", "Speed +1");
    }

    @Override
    public Banana create() {
        return new Banana();
    }
}
