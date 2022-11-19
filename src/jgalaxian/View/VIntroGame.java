package jgalaxian.View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class VIntroGame extends JPanel {

    private View view;

    private VFinestra parentJFrame;
    private SchermataGiocatore schermataGiocatore;
    private VSfondo sfondo;
    private JLayeredPane ambienti;
    private JButton buttonStart;
    private VLogo logo;
    //private VLogo gifIntro;
    private VDemo demoScreen;

    public VIntroGame(VFinestra parent, byte[][] datiSfondo) {
        super();

        View.getInstance().log("Sono qui 4");
        view = View.getInstance();
        this.parentJFrame = parent;

        int tmpCostanteLarghezza = view.getCostanteLARGHEZZA();
        int tmpCostanteAltezza = view.getCostanteALTEZZA();

        schermataGiocatore = new SchermataGiocatore(this.parentJFrame, tmpCostanteLarghezza, tmpCostanteAltezza);
        buttonStart = new javax.swing.JButton("Avvia gioco");
        sfondo = new VSfondo();
        sfondo.setMatrix(datiSfondo);
        ambienti = new JLayeredPane();
        logo = new VLogo(tmpCostanteLarghezza / 2, 50, "/Resources/Images/logo_small.png");
        //gifIntro = new VLogo(tmpCostanteLarghezza / 2, -30, "/Resources/Images/introGif.gif");
        demoScreen = VDemo.getInstance();

        View.getInstance().log("Sono qui 5");

        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AvviaGiocoActionPerformed(evt);
            }
        });

        View.getInstance().log("Sono qui 6");
        this.setLayout(new BorderLayout());

        View.getInstance().log("Sono qui 7");
        ambienti.setPreferredSize(new Dimension(tmpCostanteLarghezza, tmpCostanteAltezza));
        View.getInstance().log("Sono qui 7 BIS");
        ambienti.setSize(tmpCostanteLarghezza, tmpCostanteAltezza);
        View.getInstance().log("Sono qui 7 TRIS");
        this.add(ambienti, BorderLayout.SOUTH);
        View.getInstance().log("Sono qui 8");

        buttonStart.setPreferredSize(new Dimension(100, 50));

        schermataGiocatore.setVisible(false);

        ambienti.setLayout(new BorderLayout());
        ambienti.add(schermataGiocatore, BorderLayout.SOUTH, Integer.valueOf(-1));
        ambienti.add(logo, BorderLayout.PAGE_START, Integer.valueOf(-2));
        //ambienti.add(gifIntro, BorderLayout.CENTER, Integer.valueOf(-3));
        ambienti.add(buttonStart, BorderLayout.PAGE_END, Integer.valueOf(-3));
        ambienti.add(demoScreen, BorderLayout.SOUTH, Integer.valueOf(-4));
        ambienti.add(sfondo, BorderLayout.SOUTH, Integer.valueOf(-5));

        View.getInstance().log("Sono qui 9");
        Dimension d = new Dimension(1450, 585);
        this.setSize(d);
        View.getInstance().log("Sono qui 10");

    }

    public void resetGui() {
        schermataGiocatore.aggiornaProfiliGiocatore();
        buttonStart.setVisible(true);
        schermataGiocatore.setVisible(false);
    }

    public VSfondo getSfondo() {
        return sfondo;
    }

    private void AvviaGiocoActionPerformed(java.awt.event.ActionEvent evt) {
        buttonStart.setVisible(false);
        schermataGiocatore.setVisible(true);
    }

}
