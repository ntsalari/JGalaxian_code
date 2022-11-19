package jgalaxian.Logic;

import jgalaxian.Logic.Costanti.*;
import java.util.logging.*;

public abstract class LSpostabile {

    TipoSp chisono;
    LSpostabile prov = null;
    Bbox b;
    boolean visibile;

    public LSpostabile(TipoSp c, int width, int heigth) {
        this.chisono = c;
		b = new Bbox();
		b.setWidth(width);
		b.setHeigth(heigth);
        this.visibile = true;
    }

    public void LSpostabile(LSpostabile prov, int x, int y) {
        b.setX(x);
        b.setY(y);
        this.prov = prov;
		this.visibile = true;
    }

    public boolean isVisibile() {
        return visibile;
    }

    public void setVisibile(boolean b) {
        visibile = b;
    }

    public int getXPos() {
        return b.getX();
    }

    public int getYPos() {
        return b.getY();
    }
    
    public int getEndXPos() {
        return b.getEndX();
    }

    public synchronized int[] getCord() {
        return b.getBounds();
    }
    
    public void setImmaginiInCache() {
        Logic.getInstance().getDimensioneImmagine(getPrimaImmagine());
        Logic.getInstance().log("Ho mandato in cache: " + getPrimaImmagine());
    }
    
    public abstract String getPrimaImmagine();
    //public abstract void setDimensioniImmagini();
    //public abstract int[][] getDimensioniImmagini();
}
