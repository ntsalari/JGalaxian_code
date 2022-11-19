package jgalaxian.View.Demo;

public class DAlienoRosso extends DAlieno {

    private static String[] stati = new String[]{"/Resources/Images/bg30.png",
        "/Resources/Images/bg31.png",
        "/Resources/Images/bg30.png",
        "/Resources/Images/bg32.png"};

    public DAlienoRosso(int i, int j) {
        super(i, j);
        super.setStato(stati[statoAttuale / 16]);
    }

    @Override
    public void aggiornaStato() {
        super.aggiornaStato();
        super.setStato(stati[statoAttuale / 16]);
    }
}
