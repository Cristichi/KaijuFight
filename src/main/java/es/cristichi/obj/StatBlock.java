package es.cristichi.obj;

public class StatBlock {
    private int speed;
    private int damage;
    private int mana;

    public StatBlock(int speed, int damage, int mana){
        this.speed = speed;
        this.damage = damage;
        this.mana = mana;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public boolean receiveDamage(int quantity){
        if (damage >= quantity){
            damage-=quantity;
            return true;
        }
        return false;
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
}
