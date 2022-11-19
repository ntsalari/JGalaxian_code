package jgalaxian.View.Demo;

import jgalaxian.View.View;

public class DNaveGiocatore extends DSprite {

    private static int dir = 1;

    public DNaveGiocatore() {
        super(View.getInstance().getPrimaImmagine("Giocatore"), 0, 0);
        super.x = View.getInstance().getCostanteLARGHEZZA() / 2;
        super.y = View.getInstance().getStartYGiocatore();
    }

    @Override
    public void aggiornaStato() {
        super.x += dir;
    }

    @Override
    public void resetStato() {
        super.x = View.getInstance().getCostanteLARGHEZZA() / 2;
    }

    public static void cambiaDir() {
        dir = -dir;
    }

}
