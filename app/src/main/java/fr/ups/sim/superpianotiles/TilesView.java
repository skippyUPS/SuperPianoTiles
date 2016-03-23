package fr.ups.sim.superpianotiles;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

/**
 * Custom view that displays tiles
 */
public class TilesView extends View {



    private Queue<Tuile> rectangles = new ArrayDeque<Tuile>();
    private Drawable mExampleDrawable;
    private boolean run = true;
    private int compteur;
    private Map<String, Drawable> images = new HashMap<String, Drawable>();
    boolean init = false;

    public TilesView(Context context) {
        super(context);
        init(null, 0);
    }

    public TilesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TilesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void initTuile(Canvas canvas){
        for(int i=0; i<4; i++){
            Random rand = new Random();
            int posAleatoir = rand.nextInt(5);     //Variable aleatoire qui positionne la nouvelle tuile

            int left = getWidth() * posAleatoir / 5;
            int top = getBottom() * (3-i) / 4;
            int right = getWidth() - getWidth() * (4-posAleatoir) / 5;
            int bottom = getBottom() * (4-i) /4;
            Rect rect = new Rect(left, top, right, bottom);
            Tuile tuile = new Tuile(rect);
            //ajout de la nouvelle tuile dans la collection des tuiles
            rectangles.offer(tuile);
            addTile(rect, canvas, tuile.getNom());
        }
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        compteur = 0; //Initialisation du compteur

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TilesView, defStyle, 0);

        //Initialisation des images
        images.put("kyle",getResources().getDrawable(R.drawable.kyle));
        images.put("stan", getResources().getDrawable(R.drawable.stan));
        images.put("cartman",getResources().getDrawable(R.drawable.cartman));
        images.put("kenny", getResources().getDrawable(R.drawable.kenny));

        if (a.hasValue(R.styleable.TilesView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.TilesView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }
        a.recycle();
    }

    public Queue<Tuile> getRectangles() {
        return rectangles;
    }

    public Tuile getTuile(){
        return rectangles.peek();
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    /*Ajout d'une nouvelle tuile dans la file des rectangles*/
    public void nouvelleTuile(){
        Random rand = new Random();
        int posAleatoir = rand.nextInt(5);     //Variable aleatoire qui positionne la nouvelle tuile

        int left = getWidth() * posAleatoir / 5;
        int top = getTop() - getBottom() /4;
        int right = getWidth() - getWidth() * (4-posAleatoir) / 5;
        int bottom = getTop();
        Rect rect = new Rect(left, top, right, bottom);
        Tuile tuile = new Tuile(rect);
        //ajout de la nouvelle tuile dans la collection des tuiles
        rectangles.offer(tuile);
        compteur = 0;
    }

    public void delTuile(){
        rectangles.remove();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        if(!init){
            Log.i("TEUB","zboub");
            initTuile(canvas);
            nouvelleTuile();
            init = true;
        }
        else {
        /*Le compteur permet de savoir si il faut creer une nouvelle*/
            if (compteur == ((getBottom() - getBottom() * 3 / 4) / 10)+1)
                nouvelleTuile();
            if (!rectangles.isEmpty()) {
                Log.i("TEUB", "Size: " + rectangles.size());
                for (int i = 0; i < rectangles.size(); i++) {
                    Tuile tuile = rectangles.remove();
                    Rect r = tuile.getRectangle();
                    r.set(r.left, r.top + 10, r.right, r.bottom + 10);
                    if (r.top < getBottom() - 100) {
                        rectangles.offer(tuile);
                        addTile(r, canvas, tuile.getNom());
                    } else
                        setRun(false);
                }
            }
        }
        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
        compteur ++;
    }

    public boolean isRun() {
        return run;
    }

    /*Affiche la tuile a l'ecran, drawable donne l'image à afficher*/
    public void addTile(Rect rect, Canvas canvas, String drawable) {
        Drawable d = images.get(drawable);
        if(d != null) {
            d.setBounds(rect);
            d.draw(canvas);
        }
    }

       /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
