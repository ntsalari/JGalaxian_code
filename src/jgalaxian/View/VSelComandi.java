package jgalaxian.View;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class VSelComandi extends JDialog {

    private javax.swing.JPanel contenuto;
    private javax.swing.JButton btnAnnulla;
    private javax.swing.JButton btnOk;
    private javax.swing.JTextField cmdDx;
    private javax.swing.JTextField cmdFir;
    private javax.swing.JTextField cmdSx;
    private javax.swing.JLabel jLabelDx;
    private javax.swing.JLabel jLabelFir;
    private javax.swing.JLabel jLabelSx;
    private final int HGAP = 30;
    private final int VGAP = 10;

    public VSelComandi(JFrame parent) {
        super(parent, "Comandi gioco", true);

        contenuto = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnAnnulla = new javax.swing.JButton();
        jLabelDx = new javax.swing.JLabel();
        jLabelSx = new javax.swing.JLabel();
        jLabelFir = new javax.swing.JLabel();
        cmdDx = new javax.swing.JTextField();
        cmdSx = new javax.swing.JTextField();
        cmdFir = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnAnnulla.setText("Annulla");
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

        jLabelDx.setText("Vai a destra:");
        jLabelSx.setText("Vai a sinistra:");
        jLabelFir.setText("Spara:");

        View w = View.getInstance();

        cmdDx.setText(w.getDx());
        cmdSx.setText(w.getSx());
        cmdFir.setText(w.getSpara());

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
        String dx = cmdDx.getText();
        String sx = cmdSx.getText();
        String spara = cmdFir.getText();
        View w = View.getInstance();
        String t[] = new String[3];
        t[0] = sx;
        t[1] = dx;
        t[2] = spara;
        if (w.registraTastiUtils(t)) {
            w.registraTastiView();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "La configurazione inserita non è valida", "Errore di inserimento", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnAnnullaActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }
}
