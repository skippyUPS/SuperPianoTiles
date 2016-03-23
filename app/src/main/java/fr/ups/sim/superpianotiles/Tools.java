package fr.ups.sim.superpianotiles;

import java.net.URI;
import java.util.Random;

import fr.ups.sim.superpianotiles.R;

/**
 * Created by LeClem on 21/03/2016.
 */
public class Tools {
    private static final int CARTMAN = 0;
    private static final int KENNY = 1;
    private static final int KYLE = 2;
    private static final int STAN = 3;


    /* Renvoie un identifiant d'image,
     * -1 en cas d'echec */
    public static int randomImage()
    {
        Random r = new Random();
        int val = r.nextInt(4);

        switch(val){
            case CARTMAN: return R.drawable.cartman;
            case KENNY: return R.drawable.kenny;
            case KYLE: return R.drawable.kyle;
            case STAN: return R.drawable.stan;
        }
        return -1;
    }

    /*Renvoi un nom de personnage*/
    public static  String randomNom(){
        Random r = new Random();
        int val = r.nextInt(4);

        switch(val){
            case CARTMAN: return "cartman";
            case KENNY: return "kenny";
            case KYLE: return "kyle";
            case STAN: return "stan";
        }
        return null;
    }
    /* Pour un identifiant d'image, renvoie un identifiant de fichier audio
     * -1 en cas d'echec */
    public static int randomSound(int imageId)
    {
        switch (imageId)
        {
            case R.drawable.cartman: return R.raw.cartman1;
            case R.drawable.kenny: return R.raw.kenny1;
            case R.drawable.kyle: return R.raw.kyle;
            case R.drawable.stan: return R.raw.stanley1;
        }
        return -1;
    }

    /* Pour un nom de personnage, renvoie un identifiant de fichier audio
            * -1 en cas d'echec */
    public static int randomSound(String nomPerso)
    {
        if (nomPerso.equals("cartman")) {
            return R.raw.cartman1;
        } else if (nomPerso.equals("kenny")) {
            return R.raw.kenny1;
        } else if (nomPerso.equals("kyle")) {
            return R.raw.kyle;
        } else if (nomPerso.equals("stan")) {
            return R.raw.stanley1;
        }
        return -1;
    }
}
