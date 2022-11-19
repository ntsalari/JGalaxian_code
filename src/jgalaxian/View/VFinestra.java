package jgalaxian.View;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class VFinestra extends JFrame {

    private JMenuItem ButtonEdit;
    private JMenuItem ButtonEsci;
    private JMenuItem ButtonFine;
    private JCheckBoxMenuItem ButtonPausa;
    private JMenu menuOpz;
    private JMenuBar menubar;
    private JLayeredPane ambienti;
    private VSfondo sfondo;
    private VPrincipale area;
    private VStatici oggetti;
    private VInAnimazione animati;
    private JCheckBoxMenuItem musicaBackground;
    private JCheckBoxMenuItem effetti;
    private VIntroGame menuIniziale;
    private VClassifica classifica;
    private static VFinestra sinFinestra = null;

    public VFinestra() {
        super();

        menubar = new javax.swing.JMenuBar();
        menuOpz = new javax.swing.JMenu("Opzioni");
        ButtonEdit = new javax.swing.JMenuItem("Comandi tastiera");
        ButtonEsci = new javax.swing.JMenuItem("Esci");
        ButtonPausa = new JCheckBoxMenuItem("Pausa");
        musicaBackground = new JCheckBoxMenuItem("Musica background");
        effetti = new JCheckBoxMenuItem("Effetti sonori");
        ButtonFine = new JMenuItem("Fine partita");
        area = new VPrincipale();
        sfondo = new VSfondo();
        sfondo.initMatrix();
        ambienti = new JLayeredPane();
        oggetti = new VStatici();
        animati = new VInAnimazione(area.getAlieni());
        menuIniziale = new VIntroGame(this, sfondo.getMatrix());
        classifica = new VClassifica(this, sfondo.getMatrix());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(ExitManager.getInstance());

        classifica.setVisible(false);

        musicaBackground.setState(true);
		musicaBackground.setEnabled(false);
        musicaBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MusicaBackgroundActionPerformed(evt);
            }
        });
        menuOpz.add(musicaBackground);

        effetti.setState(true);
		effetti.setEnabled(false);
        effetti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EffettiActionPerformed(evt);
            }
        });
        menuOpz.add(effetti);

        ButtonEdit.setEnabled(false);
        ButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });
        menuOpz.add(ButtonEdit);

        ButtonPausa.setState(false);
        ButtonPausa.setEnabled(false);
        ButtonPausa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                PausaActionPerformed(ae);
            }
        });
        menuOpz.add(ButtonPausa);

        ButtonFine.setEnabled(false);
        ButtonFine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonFineActionPerformed(e);
            }
        });
        menuOpz.add(ButtonFine);

        ButtonEsci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EsciActionPerformed(evt);
            }
        });
        menuOpz.add(ButtonEsci);

        menubar.add(menuOpz);

        setJMenuBar(menubar);

        this.setLayout(new BorderLayout());
        Container content = this.getContentPane();
        View view = View.getInstance();
        ambienti.setPreferredSize(new Dimension(view.getCostanteLARGHEZZA(), view.getCostanteALTEZZA()));
        ambienti.setSize(view.getCostanteLARGHEZZA(), view.getCostanteLARGHEZZA());
        this.add(ambienti, BorderLayout.PAGE_END);

        ambienti.setLayout(new BorderLayout());
        /* L'ordine con cui vengono aggiunti è importantissimo perché swing
         * iniza a disegnare prima l'ULTIMO ELEMENTO AGGIUNTO!!!
         */

        ambienti.add(menuIniziale, BorderLayout.PAGE_START, Integer.valueOf(0));
        ambienti.add(classifica, BorderLayout.PAGE_END, Integer.valueOf(-6));
        ambienti.add(oggetti, BorderLayout.PAGE_END, Integer.valueOf(-7));
        ambienti.add(animati, BorderLayout.PAGE_END, Integer.valueOf(-8));
        ambienti.add(area, BorderLayout.PAGE_END, Integer.valueOf(-9));
        ambienti.add(sfondo, BorderLayout.PAGE_END, Integer.valueOf(-10));

        setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public static VFinestra getInstance() {
        if (sinFinestra == null) {
            sinFinestra = new VFinestra();
        }
        return sinFinestra;
    }

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {
        VSelComandi cmd = new VSelComandi(this);
        cmd.setVisible(true);
    }

    public VSfondo getSfondo() {
        return sfondo;
    }

    public VSfondo getMainGuiSfondo() {
        return menuIniziale.getSfondo();
    }

    public VStatici getStatici() {
        return oggetti;
    }

    public VPrincipale getAmbiente() {
        return area;
    }

    public VInAnimazione getAnimati() {
        return animati;
    }

    public void rimuoviLayerMenuUtente() {
        menuIniziale.setVisible(false);
    }
    
    public void rimuoviLayerClassifica() {
        classifica.setVisible(false);
    }

    public void aggiornaPunteggio(int punteggio) {
        oggetti.aggiornaPunteggio(punteggio);
        //this.repaint();
    }

    private void EsciActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void PausaActionPerformed(ActionEvent ae) {
        View.getInstance().log("Click tasto PAUSA");
        View.getInstance().pausa(Costanti.PAUSA_TASTO_GUI);
    }
    
    public boolean isPausaSelected() {
        return ButtonPausa.isSelected();
    }
	
	public void abilitaPausa(){
		ButtonPausa.setEnabled(true);
	}
	
	public void disabilitaPausa(){
		ButtonPausa.setEnabled(false);
	}

    private void MusicaBackgroundActionPerformed(java.awt.event.ActionEvent evt) {
        if (musicaBackground.isSelected()) {
            View.getInstance().togliMutoBackground();
            View.getInstance().log("Click");
        } else {
            View.getInstance().mutaBackground();
            View.getInstance().log("Clack");
        }
    }
    
    public boolean isMusicaBackgroundSelected() {
        return musicaBackground.isSelected();
    }
            
    private void EffettiActionPerformed(java.awt.event.ActionEvent evt) {
        if (effetti.isSelected()) {
            View.getInstance().togliMutoEffettiSonori();
            View.getInstance().log("Click");
        } else {
            View.getInstance().mutaEffettiSonori();
            View.getInstance().log("Clack");
        }
    }
    
    public boolean isEffettiSelected() {
        return effetti.isSelected();
    }
    
    public void mostraMenuIniziale() {
        View.getInstance().log("SONO DENTRO MOSTRA MENU INIZIALE");
        menuIniziale.resetGui();
        menuIniziale.setVisible(true);
    }


    private void ButtonFineActionPerformed(ActionEvent ae) {
        View view = View.getInstance();
        boolean mutoBackground = (view.getVolumeBackground() == 0 ? true : false);
        boolean mutoEffettiSonori = (view.getVolumeEffettiSonori() == 0 ? true : false);

        if (isPausaSelected()) {            
            view.mutaEffettiSonori();
            view.mutaBackground();
            PausaActionPerformed(null);
            ButtonPausa.setState(false);
        }
        
        view.terminaGioco();
        view.stopTutti();
        view.resetMusicaBackground(true);
        
        if (!mutoBackground)
            view.togliMutoBackground();
        
        if (!mutoEffettiSonori)
            view.togliMutoEffettiSonori();
        
        mostraMenuIniziale();
    }

    public void gameOver(String nomeGiocatore, int punteggio) {
        View.getInstance().terminaGioco();
        classifica.aggiornaClassifica(nomeGiocatore, punteggio);
        classifica.setVisible(true);
    }
    
    public void stopClickFineGioco() {
        ButtonEdit.setEnabled(false);
        ButtonPausa.setEnabled(false);
        ButtonFine.setEnabled(false);
    }
    
    public void abilitaClickInizioGioco() {
        ButtonEdit.setEnabled(true);
        ButtonPausa.setEnabled(true);
        ButtonFine.setEnabled(true);
    }
	
	public void abilitaClickMusiche(){
		musicaBackground.setEnabled(true);
		effetti.setEnabled(true);
	}
    
}
