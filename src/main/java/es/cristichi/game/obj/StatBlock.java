package es.cristichi.game.obj;

public class StatBlock {
    private int speed;
    private int damage;
    private int mana;
    private int maxHealth;

    public StatBlock(int speed, int damage, int mana, int maxLife){
        this.speed = speed;
        this.damage = damage;
        this.mana = mana;
        this.maxHealth = maxLife;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public int getMana() {
        return mana;
    }

    public boolean useMana(int quantity){
        if (mana >= quantity){
            mana-=quantity;
            return true;
        }
        return false;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
}
