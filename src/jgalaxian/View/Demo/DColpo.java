package jgalaxian.View.Demo;

import jgalaxian.View.View;

public class DColpo extends DSprite {

    private DNaveGiocatore nave;
    private static final int X_POS = 12;
    private static final int Y_POS = 0;

    public DColpo(DNaveGiocatore nave) {
        super(View.getInstance().getPrimaImmagine("ColpoGiocatore"), 0, 0);
        this.nave = nave;
        super.x = nave.x + X_POS;
        super.y = nave.y - Y_POS;
    }

    @Override
    public void aggiornaStato() {
        int xy[] = nave.getCoord();
        x = xy[0] + X_POS;
        y = xy[1] - Y_POS;
    }

    @Override
    public void resetStato() {
        aggiornaStato();
    }
}
