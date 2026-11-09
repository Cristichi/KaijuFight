package es.cristichi.kaiju.game.obj;

/**
 * AliveObject
 */
public interface AliveObject {
    boolean isAlive();
    int getHealth();
    boolean modifyHealth(int quantity);
}
