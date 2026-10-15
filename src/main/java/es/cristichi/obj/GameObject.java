package es.cristichi.obj;

import java.util.UUID;

import es.cristichi.util.Color;

public class GameObject implements Comparable<GameObject> {
    private UUID uuid;
    private char textDisplay;
    private Color colorDisplay;

    public GameObject(char textDisplay, Color colorDisplay) {
        uuid = UUID.randomUUID();
        this.textDisplay = textDisplay;
        this.colorDisplay = colorDisplay;
    }

    public char getTextDisplay() {
        return textDisplay;
    }

    public Color getColorDisplay() {
        return colorDisplay;
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
