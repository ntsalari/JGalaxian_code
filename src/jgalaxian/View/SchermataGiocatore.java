package jgalaxian.View;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SchermataGiocatore extends JPanel {

    private VFinestra parentJFrame;
    private String[] profiliUtente = null;
    private JButton[] utenti = null;
    private JLabel crea;
    private JButton creaButton;
    private JLabel scegli;
    private JScrollPane menuUtenti;
    private JPanel panelUtenti;
    private ActionListener scegliUtente;
    private Component spazioSottoJLabelCrea;
    private Component spazioSottoJButtonCrea;
    private Component spazioSottoJLabelScegli;

    private int larghezza;
    private int altezza;
    private int margineSuperiore = 30;
    private int margineSinistro = 30;
    private int margineDestro = 30;
    private int margineInferiore = 10;
    private int margineSuperioreMenuUtenti = margineSuperiore / 2;
    private int margineInferioreMenuUtenti = margineInferiore / 2;

    public SchermataGiocatore(VFinestra parentJFrame, int larghezza, int altezza) {
        super();
        this.parentJFrame = parentJFrame;
        this.larghezza = larghezza;
        this.altezza = altezza;
        Dimension d = new Dimension(larghezza, altezza);
        this.setPreferredSize(d);
        this.setSize(d);
        this.setOpaque(true);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(
                margineSuperiore,
                margineSinistro,
                margineInferiore,
                margineDestro
        ));

        crea = new JLabel("Crea nuovo utente:");
        crea.setFont(new Font("Arial", Font.PLAIN, 18));
        crea.setAlignmentX(crea.CENTER_ALIGNMENT);
        creaButton = new JButton("CREA");
        creaButton.setFont(new Font("Arial", Font.PLAIN, 16));
        creaButton.setAlignmentX(creaButton.CENTER_ALIGNMENT);
        scegli = new JLabel("Scegli un utente:");
        scegli.setFont(new Font("Arial", Font.PLAIN, 18));
        scegli.setAlignmentX(scegli.CENTER_ALIGNMENT);

        creaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreaGiocatoreActionPerformed(evt);
            }
        });

        scegliUtente = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View view = View.getInstance();
                if (!View.getInstance().avvioConcluso()) {
                    synchronized (view) {
                        try {
                            view.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SchermataGiocatore.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                JButton button = (JButton) evt.getSource();
                selezionaConfGiocatore(button.getText());
                parentJFrame.rimuoviLayerMenuUtente();
            }
        };

        spazioSottoJLabelCrea = Box.createVerticalStrut(10);
        spazioSottoJButtonCrea = Box.createVerticalStrut(50);
        spazioSottoJLabelScegli = Box.createVerticalStrut(10);

        menuUtenti = new JScrollPane();
        menuUtenti.setHorizontalScrollBarPolicy(menuUtenti.HORIZONTAL_SCROLLBAR_NEVER);
        menuUtenti.setVerticalScrollBarPolicy(menuUtenti.VERTICAL_SCROLLBAR_ALWAYS);
        menuUtenti.setPreferredSize(new Dimension(larghezza, altezza));

        panelUtenti = new JPanel();
        panelUtenti.setPreferredSize(new Dimension(
                larghezza - margineSinistro - margineDestro,
                altezza
        ));
        panelUtenti.setLayout(new GridBagLayout());

        setProfiliUtente();

        if (utenti != null) {
            for (int i = 0; i < this.profiliUtente.length; i++) {
                utenti[i].addActionListener(scegliUtente);
            }
        }
    }

    private void setProfiliUtente() {
        View.getInstance().log("Sono dentro SET PROFILI UTENTE");
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.ipadx = 100;
        c.ipady = 30;
        c.gridx = 1;
        c.insets = new Insets(margineSuperioreMenuUtenti, 0, margineInferioreMenuUtenti, 0);

        String[] utentiConf = View.getInstance().getNomiConfigFileProfiliUtente();

        View.getInstance().log("utentiConf = " + utentiConf);

        if (utentiConf != null && utentiConf.length > 0) {
            this.profiliUtente = utentiConf;
            utenti = new JButton[utentiConf.length];

            for (int i = 0; i < utentiConf.length; i++) {
                View.getInstance().log("\n\n" + utentiConf[i]);
                utenti[i] = new JButton(utentiConf[i].replace(".txt", ""));
                utenti[i].setFont(new Font("Arial", Font.PLAIN, 16));
                utenti[i].setAlignmentX(JButton.CENTER_ALIGNMENT);
                final String nomeGioatore = utenti[i].getText();

                utenti[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        View.getInstance().setStartGioco(nomeGioatore);
                    }
                });

                panelUtenti.add(utenti[i], c);
            }

            String[] hJButton = String.valueOf(utenti[0].getPreferredSize()).split("=");
            hJButton = hJButton[2].split("]");
            int altezzaJButton = Integer.valueOf(hJButton[0]);

            panelUtenti.setPreferredSize(new Dimension(
                    larghezza - margineSinistro - margineDestro,
                    utentiConf.length * (margineSuperioreMenuUtenti + altezzaJButton + c.ipady + margineInferioreMenuUtenti)
            ));

            menuUtenti.add(panelUtenti);
            menuUtenti.setViewportView(panelUtenti);

            this.add(crea);
            this.add(spazioSottoJLabelCrea);
            this.add(creaButton);
            this.add(spazioSottoJButtonCrea);
            this.add(scegli);
            this.add(spazioSottoJLabelScegli);
            this.add(menuUtenti);

        } else {
            View.getInstance().log("\n\nNON HO TROVATO DEI PROFILI UTENTE...");

            this.add(Box.createVerticalStrut(50));
            this.add(crea);
            this.add(Box.createVerticalStrut(20));
            this.add(creaButton);
        }
    }

    private void CreaGiocatoreActionPerformed(java.awt.event.ActionEvent evt) {
        creaButton.setEnabled(false);
        VCreaUtente creaUtente = new VCreaUtente(
                this.parentJFrame,
                this,
                creaButton,
                this.profiliUtente,
                this.larghezza,
                this.altezza
        );
        creaUtente.setVisible(true);
    }

    private void selezionaConfGiocatore(String nomeUtente) {
        View w = View.getInstance();
        w.cambiaFileConfigurazione("ConfigUtenti/" + nomeUtente + ".txt");
        w.registraTastiView();
    }

    public void aggiornaProfiliGiocatore() {
        panelUtenti.removeAll();

        this.remove(menuUtenti);
        this.remove(spazioSottoJLabelCrea);
        this.remove(scegli);
        this.remove(spazioSottoJButtonCrea);
        this.remove(creaButton);
        this.remove(spazioSottoJLabelCrea);
        this.remove(crea);

        panelUtenti = new JPanel();
        panelUtenti.setPreferredSize(new Dimension(
                larghezza - margineSinistro - margineDestro,
                altezza
        ));
        panelUtenti.setLayout(new GridBagLayout());

        setProfiliUtente();

        if (utenti != null) {
            for (int i = 0; i < this.profiliUtente.length; i++) {
                utenti[i].addActionListener(scegliUtente);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
