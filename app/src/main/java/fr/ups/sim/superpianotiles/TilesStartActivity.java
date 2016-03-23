package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
    MediaPlayer mPlayer;
    //Dialog dialog = new Dialog(this);

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

    /*
     * ICI - Commentez le code
     */
    private boolean onTouchEventHandler (MotionEvent evt){
        if(evt.getAction()==MotionEvent.ACTION_DOWN) {
            Log.i("TilesView", "Touch event handled");
            Tuile tuile = this.tilesView.getTuile();
            if(tuile != null)
            {
                Rect r = tuile.getRectangle();
                if(r.contains((int) evt.getX(), (int) evt.getY())) {
                    int raw = tuile.getRaw();
                    mPlayer = MediaPlayer.create(this, raw);
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mPlayer.start();
                    tilesView.delTuile();
                }
            }
            tilesView.setRun(true);

            Runnable updateRunnable = new Runnable(){
                @Override
                public void run(){
                    if(tilesView.isRun()) {
                        if (tilesView.isRun()){
                            tilesView.postInvalidate();
                            tilesView.postDelayed(this, 1);//1000 ms / 30fps
                        }
                       /* else{
                            dialog.setContentView(R.layout.popup_mort);
                            dialog.show();
                            Log.i("TEUB","PERDU!");
                        }*/
                    }
                }
            };
            runOnUiThread(updateRunnable);
        }
        return true;
    }
}
