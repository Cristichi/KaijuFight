package es.cristichi.obj.game;

import java.awt.Color;

import es.cristichi.obj.GameObject;

public class Square {
    private final char NULLTEXT = '-';
    private final Color NULLCOLOR = new Color(0, 0, 0);

    protected GameObject object;

    public Square(GameObject object) {
        this.object = object;
    }

    public void setObject(GameObject object) {
        this.object = object;
    }

    public GameObject getObject() {
        return object;
    }

    public char getTextDisplay() {
        return object==null?NULLTEXT:object.getTextDisplay();
    }

    public Color getColorDisplay() {
        return object==null?NULLCOLOR:object.getColorDisplay();
    }
}
