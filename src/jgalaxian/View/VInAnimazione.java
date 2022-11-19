package jgalaxian.View;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class VInAnimazione extends JPanel {

    private VAlieno[][] alieni;
    private VExpAlieno[] esplosioni;
    private static final int maxExp = 20;
    private VExpGiocatore esplosioneGiocatore;
    private VSprite[] sprites;
    private VCambioLivello cambio;
    int auxindex = 0;

    public VInAnimazione(VAlieno[][] alieni) {
        super();
        this.alieni = alieni;
        esplosioni = new VExpAlieno[maxExp];
        esplosioneGiocatore = new VExpGiocatore();
        sprites = new VSprite[maxExp + 2];
        cambio = new VCambioLivello();
        View view = View.getInstance();
        for (int q = 0; q < maxExp; q++) {
            sprites[q] = esplosioni[q] = new VExpAlieno();
        }
        sprites[maxExp] = esplosioneGiocatore;
        sprites[maxExp + 1] = cambio;
        Dimension d = new Dimension(view.getCostanteLARGHEZZA(), view.getCostanteALTEZZA());
        this.setPreferredSize(d);
        this.setSize(d);
        this.setOpaque(false);
    }

    public void esplodiAlieno(int x, int y) {
        int expCorrente;
        synchronized (esplosioni) {
            expCorrente = auxindex;
            auxindex = (auxindex + 1) % maxExp;
        }
        esplosioni[expCorrente].startExp(x, y);
    }

    public VSprite[] getSprites() {
        return sprites;
    }

    public void animaElem() {
        for (VSprite s : sprites) {
            s.aggiornaStato();
        }
    }

    public void esplodiGiocatore(int x, int y) {
        esplosioneGiocatore.startExp(x, y);
    }

    public void cambioLivello() {
        View.getInstance().pausa(Costanti.INIZIO_CAMBIO_LIVELLO);
        cambio.startAnim(View.getInstance().getLivelloAttuale());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g1 = (Graphics2D) g;
        View view = View.getInstance();
        for (int i = 0; i < alieni.length; i++) {
            for (int j = 0; j < alieni[i].length; j++) {
                VAlieno a = alieni[i][j];
                if (a != null && a.isVisualizzabile()
                        && !view.alienoInFormazione(i, j)) {
                    a.render(g1);
                }
            }
        }
        for (int i = 0; i < esplosioni.length; i++) {
            if (esplosioni[i].isVisualizzabile()) {
                esplosioni[i].render(g1);
            }
        }
        if (esplosioneGiocatore.isVisualizzabile()) {
            esplosioneGiocatore.render(g1);
        }
        cambio.render(g1);
    }
}
