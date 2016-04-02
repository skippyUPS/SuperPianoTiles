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
    Boolean start = false;
    Boolean pause = false;
    Dialog dialogMort;
    Dialog dialogPause;
    Dialog dialogCompteur;
    int score;
    TextView compteurScore;
    int compteurBro = 4;
    ToggleButton soundButton;

    private Map<String, MediaPlayer> sound = new HashMap<String, MediaPlayer>();

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
        compteurScore.setText("0");
        dialogMort = new Dialog(tilesView.getContext());
        dialogMort.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMort.setContentView(R.layout.popup_mort);
        dialogMort.setCancelable(false);
        final Button boutonOk = (Button) dialogMort.findViewById(R.id.button);
        final Button boutonRecommencer = (Button) dialogMort.findViewById(R.id.buttonRecommencer);

        boutonRecommencer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogMort.dismiss();
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
        dialogCompteur.setCancelable(false);
        dialogCompteur.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reprise();
            }
        });

        soundButton = (ToggleButton) dialogPause.findViewById(R.id.toggleButtonMusicPause);
        soundButton.setChecked(getIntent().getBooleanExtra("fr.ups.sim.superpianotiles.SON", false));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    tilesView.setRun(true);
                    runTiles();
                }
            }
        };
        handler.post(runCompteur);
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
        Log.i("TEUB", "Bouton : "+ evt.getButtonState());
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
}