package jgalaxian.View;

import java.awt.Dimension;
import java.awt.Graphics;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class VLogo extends JPanel {

    private ImageIcon icon;
    private URL imgURL;
    private int larghezzaPannello; //In realtà passo la metà della lunghezza
    private int altezzaFoto;
    private int larghezzaFoto;
    private int altezzaMargin;

    public VLogo(int larghezza, int altezzaMargin, String path) {
        this.larghezzaPannello = larghezza;
        this.altezzaMargin = altezzaMargin;
        loadImage(path);
        initPanel();
    }

    private void loadImage(String path) {
        imgURL = getClass().getResource(path);

        if (imgURL != null) {
            icon = new ImageIcon(imgURL);
            this.larghezzaFoto = icon.getIconWidth();
            this.altezzaFoto = icon.getIconHeight();
        }
    }

    private void initPanel() {
        setPreferredSize(new Dimension(this.larghezzaFoto, this.altezzaFoto + altezzaMargin));
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Larghezza pannello / 2 = this.x
        //Metto l'immagine centrata al pannello in base alla larghezza dell'immagine stessa
        icon.paintIcon(this, g, this.larghezzaPannello - (this.larghezzaFoto / 2), altezzaMargin / 2);

    }
}
