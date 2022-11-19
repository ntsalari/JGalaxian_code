package jgalaxian.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VStatici extends JPanel {

    JLabel punteggio;
    JLabel punteggioMax;
    JLabel oneup;
    ImageIcon vite = null;
    ImageIcon bandieraLivello = null;

    final int MARGINE = 5;
    static final int NUM_MAX_LIVELLI_MOSTRABILI = 15;

    public VStatici() {
        super();
        View view = View.getInstance();
        punteggio = new JLabel("0");
        
        View.getInstance().log("Miglior punteggio in VStatici: " + String.valueOf(view.getMigliorPunteggioClassifica()));
                
        punteggioMax = new JLabel(String.valueOf(view.getMigliorPunteggioClassifica()));
        oneup = new JLabel("1UP");
        JLabel topscore = new JLabel("TOP SCORE");
        int height = 20;
        final int LARGHEZZA = view.getCostanteLARGHEZZA();
        final int ALTEZZA = view.getCostanteALTEZZA();

        this.setLayout(null);
        JPanel nord[] = new JPanel[2];
        for (int i = 0; i < 2; i++) {
            nord[i] = new JPanel();
            nord[i].setOpaque(false);
            nord[i].setLayout(null);
            this.add(nord[i]);
            nord[i].setBounds(0, i * height, LARGHEZZA, height);
        }
        nord[0].add(oneup);
        nord[0].add(topscore);
        nord[1].add(punteggio);
        nord[1].add(punteggioMax);
        oneup.setBounds(0, 0, 30, height);
        oneup.setForeground(Color.white);
		int x = view.getCostanteLARGHEZZA()/2;
        topscore.setBounds(x-35, 0, 80, height);
        topscore.setForeground(Color.white);
        punteggio.setBounds(0, 0, 60, height);
        punteggio.setForeground(Color.red);
        punteggioMax.setBounds(x, 0, 60, height);
        punteggioMax.setForeground(Color.red);

        this.vite = new ImageIcon(this.getClass().getResource(View.getInstance().getPrimaImmagine("Vita")));
        this.bandieraLivello = new ImageIcon(this.getClass().getResource(View.getInstance().getPrimaImmagine("BandieraLivello")));

        Dimension d = new Dimension(LARGHEZZA, ALTEZZA);
        this.setPreferredSize(d);
        this.setSize(d);
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        View view = View.getInstance();

        for (int i = 0; i < view.getViteAttuali(); i++) {
            vite.paintIcon(this, g, i * vite.getIconWidth() + MARGINE + MARGINE * i, this.getHeight() - vite.getIconHeight() - MARGINE);
        }

        int livelloAttuale = view.getLivelloAttuale();
        
        if (livelloAttuale > NUM_MAX_LIVELLI_MOSTRABILI)
            livelloAttuale = NUM_MAX_LIVELLI_MOSTRABILI;
        
        for (int i = 0; i < livelloAttuale; i++) {
            bandieraLivello.paintIcon(this, g, this.getWidth() - (i + 1) * bandieraLivello.getIconWidth() - (MARGINE + MARGINE * i), this.getHeight() - bandieraLivello.getIconHeight() - MARGINE);
        }
    }

    public void aggiornaPunteggio(int punti) {
        punteggio.setText(Integer.toString(punti));
        
        if (punti > Integer.parseInt(punteggioMax.getText()))
            punteggioMax.setText(String.valueOf(punti));
    }
}
