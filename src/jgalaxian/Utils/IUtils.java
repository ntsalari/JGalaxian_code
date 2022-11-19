package jgalaxian.Utils;

import jgalaxian.Logic.ILogic;
import jgalaxian.View.IView;


public interface IUtils {

    public static IUtils getInstance() {
        return Utils.getInstance();
    }

    public void setView(IView view);
    
    /**************** MUSICA ****************/
    public void creaManagerMusica();
    public void precaricaMusica();
    public void stopTutti();
    public void closeAll();
    
    public void playMusicaBackground();
    public void replayMusicaBackground();
    public void resetMusicaBackground(boolean isNewGame);
    public void pausaMusicaBackground();
    public void cambiaMusicaBackground();
    public void mutaBackground();
    public void togliMutoBackground();
    public float getVolumeBackground();
    
    public void playColpitoAlienoComune();
    public void playColpitoAlienoBoss();
    public void playColpitoAlienoRosso();
    public void playColpitoGiocatore();
    public void playPartenzaAlieno();
    public void playPrimoClassificato();
    public void playSecondoQuintoClassificato();
    public void terminaMusicaClassifica();
    public void playSparoGiocatore();
    public void playSetVite();
    public void playStartGame();
    public void pausaEffettiSonori();
    public void terminaPausaEffettiSonori();
    public void mutaEffettiSonori();
    public void togliMutoEffettiSonori();
    public float getVolumeEffettiSonori();
    
    /************** CONFIG FILE **************/
    
    public void creaFileConfigurazione(String Nome, String Sparo, String Sinistra, String Destra);
    public void cambiaFileConfigurazione(String path);

    public String[] getNomiConfigFileProfiliUtente();
    public String getProprietaNome();
    public String getProprietaTastoSparo();
    public String getProprietaTastoMuoviSinistra();
    public String getProprietaTastoMuoviDestra();
    public String[] getProprietaCurvaSinistra();
    public String getProprietaDiametroCurvaSinistra();
    public String[] getProprietaCambiaSkinSinistra();
    public String getProprietaNumeroPuntiFalsaCurvaSinistra();
    public String[] getProprietaCurvaDestra();
    public String getProprietaDiametroCurvaDestra();
    public String[] getProprietaCambiaSkinDestra();
    public String getProprietaNumeroPuntiFalsaCurvaDestra();
    public String[] getProprietaSecondaCurvaSinistra();
    public String[] getProprietaSecondaCurvaDestra();
    public String[] getProprietaPuntiFineAttaccoSinistra();
    public String[] getProprietaPuntiFineAttaccoDestra();
    public String[] getProprietaPuntiAttaccoLivello1();
    public String[] getProprietaPuntiAttaccoLivello2();
    public String[] getProprietaPuntiAttaccoLivello3();
    public void setProprietaTastoSparo(String tasto);
    public void setProprietaTastoMuoviSinistra(String tasto);
    public void setProprietaTastoMuoviDestra(String tasto);
    
    public void aggiornaClassifica(String nomeGiocatore, int punteggio); 
    public int getMigliorPunteggioClassifica();
    public String[][] getPrimeNRigheClassifica(int n);
}
