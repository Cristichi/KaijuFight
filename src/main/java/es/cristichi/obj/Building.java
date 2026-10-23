package es.cristichi.obj;

import java.awt.Color;

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

}
