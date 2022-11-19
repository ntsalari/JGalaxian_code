package jgalaxian.View;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

public class EventiTasti extends KeyAdapter {

    private View view;
    private int tastoDx;
    private int tastoSx;
    private int tastoSpara;

    private boolean esegui = true;
    private boolean movSx = false;
    private boolean movDx = false;

    public EventiTasti(String muoviSx, String muoviDx, String spara, boolean esegui) {
        super();
        view = View.getInstance();
        tastoDx = convertiATasto(muoviDx);
        tastoSx = convertiATasto(muoviSx);
        tastoSpara = convertiATasto(spara);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (esegui) {
            int premuto = e.getKeyCode();
            if (premuto == tastoDx) {
                movDx = true;
            }
            if (premuto == tastoSx) {
                movSx = true;
            }
            if (premuto == tastoSpara) {
                if (!view.giocatoreEsploso() && !view.colpoGVisibile()) {
                    view.sparaColpoG();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int rilasciato = e.getKeyCode();
        if (rilasciato == tastoDx) {
            movDx = false;
        }
        if (rilasciato == tastoSx) {
            movSx = false;
        }
    }

    public void pausa(boolean esegui) {
        this.esegui = esegui;
    }

    public void muoviNavetta() {
        if (movSx) {
            view.spostaGiocatoreSx();
        }
        if (movDx) {
            view.spostaGiocatoreDx();
        }
    }

    public static int convertiATasto(String st) {
        int dest = KeyEvent.VK_ESCAPE;
        st = st.trim().toLowerCase();
        if (st.length() == 1) {
            char ch = st.charAt(0);
            KeyStroke ks = KeyStroke.getKeyStroke(ch, 0);
            dest = ks.getKeyCode();
            ks.getKeyCode();
            if (st.compareToIgnoreCase("a") >= 0 && st.compareToIgnoreCase("z") <= 0) {
                dest -= 32;
            }
        } else {
            switch (st) {
                case "space":
                    dest = KeyEvent.VK_SPACE;
                    break;
                case "up":
                    dest = KeyEvent.VK_UP;
                    break;
                case "down":
                    dest = KeyEvent.VK_DOWN;
                    break;
                case "left":
                    dest = KeyEvent.VK_LEFT;
                    break;
                case "right":
                    dest = KeyEvent.VK_RIGHT;
            }
        }
        return dest;
    }
}
