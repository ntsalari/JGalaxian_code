package jgalaxian.View;

import java.awt.Graphics2D;

public class VAlieno extends VSprite {

    private int i, j;

    public VAlieno(int i, int j) {
        super();
        this.i = i;
        this.j = j;
    }

    @Override
    public void aggiornaStato() {
        if (!View.getInstance().alienoEsploso(i, j)) {
            int[] xy = View.getInstance().getCoordAlieno(i, j);
            String path = View.getInstance().getNuovaSkin(i, j);
            stato = ImgCache.getInstance().getImage(path);
            super.x = xy[0];
            super.y = xy[1];
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (View.getInstance().alienoEsploso(i, j)) {
            super.stato = null;
        } else {
            super.render(g);
        }
    }

}
