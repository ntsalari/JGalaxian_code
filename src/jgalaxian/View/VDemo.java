package jgalaxian.View;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import jgalaxian.View.Demo.*;

public class VDemo extends JPanel {

    private DAlieno[][] alieni;
    private DNaveGiocatore giocatore;
    private DColpo colpo;
    private static VDemo sinDemo;

    private VDemo() {
        super();
        giocatore = new DNaveGiocatore();
        colpo = new DColpo(giocatore);
        alieni = new DAlieno[8][10];
        alieni[0][3] = new DAlienoBoss(0, 3);
        alieni[0][6] = new DAlienoBoss(0, 6);
        int i = 1;
        for (int j = 2; j < 8; j++) {
            alieni[i][j] = new DAlienoRosso(i, j);
        }
        i++;
        for (int j = 1; j < 9; j++) {
            alieni[i][j] = new DAlienoViola(i, j);
        }
        for (i = 3; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                alieni[i][j] = new DAlienoBlu(i, j);
            }
        }
        View view = View.getInstance();
        Dimension d = new Dimension(view.getCostanteLARGHEZZA(), view.getCostanteALTEZZA());
        this.setPreferredSize(d);
        this.setSize(d);
        this.setOpaque(false);
    }

    public static VDemo getInstance() {
        if (sinDemo == null) {
            sinDemo = new VDemo();
        }
        return sinDemo;
    }

    public void nextAnim() {
        boolean limite = false;
        giocatore.aggiornaStato();
        colpo.aggiornaStato();
        limite = giocatore.limiteRaggiunto();
        if (limite) {
            DNaveGiocatore.cambiaDir();
        }
        limite = false;
        for (DAlieno[] rigaAlieni : alieni) {
            for (DAlieno a : rigaAlieni) {
                if (a != null) {
                    a.aggiornaStato();
                    if (!limite) {
                        limite = a.limiteRaggiunto();
                    }
                }
            }
        }
        if (limite) {
            DAlieno.cambiaDir();
        }
    }

    public void resetDemo() {
        giocatore.resetStato();
        colpo.resetStato();
        for (int i = 0; i < alieni.length; i++) {
            for (DAlieno a : alieni[i]) {
                if (a != null) {
                    a.resetStato();
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g1 = (Graphics2D) g;
        giocatore.render(g1);
        colpo.render(g1);
        for (int i = 0; i < alieni.length; i++) {
            for (DAlieno a : alieni[i]) {
                if (a != null) {
                    a.render(g1);
                }
            }
        }
    }
}
