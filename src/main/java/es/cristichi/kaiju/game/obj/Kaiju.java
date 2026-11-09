package es.cristichi.kaiju.game.obj;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import es.cristichi.kaiju.game.GameObject;
import es.cristichi.kaiju.game.io.ResourceException;
import es.cristichi.kaiju.game.io.Resources;

public class Kaiju extends GameObject implements AliveObject, AttackerObject, ManaUserObject {
    private String species;
    private int speed;
    private int damage;
    private int mana;
    private int health;
    private boolean cpu;

    public Kaiju(String name, String species, int speed, int damage, int mana, int health, String textDisplay, String imageResource, Color colorDisplay, boolean cpu) throws ResourceException {
        super(name, textDisplay, imageResource==null?null:Resources.loadImage(imageResource), colorDisplay);
        this.species = species;
        this.speed = speed;
        this.damage = damage;
        this.mana = mana;
        this.health = health;
        this.cpu = cpu;
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

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public boolean modifyMana(int quantity) {
        int result = mana + quantity;
        if (result<0){
            return false;
        }
        mana = result;
        return true;
    }

    @Override
    public boolean isAlive() {
        return health>0;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public boolean modifyHealth(int quantity){
        health+=quantity;
        if (health<=0){
            return false;
        }
        return true;
    }
    
    public boolean isCpu() {
        return cpu;
    }

    @Override
    public boolean canBeAttackedBy(AttackType type, GameObject other) {
        return isAlive();
    }

    @Override
    public AttackResult receiveAttack(AttackType type, AttackerObject attacker, int quantity) {
        return new AttackResult(0, modifyHealth(-quantity)?quantity:0);
    }

    @Override
    public void displayInfo(Graphics2D g2d, Rectangle bounds) {
        super.displayInfo(g2d, bounds);
        FontMetrics fm = g2d.getFontMetrics();
        if (isAlive()){
            {
                String dHP = "Health: "+health;
                Rectangle2D strBounds = fm.getStringBounds(dHP, g2d);
                int x = bounds.x+5;
                int y = (int) (bounds.y + strBounds.getHeight()/2 + fm.getAscent());
                
                g2d.setColor(Color.BLACK);
                g2d.drawString(dHP, x-1, y+1);
                g2d.drawString(dHP, x+1, y+1);
                g2d.drawString(dHP, x-1, y-1);
                g2d.drawString(dHP, x+1, y-1);
                g2d.setColor(Color.GREEN);
                g2d.drawString(dHP, x, y);
            }
            {
                String dMana = "Mana: "+mana;
                Rectangle2D strBounds = fm.getStringBounds(dMana, g2d);
                int x = bounds.x+5;
                int y = (int) (bounds.y - strBounds.getHeight() + fm.getAscent() + bounds.getHeight() -5);
                
                g2d.setColor(Color.BLACK);
                g2d.drawString(dMana, x-1, y+1);
                g2d.drawString(dMana, x+1, y+1);
                g2d.drawString(dMana, x-1, y-1);
                g2d.drawString(dMana, x+1, y-1);
                g2d.setColor(Color.CYAN);
                g2d.drawString(dMana, x, y);
            }
        } else {
            {
                String msg = "Eliminated";
                Rectangle2D strBounds = fm.getStringBounds(msg, g2d);
                int x = bounds.x+5;
                int y = (int) (bounds.y + strBounds.getHeight()/2 + fm.getAscent());
                
                g2d.setColor(Color.BLACK);
                g2d.drawString(msg, x-1, y+1);
                g2d.drawString(msg, x+1, y+1);
                g2d.drawString(msg, x-1, y-1);
                g2d.drawString(msg, x+1, y-1);
                g2d.setColor(Color.RED);
                g2d.drawString(msg, x, y);
            }
        }
    }
}
