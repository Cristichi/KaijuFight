package es.cristichi.game.obj;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import es.cristichi.game.GameObject;

public class Kaiju extends GameObject implements AttackerObject, ManaUserObject {
    private String species;
    private int speed;
    private int damage;
    private int mana;
    private int health;

    public Kaiju(String species, int speed, int damage, int mana, int health, String textDisplay, Color colorDisplay) {
        super(textDisplay, colorDisplay);
        this.species = species;
        this.speed = speed;
        this.damage = damage;
        this.mana = mana;
        this.health = health;
    }

    @Override
    public boolean canBeReplacedBy(GameObject other) {
        return false;
    }

    @Override
    public boolean stopsRangedAttacks(GameObject other) {
        return true;
    }

    public String getSpecies() {
        return species;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public int getAttackDamage(AttackType type) {
        return damage;
    }

    public int getMana() {
        return mana;
    }

    @Override
    public boolean modifyMana(int quantity) {
        int result = mana - quantity;
        if (result<0){
            return false;
        }
        mana = result;
        return true;
    }

    public int getHealth() {
        return health;
    }

    public boolean modifyHealth(int quantity){
        health+=quantity;
        if (health<=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeAttackedBy(AttackType type, GameObject other) {
        return true;
    }

    @Override
    public AttackResult receiveAttack(AttackType type, AttackerObject attacker, int quantity) {
        return new AttackResult(0, modifyHealth(-quantity)?quantity:0);
    }

    @Override
    public void displayInfo(Graphics2D g2d, Rectangle bounds) {
        super.displayInfo(g2d, bounds);
        String hp = "Health: "+health;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(hp, g2d);
        int x = bounds.x+5;
        int y = (int) (bounds.y + r.getHeight()/2) + fm.getAscent();
        
        g2d.setColor(Color.BLACK);
        g2d.drawString(hp, x-1, y+1);
        g2d.drawString(hp, x+1, y+1);
        g2d.drawString(hp, x-1, y-1);
        g2d.drawString(hp, x+1, y-1);
        g2d.setColor(Color.GREEN);
        g2d.drawString(hp, x, y);
    }
}
