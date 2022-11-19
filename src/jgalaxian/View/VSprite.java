package jgalaxian.View;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class VSprite {

    BufferedImage stato = null;
    Object sync;
    int x, y;

    VSprite() {
        this.sync = new Object();
    }

    VSprite(String path, int x, int y) {
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

    public boolean isVisualizzabile() {
        if (stato != null) {
            return true;
        } else {
            return false;
        }
    }

    public abstract void aggiornaStato();

}
