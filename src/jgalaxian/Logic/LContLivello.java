package jgalaxian.Logic;

public class LContLivello {

    private int livelloAttuale;

    public LContLivello() {

    }

    public String getImmagine() {
        return "/Resources/Images/levelflag0.png";
    }

    public void setImmaginiInCache() {
        Logic.getInstance().getDimensioneImmagine(getImmagine());
        Logic.getInstance().log("Ho mandato in cache: " + getImmagine());
    }

    public int getLivello() {
        return this.livelloAttuale;
    }

    public void resetLivello() {
        this.livelloAttuale = 1;
    }

    public void incrementaLivello() {
        this.livelloAttuale++;
        Logic.getInstance().clearColpi();
        Logic.getInstance().cambioLivello();
    }
}
