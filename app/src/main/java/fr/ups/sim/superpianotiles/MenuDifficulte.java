package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

/**
 * Created by Quentin on 02/04/2016.
 */
public class MenuDifficulte extends Activity{

    MediaPlayer son; /* Variable pour gerer le son du Menu*/
    ToggleButton soundButton; /* Boutton a deux etat  Off/On pour le son dans le menu et le reste
                                de l'application */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_difficulte);  /* On utilise le layout que l'on a cree */

        /*On recupere les differents boutton g*/

        final Button proButton = (Button) findViewById(R.id.button);
        final Button noobButton = (Button) findViewById(R.id.button3);
        final Button retour = (Button) findViewById(R.id.button2);
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
        proButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                son.stop();
                boolean soundEnable = soundButton.isChecked();
                /*On lance  l'activity Chasse Taupe en lui passant en parametre
                si le son est activé ou pas
                 */


                Intent intent = new Intent(MenuDifficulte.this, ChasseTaupeActivity.class);
                intent.putExtra("fr.ups.sim.superpianotiles.SON", soundEnable);
                intent.putExtra("Difficulte", "Dur");
                startActivityForResult(intent, 0);

            }

        });

        /* Gestion de l'appuie sur le bouton pour lancer le mode Defilement*/
        noobButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                son.stop();
                boolean soundEnable = soundButton.isChecked();


                Intent intent = new Intent(MenuDifficulte.this, ChasseTaupeActivity.class);
                intent.putExtra("fr.ups.sim.superpianotiles.SON", soundEnable);
                intent.putExtra("Difficulte", "Facile");
                startActivityForResult(intent, 0);

            }

        });

        retour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent data = new Intent();
                data.putExtra("fr.ups.sim.superpianotiles.SON", soundButton.isChecked());
                setResult(RESULT_OK, data);
                finish();

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
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("fr.ups.sim.superpianotiles.SON", soundButton.isChecked());
        setResult(RESULT_OK, data);
        finish();

    }


    @Override
    protected void onStop() {
        super.onStop();
        son.stop();
    }

    /*Initalise le son pendant le menu et aussi quand l'on revient sur le menu*/
    @Override
    protected void onResume() {
        super.onResume();
        if(soundButton.isChecked()) {
            son = MediaPlayer.create(this, R.raw.generique);
            son.start();
        }
    }
}
