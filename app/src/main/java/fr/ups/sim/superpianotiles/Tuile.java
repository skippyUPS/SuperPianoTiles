package fr.ups.sim.superpianotiles;

import android.graphics.Rect;

/**
 * Created by Corentin on 21/03/2016.
 */
public class Tuile {
    private Rect rectangle;
    private int drawable;
    private int raw;

    public Tuile(Rect rectangle) {
        this.drawable = Tools.randomImage();
        this.rectangle = rectangle;
        this.raw = Tools.randomSound(drawable);
    }

    public void setRectangle(Rect rectangle) {
        this.rectangle = rectangle;
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public int getDrawable() {
        return drawable;
    }

    public int getRaw() {
        return raw;
    }
}
