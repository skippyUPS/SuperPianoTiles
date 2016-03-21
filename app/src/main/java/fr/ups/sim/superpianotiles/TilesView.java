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

    private List<Rect> rectangles = new ArrayList<Rect>();
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

    public Rect getRectFromTilesView(int x, int y)
    {
        for(Iterator<Rect> it = this.rectangles.listIterator(); it.hasNext(); )
        {
            Rect currentRect = it.next();
            if(currentRect.contains(x, y))
            {
                return currentRect;
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
        if(compteur == 10){
            int left = getWidth() * 2 / 5;
            int top = getBottom() * 3 / 4;
            int right = getWidth() - getWidth() / 5;
            int bottom = getBottom();
            Random rand = new Random();
            int nombreAleatoire = rand.nextInt(4);
            int drawable = 0;
            switch (nombreAleatoire){
                case(0): drawable = R.drawable.kyle;
                        break;
                case(1): drawable = R.drawable.stan;
                        break;
                case(2): drawable = R.drawable.kenny;
                    break;
                case(3): drawable = R.drawable.cartman;
                    break;
            }
            Rect rect = new Rect(left, top, right, bottom);
            rectangles.add(rect);
            addTile(rect, canvas, drawable);
            Log.i("Teub", "Couille");
            compteur = 0;
        }
        if(!present) {
            //Tile 1
            int left = 0;
            int top = getBottom() * 3 / 4;
            int right = getWidth() / 5;
            int bottom = getBottom();
            Rect rect = new Rect(left, top, right, bottom);
            addTile(rect, canvas, R.drawable.kyle);
            rectangles.add(rect);

            //Tile 2
            left = getWidth() * 3 / 5;
            top = getBottom() - getBottom() * 3 / 4;
            right = getWidth() - getWidth() / 5;
            bottom = getBottom() / 2;
            rect = new Rect(left, top, right, bottom);
            addTile(rect, canvas, R.drawable.kyle);
            rectangles.add(rect);
            present = true;
        }
        else{
            if(!rectangles.isEmpty()) {
                ArrayList<Rect> newRectangles = new ArrayList<Rect>();
                for(Rect r : rectangles) {
                    r.set(r.left, r.top - 10, r.right, r.bottom - 10);
                    newRectangles.add(r);
                    addTile(r, canvas, R.drawable.kyle);
                }
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
