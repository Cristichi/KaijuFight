package es.cristichi.kaiju.game.obj.carry;

public interface CarryableFactory<E extends Carryable> {
    E create();
    String getDisplayName();
    String getDesc();
}
