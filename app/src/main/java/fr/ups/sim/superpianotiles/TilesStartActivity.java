package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TilesStartActivity extends Activity {

    private final String PROGRESS_BAR_INCREMENT="ProgreesBarIncrementId";
    TilesView tilesView;


    //j'aime le caca
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiles_start);
        //ICI - Commentez le code
        tilesView = (TilesView) findViewById(R.id.view);
        //ICI - Commentez le code
        tilesView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEventHandler(event);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("Teub","Couille");
            }
        });
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tiles_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // ICI - A compléter pour déclencher l'ouverture de l'écran de paramétrage
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void update(){

    }

    /*
     * ICI - Commentez le code
     */
    private boolean onTouchEventHandler (MotionEvent evt){
        if(evt.getAction()==MotionEvent.ACTION_DOWN) {
            Log.i("TilesView", "Touch event handled");
            tilesView.setRun(true);
            Runnable updateRunnable = new Runnable(){
                @Override
                public void run(){
                    if(tilesView.isRun()) {
                        tilesView.postInvalidate();
                        tilesView.postDelayed(this, 1);//1000 ms / 30fps
                    }
                }
            };
            //tilesView.post(updateRunnable);
            runOnUiThread(updateRunnable);
        }
        if(evt.getAction()==MotionEvent.ACTION_UP){
            tilesView.setRun(false);
        }
        /*if(evt.getAction()==MotionEvent.ACTION_DOWN){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i <= 50; i++) {
                        tilesView.post(new Runnable() {
                            @Override
                            public void run() {
                                tilesView.invalidate();
                            }
                        });
                    }
                }
            };
            new Thread(runnable).start();
        }*/
        return true;
    }
}
