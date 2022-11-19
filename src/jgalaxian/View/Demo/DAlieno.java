package jgalaxian.View.Demo;

import jgalaxian.View.View;

public abstract class DAlieno extends DSprite {

    private int i, j;

    private static int dir = 1;
    private static final int Y_POS = 95;

    int statoAttuale;

    public DAlieno(int i, int j) {
        super();
        View view = View.getInstance();
        this.i = i;
        this.j = j;
        if (j % 2 == 0) {
            statoAttuale = 0;
        } else {
            statoAttuale = 32;
        }
        super.y = Y_POS + view.getRowSpacing() * i;
        super.x = view.getColSpacing() * j;
    }

    @Override
    public void aggiornaStato() {
        super.x += dir;
        statoAttuale = (statoAttuale + 1) % 64;
    }

    public static void cambiaDir() {
        dir = -dir;
    }

    @Override
    public void resetStato() {
        if (i % 2 == 0) {
            statoAttuale = 0;
        } else {
            statoAttuale = 32;
        }
    }

}
