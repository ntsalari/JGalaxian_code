package jgalaxian.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class USound {

    public final String SNDPATH = "/Resources/Audio/";

    URL sndurl = null;
    String path;
    String nomeFile;

    USound(String path) {
        String est = path.substring(path.lastIndexOf("."));
        URL sndurl = this.getClass().getResource(SNDPATH + path);

        if ((est.equals(".wav") || est.equals(".mp3")) && sndurl != null) {
            this.sndurl = sndurl;
            this.path = SNDPATH + path;
            this.nomeFile = path;
            InputStream inp = this.getClass().getResourceAsStream(this.path);
        } else {
            Logger l = Logger.getLogger(USound.class.getName());
            l.log(Level.WARNING, "Il file: " + SNDPATH + path + " non è valido");
        }
    }

    public abstract void play();

    public abstract void pausa();

    public abstract void stop();

}
