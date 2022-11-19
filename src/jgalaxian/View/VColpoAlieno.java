package jgalaxian.View;

import java.awt.Graphics2D;

public class VColpoAlieno extends VSprite {

    private int i;

    public VColpoAlieno(int i) {
        super(View.getInstance().getPrimaImmagine("ColpoAlieno"), 0, 0);
        this.i = i;
    }

    @Override
    public void render(Graphics2D g) {
        if (View.getInstance().colpoAlienoVisibile(i)) {
            super.render(g);
        }
    }

    @Override
    public void aggiornaStato() {
        View view = View.getInstance();

        if (view.colpoAlienoVisibile(i)) {
            int xy[] = view.getCoordColpoAlieno(i);
            x = xy[0];
            y = xy[1];
        }
    }
}
