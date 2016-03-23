package fr.ups.sim.superpianotiles;

import android.graphics.Rect;

/**
 * Created by Corentin on 21/03/2016.
 */
public class Tuile {
    private Rect rectangle;
    private int raw;
    private String nom;

    public Tuile(Rect rectangle) {
        this.nom = Tools.randomNom();
        this.rectangle = rectangle;
        this.raw = Tools.randomSound(nom);
    }

    public void setRectangle(Rect rectangle) {
        this.rectangle = rectangle;
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public String getNom() {
        return nom;
    }

    public int getRaw() {
        return raw;
    }

    public void setInvisible()
    {
        rectangle.setEmpty();
    }
}
