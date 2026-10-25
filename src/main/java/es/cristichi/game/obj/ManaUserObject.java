package es.cristichi.game.obj;

public interface ManaUserObject {

    /**
     * Adds or removes the indicated quantity to the amount of mana. If it would reduce the mana to less than 0, it returns false instead. 
     * @param quantity
     */
    boolean modifyMana(int quantity);
    
}
