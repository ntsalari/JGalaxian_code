package jgalaxian.View.Demo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import jgalaxian.View.ImgCache;
import jgalaxian.View.View;

public abstract class DSprite {

    BufferedImage stato = null;
    Object sync;
    int x, y;

    DSprite() {
        this.sync = new Object();
    }

    DSprite(String path, int x, int y) {
        this.x = x;
        this.y = y;
        this.sync = new Object();
        stato = ImgCache.getInstance().getImage(path);
    }

    public void setStato(String path) {
        synchronized (sync) {
            stato = ImgCache.getInstance().getImage(path);
        }
    }

    public BufferedImage getStato() {
        synchronized (sync) {
            return stato;
        }
    }

    public void render(Graphics2D g) {
        synchronized (sync) {
            g.drawImage(stato, x, y, null);
        }
    }

    public int[] getCoord() {
        int xy[] = ArrayPosizioni.getInstance().getArray();
        xy[0] = this.x;
        xy[1] = this.y;
        return xy;
    }

    public boolean isVisualizzabile() {
        if (stato != null) {
            return true;
        } else {
            return false;
        }
    }

    public abstract void aggiornaStato();

    public abstract void resetStato();

    public boolean limiteRaggiunto() {
        View view = View.getInstance();

        if (stato != null) {
            if (x <= 0 || (x + stato.getWidth()) > View.getInstance().getCostanteLARGHEZZA()) {
                return true;
            }
        }
        return false;
    }

}
