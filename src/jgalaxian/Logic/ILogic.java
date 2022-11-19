package jgalaxian.Logic;

import jgalaxian.View.IView;
import jgalaxian.Utils.IUtils;

public interface ILogic {

    public static ILogic getInstance() {
        return Logic.getInstance();
    }

    public void setView(IView view);
    public void setUtils(IUtils utils);
    
    /********** MUSICA *********/
    public void setMusica(boolean scelta);
    public void setMusicaBackground(boolean scelta);
    public boolean getMusica();
    public boolean getMusicaBackground();

    /********* PARTITA *********/
    public int getPunteggioPartita();
    public int getLivelloAttuale();
    public int getViteAttuali();
    public void clearBattaglione();
    public boolean giocoIniziato();
    public void setStartGioco(String nomeGiocatore);
    public void terminaGioco();

    /********** ANIMAZIONI *********/
    public void spostaGiocatoreDx();
    public void spostaGiocatoreSx();
    public void sparaColpoG();
    public int[] getCoordColpoG();
    public boolean colpoGVisibile();
    public void avanzaAnimazioni();
    public int[] getCoordAlieno(int i, int j);
    public boolean alienoEsploso(int i, int j);
    public boolean alienoInFormazione(int i, int j);
    public int[] getCoordColpoAlieno(int i);
    public boolean colpoAlienoVisibile(int i);
    public String getNuovaSkin(int i, int j);

    /********* GIOCATORE *********/
    public String getNomeGiocatore(); 
    public int getPosizioneGiocatore();
    public boolean giocatoreEsploso();
    public boolean getGiocatoreMorto();
	public void resetGiocatore();

    /********* IMMAGINI *********/
    public void inviaTutteImmaginiInCache();
    public String getPrimaImmagine(String oggetto);

    /********* COSTANTI *********/
    public int getCostanteALTEZZA();
    public int getCostanteLARGHEZZA();
    public int getStartYGiocatore();
    public int getColpiAlieni();
    public int getRowSpacing();
    public int getColSpacing();

}
