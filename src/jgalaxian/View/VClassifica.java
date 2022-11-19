package jgalaxian.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class VClassifica extends JPanel {

    private View view;

    private ThreadLampeggio tl = null;

    private VFinestra parentJFrame;
    private VSfondo sfondo;
    private JPanel panelRisultati;
    private JPanel panelPulsante;
    private JLayeredPane ambienti;
    private JLabel[] risultati = null;
    private JLabel labelClassifica;
    private JLabel labelPosizione;
    private JLabel labelNome;
    private JLabel labelPunteggio;
    private JLabel labelFuoriClassifica;
    private JLabel labelPosizioneFuoriClassifica;
    private JLabel labelNomeFuoriClassifica;
    private JLabel labelPunteggioFuoriClassifica;
    private JLabel labelErrore;
    private JButton buttonProsegui;
    private boolean primoClassificato = false;
    private boolean inClassificaMigliori = false;
    private int indexRisultati = -1;
    private int altezzaJPanelClassifica;
    private int altezzaJPanelPulsanteProsegui = 50;
    private int margineSuperiore = 30;
    private int margineSinistro = 30;
    private int margineDestro = 30;
    private int margineInferiore = 10;

    public VClassifica(VFinestra parent, byte[][] datiSfondo) {
        super();

        View.getInstance().log("Sono in VClassifica");
        view = View.getInstance();
        this.parentJFrame = parent;

        int larghezza = view.getCostanteLARGHEZZA();
        int altezza = view.getCostanteALTEZZA();

        this.altezzaJPanelClassifica = altezza - altezzaJPanelPulsanteProsegui;

        buttonProsegui = new javax.swing.JButton("Prosegui");
        sfondo = new VSfondo();
        sfondo.setMatrix(datiSfondo);
        ambienti = new JLayeredPane();
        ambienti.setPreferredSize(new Dimension(larghezza, altezza));
        ambienti.setSize(larghezza, altezza);

        buttonProsegui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View.getInstance().log("PREMUTO TASTO PROSEGUI IN VCLASSIFICA");
                view.terminaMusicaClassifica();
                if (tl != null) {
                    tl.fermati();
                }
                parentJFrame.rimuoviLayerClassifica();
                parentJFrame.mostraMenuIniziale();
            }
        });

        panelRisultati = new JPanel();
        panelRisultati.setPreferredSize(new Dimension(
                larghezza - margineSinistro - margineDestro,
                altezzaJPanelClassifica
        ));

        GridLayout layoutPanelRisultati = new GridLayout(0, 3);
        panelRisultati.setLayout(layoutPanelRisultati);
        panelRisultati.setOpaque(false);

        scriviClassifica(panelRisultati, null, -1);

        buttonProsegui.setPreferredSize(new Dimension(60, 30));
        buttonProsegui.setHorizontalAlignment(JButton.CENTER);

        panelPulsante = new JPanel();
        panelPulsante.setPreferredSize(new Dimension(
                larghezza - margineSinistro - margineDestro,
                altezzaJPanelPulsanteProsegui
        ));
        panelPulsante.setLayout(new BorderLayout());
        panelPulsante.setOpaque(false);
        panelPulsante.add(buttonProsegui, BorderLayout.CENTER);

        ambienti.setOpaque(false);
        ambienti.setLayout(new BorderLayout());
        ambienti.add(panelRisultati, BorderLayout.CENTER, Integer.valueOf(-1));
        ambienti.add(panelPulsante, BorderLayout.PAGE_END, Integer.valueOf(-2));
        ambienti.add(sfondo, BorderLayout.SOUTH, Integer.valueOf(-3));

        Dimension d = new Dimension(larghezza, altezza);
        this.setSize(d);
        this.setOpaque(false);
        this.add(ambienti);
    }

    private void scriviClassifica(JPanel panel, String nomeGiocatore, int punteggio) {
        View.getInstance().log("Sono dentro scriviClassifica");

        primoClassificato = false;
        inClassificaMigliori = false;
        indexRisultati = -1;

        String[][] classifica = view.getPrimeNRigheClassifica(0); //Tutta la classifica

        if (classifica != null) {

            panel.add(new JLabel(" ", JLabel.CENTER));
            labelClassifica = new JLabel("CLASSIFICA:", JLabel.CENTER);
            labelClassifica.setForeground(java.awt.Color.RED);
            labelClassifica.setFont(new Font("Arial", Font.BOLD, 23));
            panel.add(labelClassifica);
            panel.add(new JLabel(" ", JLabel.CENTER));

            labelPosizione = new JLabel("POSIZIONE", JLabel.CENTER);
            labelPosizione.setForeground(java.awt.Color.RED);
            labelPosizione.setFont(new Font("Arial", Font.BOLD, 21));
            panel.add(labelPosizione);
            labelNome = new JLabel("NOME", JLabel.CENTER);
            labelNome.setForeground(java.awt.Color.RED);
            labelNome.setFont(new Font("Arial", Font.BOLD, 21));
            panel.add(labelNome);
            labelPunteggio = new JLabel("PUNTEGGIO", JLabel.CENTER);
            labelPunteggio.setForeground(java.awt.Color.RED);
            labelPunteggio.setFont(new Font("Arial", Font.BOLD, 21));
            panel.add(labelPunteggio);
            risultati = new JLabel[classifica.length * 3];

            int i = 0, len = 0;

            if (classifica.length <= Costanti.NUM_RIGHE_CLASSIFICA) {
                len = classifica.length;
            } else {
                len = Costanti.NUM_RIGHE_CLASSIFICA;
            }

            for (int j = 0; i < len; i++, j = j + 3) {
                //So che tutti i risultati sono composti per forza da 3 parti (posizione, nome, punteggio) quindi:
                View.getInstance().log(classifica[i][0] + "\t"); //Posizione
                View.getInstance().log(classifica[i][1] + "\t\t"); //Nome
                View.getInstance().log(classifica[i][2]); //Punteggio

                Color color = findColor(i);

                risultati[j] = new JLabel(classifica[i][0] + "°", JLabel.CENTER);
                risultati[j].setForeground(color);
                risultati[j].setFont(new Font("Arial", Font.PLAIN, 21));
                risultati[j + 1] = new JLabel(classifica[i][1], JLabel.CENTER);
                risultati[j + 1].setForeground(color);
                risultati[j + 1].setFont(new Font("Arial", Font.PLAIN, 21));
                risultati[j + 2] = new JLabel(classifica[i][2], JLabel.CENTER);
                risultati[j + 2].setForeground(color);
                risultati[j + 2].setFont(new Font("Arial", Font.PLAIN, 21));
                View.getInstance().log("\n");

                panel.add(risultati[j]);
                panel.add(risultati[j + 1]);
                panel.add(risultati[j + 2]);

                //Non si dovrebbe mai verificare questo errore ma per sicurezza lo metto
                try {
                    if (classifica[i][1].equals(nomeGiocatore) && Integer.valueOf(classifica[i][2]) == punteggio) {
                        if (i == 0) {
                            primoClassificato = true;
                            indexRisultati = 0;
                        } else {
                            inClassificaMigliori = true;
                            indexRisultati = j;
                        }
                    }
                } catch (NumberFormatException nfe) {
                    View.getInstance().log("ERRORE: Il punteggio in classifica non è un numero!");
                }
            }

            if (classifica.length > Costanti.NUM_RIGHE_CLASSIFICA) {
                len = classifica.length;
            } else {
                len = Costanti.NUM_RIGHE_CLASSIFICA;
            }

            for (; i < len; i++) {
                Color color = findColor(i);
                //Righe classifica vuote
                if (i < Costanti.NUM_RIGHE_CLASSIFICA) {
                    JLabel label = new JLabel(String.valueOf(i + 1) + "°", JLabel.CENTER);
                    label.setForeground(color);
                    label.setFont(new Font("Arial", Font.PLAIN, 21));

                    panel.add(label);
                    panel.add(new JLabel(" ", JLabel.CENTER));
                    panel.add(new JLabel(" ", JLabel.CENTER));
                } else { //Cerco la posizione ottenuta da questa persona
                    if (primoClassificato == false && inClassificaMigliori == false) {
                        try {
                            if (classifica[i][1].equals(nomeGiocatore) && Integer.valueOf(classifica[i][2]) == punteggio) {
                                //Prima riga
                                panel.add(new JLabel(" ", JLabel.CENTER));
                                labelFuoriClassifica = new JLabel("RISULTATO:", JLabel.CENTER);
                                labelFuoriClassifica.setForeground(java.awt.Color.RED);
                                labelFuoriClassifica.setFont(new Font("Arial", Font.BOLD, 23));
                                panel.add(labelFuoriClassifica);
                                panel.add(new JLabel(" ", JLabel.CENTER));

                                //Seconda riga
                                labelPosizioneFuoriClassifica = new JLabel(classifica[i][0] + "°", JLabel.CENTER);
                                labelPosizioneFuoriClassifica.setForeground(color);
                                labelPosizioneFuoriClassifica.setFont(new Font("Arial", Font.PLAIN, 21));
                                labelNomeFuoriClassifica = new JLabel(classifica[i][1], JLabel.CENTER);
                                labelNomeFuoriClassifica.setForeground(color);
                                labelNomeFuoriClassifica.setFont(new Font("Arial", Font.PLAIN, 21));
                                labelPunteggioFuoriClassifica = new JLabel(classifica[i][2], JLabel.CENTER);
                                labelPunteggioFuoriClassifica.setForeground(color);
                                labelPunteggioFuoriClassifica.setFont(new Font("Arial", Font.PLAIN, 21));

                                panel.add(labelPosizioneFuoriClassifica);
                                panel.add(labelNomeFuoriClassifica);
                                panel.add(labelPunteggioFuoriClassifica);

                                break;
                            }
                        } catch (NumberFormatException nfe) {
                            View.getInstance().log("ERRORE: Il punteggio in classifica non è un numero!");
                        }
                    }
                }
            }

        } else {
            View.getInstance().log("\n\nNON HO TROVATO RISULTATI NELLA CLASSIFICA...");
            panel.add(new JLabel(" ", JLabel.CENTER));
            labelErrore = new JLabel("NON HO TROVATO RISULTATI NELLA CLASSIFICA", JLabel.CENTER);
            labelErrore.setForeground(java.awt.Color.RED);
            labelErrore.setFont(new Font("Arial", Font.PLAIN, 21));
            panel.add(labelErrore);
            panel.add(new JLabel(" ", JLabel.CENTER));
        }
    }

    private Color findColor(int i) {
        switch (i) {
            case 0:
                return Color.YELLOW;
            case 1:
                return Color.RED;
            case 2:
                return new Color(156, 0, 255); //VIOLA
            default:
                return new Color(0, 229, 255); //BLU-CELESTE
        }
    }

    public void aggiornaClassifica(String nomeGiocatore, int punteggio) {
        panelRisultati.removeAll();

        scriviClassifica(panelRisultati, nomeGiocatore, punteggio);

        if (primoClassificato == true) {
            view.playPrimoClassificato();
            lampeggiaJLabelRisultato();
        } else if (inClassificaMigliori == true) {
            view.playSecondoQuintoClassificato();
            lampeggiaJLabelRisultato();
        }
    }

    private void lampeggiaJLabelRisultato() {
        if (tl == null) {
            tl = new ThreadLampeggio(this, risultati[indexRisultati], risultati[indexRisultati + 1], risultati[indexRisultati + 2]);
            ExitManager.getInstance().registra(tl);
            Thread t = new Thread(tl, "Lampeggiatore");
            t.start();
        } else {
            tl.riparti(risultati[indexRisultati], risultati[indexRisultati + 1], risultati[indexRisultati + 2]);
        }
    }
}
