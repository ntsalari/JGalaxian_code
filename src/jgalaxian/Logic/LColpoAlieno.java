package jgalaxian.Logic;

import jgalaxian.Logic.Costanti.*;

public class LColpoAlieno extends LSpostabile {

    private final int yvel = Costanti.VEL_COLPO;

    public LColpoAlieno() {
        super(TipoSp.COLPOALIENO, 2, 8);
        super.visibile = false;
    }

    public void LColpo(LSpostabile provenienza, int xpos, int ypos) {
        super.LSpostabile(provenienza, xpos+14, ypos+14);
        super.visibile = true;
    }

    @Override
    public String getPrimaImmagine() {
        return "/Resources/Images/emissile0.png";
    }

    public void muoviti() {
        if (visibile) {
            int tmp = super.b.getY() + yvel;
            if (tmp > Costanti.ALTEZZA) {
                super.visibile = false;
            } else {
                super.b.setY(tmp);
            }
        }
    }

    public void checkTarget() {
        LNavGiocatore g = Logic.getInstance().getNaveGiocatore();
        
        if (!g.isVisibile())
            return;
        
        if (g.b.inBound(this.b)) {
            g.riceviColpo();
            visibile = false;
        }
    }
}
