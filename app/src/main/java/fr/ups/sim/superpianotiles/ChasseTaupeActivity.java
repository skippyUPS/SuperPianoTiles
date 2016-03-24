package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

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
    long temp ;
    int i, score;
    Dialog dialog;
    boolean fini =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Initialisation du son */
        sound.put("kyle", MediaPlayer.create(this, R.raw.kyle));
        sound.put("stan", MediaPlayer.create(this, R.raw.stanley1));
        sound.put("cartman", MediaPlayer.create(this, R.raw.cartman1));
        sound.put("kenny", MediaPlayer.create(this, R.raw.kenny1));
        /*on recupere le temps en UTC depuis 1970 en ms*/
        temp = System.currentTimeMillis() + 1000;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chassetaupe);
        TextView afficheScore = (TextView) findViewById(R.id.score);
        //ICI - Commentez le code
        tilesView = (ChasseTaupeView) findViewById(R.id.view2);

        dialog = new Dialog(tilesView.getContext());
        dialog.setContentView(R.layout.popup_mort);

        //ICI - Commentez le code


        tilesView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (System.currentTimeMillis() >= temp) {

                    dialog.show();
                    final Button exit = (Button) dialog.findViewById(R.id.button);
                    final TextView scoreMort = (TextView) dialog.findViewById(R.id.score);
                    scoreMort.setText("Score : "+score);
                    fini = true;
                    exit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            finish();
                        }

                    });



                }

                return onTouchEventHandler(event);

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

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
                /* on reduit le delais de la prochaine brique*/
                i+=50;
                temp =  System.currentTimeMillis()+1000 - i;
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
                score += 50;
                final TextView affiche = (TextView) findViewById(R.id.score);
                affiche.setText(""+score);
                tilesView.setTiles(null);
                tilesView.invalidate();
            }


        }
        if ( fini)
        {
            
        }


        return true;
    }
}