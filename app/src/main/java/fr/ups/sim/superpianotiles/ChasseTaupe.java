package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

/**
 * Created by Quentin on 22/03/2016.
 */
public class ChasseTaupe extends Activity{


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tiles_start);

            //ICI - Commentez le code
            TilesView tilesView = (TilesView) findViewById(R.id.view);

            //ICI - Commentez le code
            tilesView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return onTouchEventHandler(event);

                }
            });
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
            Log.i("TilesView", "Touch event handled");
            return true;
        }
}




