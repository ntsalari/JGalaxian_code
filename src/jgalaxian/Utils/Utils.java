package jgalaxian.Utils;

import jgalaxian.Logic.ILogic;
import jgalaxian.View.IView;

import java.io.Closeable;

public class Utils implements IUtils {

    private IView view;
    private Config config = null;
    private MusicManager manager = null;

    private static Utils sinUtils = null;

    private Utils() {
        this.config = Config.getInstance();
    }

    public void log(String s) {
        view.log(s);
    }

    public static Utils getInstance() {
        if (sinUtils == null) {
            sinUtils = new Utils();
        }
        return sinUtils;
    }

    @Override
    public void setView(IView view) {
        this.view = view;
    }

    @Override
    public void creaManagerMusica() {
        this.manager = MusicManager.getInstance();
    }

    @Override
    public void playMusicaBackground() {
        this.manager.playMusicaBackground();
    }

    @Override
    public void replayMusicaBackground() {
        this.manager.replayMusicaBackground();
    }

    @Override
    public void resetMusicaBackground(boolean isNewGame) {
        this.manager.resetMusicaBackground(isNewGame);
    }

    @Override
    public void pausaMusicaBackground() {
        this.manager.pausaMusicaBackground();
    }

    @Override
    public void cambiaMusicaBackground() {
        this.manager.cambiaMusicaBackground();
    }

    @Override
    public void mutaBackground() {
        this.manager.mutaBackground();
    }

    @Override
    public void togliMutoBackground() {
        this.manager.togliMutoBackground();
    }

    @Override
    public float getVolumeBackground() {
        return this.manager.getVolumeBackground();
    }

    @Override
    public float getVolumeEffettiSonori() {
        return this.manager.getVolumeEffettiSonori();
    }

    @Override
    public void playColpitoAlienoComune() {
        this.manager.playColpitoAlienoComune();
    }

    @Override
    public void playColpitoAlienoBoss() {
        this.manager.playColpitoAlienoBoss();
    }

    @Override
    public void playColpitoAlienoRosso() {
        this.manager.playColpitoAlienoRosso();
    }

    @Override
    public void playColpitoGiocatore() {
        this.manager.playColpitoGiocatore();
    }

    @Override
    public void playPartenzaAlieno() {
        this.manager.playPartenzaAlieno();
    }

    @Override
    public void playPrimoClassificato() {
        this.manager.playPrimoClassificato();
    }

    @Override
    public void playSecondoQuintoClassificato() {
        this.manager.playSecondoQuintoClassificato();
    }

    @Override
    public void terminaMusicaClassifica() {
        this.manager.terminaMusicaClassifica();
    }

    @Override
    public void playSparoGiocatore() {
        this.manager.playSparoGiocatore();
    }

    @Override
    public void playSetVite() {
        this.manager.playSetVite();
    }

    @Override
    public void playStartGame() {
        this.manager.playStartGame();
    }

    @Override
    public void pausaEffettiSonori() {
        this.manager.pausaEffettiSonori();
    }

    @Override
    public void terminaPausaEffettiSonori() {
        this.manager.terminaPausaEffettiSonori();
    }

    @Override
    public void mutaEffettiSonori() {
        this.manager.mutaEffettiSonori();
    }

    @Override
    public void togliMutoEffettiSonori() {
        this.manager.togliMutoEffettiSonori();
    }

    @Override
    public void precaricaMusica() {
        manager.preparaMusica();
    }

    @Override
    public void closeAll() {
        this.manager.close();
    }

    @Override
    public void creaFileConfigurazione(String Nome, String Sparo, String Sinistra, String Destra) {
        this.config.creaConfigFile(Nome, Sparo, Sinistra, Destra);
    }

    @Override
    public void cambiaFileConfigurazione(String path) {
        this.config.cambiaConfigFile(path);
    }

    @Override
    public String getProprietaNome() {
        return this.config.getNome();
    }

    @Override
    public String getProprietaTastoSparo() {
        return this.config.getTastoSparo();
    }

    @Override
    public String getProprietaTastoMuoviSinistra() {
        return this.config.getTastoMuoviSinistra();
    }

    @Override
    public String getProprietaTastoMuoviDestra() {
        return this.config.getTastoMuoviDestra();
    }

    @Override
    public String[] getProprietaCurvaSinistra() {
        return this.config.getCurvaSinistra();
    }

    @Override
    public String getProprietaDiametroCurvaSinistra() {
        return this.config.getDiametroCurvaSinistra();
    }

    @Override
    public String[] getProprietaCambiaSkinSinistra() {
        return this.config.getCambiaSkinSinistra();
    }

    @Override
    public String getProprietaNumeroPuntiFalsaCurvaSinistra() {
        return this.config.getNumeroPuntiFalsaCurvaSinistra();
    }

    @Override
    public String[] getProprietaCurvaDestra() {
        return this.config.getCurvaDestra();
    }

    @Override
    public String getProprietaDiametroCurvaDestra() {
        return this.config.getDiametroCurvaDestra();
    }

    @Override
    public String[] getProprietaCambiaSkinDestra() {
        return this.config.getCambiaSkinDestra();
    }

    @Override
    public String getProprietaNumeroPuntiFalsaCurvaDestra() {
        return this.config.getNumeroPuntiFalsaCurvaDestra();
    }

    @Override
    public String[] getProprietaSecondaCurvaSinistra() {
        return this.config.getSecondaCurvaSinistra();
    }

    @Override
    public String[] getProprietaSecondaCurvaDestra() {
        return this.config.getSecondaCurvaDestra();
    }

    @Override
    public String[] getProprietaPuntiFineAttaccoSinistra() {
        return this.config.getPuntiFineAttaccoSinistra();
    }

    @Override
    public String[] getProprietaPuntiFineAttaccoDestra() {
        return this.config.getPuntiFineAttaccoDestra();
    }

    @Override
    public String[] getProprietaPuntiAttaccoLivello1() {
        return this.config.getPuntiAttaccoLivello1();
    }

    @Override
    public String[] getProprietaPuntiAttaccoLivello2() {
        return this.config.getPuntiAttaccoLivello2();
    }

    @Override
    public String[] getProprietaPuntiAttaccoLivello3() {
        return this.config.getPuntiAttaccoLivello3();
    }

    @Override
    public void setProprietaTastoSparo(String tasto) {
        this.config.setTastoSparo(tasto);
    }

    @Override
    public void setProprietaTastoMuoviSinistra(String tasto) {
        this.config.setTastoMuoviSinistra(tasto);
    }

    @Override
    public void setProprietaTastoMuoviDestra(String tasto) {
        this.config.setTastoMuoviDestra(tasto);
    }

    @Override
    public void aggiornaClassifica(String nomeGiocatore, int punteggio) {
        Classifica.aggiornaClassifica(nomeGiocatore, punteggio);
    }

    @Override
    public int getMigliorPunteggioClassifica() {
        return Classifica.getPunteggioMigliore();
    }

    @Override
    public String[][] getPrimeNRigheClassifica(int n) {
        return Classifica.getPrimeNRigheClassifica(n);
    }

    @Override
    public String[] getNomiConfigFileProfiliUtente() {
        return config.getNomiConfigFileProfiliUtente();
    }

    public void exitGamePerErroreSerio(Object message, String title) {
        this.view.exitGamePerErroreSerio(message, title);
    }

    public void registraAtExit(Closeable c) {
        view.registraAtExit(c);
    }

    @Override
    public void stopTutti() {
        MusicManager.getInstance().stopTutti();
    }
}
