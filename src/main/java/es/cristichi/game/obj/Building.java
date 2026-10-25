package es.cristichi.game.obj;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import es.cristichi.game.GameObject;

public class Building extends GameObject {
    private static final Color MANAED_COLOR = new Color(100, 100, 200);
    private static final Color EMPTIED_COLOR = new Color(100, 100, 100);
    private int charges;

    public Building(int charges) {
        super("Building", null, charges>0?MANAED_COLOR:EMPTIED_COLOR);
        this.charges = charges;
    }

    @Override
    public boolean canBeReplacedBy(GameObject other) {
        return false;
    }

    @Override
    public boolean stopsRangedAttacks(GameObject other) {
        return true;
    }

    @Override
    public boolean canBeAttackedBy(AttackType type, GameObject other) {
        return type.equals(AttackType.MELEE);
    }

    @Override
    public AttackResult receiveAttack(AttackType type, AttackerObject attacker, int quantity) {
        if (charges>0 && attacker instanceof ManaUserObject manaUser){
            charges--;
            manaUser.modifyMana(1);
        }
        colorDisplay = charges>0?MANAED_COLOR:EMPTIED_COLOR;
        return new AttackResult(0, 0);
    }

    @Override
    public void displayInfo(Graphics2D g2d, Rectangle bounds) {
        super.displayInfo(g2d, bounds);
        String manaStored = "Mana stored: "+charges;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(manaStored, g2d);
        int x = bounds.x+5;
        int y = (int) (bounds.y + r.getHeight()/2) + fm.getAscent();
        
        g2d.setColor(Color.BLACK);
        g2d.drawString(manaStored, x-1, y+1);
        g2d.drawString(manaStored, x+1, y+1);
        g2d.drawString(manaStored, x-1, y-1);
        g2d.drawString(manaStored, x+1, y-1);
        g2d.setColor(Color.CYAN);
        g2d.drawString(manaStored, x, y);
    }

}
