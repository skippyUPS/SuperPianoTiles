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

/**
 * Created by Quentin on 21/03/2016.
 */
public class Menu extends Activity{
    MediaPlayer son;
    ToggleButton soundButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Button defilementButton = (Button) findViewById(R.id.button);
        final Button chasseTaupeButton = (Button) findViewById(R.id.button3);
        soundButton = (ToggleButton) findViewById(R.id.toggleButtonMusic);
        //son = MediaPlayer.create(this, R.raw.generique);

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

        chasseTaupeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                son.stop();
                Intent intent = new Intent(Menu.this, ChasseTaupeActivity.class);
                startActivity(intent);
            }

        });

        defilementButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                son.stop();
                boolean soundEnable = soundButton.isChecked();
                Log.i("BOOL", " : " + soundEnable);
                Intent intent = new Intent(Menu.this, TilesStartActivity.class);
                intent.putExtra("fr.ups.sim.superpianotiles.SON", soundEnable);
                startActivityForResult(intent, 0);
                //soundButton.setChecked(intent.getBooleanExtra("fr.ups.sim.superpianotiles.SON", true));
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
