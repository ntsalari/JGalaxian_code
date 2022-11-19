package jgalaxian.View;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExitManager extends WindowAdapter {

    LinkedList<Closeable> chiudi;
    public static ExitManager singleton = null;
    private boolean b = true;
    Closeable logger;

    private ExitManager() {
        chiudi = new LinkedList<>();
    }

    public static ExitManager getInstance() {
        if (singleton == null) {
            singleton = new ExitManager();
        }
        return singleton;
    }

    public void registra(Closeable c) {
        chiudi.add(c);
    }

    public void registraLogger(Closeable c) {
        logger = c;
    }

    public void terminaTutti() {
        for (Closeable c : chiudi) {
            try {
                c.close();
            } catch (IOException ex) {
                Logger.getLogger(ExitManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void azioneChiusura() {
        if (b) {
            terminaTutti();
        }
        b = false;
        if (logger != null) {
            try {
                logger.close();
            } catch (IOException ex) {
                Logger.getLogger(ExitManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        VFinestra.getInstance().setVisible(false);
        azioneChiusura();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        azioneChiusura();
    }
}
