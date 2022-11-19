package jgalaxian.Logic;

public class LColpoGiocatore extends LSpostabile {

    private final int yvel = -Costanti.VEL_COLPO;

    public LColpoGiocatore() {
        super(Costanti.TipoSp.COLPOGIOCATORE, 2, 8);
        super.visibile = false;
    }
    
    @Override
    public String getPrimaImmagine() {
        return "/Resources/Images/pmissile0.png";
    }

    public void checkTarget() {
        LAlieno[][] alieni = Logic.getInstance().getNavette();
        for (int i = 0; i < alieni.length; i++) {
            for (int j = 0; j < alieni[i].length; j++) {
                LAlieno a = alieni[i][j];
                if (a != null && a.isVisibile()) {
                    if (b.inBound(a.b)) {
                        a.riceviColpo();
                        a.playColpitoAlieno();
                        visibile = false;
                    }
                }
            }
        }
    }

    public void LColpo(LSpostabile provenienza, int xpos, int ypos) {
        super.LSpostabile(provenienza, xpos, ypos);
        super.visibile = true;
    }

    public void muoviti() {
        if (visibile) {
            int tmp = super.b.getY() + yvel;
            if (tmp < 0) {
                super.visibile = false;
            } else {
                super.b.setY(tmp);
            }
        }
    }
}
