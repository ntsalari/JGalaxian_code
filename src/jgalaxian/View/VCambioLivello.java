package jgalaxian.View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class VCambioLivello extends VSprite {

    private static final int maxframes = 5 * 60;
    private static final int animAttiva = 3 * 60;
    private int contaframe = -1;
    private int livello;
    private Font font;
    private int step;

    public VCambioLivello() {
        super();
        View view = View.getInstance();
        this.x = 0;
        this.y = view.getCostanteALTEZZA() / 2;
        font = new Font("Arial", font.PLAIN, 34);
        step = view.getCostanteLARGHEZZA() / (maxframes - (maxframes - animAttiva));
    }

    public void startAnim(int livello) {
        contaframe = maxframes;
        this.livello = livello;
        VFinestra.getInstance().disabilitaPausa();
    }

    @Override
    public void aggiornaStato() {
        if (contaframe > -1) {
            contaframe--;
            if (contaframe == 0) {
                View.getInstance().pausa(Costanti.FINE_CAMBIO_LIVELLO);
                View.getInstance().clearBattaglione();
                x = 0;
                VFinestra.getInstance().abilitaPausa();
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (contaframe > 0 && contaframe < animAttiva) {
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString("LIVELLO " + Integer.toString(livello), x += step, y);
        }
    }

}
