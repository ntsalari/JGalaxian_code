package jgalaxian.Logic;

import jgalaxian.Logic.Costanti.*;

public class LAlienoBlu extends LAlieno {

    private int startAnimX = -1;
    private int startAnimY = -1;

    public LAlienoBlu(int xpos, int ypos) {
        super(TipoAlieno.BLU, xpos, ypos);
    }

    @Override
    public String getPrimaImmagine() {
        return "/Resources/Images/bg10.png";
    }

    @Override
    public int getPunti() {
        if (this.tipoAnimazione == Costanti.SCHIERAMENTO) {
            return Costanti.PUNTI_BLU_SCHIERAMENTO;
        } else {
            return Costanti.PUNTI_BLU_ATTACCO;
        }
    }

}
