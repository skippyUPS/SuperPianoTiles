package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TilesStartActivity extends Activity {

    private final String PROGRESS_BAR_INCREMENT = "ProgreesBarIncrementId";
    TilesView tilesView;
    MediaPlayer mPlayer;
    Boolean start = false;
    Dialog dialogMort;
    Dialog dialogPause;
    Dialog dialogCompteur;
    int score = 0;
    int compteurBro = 4;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //Dialog dialog = new Dialog(this);
    //Intent intent = new Intent(TilesStartActivity.this, Menu.class);

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

        dialogMort = new Dialog(tilesView.getContext());
        dialogMort.setContentView(R.layout.popup_mort);
        final Button boutonOk = (Button) dialogMort.findViewById(R.id.button);

        boutonOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });

        dialogPause = new Dialog(tilesView.getContext());
        dialogPause.setContentView(R.layout.popup_pause);
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
        boutonRetour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                reprise();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void reprise(){
        dialogCompteur.show();
        dialogPause.dismiss();
        final TextView textCompteur = (TextView) dialogCompteur.findViewById(R.id.textCompteur);
        textCompteur.setText("3");
        Runnable runCompteur = new Runnable() {
            @Override
            public void run() {
                if(compteurBro>0) {
                    compteurBro--;
                    textCompteur.setText(String.valueOf(compteurBro));
                    tilesView.postDelayed(this, 1000);
                }
                else {

                    dialogCompteur.dismiss();
                    tilesView.setRun(true);
                    run();
                }
            }
        };
        new Thread(runCompteur);
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

    private void run() {
        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (tilesView.isRun()) {
                    if (tilesView.isRun()) {
                        tilesView.postInvalidate();
                        tilesView.postDelayed(this, 1);//1000 ms / 30fps
                    }
                } else {
                    /*TextView textScore = (TextView) dialog.findViewById(R.id.score);
                    textScore.setText("Score: "+ score);
                    dialog.show();*/
                    end();
                }
            }
        };
        runOnUiThread(updateRunnable);
    }


    private void end(){
        TextView textScore = (TextView) dialogMort.findViewById(R.id.score);
        textScore.setText("Score: "+ score);
        tilesView.setAlpha(0.5f);
        dialogMort.show();
    }


    /*
     * ICI - Commentez le code
     */
    private boolean onTouchEventHandler(MotionEvent evt) {
        if (evt.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i("TilesView", "Touch event handled");
            Tuile tuile = this.tilesView.getTuile();
            if (tuile != null) {
                Rect r = tuile.getRectangle();
                if (r.contains((int) evt.getX(), (int) evt.getY())) {
                    int raw = tuile.getRaw();
                    mPlayer = MediaPlayer.create(this, raw);
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mPlayer.start();
                    tilesView.delTuile();
                    score ++;
                } else {
                    end();
                }
            }
            if (!start)
                run();
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
        tilesView.setRun(false);
        dialogPause.show();
    }

    @Override
    public void onStop() {
        super.onStop();

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
