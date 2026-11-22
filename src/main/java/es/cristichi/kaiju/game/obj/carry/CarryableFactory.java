package es.cristichi.kaiju.game.obj.carry;

public abstract class CarryableFactory<E extends Carryable> {
    protected String name;
    protected String desc;
    protected boolean unique;

    /**
     * 
     * @param name
     * @param desc
     * @param unique Whether it has to be removed from a reward pool during a Campaign after being taken
     */
    public CarryableFactory(String name, String desc, boolean unique){
        this.name = name;
        this.desc = desc;
        this.unique = unique;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isUnique() {
        return unique;
    }

    public abstract E create();

    @Override
    public String toString() {
        return "[%s name=%s]".formatted(this.getClass().getName(), name);
    }
}
