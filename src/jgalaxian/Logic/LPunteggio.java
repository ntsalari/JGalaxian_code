package jgalaxian.Logic;

public class LPunteggio {

    private int punteggio;
    private LContVite vite;

    public LPunteggio(LContVite vite) {
        this.vite = vite;
    }

    public void incrementaPunteggio(int incremento) {
        punteggio += incremento;
        if (punteggio >= vite.proxOneUp()) {
            vite.oneUp();
        }
    }

    public void resetPunteggio() {
        punteggio = 0;
    }

    public int getPunteggio() {
        return this.punteggio;
    }
}
