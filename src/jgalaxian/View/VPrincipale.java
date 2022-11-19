package jgalaxian.View;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class VPrincipale extends JPanel {

    private VNaveGiocatore navetta;
    private VColpoGiocatore colpoGiocatore;
    private VSprite[] elementi;
    private VSprite[] elementiEAlieni;
    private VAlieno[][] alieni;

    private VColpoAlieno[] colpiAlieni;

    public VPrincipale() {
        super();
        View view = View.getInstance();
        elementi = new VSprite[200];
        elementiEAlieni = new VSprite[200];
        alieni = new VAlieno[6][10];
        colpiAlieni = new VColpoAlieno[view.getColpiAlieni()];
        elementi[0] = navetta = new VNaveGiocatore(View.getInstance().getPosizioneGiocatore(), view.getStartYGiocatore());
        elementi[1] = colpoGiocatore = new VColpoGiocatore(0, 0);
        alieni[0][3] = new VAlieno(0, 3);
        alieni[0][6] = new VAlieno(0, 6);
        int k = 2;
        int i = 1;
        for (int j = 2; j < 8; j++) {
            alieni[i][j] = new VAlieno(i, j);
        }
        i++;
        for (int j = 1; j < 9; j++) {
            alieni[i][j] = new VAlieno(i, j);
        }
        for (i = 3; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                alieni[i][j] = new VAlieno(i, j);
            }
        }
        for (i = 0; i < colpiAlieni.length; i++) {
            elementi[k] = colpiAlieni[i] = new VColpoAlieno(i);
            k++;
        }
        for (k = 0; k < elementi.length; k++) {
            elementiEAlieni[k] = elementi[k];
            if (elementi[k] == null) {
                break;
            }
        }
        for (i = 0; i < alieni.length; i++) {
            for (int j = 0; j < alieni[i].length; j++) {
                VAlieno a = alieni[i][j];
                if (a != null) {
                    elementiEAlieni[k] = alieni[i][j];
                    k++;
                }
            }
        }
        Dimension d = new Dimension(view.getCostanteLARGHEZZA(), view.getCostanteALTEZZA());
        this.setPreferredSize(d);
        this.setSize(d);
        this.setOpaque(false);
    }

    public VSprite[] getSprites() {
        return elementiEAlieni;
    }

    public VAlieno[][] getAlieni() {
        return alieni;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g1 = (Graphics2D) g;
        View view = View.getInstance();
        for (VSprite i : elementi) {
            if (i != null) {
                if (i.isVisualizzabile()) {
                    i.render(g1);
                }
            } else {
                break;
            }
        }

        for (int i = 0; i < alieni.length; i++) {
            for (int j = 0; j < alieni[i].length; j++) {
                VAlieno a = alieni[i][j];
                if (a != null && a.isVisualizzabile()
                        && view.alienoInFormazione(i, j)) {
                    a.render(g1);
                }
            }
        }
    }
}
