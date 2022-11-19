package jgalaxian.Logic;

import jgalaxian.Logic.Costanti.*;

public class LAlienoViola extends LAlieno {

    public LAlienoViola(int xpos, int ypos) {
        super(TipoAlieno.VIOLA, xpos, ypos);
    }

    @Override
    public String getPrimaImmagine() {
        return "/Resources/Images/bg50.png";
    }

    @Override
    public int getPunti() {
        if (this.tipoAnimazione == Costanti.SCHIERAMENTO) {
            return 40;
        } else {
            return 80;
        }
    }

}
