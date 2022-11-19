package jgalaxian.View;

import java.awt.Graphics2D;

public class VNaveGiocatore extends VSprite {

    public VNaveGiocatore(int x, int y) {
        super(View.getInstance().getPrimaImmagine("Giocatore"), x, y);
    }

    @Override
    public void aggiornaStato() {
        int pos = View.getInstance().getPosizioneGiocatore();
        x = pos;
    }
	
	@Override
    public void render(Graphics2D g) {
        if (!View.getInstance().giocatoreEsploso()) {
            super.render(g);
        }
    }

}
