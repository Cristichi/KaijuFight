package es.cristichi.game;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.UUID;

import es.cristichi.game.obj.AttackResult;
import es.cristichi.game.obj.AttackType;
import es.cristichi.game.obj.AttackerObject;

/**
 * Represents anything that can be inside a Square in the Board. It includes Players, walls, etc.
 */
public abstract class GameObject implements Comparable<GameObject> {
    private UUID uuid;
    protected String textDisplay;
    protected Color colorDisplay;

    public GameObject(String textDisplay, Color colorDisplay) {
        uuid = UUID.randomUUID();
        this.textDisplay = textDisplay;
        this.colorDisplay = colorDisplay;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTextDisplay() {
        return textDisplay;
    }

    public Color getColorDisplay() {
        return colorDisplay;
    }

    /**
     * @param other
     * @return <code>true</code> if this GameObject can be replaced by <code>other</code>, <code>false</code> otherwise.
     */
    public abstract boolean canBeReplacedBy(GameObject other);

    /**
     * @param other
     * @return <code>true</code> if this GameObject can be attacked, <code>false</code> otherwise.
     */
    public abstract boolean canBeAttackedBy(AttackType type, GameObject other);

    /**
     * @param attacker
     * @return <code>true</code> if this GameObject can be attacked, <code>false</code> otherwise.
     */
    public abstract AttackResult receiveAttack(AttackType type, AttackerObject attacker, int quantity);

    /**
     * @param other
     * @return <code>true</code> if this GameObject is an obstacle for ranged attacks (think of a wall), <code>false</code> otherwise.
     */
    public abstract boolean stopsRangedAttacks(GameObject other);

    /**
     * It enables information to be drawn inside the designated space for this GameObject.
     * @param g2d Graphics used to draw
     * @param bounds The designated space for this GameObject. Try to draw inside this bounds.
     */
    public void displayInfo(Graphics2D g2d, Rectangle bounds){
        FontMetrics fm = g2d.getFontMetrics();
        
        g2d.setColor(colorDisplay);
        g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        Rectangle2D r = fm.getStringBounds(textDisplay, g2d);
        int x = (int) (bounds.x + bounds.width/2 - r.getWidth()/2);
        int y = (int) (bounds.y + bounds.height/2 - r.getHeight()/2) + fm.getAscent();

        g2d.setColor(Color.BLACK);
        g2d.drawString(textDisplay, x-1, y+1);
        g2d.drawString(textDisplay, x+1, y+1);
        g2d.drawString(textDisplay, x-1, y-1);
        g2d.drawString(textDisplay, x+1, y-1);
        g2d.setColor(Color.WHITE);
        g2d.drawString(textDisplay, x, y);
    }

    @Override
    public int compareTo(GameObject o) {
        return uuid.compareTo(o.uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameObject gameObject){
            return compareTo(gameObject)==0;
        }
        return super.equals(obj);
    }
}
