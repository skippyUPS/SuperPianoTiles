package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;


/**
 * Created by LeClem on 02/04/2016.
 */
public class ScoreActivity extends Activity {
    /* Enregistrement du score */
    private SharedPreferences settings;
    public static final String SCORES_DEFILEMENT_XML = "scoresDefilement.xml";
    public static final String SCORE_TAUPE_XML = "scoreTaupe.xml";
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
    private Set<Integer> scoreSetDynamique = new TreeSet<Integer>(comp);
    private Set<Integer> scoreSetStatique= new TreeSet<Integer>(comp);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        /* DEFILEMENT */
        /* Récupération du fichier, puis stockage dans Tree */
        settings = getSharedPreferences(SCORES_DEFILEMENT_XML, 0);
        for(String key : settings.getAll().keySet())
        {
            scoreSetDynamique.add((Integer) settings.getAll().get(key));
        }

        /* Envoie des données à la liste */
        ArrayAdapter<?> myAdapter=new
                ArrayAdapter<Object>(
                this,
                android.R.layout.simple_list_item_1,
                scoreSetDynamique.toArray());

        /* Mise à jour */
        ListView scoreListViewDyn = (ListView) findViewById(R.id.listViewDyn);
        scoreListViewDyn.setAdapter(myAdapter);





        /* CHASSE TAUPE */
        /* Récupération du fichier, puis stockage dans Tree */
        settings = getSharedPreferences(SCORE_TAUPE_XML, 0);
        for(String key : settings.getAll().keySet())
        {
            scoreSetStatique.add((Integer) settings.getAll().get(key));
        }

        /* Envoie des données à la liste */
        myAdapter=new
                ArrayAdapter<Object>(
                this,
                android.R.layout.simple_list_item_1,
                scoreSetStatique.toArray());

        /* Mise à jour */
        ListView scoreListViewStat = (ListView) findViewById(R.id.listViewStat);
        scoreListViewStat.setAdapter(myAdapter);

        Log.e("COUILLE", scoreSetStatique.toString());
    }

    @Override
    public void onBackPressed() {
        finish();
    }



}
