package jgalaxian.View;

import java.io.Closeable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ThreadLampeggio implements Runnable, Closeable {

    private JPanel parent;
    private JLabel posizione;
    private JLabel nome;
    private JLabel punteggio;
    private boolean stopLampeggio;
    private boolean esegui;

    public ThreadLampeggio(JPanel parent, JLabel posizione, JLabel nome, JLabel punteggio) {
        this.parent = parent;
        this.posizione = posizione;
        this.nome = nome;
        this.punteggio = punteggio;
        stopLampeggio = false;
        esegui = true;
    }

    @Override
    public void run() {
        while (esegui) {

            View.getInstance().log("Visibile PRIMA: " + posizione.isVisible());

            dormi(1500);

            posizione.setVisible(false);
            nome.setVisible(false);
            punteggio.setVisible(false);

            View.getInstance().log("Visibile DOPO: " + posizione.isVisible());

            dormi(500);

            posizione.setVisible(true);
            nome.setVisible(true);
            punteggio.setVisible(true);

            if (stopLampeggio) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadLampeggio.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }

    private void dormi(long tempo) {
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadLampeggio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void riparti(JLabel posizione, JLabel nome, JLabel punteggio) {
        stopLampeggio = false;

        this.posizione = posizione;
        this.nome = nome;
        this.punteggio = punteggio;

        synchronized (this) {
            this.notify();
        }
    }

    public void fermati() {
        synchronized (this) {
            stopLampeggio = true;
        }
    }

    @Override
    public void close() {
        stopLampeggio = false;
        esegui = false;
        synchronized (this) {
            this.notify();
        }
    }
}
