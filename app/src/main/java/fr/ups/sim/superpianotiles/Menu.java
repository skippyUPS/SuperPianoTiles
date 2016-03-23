package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Quentin on 21/03/2016.
 */
public class Menu extends Activity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        final Button defilementButton = (Button) findViewById(R.id.button);
        final Button chasseTaupeButton = (Button) findViewById(R.id.button3);

        chasseTaupeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, ChasseTaupe.class);
                startActivity(intent);
            }

        });

        defilementButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, TilesStartActivity.class);
                startActivity(intent);
            }

        });


    }

}
