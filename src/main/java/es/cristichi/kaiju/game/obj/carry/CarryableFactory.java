package es.cristichi.kaiju.game.obj.carry;

public abstract class CarryableFactory<E extends Carryable> {
    protected String name;
    protected String desc;

    public CarryableFactory(String name, String desc){
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public abstract E create();
}
