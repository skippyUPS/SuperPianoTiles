package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Chronometer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Quentin on 22/03/2016.
 */
public class ChasseTaupeActivity extends Activity{

    ChasseTaupeView tilesView;
    MediaPlayer mPlayer;
    private Map<String, MediaPlayer> sound = new HashMap<String, MediaPlayer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Initialisation du son */
        sound.put("kyle", MediaPlayer.create(this, R.raw.kyle));
        sound.put("stan", MediaPlayer.create(this, R.raw.stanley1));
        sound.put("cartman", MediaPlayer.create(this, R.raw.cartman1));
        sound.put("kenny", MediaPlayer.create(this, R.raw.kenny1));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chassetaupe);

        //ICI - Commentez le code
        tilesView = (ChasseTaupeView) findViewById(R.id.view2);



        //ICI - Commentez le code
        tilesView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEventHandler(event);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
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

        Tuile tuile = this.tilesView.getTuileFromPos((int)evt.getX(),(int) evt.getY());

        if(evt.getAction()==MotionEvent.ACTION_DOWN) {
            Log.i("TilesView", "Touch event handled");
            if(tuile != null)
            {
                Rect r = tuile.getRectangle();
                if(r.contains((int) evt.getX(), (int) evt.getY())) {

                    /* Instructions nécessaire pour jouer un son
                    (cf. Diagramme d'état de la classe MediaPlayer */
                    mPlayer = sound.get(tuile.getNom());
                    mPlayer.start();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.stop();
                            try {
                                mp.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
            if(tuile != null)
            {
                tilesView.setTiles(null);
                tilesView.invalidate();
            }
        }
        if(evt.getAction()==MotionEvent.ACTION_UP){

        }

        return true;
    }
}