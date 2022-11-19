package jgalaxian.Logic;

public class LNavGiocatore extends LSpostabile {
    
    private boolean morto = false;
    
    public LNavGiocatore() {
        super(Costanti.TipoSp.GIOCATORE, 26, 40);
        super.LSpostabile(null, Costanti.LARGHEZZA / 2, Costanti.STARTGIOCATORE);
    }

    public void resetVita() {
        super.LSpostabile(null, Costanti.LARGHEZZA / 2, Costanti.STARTGIOCATORE);
        super.visibile = true;
    }

    public void spostaDx() {
        int tentativo = super.b.getEndX() + Costanti.MOV_NAV;
        if (tentativo <= Costanti.LARGHEZZA) {
            super.b.setX(super.b.getX() + Costanti.MOV_NAV);
        }
    }

    public void spostaSx() {
        int tentativo = super.b.getX() - Costanti.MOV_NAV;
        if (tentativo >= 0) {
            super.b.setX(tentativo);
        }
    }

    public LColpoGiocatore sparaColpo() {  
        Logic logic = Logic.getInstance();
        LColpoGiocatore g = logic.getColpo();
        g.LColpo(this, this.b.getX() + 12, Costanti.STARTGIOCATORE - 1);
        logic.playSparoGiocatore();
        return g;
    }

    @Override
    public String getPrimaImmagine() {
        return "/Resources/Images/player0.png";
    }

    public void riceviColpo() {
        Logic logic = Logic.getInstance();
        this.visibile = false;
        morto = true;
        //Decrementare le vite
        Logic.getInstance().log("GIOCATORE COLPITO");
        logic.esplodiGiocatore(super.getXPos(), super.getYPos());
        logic.setTempoMorteGiocatore(System.currentTimeMillis());
        logic.playColpitoGiocatore();
    }
    
    public void setGiocatoreMorto(boolean morto) {
        this.morto = morto;
    }
    
    public boolean getGiocatoreMorto() {
        return this.morto;
    }

}
