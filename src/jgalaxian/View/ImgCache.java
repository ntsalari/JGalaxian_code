package jgalaxian.View;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ImgCache {

    private HashMap<String, BufferedImage> immagini;
    private static ImgCache sinCache = null;

    private ImgCache() {
        immagini = new HashMap<>(300);
    }

    public static ImgCache getInstance() {
        if (sinCache == null) {
            sinCache = new ImgCache();
        }
        return sinCache;
    }

    public BufferedImage addImage(String path) {
        BufferedImage b = null;
        InputStream i = this.getClass().getResourceAsStream(path);
        try {
            b = ImageIO.read(i);
            synchronized (this) {
                immagini.put(path, b);
            }
        } catch (IOException ex) {
            Logger.getLogger(ImgCache.class.getName()).log(Level.SEVERE, "Errore di I/O: " + ex.getMessage(), ex);
        }
        return b;
    }

    public BufferedImage getImage(String path) {
        BufferedImage b = null;
        if (immagini.containsKey(path)) {
            b = immagini.get(path);
        } else {
            b = addImage(path);
        }
        return b;
    }
}
