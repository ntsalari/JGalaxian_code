package jgalaxian.Logic;

public class LContVite {

    private int prox;
    private int oneup;
    private int numVite;

    public LContVite() {

    }

    public void oneUp() {
        if (oneup < Costanti.NUM_MAX_ONEUP) {
            oneup++;
            numVite++;
            prox += Costanti.ONEUP;
            Logic.getInstance().playSetVite();
        }
    }

    public int proxOneUp() {
        return prox;
    }

    public String getImmagine() {
        return "/Resources/Images/extralife0.png";
    }

    public void setImmaginiInCache() {
        Logic.getInstance().getDimensioneImmagine(getImmagine());
        Logic.getInstance().log("Ho mandato in cache: " + getImmagine());
    }

    public int getVite() {
        return this.numVite;
    }

    public void resetVite() {
        oneup = 0;
        numVite = 3;
        prox = Costanti.ONEUP;
        Logic.getInstance().setGiocatoreMorto(false);
    }

    public void decrementaVite() {
        numVite--;
        if (numVite == 0) {
            Logic.getInstance().gameOver();
        }
    }

}
