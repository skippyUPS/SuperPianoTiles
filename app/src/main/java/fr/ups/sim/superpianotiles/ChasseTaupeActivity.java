package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

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
    int i, score, compteurBro;
    Dialog dialogFin,dialogPause,dialogCompteur;
    boolean fini =false , pause = false;
    private float alpha;
    ToggleButton soundButton;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Initialisation du son */
        sound.put("kyle", MediaPlayer.create(this, R.raw.kyle));
        sound.put("stan", MediaPlayer.create(this, R.raw.stanley1));
        sound.put("cartman", MediaPlayer.create(this, R.raw.cartman1));
        sound.put("kenny", MediaPlayer.create(this, R.raw.kenny1));

        temp = 1000;
        score = 0;
        alpha = 1;
        i = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chassetaupe);
        TextView afficheScore = (TextView) findViewById(R.id.score);
        //ICI - Commentez le code
        tilesView = (ChasseTaupeView) findViewById(R.id.view2);

        /* On creer une  fenetre pour la fin*/
        dialogFin = new Dialog(tilesView.getContext());
        dialogFin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFin.setContentView(R.layout.popup_mort);
        dialogFin.setCancelable(false);

        /* On creer une fenetre pour la pause */
        dialogPause = new Dialog(tilesView.getContext());
        dialogPause.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPause.setContentView(R.layout.popup_pause);
        dialogPause.setCancelable(false);


        final Button boutonRetour = (Button) dialogPause.findViewById(R.id.buttonRetour);
        final Button boutonFini = (Button) dialogPause.findViewById(R.id.buttonFini);

        boutonFini.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialogPause.dismiss();
                end();
            }
        });

        dialogCompteur = new Dialog(tilesView.getContext());
        dialogCompteur.setContentView(R.layout.popup_compteur);
        dialogCompteur.setCanceledOnTouchOutside(false);
        dialogCompteur.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reprise();
            }
        });

        soundButton = (ToggleButton) dialogPause.findViewById(R.id.toggleButtonMusicPause);
        soundButton.setChecked(getIntent().getBooleanExtra("fr.ups.sim.superpianotiles.SON", false));
        final Button boutonOk = (Button) dialogFin.findViewById(R.id.button);
        final Button boutonRecommencer = (Button) dialogFin.findViewById(R.id.buttonRecommencer);

        boutonRecommencer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogFin.dismiss();
                onCreate(savedInstanceState);
            }

        });

        boutonOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("fr.ups.sim.superpianotiles.SON", soundButton.isChecked());
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
                if (alpha > 0) {
                    alpha -= 0.1;
                    tilesView.setAlpha(alpha);
                    if ( !pause)
                        handlerTimer.postDelayed(this, time);
                } else {
                    TextView text = (TextView) dialogFin.findViewById(R.id.score);
                    text.setText("Score : " + score);
                    dialogFin.show();
                }
            }
        }, 1000);
    }

    public void lancerBoucle ()
    {
        final Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(new Runnable() {
            private long time = 0;

            @Override
            public void run() {
                time = temp / 10;
                if (alpha > 0) {
                    alpha -= 0.1;
                    tilesView.setAlpha(alpha);
                    if ( !pause)
                        handlerTimer.postDelayed(this, time);
                } else {
                    TextView text = (TextView) dialogFin.findViewById(R.id.score);
                    text.setText("Score : " + score);
                    dialogFin.show();
                }
            }
        }, 1000);
    }
    private void end(){
        tilesView.setRun(false);
        TextView textScore = (TextView) dialogFin.findViewById(R.id.score);
        textScore.setText("Score: "+ score);
        tilesView.setAlpha(0.5f);
        dialogFin.show();
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

    private void reprise(){
        dialogCompteur.show();
        dialogPause.dismiss();
        compteurBro = 4;
        final TextView textCompteur = (TextView) dialogCompteur.findViewById(R.id.textCompteur);
        textCompteur.setText("3");
        final Handler handler = new Handler();
        final Runnable runCompteur = new Runnable() {
            @Override
            public void run() {
                if(compteurBro>0) {
                    compteurBro--;
                    if(compteurBro==0) {
                        textCompteur.setText("GO");
                        handler.postDelayed(this, 300);
                    }
                    else {
                        textCompteur.setText(String.valueOf(compteurBro));
                        handler.postDelayed(this, 1000);
                    }
                }
                else {
                    dialogCompteur.dismiss();
                    pause =  false;
                    lancerBoucle();
                }
            }
        };

        handler.post(runCompteur);
    }



    @Override
    public void onBackPressed() {
        pause = true;
        tilesView.setRun(false);
        dialogPause.show();
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
                    if (soundButton.isChecked()) {
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