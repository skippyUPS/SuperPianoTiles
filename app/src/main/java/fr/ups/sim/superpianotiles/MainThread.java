package fr.ups.sim.superpianotiles;

/**
 * Created by Corentin on 19/03/2016.
 */
public class MainThread extends Thread {
    public TilesView tilesView;

    public MainThread(TilesView tilesView) {
        this.tilesView = tilesView;
    }

    @Override
    public void run() {
        super.run();
        tilesView.invalidate();
        tilesView.postDelayed(this, 10);//1000 ms / 30fps
    }
}
