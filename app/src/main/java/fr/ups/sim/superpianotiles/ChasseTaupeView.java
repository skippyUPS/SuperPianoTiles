package fr.ups.sim.superpianotiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by Quentin on 23/03/2016.
 */
public class ChasseTaupeView  extends View{


    private Drawable mExampleDrawable;
    private Tuile tiles ;
    private boolean run = true;

    public ChasseTaupeView(Context context) {
        super(context);
        init(null, 0);
    }

    public ChasseTaupeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ChasseTaupeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TilesView, defStyle, 0);


        if (a.hasValue(R.styleable.TilesView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.TilesView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();
    }

    public Tuile getTuileFromPos(int x,int y)
    {
        if( this.tiles.getRectangle().contains(x,y))
                return tiles;
        else
                return null;
    }

    public void setTiles(Tuile tiles) {
        this.tiles = tiles;
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



        Random rand = new Random();
        int posAleatoir = rand.nextInt(4);
        int left = getWidth() * posAleatoir / 5;
        int top = getBottom();
        int right = getWidth() - getWidth() * (4-posAleatoir) / 5;
        int bottom = getBottom() + getBottom()  / 4;
        Rect rect = new Rect(left, top, right, bottom);
        tiles= new Tuile(rect);
        addTile(tiles.getRectangle(),canvas,tiles.getDrawable());


        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
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

    public void setRun(boolean run) {
        this.run = run;
    }
}