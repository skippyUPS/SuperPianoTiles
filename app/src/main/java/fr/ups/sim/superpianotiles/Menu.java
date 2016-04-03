package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import java.io.IOException;

/** Cette classe est notre main Activity et permet de choisir entre deux mode de jeux Chasse-Taupe
 * Defilement ainsi que de choisir sur l'on veut que la music soit activé ou pas
 */


public class Menu extends Activity{

    MediaPlayer son; /* Variable pour gerer le son du Menu*/
    ToggleButton soundButton; /* Boutton a deux etat  Off/On pour le son dans le menu et le reste
                                de l'application */
    Boolean init = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);  /* On utilise le layout que l'on a cree */

        /*On recupere les differents boutton g*/

        final Button defilementButton = (Button) findViewById(R.id.button);
        final Button chasseTaupeButton = (Button) findViewById(R.id.button3);
        final Button scoreButton = (Button) findViewById(R.id.scoreButton);

        soundButton = (ToggleButton) findViewById(R.id.toggleButtonMusic);

          /* Gestion de l'appuie sur le bouton On/off du son */
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean soundEnable = !soundButton.isChecked();
                if (soundEnable) {
                    son.stop();
                }
                onResume();
            }
        });


        /*Gestion de l'appuie sur le boutton pour lancer le mode Chasse-Taupe*/
        chasseTaupeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                son.stop();
                boolean soundEnable = soundButton.isChecked();
                /*On lance  l'activity Chasse Taupe en lui passant en parametre
                si le son est activé ou pas
                 */
                Intent intent = new Intent(Menu.this, MenuDifficulte.class);
                intent.putExtra("fr.ups.sim.superpianotiles.SON", soundEnable);
                startActivityForResult(intent, 0);

               
            }

        });

        /* Gestion de l'appuie sur le bouton pour lancer le mode Defilement*/
        defilementButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                son.stop();
                boolean soundEnable = soundButton.isChecked();
                
                Intent intent = new Intent(Menu.this, TilesStartActivity.class);
                intent.putExtra("fr.ups.sim.superpianotiles.SON", soundEnable);
                startActivityForResult(intent, 0);
               
            }

        });

        scoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, ScoreActivity.class);
                startActivity(intent);
            }

        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // On vérifie tout d'abord à quel intent on fait référence ici à l'aide de notre identifiant

        if (requestCode == 0) {
            soundButton.setChecked(data.getBooleanExtra("fr.ups.sim.superpianotiles.SON", true));
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        son.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(soundButton.isChecked()) {
            son = MediaPlayer.create(this, R.raw.generique);
            son.start();
        }
    }
}
