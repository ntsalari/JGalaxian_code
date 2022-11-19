package jgalaxian.View;

import java.awt.Graphics2D;

public class VColpoGiocatore extends VSprite {

    public VColpoGiocatore(int x, int y) {
        super(View.getInstance().getPrimaImmagine("ColpoGiocatore"), x, y);
    }

    @Override
    public void render(Graphics2D g) {
        if (View.getInstance().colpoGVisibile()) {
            super.render(g);
        }
    }

    @Override
    public void aggiornaStato() {
        View view = View.getInstance();
        
        if (view.colpoGVisibile()) {
            int xy[] = view.getCoordColpoG();
            x = xy[0];
            y = xy[1];
        }
    }

}
