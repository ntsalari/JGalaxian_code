
package jgalaxian.View;

import java.io.Closeable;
import jgalaxian.Logic.ILogic;
import jgalaxian.Utils.IUtils;

public interface IView {
    
    public void setLogic(ILogic logic);
    public void setUtils(IUtils utils);
        
    public void mostraGUI();
    
    //return[0] = Height ; return[1] = Width
    public int[] getDimensioneImmagine(String path);
    public void setImmaginiEsplosioniInCache();
    public void inviaTutteImmaginiInCache();
    
    public int getLivelloAttuale();
    public int getViteAttuali();
    public void aggiornaPunteggioPartita(int punteggio);
	
    public void esplodiGiocatore(int x, int y);
    public void esplodiAlieno(int x, int y);

    public void registraAtExit(Closeable c);
    public void startLogger();
    public void log(String s);
	
    public void cambioLivello();
    public void fineAvvio();

    public void stopTastiView();
    public void gameOver();
    public void exitGamePerErroreSerio(Object message, String title);

}
