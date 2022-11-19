package jgalaxian.Logic;

import jgalaxian.Logic.Costanti.*;

public class LAlienoBoss extends LAlieno {

    private LAlienoRosso[] membriScorta;

    public LAlienoBoss(int xpos, int ypos) {
        super(TipoAlieno.BOSS, xpos, ypos);
        membriScorta = new LAlienoRosso[2];
    }

    @Override
    public String getPrimaImmagine() {
        return "/Resources/Images/g00.png";
    }
    
    @Override
    public void riceviColpo() {
        super.riceviColpo();
        
        Logic logic = Logic.getInstance();
                        
        for (int s = 0; s < membriScorta.length; s++) {
            if (membriScorta[s] != null && membriScorta[s].isVisibile()) {
                LAlienoRosso soldato = membriScorta[s];
                
                Logic.getInstance().log("MORTO BOSS CHE AVEVA SCORTA!!");
                Logic.getInstance().log("Passate info ad alieno " + soldato.t + " id " + soldato.id);
                
                int margineMorteBossX = calcolaMargine(soldato, logic);
                int margineMorteBossY = logic.getDimensioneImmagine(soldato.pathAnimazione.toString())[0];
                
                soldato.setMargineMorteBoss(margineMorteBossX, margineMorteBossY);
                
                soldato.kamikaze = this.kamikaze;
                soldato.hold2 = this.hold2;
                soldato.segmentoCorrente = this.segmentoCorrente;
                soldato.indexCoordinate = this.indexCoordinate;
                soldato.indexCambiaSkinSinistra = this.indexCambiaSkinSinistra;
                soldato.indexCambiaSkinDestra = this.indexCambiaSkinDestra;
                soldato.rettaUnisciCurve = this.rettaUnisciCurve;
                soldato.rettaUnisciCurve2 = this.rettaUnisciCurve2;
                soldato.indexCoordinate = this.indexCoordinate;
                soldato.margineX = this.margineX;
                soldato.margineY = this.margineY;
            }
        }
    }
    
    @Override
    public void playColpitoAlieno() {
        Logic.getInstance().playColpitoAlienoBoss();
    }
    
    public void setMembriScorta(LAlienoRosso r1, LAlienoRosso r2) {
        this.membriScorta[0] = r1;
        this.membriScorta[1] = r2;
    }
    
    public void setMembriScorta(int index, LAlienoRosso r) {
        this.membriScorta[index] = r;
    }
    
    public LAlienoRosso[] getMembriScorta() {
        return this.membriScorta;
    }
    
    @Override
    public void resetAlienoFineRientro() {
        Logic.getInstance().log("Ho fatto resetAlienoFineRientro PER UN ALIENO BOSS con id : " + this.id);
        super.resetAlienoFineRientro();
        setMembriScorta(null, null);
    }
    
    @Override
    public int getPunti() {
        if (this.tipoAnimazione == Costanti.SCHIERAMENTO) {
            return 60;
        } else {
            int numMembriScortaVivi = Logic.getInstance().getNumMembriScortaVivi();
            int numAnimazione = Logic.getInstance().getNumAnimazione();

            if (numMembriScortaVivi == -1) {
                Logic.getInstance().log("Punteggio BOSS: " + Costanti.PUNTI_BOSS_ATTACCO_SINGOLO);
                return Costanti.PUNTI_BOSS_ATTACCO_SINGOLO;
            } else if (numAnimazione == Costanti.BOSS_1_SCORTA && numMembriScortaVivi == 0) {
                Logic.getInstance().log("Punteggio BOSS: " + Costanti.PUNTI_BOSS_ATTACCO_1_SCORTA_DISTRUTTA);
                return Costanti.PUNTI_BOSS_ATTACCO_1_SCORTA_DISTRUTTA;
            } else if (numAnimazione == Costanti.BOSS_1_SCORTA && numMembriScortaVivi == 1) {
                Logic.getInstance().log("Punteggio BOSS: " + Costanti.PUNTI_BOSS_ATTACCO_1_SCORTA_VIVA);
                return Costanti.PUNTI_BOSS_ATTACCO_1_SCORTA_VIVA;
            } else if (numAnimazione == Costanti.BOSS_2_SCORTA && numMembriScortaVivi == 0) {
                Logic.getInstance().log("Punteggio BOSS: " + Costanti.PUNTI_BOSS_ATTACCO_2_SCORTE_DISTRUTTE);
                return Costanti.PUNTI_BOSS_ATTACCO_2_SCORTE_DISTRUTTE;
            } else { //numAnimazione == Costanti.BOSS_2_SCORTA && numMembriScortaVivi != 0
                Logic.getInstance().log("Punteggio BOSS: " + Costanti.PUNTI_BOSS_ATTACCO_2_SCORTE_VIVE);
                return Costanti.PUNTI_BOSS_ATTACCO_2_SCORTE_VIVE;
            }
        }
    }
}
