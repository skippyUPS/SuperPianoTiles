package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

/**
 * Created by Quentin on 22/03/2016.
 */
public class ChasseTaupe extends Activity{

    ChasseTaupeView tilesView;
    MediaPlayer mPlayer;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chassetaupe);

            //ICI - Commentez le code
            tilesView = (ChasseTaupeView) findViewById(R.id.view2);

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

            Tuile tuile = this.tilesView.getTuileFromPos((int)evt.getX(),(int) evt.getY());

            if(evt.getAction()==MotionEvent.ACTION_DOWN) {
                Log.i("TilesView", "Touch event handled");
                if(tuile != null)
                {
                    Rect r = tuile.getRectangle();
                    if(r.contains((int) evt.getX(), (int) evt.getY())) {
                        int raw = tuile.getRaw();
                        mPlayer = MediaPlayer.create(this, raw);
                        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mPlayer.start();
                    }
                }
                tilesView.setRun(true);

            }
            if(evt.getAction()==MotionEvent.ACTION_UP){
                tilesView.setRun(false);
                if(tuile != null)
                {
                    tilesView.setTiles(null);
                    tilesView.invalidate();
                }
            }

            return true;
        }
}




