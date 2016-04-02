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
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.HashMap;
import java.util.Map;

public class TilesStartActivity extends Activity {

    private final String PROGRESS_BAR_INCREMENT = "ProgreesBarIncrementId";
    TilesView tilesView;
    MediaPlayer mPlayer;
    Boolean start = false; //Permet de savoir si le jeu doit tourner
    Boolean pause = false; //Permet de savoir si le jeu est en pause
    Dialog dialogMort;
    Dialog dialogPause;
    Dialog dialogCompteur;
    TextView compteurScore; //affiche le score a l'ecran
    int score;  //score du jeu
    int compteurReprise; //compteur afficher lors d'une reprise, utile pour le thread
    ToggleButton soundButton; //toggleButton permettant de savoir si le jeu est mute
    private Map<String, MediaPlayer> sound = new HashMap<String, MediaPlayer>(); //HashMap qui stock tout les sons du jeu

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //Dialog dialog = new Dialog(this);
    //Intent intent = new Intent(TilesStartActivity.this, Menu.class);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Initialisation des variables*/
        score = 0;
        compteurReprise = 4;

        /* Initialisation du son */
        sound.put("kyle", MediaPlayer.create(this, R.raw.kyle));
        sound.put("stan", MediaPlayer.create(this, R.raw.stanley1));
        sound.put("cartman", MediaPlayer.create(this, R.raw.cartman1));
        sound.put("kenny", MediaPlayer.create(this, R.raw.kenny1));

        setContentView(R.layout.activity_tiles_start);
        /*On recupère la vue des tuiles pour pouvoir la modifier*/
        tilesView = (TilesView) findViewById(R.id.view);

        /*Text view affiche au milieu de l'ecran pour dire au joueur de toucher la tuile pour commencer*/
        final TextView touche = (TextView) this.findViewById(R.id.textViewTouche);

        /*Ajout du listener permettant de detecter si le joueur a toucher l'ecran et va utiliser la
        * fonction onTouchEventHandler modifier plus bas*/
        tilesView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*Efface le text de l'ecran*/
                touche.setAlpha(0);
                return onTouchEventHandler(event);
            }
        });
        compteurScore = (TextView) this.findViewById(R.id.textScore);
        compteurScore.setText(String.valueOf(score));

        /*Fenetre de dialogue affiche lorsque le joueur a perdu*/
        dialogMort = new Dialog(tilesView.getContext());
        dialogMort.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMort.setContentView(R.layout.popup_mort);
        dialogMort.setCancelable(false);
        /*Les deux boutons de la fenetre de dialogue*/
        final Button boutonOk = (Button) dialogMort.findViewById(R.id.button);
        final Button boutonRecommencer = (Button) dialogMort.findViewById(R.id.buttonRecommencer);
        /*Listener qui permet de recommencer une partie quand le joueur appui sur le bouton "Recommencer"*/
        boutonRecommencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMort.dismiss();
                onCreate(savedInstanceState);
            }
        });
        /*Listener qui permet de quitter le jeu et revenir au menu quand le joueur appui sur le bouton "OK"*/
        boutonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("fr.ups.sim.superpianotiles.SON", soundButton.isChecked());
                setResult(RESULT_OK, data);
                finish();
            }
        });

        /*Fenetre de dialogue qui s'affiche quand le joueur fait pause*/
        dialogPause = new Dialog(tilesView.getContext());
        dialogPause.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPause.setContentView(R.layout.popup_pause);
        dialogPause.setCancelable(false);
        final Button boutonRetour = (Button) dialogPause.findViewById(R.id.buttonRetour);
        final Button boutonFini = (Button) dialogPause.findViewById(R.id.buttonFini);
        soundButton = (ToggleButton) dialogPause.findViewById(R.id.toggleButtonMusicPause);
        soundButton.setChecked(getIntent().getBooleanExtra("fr.ups.sim.superpianotiles.SON", false));
        /*Si le joueur appuie sur le bouton "Quitter" alors il y aura les même effet que s'il avait perdu*/
        boutonFini.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialogPause.dismiss();
                end();
            }
        });
        /*Si le joueur appuie sur "Retour" alors la fentre de pause disparait et un compteur apparait
        * avant de pourvoir recommencer a jouer*/
        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reprise();
            }
        });

        /*Fenetre de dialogue qui ne va servir qu'a afficher le compteur au milieu de l'ecran
        * a la reprise d'une pause, permettant d'assombrir le reste de l'ecran*/
        dialogCompteur = new Dialog(tilesView.getContext());
        dialogCompteur.setContentView(R.layout.popup_compteur);
        dialogCompteur.setCancelable(false);
        dialogCompteur.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        pause = true;
        tilesView.setRun(false);
        dialogPause.show();
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
    * Fonction utilise lors de la reprise du jeu permettant d'afficher le compteur avant de reprendre
    * */
    private void reprise(){
        dialogCompteur.show();
        dialogPause.dismiss();
        compteurReprise = 4;
        final TextView textCompteur = (TextView) dialogCompteur.findViewById(R.id.textCompteur);
        textCompteur.setText("3");

        /*Handler qui fait tourner le compteur*/
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
    * Fonction utilise pour faire tourner le jeu (defiler les tuile)
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
    * Fonction utilise lors de la fin du jeu
    * */
    private void end(){
        tilesView.setRun(false);
        TextView textScore = (TextView) dialogMort.findViewById(R.id.score);
        textScore.setText("Score: "+ score);
        tilesView.setAlpha(0.5f);
        dialogMort.show();
    }


    /*
     * ICI - Commentez le code
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