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
import android.os.Handler;
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
    private float alpha;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Initialisation du son */
        sound.put("kyle", MediaPlayer.create(this, R.raw.kyle));
        sound.put("stan", MediaPlayer.create(this, R.raw.stanley1));
        sound.put("cartman", MediaPlayer.create(this, R.raw.cartman1));
        sound.put("kenny", MediaPlayer.create(this, R.raw.kenny1));
        /*on recupere le temps en UTC depuis 1970 en ms*/
        temp = 1000;
        score = 0;
        alpha = 1;
        i = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chassetaupe);
        TextView afficheScore = (TextView) findViewById(R.id.score);
        //ICI - Commentez le code
        tilesView = (ChasseTaupeView) findViewById(R.id.view2);

        dialog = new Dialog(tilesView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_mort);
        dialog.setCancelable(false);

        final Button boutonOk = (Button) dialog.findViewById(R.id.button);
        final Button boutonRecommencer = (Button) dialog.findViewById(R.id.buttonRecommencer);

        boutonRecommencer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onCreate(savedInstanceState);
            }

        });

        boutonOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                //data.putExtra("fr.ups.sim.superpianotiles.SON", soundButton.isChecked());
                setResult(RESULT_OK, data);
                finish();
            }

        });

        //ICI - Commentez le code


        tilesView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEventHandler(event);

            }
        });



        /*Handler pour animation temps et alpha des tuiles*/
        final Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(new Runnable() {
            private long time = 0;
            @Override
            public void run() {
                time = temp / 10;
                if(alpha > 0) {
                    alpha -= 0.1;
                    tilesView.setAlpha(alpha);
                    handlerTimer.postDelayed(this, time);
                }
                else{
                    TextView text = (TextView) dialog.findViewById(R.id.score);
                    text.setText("Score : " + score);
                    dialog.show();
                }
            }
        }, 1000);
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
                alpha = 1;
                i+=50;
                temp =  1000 - i;
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