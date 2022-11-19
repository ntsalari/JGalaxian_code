package jgalaxian.View;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VCreaUtente extends JDialog {

    private View w;
    private VFinestra parentJFrame;
    private JPanel parent;
    private JPanel contenuto;
    private JButton btnAnnulla;
    private JButton btnOk;
    private JTextField cmdDx;
    private JTextField cmdFir;
    private JTextField cmdSx;
    private JTextField cmdNomeUtente;
    private JLabel jLabelDx;
    private JLabel jLabelFir;
    private JLabel jLabelSx;
    private JLabel jLabelnomeUtente;
    private JButton oldCreaButton;
    private String[] profiliUtente;
    private final int HGAP = 30;
    private final int VGAP = 10;

    public VCreaUtente(VFinestra parentJFrame, JPanel parent, JButton creaButton, String[] profiliUtente, int larghezza, int altezza) {

        this.w = View.getInstance();
        this.parentJFrame = parentJFrame;
        this.parent = parent;
        this.oldCreaButton = creaButton;
        this.profiliUtente = profiliUtente;

        contenuto = new JPanel();
        btnOk = new JButton("Ok");
        btnAnnulla = new JButton("Annulla");
        jLabelDx = new JLabel("Vai a destra:");
        jLabelSx = new JLabel("Vai a sinistra:");
        jLabelFir = new JLabel("Spara:");
        jLabelnomeUtente = new JLabel("Nome utente:");
        cmdDx = new JTextField();
        cmdSx = new JTextField();
        cmdFir = new JTextField();
        cmdNomeUtente = new JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                oldCreaButton.setEnabled(true);
            }
        });

        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnAnnulla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnullaActionPerformed(evt);
            }
        });

        GridLayout grid = new GridLayout(0, 2);
        grid.setHgap(HGAP);
        grid.setVgap(VGAP);
        contenuto.setLayout(grid);
        contenuto.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contenuto.add(jLabelnomeUtente);
        contenuto.add(cmdNomeUtente);
        contenuto.add(jLabelDx);
        contenuto.add(cmdDx);
        contenuto.add(jLabelSx);
        contenuto.add(cmdSx);
        contenuto.add(jLabelFir);
        contenuto.add(cmdFir);
        contenuto.add(btnOk);
        contenuto.add(btnAnnulla);

        this.add(contenuto);

        this.pack();
        this.setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {
        String nome = cmdNomeUtente.getText();
        String dx = cmdDx.getText();
        String sx = cmdSx.getText();
        String spara = cmdFir.getText();

        boolean ok = true;

        View.getInstance().log("\n\nprofili Utente in JDialog: " + profiliUtente);

        if (nome.length() == 0) {
            ok = false;
            JOptionPane.showMessageDialog(null, "Il nome inserito è troppo corto.\nInserisci almeno un carattere.", "Errore di inserimento", JOptionPane.ERROR_MESSAGE);
            cmdNomeUtente.setText("");
        }


        if (nome.length() > 12) {
            ok = false;
            JOptionPane.showMessageDialog(null, "Il nome inserito è troppo lungo.\nLunghezza max accettata: 12 caratteri.", "Errore di inserimento", JOptionPane.ERROR_MESSAGE);
            cmdNomeUtente.setText("");
        }

        if (nome.contains(";") || nome.contains("Resources")) {
            ok = false;
            JOptionPane.showMessageDialog(null, "Il nome inserito non è accettabile.\nIl nome non può contenere:\n- ';'\n- 'Resources'", "Errore di inserimento", JOptionPane.ERROR_MESSAGE);
            cmdNomeUtente.setText("");
        }

        if (profiliUtente != null && profiliUtente.length > 0 && ok == true) {
            for (int i = 0; i < profiliUtente.length; i++) {

                if (nome.equals(profiliUtente[i].replace(".txt", ""))) {
                    ok = false;
                    JOptionPane.showMessageDialog(null, "Il nome inserito è già in utilizzo.\nScegline uno diverso.", "Errore di inserimento", JOptionPane.ERROR_MESSAGE);
                    cmdNomeUtente.setText("");
                }
            }
        }

        if (ok == true) {
            String t[] = new String[3];
            t[0] = sx;
            t[1] = dx;
            t[2] = spara;
            if (this.w.controllaTastiUtils(t)) {
                View.getInstance().log("\n\nSto per creare file conf tasti in Dialog Crea Utente");
                this.w.creaFileConfigurazione(nome, spara, sx, dx);
                View.getInstance().log("\n\nCreato file conf in Dialog Crea Utente");
                this.w.cambiaFileConfigurazione("ConfigUtenti/" + nome + ".txt");
                View.getInstance().log("\n\nSettato path to file conf in Dialog Crea Utente");
                this.w.registraTastiView();

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

                parentJFrame.rimuoviLayerMenuUtente();
                this.dispose();

                View.getInstance().setStartGioco(nome);
            } else {
                JOptionPane.showMessageDialog(null, "La configurazione di tasti inserita non è valida.\n\nNon puoi:\n- usare più di un carattere per tasto\n- usare uno stesso carattere per più tasti\n- usare caratteri non alfanumerici o diversi da:\n  'left', 'right', 'up', 'down', 'space'", "Errore di inserimento", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btnAnnullaActionPerformed(java.awt.event.ActionEvent evt) {
        this.oldCreaButton.setEnabled(true);
        this.dispose();
    }

}
