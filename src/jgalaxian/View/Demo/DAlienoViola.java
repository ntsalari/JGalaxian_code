package jgalaxian.View.Demo;

public class DAlienoViola extends DAlieno {

    private static String[] stati = new String[]{"/Resources/Images/bg50.png",
        "/Resources/Images/bg51.png",
        "/Resources/Images/bg50.png",
        "/Resources/Images/bg52.png"};

    public DAlienoViola(int i, int j) {
        super(i, j);
        super.setStato(stati[statoAttuale / 16]);
    }

    @Override
    public void aggiornaStato() {
        super.aggiornaStato();
        super.setStato(stati[statoAttuale / 16]);
    }
}
