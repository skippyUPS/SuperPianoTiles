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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Custom view that displays tiles
 */
public class TilesView extends View {

    private List<Tuile> rectangles = new ArrayList<Tuile>();
    private int tileColor = Color.BLUE;
    private int textColor = Color.WHITE;
    private Drawable mExampleDrawable;
    private float textSize = 40;
    private boolean present = false;
    private boolean run = true;
    Paint pText = new Paint();
    Paint pTile = new Paint();
    private int compteur;

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

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        compteur = 0;
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TilesView, defStyle, 0);


        if (a.hasValue(R.styleable.TilesView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.TilesView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }
        a.recycle();
    }

    public Tuile getTuileFromPos(int x, int y) {
        for(Iterator<Tuile> it = this.rectangles.listIterator(); it.hasNext(); )
        {
            Tuile currentRect = it.next();
            if(currentRect.getRectangle().contains(x, y))
            {
                return currentRect;
            }
        }
        return null;
    }

    public Rect getRectFromTilesView(int x, int y)
    {
        for(Iterator<Tuile> it = this.rectangles.listIterator(); it.hasNext(); )
        {
            Tuile currentRect = it.next();
            if(currentRect.getRectangle().contains(x, y))
            {
                return currentRect.getRectangle();
            }
        }
        return null;
    }

    public void setRun(boolean run) {
        this.run = run;
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

        pText.setTextSize(textSize);
        pText.setColor(textColor);
        pTile.setColor(tileColor);
        if(compteur == (getBottom() - getBottom() * 3 / 4)/10){
            Random rand = new Random();
            int posAleatoir = rand.nextInt(4);
            int left = getWidth() * posAleatoir / 5;
            int top = getBottom();
            int right = getWidth() - getWidth() * (4-posAleatoir) / 5;
            int bottom = getBottom() + getBottom()  / 4;
            Rect rect = new Rect(left, top, right, bottom);
            Tuile tuile = new Tuile(rect);
            rectangles.add(tuile);
            addTile(rect, canvas, tuile.getDrawable());
            compteur = 0;
        }
        if(!present) {
            //Tile 1
            int left = 0;
            int top = getBottom() * 3 / 4;
            int right = getWidth() / 5;
            int bottom = getBottom();
            Rect rect = new Rect(left, top, right, bottom);
            Tuile tuile = new Tuile(rect);
            addTile(rect, canvas, tuile.getDrawable());
            rectangles.add(tuile);

            //Tile 2
            left = getWidth() * 3 / 5;
            top = getBottom() - getBottom() * 3 / 4;
            right = getWidth() - getWidth() / 5;
            bottom = getBottom() / 2;
            rect = new Rect(left, top, right, bottom);
            tuile = new Tuile(rect);
            addTile(rect, canvas, tuile.getDrawable());
            rectangles.add(tuile);
            present = true;
        }
        else{
            if(!rectangles.isEmpty()) {
                Log.i("TEUB","Size: "+ rectangles.size());
                ArrayList<Tuile> newRectangles = new ArrayList<Tuile>();
                for(Tuile tuile : rectangles) {
                    Rect r = tuile.getRectangle();
                    r.set(r.left, r.top - 10, r.right, r.bottom - 10);
                    if(r.bottom > getTop()+60)
                        newRectangles.add(tuile);
                    tuile.setRectangle(r);
                    addTile(r, canvas, tuile.getDrawable());
                }
                rectangles.clear();
                rectangles = newRectangles;
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

    public void addTile(Rect rect, Canvas canvas, int drawable) {
        Drawable d = getResources().getDrawable(drawable);
        d.setBounds(rect);
        d.draw(canvas);
        //canvas.drawRect(rect, pTile);
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
