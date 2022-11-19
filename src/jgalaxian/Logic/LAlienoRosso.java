package jgalaxian.Logic;

import jgalaxian.Logic.Costanti.*;

public class LAlienoRosso extends LAlieno {

    private int[] scorta;
    private int margineMorteBossX = 0;
    private int margineMorteBossY = 0;
    
    private boolean eseguitoRossoMorteBoss = false;
    
    public LAlienoRosso(int xpos, int ypos) {
        super(TipoAlieno.ROSSO, xpos, ypos);
        scorta = new int[]{0,-1};
    }

    @Override
    public String getPrimaImmagine() {
        return "/Resources/Images/bg30.png";
    }
    
    /**
     * 
     * @param isScorta <br>
     * &nbsp;&nbsp;&nbsp;&nbsp; 0 -> Non è una scorta <br>
     * &nbsp;&nbsp;&nbsp;&nbsp; 1 -> E' una scorta
     * 
     * @param idBoss id del boss di cui è scorta
     */
    public void setScorta(int isScorta, int idBoss) {
        this.scorta[0] = isScorta;
        
        if (isScorta == 0)
            this.scorta[1] = -1;
        else
            this.scorta[1] = idBoss;
    }
    
    public int[] getScorta() {
        return this.scorta;
    }
    
    public void setMargineMorteBoss(int x, int y) {
        Logic.getInstance().log("Ho settato margine(" + x + "," + y + ") per id : " + this.id);
        margineMorteBossX = x;
        margineMorteBossY = y;
    }
    
    public int[] getMargineMorteBoss() {
        return new int[]{margineMorteBossX, margineMorteBossY};
    }
    
    public void setEeguitoRossoMorteBoss(boolean eseguito) {
        eseguitoRossoMorteBoss = eseguito;
    }
    
    public boolean getEeguitoRossoMorteBoss() {
        return eseguitoRossoMorteBoss;
    }
    
    @Override
    public void playColpitoAlieno() {
        Logic.getInstance().playColpitoAlienoRosso();
    }

    @Override
    public void resetAlienoFineAttacco(boolean attaccoDiretto) {
        Logic.getInstance().log("Ho fatto resetAlienoFineAttacco PER UN ALIENO ROSSO con id : " + this.id);
        super.resetAlienoFineAttacco(attaccoDiretto);
        setScorta(0, -1);
        setMargineMorteBoss(0,0);
        setEeguitoRossoMorteBoss(false);
    }

    
    @Override
    public void resetAlienoFineRientro() {
        Logic.getInstance().log("Ho fatto resetAlienoFineRientro PER UN ALIENO ROSSO con id : " + this.id);
        super.resetAlienoFineRientro();
        setScorta(0, -1);
    }
    
    @Override
    public int getPunti() {
        if (this.tipoAnimazione == Costanti.SCHIERAMENTO) {
            return 50;
        } else {
            return 100;
        }
    }
}
