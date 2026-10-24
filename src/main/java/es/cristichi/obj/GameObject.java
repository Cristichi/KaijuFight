package es.cristichi.obj;

import java.awt.Color;
import java.util.UUID;

import es.cristichi.obj.game.AttackResult;
import es.cristichi.obj.game.AttackType;

/**
 * Represents anything that can be inside a Square in the Board. It includes Players, walls, etc.
 */
public abstract class GameObject implements Comparable<GameObject> {
    private UUID uuid;
    private String textDisplay;
    private Color colorDisplay;

    public GameObject(String textDisplay, Color colorDisplay) {
        uuid = UUID.randomUUID();
        this.textDisplay = textDisplay;
        this.colorDisplay = colorDisplay;
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
    public abstract AttackResult receiveAttack(AttackType type, int quantity);

    /**
     * @param other
     * @return <code>true</code> if this GameObject is an obstacle for ranged attacks (think of a wall), <code>false</code> otherwise.
     */
    public abstract boolean stopsRangedAttacks(GameObject other);

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
