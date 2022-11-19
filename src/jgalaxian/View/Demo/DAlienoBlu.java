package jgalaxian.View.Demo;

public class DAlienoBlu extends DAlieno {

    private static String[] stati = new String[]{"/Resources/Images/bg10.png",
        "/Resources/Images/bg11.png",
        "/Resources/Images/bg10.png",
        "/Resources/Images/bg12.png"};

    public DAlienoBlu(int i, int j) {
        super(i, j);
        super.setStato(stati[statoAttuale / 16]);
    }

    @Override
    public void aggiornaStato() {
        super.aggiornaStato();
        super.setStato(stati[statoAttuale / 16]);
    }
}
