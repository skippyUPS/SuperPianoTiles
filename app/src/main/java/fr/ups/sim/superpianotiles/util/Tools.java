package fr.ups.sim.superpianotiles.util;

import java.net.URI;
import java.util.Random;

import fr.ups.sim.superpianotiles.R;

/**
 * Created by LeClem on 21/03/2016.
 */
public class Tools {
    private final int CARTMAN = 0;
    private final int KENNY = 1;
    private final int KYLE = 2;
    private final int STAN = 3;


    /* Renvoie un identifiant d'image,
     * -1 en cas d'echec */
    public int randomImage()
    {
        Random r = new Random();

        switch(r.nextInt(0 - 3 + 1) + 0)
        {
            case CARTMAN: return R.drawable.cartman;
            case KENNY: return R.drawable.kenny;
            case KYLE: return R.drawable.kyle;
            case STAN: return R.drawable.stan;
        }
        return -1;
    }


    /* Pour un identifiant d'image, renvoie un identifiant de fichier audio
     * -1 en cas d'echec */
    public int randomSound(int imageId)
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
}
