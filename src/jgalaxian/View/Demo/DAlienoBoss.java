package jgalaxian.View.Demo;

public class DAlienoBoss extends DAlieno {

    private static String[] stati = new String[]{"/Resources/Images/g00.png",
        "/Resources/Images/g01.png",
        "/Resources/Images/g00.png",
        "/Resources/Images/g02.png"};

    public DAlienoBoss(int i, int j) {
        super(i, j);
        super.setStato(stati[statoAttuale / 16]);
    }

    @Override
    public void aggiornaStato() {
        super.aggiornaStato();
        super.setStato(stati[statoAttuale / 16]);
    }
}
