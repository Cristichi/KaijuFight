package es.cristichi.kaiju.game.obj;

public interface ManaUserObject {

    int getMana();
    
    /**
     * Adds or removes the indicated quantity to the amount of mana. If it would reduce the mana to less than 0, it returns false instead. 
     * @param quantity
     * @return Whether or not it was possible to modify the mana (if the operation would not reduce the mana to lower than 0)
     */
    boolean modifyMana(int quantity);
}