package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TilesStartActivity extends Activity {

    TilesView tilesView;
    MediaPlayer mPlayer;
    Boolean start = false;
    Boolean pause = false;
    Dialog dialogMort;
    Dialog dialogPause;
    Dialog dialogCompteur;
    int score;
    TextView compteurScore;
    int compteurReprise;
    ToggleButton soundButton;
    private Map<String, MediaPlayer> sound = new HashMap<String, MediaPlayer>();

    /* Enregistrement du score */
    public static final String SCORES = "scoresDefilement.xml";
    Comparator<Integer> comp = new Comparator<Integer>(){
        @Override
        public int compare(Integer a, Integer b) {
            if(a < b)
                return 1;
            if(a > b)
                return -1;
            return 0;
        }
    };
    public TreeSet<Integer> scoreSet = new TreeSet<Integer>(comp);
    private SharedPreferences settings;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Initialisation des variables*/
        score = 0;
        compteurReprise = 4;

        /* Score */
        settings = getSharedPreferences(SCORES, 0);
        for(String key : settings.getAll().keySet())
        {
            scoreSet.add((Integer) settings.getAll().get(key));
        }

        /* Initialisation du son */
        sound.put("kyle", MediaPlayer.create(this, R.raw.kyle));
        sound.put("stan", MediaPlayer.create(this, R.raw.stanley1));
        sound.put("cartman", MediaPlayer.create(this, R.raw.cartman1));
        sound.put("kenny", MediaPlayer.create(this, R.raw.kenny1));

        score = 0;
        setContentView(R.layout.activity_tiles_start);
        //ICI - Commentez le code
        tilesView = (TilesView) findViewById(R.id.view);
        final TextView touche = (TextView) this.findViewById(R.id.textViewTouche);

        //ICI - Commentez le code
        tilesView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touche.setText("");
                return onTouchEventHandler(event);
            }
        });
        compteurScore = (TextView) this.findViewById(R.id.textScore);
        compteurScore.setText(String.valueOf(score));

        /*Fenetre de dialogue qui s'affiche quand le personnage est mort*/
        dialogMort = new Dialog(tilesView.getContext());
        dialogMort.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMort.setContentView(R.layout.popup_mort);
        dialogMort.setCancelable(false);
        final Button boutonOk = (Button) dialogMort.findViewById(R.id.button);
        final Button boutonRecommencer = (Button) dialogMort.findViewById(R.id.buttonRecommencer);

        /*Listener permettant de savoir si le joueur a appuye sur le bouton "Recommencer" et relance
        * le jeu*/
        boutonRecommencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMort.dismiss();
                onCreate(savedInstanceState);
            }
        });

        /*Listener permettant de savoir si le joueur appui sur "OK" et quitte l'activity*/
        boutonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("fr.ups.sim.superpianotiles.SON", soundButton.isChecked());
                setResult(RESULT_OK, data);
                finish();
            }
        });

        /*Fenetre de dialogue s'affichant quand le joueur fait pause (appui sur le bouton retour)*/
        dialogPause = new Dialog(tilesView.getContext());
        dialogPause.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPause.setContentView(R.layout.popup_pause);
        dialogPause.setCancelable(false);
        soundButton = (ToggleButton) dialogPause.findViewById(R.id.toggleButtonMusicPause);
        soundButton.setChecked(getIntent().getBooleanExtra("fr.ups.sim.superpianotiles.SON", false));
        final Button boutonRetour = (Button) dialogPause.findViewById(R.id.buttonRetour);
        final Button boutonFini = (Button) dialogPause.findViewById(R.id.buttonFini);

        /*Listener permettant de savoir quand le joueur appuie sur "Quitter" et affiche la boite de
        * dialogue dialogMort*/
        boutonFini.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialogPause.dismiss();
                end();
            }
        });
        /*Reprend le jeu*/
        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reprise();
            }
        });

        /*Boite de dialogue ne servant just a afficher un compteur au milieu de l'ecran lors de la
        * reprise du jeu, permet d'assombrir le reste de l'ecran*/
        dialogCompteur = new Dialog(tilesView.getContext());
        dialogCompteur.setContentView(R.layout.popup_compteur);
        dialogCompteur.setCanceledOnTouchOutside(false);
        dialogCompteur.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TilesStart Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://fr.ups.sim.superpianotiles/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    /*Quand l'utilisateur appuie sur la touche retour le menu paus s'affiche*/
    @Override
    public void onBackPressed() {
        pause = true;
        tilesView.setRun(false);
        dialogPause.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        onBackPressed();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TilesStart Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://fr.ups.sim.superpianotiles/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /*
    * reprise() permet de reprendre le jeu après une pause en affichant le compteur
    * */
    private void reprise(){
        dialogCompteur.show();
        dialogPause.dismiss();
        compteurReprise = 4;
        final TextView textCompteur = (TextView) dialogCompteur.findViewById(R.id.textCompteur);
        textCompteur.setText("3");
        final Handler handler = new Handler();
        final Runnable runCompteur = new Runnable() {
            @Override
            public void run() {
                if(compteurReprise>0) {
                    compteurReprise--;
                    if(compteurReprise==0) {
                        textCompteur.setText("GO");
                        handler.postDelayed(this, 300);
                    }
                    else {
                        textCompteur.setText(String.valueOf(compteurReprise));
                        handler.postDelayed(this, 1000);
                    }
                }
                else {
                    dialogCompteur.dismiss();
                    tilesView.setRun(true);
                    runTiles();
                }
            }
        };
        handler.post(runCompteur);
    }

    /*
    * runTiles() permet de faire tourner le jeu dans une autre thread
    * */
    private void runTiles() {
        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (tilesView.isRun()) {
                    tilesView.postInvalidate();
                    tilesView.postDelayed(this, 1);//1000 ms / 30fps
                } else {
                    if(!pause)
                        end();
                }
            }
        };
        runOnUiThread(updateRunnable);
    }

    /*
    * Fonction utilise quand le personnage perd la partie
    * */
    private void end(){
        tilesView.setRun(false);
        TextView textScore = (TextView) dialogMort.findViewById(R.id.score);
        textScore.setText("Score: " + score);

        /* Ajout du score, puis suppression des plus petits */
        scoreSet.add(score);
        while(scoreSet.size() > 8)
        {
            scoreSet.remove(scoreSet.last());
        }

        /* Mise à jour */
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        for(Integer i : scoreSet)
        {
            editor.putInt(Integer.toString(i), i);
        }
        editor.commit();


        tilesView.setAlpha(0.5f);
        dialogMort.show();
    }


    /*
     * Action effectuer lorsque l'utilisateur appuie sur l'ecran, la fonction test si l'utilisateur
     * a bien clique sur la bonne tuile, va soit incrémenter le score soit arreter le jeu et
     * afficher le score a l'ecran
     */
    private boolean onTouchEventHandler(MotionEvent evt) {
        Log.i("TEUB", "Bouton : " + evt.getButtonState());
        if (evt.getAction() == MotionEvent.ACTION_DOWN ) {
            Log.i("TilesView", "Touch event handled");
            Tuile tuile = this.tilesView.getTuile();
            if (tuile != null) {
                Rect r = tuile.getRectangle();
                if (r.contains((int) evt.getX(), (int) evt.getY())) {
                    /* Instructions nécessaire pour jouer un son
                    (cf. Diagramme d'état de la classe MediaPlayer */
                    if(soundButton.isChecked()) {
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
                    tilesView.delTuile();
                    score ++;
                    compteurScore.setText(String.valueOf(score));
                } else {
                    end();
                }
            }
            if (!start)
                runTiles();
        }
        return true;
    }
}