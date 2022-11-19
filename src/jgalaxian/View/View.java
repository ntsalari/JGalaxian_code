package jgalaxian.View;

import jgalaxian.Logic.ILogic;
import jgalaxian.Utils.IUtils;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import javax.swing.JOptionPane;
import java.util.logging.*;

public class View implements IView {

    private ILogic logic;
    private IUtils utils;
    private VFinestra finestra;

    private EventiTasti tasti = null;
    private StartGame startGame = null;
    private boolean avvioCompleto = false;
    private AsyncLogger debuglogger;

    private static View sinView = null;

    private View() {
        //Empty
    }

    @Override
    public void startLogger() {
        debuglogger = new AsyncLogger(Logger.getLogger("DEBUGLOG"));
        ExitManager.getInstance().registraLogger(debuglogger);
        debuglogger.start();
    }

    public static View getInstance() {
        if (sinView == null) {
            sinView = new View();
        }
        return sinView;
    }

    @Override
    public void log(String s) {
        debuglogger.log(s);
    }

    public void setFinestra(VFinestra finestra) {
        this.finestra = finestra;
    }

    @Override
    public void mostraGUI() {
        this.startGame = StartGame.getInstance();
    }

    public void creaFileConfigurazione(String Nome, String Sparo, String Sinistra, String Destra) {
        utils.creaFileConfigurazione(Nome, Sparo, Sinistra, Destra);
    }

    public void cambiaFileConfigurazione(String path) {
        utils.cambiaFileConfigurazione(path);
    }

    public String getSx() {
        return utils.getProprietaTastoMuoviSinistra();
    }

    public String getDx() {
        return utils.getProprietaTastoMuoviDestra();
    }

    public String getSpara() {
        return utils.getProprietaTastoSparo();
    }

    @Override
    public void esplodiGiocatore(int x, int y) {
        finestra.getAnimati().esplodiGiocatore(x, y);
    }

    @Override
    public void esplodiAlieno(int x, int y) {
        finestra.getAnimati().esplodiAlieno(x, y);
    }

    public boolean controllaTastiUtils(String tasti[]) {
        boolean valido = true;
        for (int i = 0; i < tasti.length; i++) {
            if (!seValido(tasti[i])) {
                return false;
            }
        }
        for (int i = 0; i < tasti.length; i++) {
            if (tasti[i].equals(tasti[(i + 1) % 3])) {
                return false;
            }
        }
        return valido;
    }

    public boolean registraTastiUtils(String tasti[]) {
        if (controllaTastiUtils(tasti)) {
            utils.setProprietaTastoMuoviSinistra(tasti[0]);
            utils.setProprietaTastoMuoviDestra(tasti[1]);
            utils.setProprietaTastoSparo(tasti[2]);
            return true;
        }
        return false;
    }
	
    public void registraTastiView() {
        if (tasti != null) {
            stopTastiView();
        }
        tasti = new EventiTasti(
                utils.getProprietaTastoMuoviSinistra(),
                utils.getProprietaTastoMuoviDestra(),
                utils.getProprietaTastoSparo(),
				StartGame.getInstance().getPausa()
        );

        View.getInstance().log("Caricati i comandi dell'utente. Ricevuto:");
        View.getInstance().log("Tasto sinistra: " + utils.getProprietaTastoMuoviSinistra());
        View.getInstance().log("Tasto destra: " + utils.getProprietaTastoMuoviDestra());
        View.getInstance().log("Tasto sparo: " + utils.getProprietaTastoSparo());

        finestra.addKeyListener(tasti);
        finestra.requestFocusInWindow();
    }

    public void movimentoNavetta() {
        if (tasti != null) {
            tasti.muoviNavetta();
        }
    }

    @Override
    public void stopTastiView() {
        if (tasti != null) {
            finestra.removeKeyListener(tasti);
            tasti = null;
        }
    }

    private static boolean seValido(String s) {
        boolean v = false;
        s = s.trim().toLowerCase();
        if (s.length() == 1) {
            for (char i = 'a'; i <= 'z'; i++) {
                if (s.charAt(0) == i) {
                    return true;
                }
            }
            for (char i = '0'; i <= '9'; i++) {
                if (s.charAt(0) == i) {
                    return true;
                }
            }
        } else {
            if (s.equals("space")
                    || s.equals("up")
                    || s.equals("down")
                    || s.equals("left")
                    || s.equals("right")) {
                v = true;
            }
        }
        return v;
    }

    @Override
    public int[] getDimensioneImmagine(String path) {
        ImgCache imgcache = ImgCache.getInstance();
        BufferedImage buff = imgcache.getImage(path);

        int[] res = new int[]{buff.getHeight(), buff.getWidth()};
        return res;
    }

    public boolean avvioConcluso() {
        return avvioCompleto;
    }

    @Override
    public void fineAvvio() {
        avvioCompleto = true;
    }

    public void precaricaMusica() {
        utils.precaricaMusica();
    }

    @Override
    public void setLogic(ILogic logic) {
        this.logic = logic;
    }

    @Override
    public void setUtils(IUtils utils) {
        this.utils = utils;
    }

    public boolean colpoGVisibile() {
        return this.logic.colpoGVisibile();
    }

    public void spostaGiocatoreSx() {
        this.logic.spostaGiocatoreSx();
    }

    public void spostaGiocatoreDx() {
        this.logic.spostaGiocatoreDx();
    }

    public void sparaColpoG() {
        this.logic.sparaColpoG();
    }

    public int getCostanteLARGHEZZA() {
        return this.logic.getCostanteLARGHEZZA();
    }

    public int getCostanteALTEZZA() {
        return this.logic.getCostanteALTEZZA();
    }

    public String[] getNomiConfigFileProfiliUtente() {
        return this.utils.getNomiConfigFileProfiliUtente();
    }

    public void avanzaAnimazioni() {
        this.logic.avanzaAnimazioni();
    }

    public String getPrimaImmagine(String oggetto) {
        return this.logic.getPrimaImmagine(oggetto);
    }

    public int[] getCoordColpoG() {
        return this.logic.getCoordColpoG();
    }

    public int getPosizioneGiocatore() {
        return this.logic.getPosizioneGiocatore();
    }

    public int getPunteggioPartita() {
        return this.logic.getPunteggioPartita();
    }

    @Override
    public void aggiornaPunteggioPartita(int punteggio) {
        finestra.aggiornaPunteggio(punteggio);
    }

    public void aggiornaClassifica(String nomeGiocatore, int punteggio) {
        this.utils.aggiornaClassifica(nomeGiocatore, punteggio);
    }

    public int getMigliorPunteggioClassifica() {
        return this.utils.getMigliorPunteggioClassifica();
    }

    public String[][] getPrimeNRigheClassifica(int n) {
        return this.utils.getPrimeNRigheClassifica(n);
    }

    @Override
    public void registraAtExit(Closeable c) {
        ExitManager.getInstance().registra(c);
    }

    @Override
    public void exitGamePerErroreSerio(Object message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void setImmaginiEsplosioniInCache() {
        VExpGiocatore.setImmaginiEsplosioneGiocatoreInCache();
        VExpAlieno.setImmaginiEsplosioneAlienoInCache();
    }

    public boolean giocatoreEsploso() {
        return logic.giocatoreEsploso();
    }

    public boolean getGiocatoreMorto() {
        return logic.getGiocatoreMorto();
    }

    public int[] getCoordAlieno(int i, int j) {
        return logic.getCoordAlieno(i, j);
    }

    public boolean alienoEsploso(int i, int j) {
        return logic.alienoEsploso(i, j);
    }

    public boolean alienoInFormazione(int i, int j) {
        return logic.alienoInFormazione(i, j);
    }

    public boolean colpoAlienoVisibile(int i) {
        return logic.colpoAlienoVisibile(i);
    }

    public int[] getCoordColpoAlieno(int i) {
        return logic.getCoordColpoAlieno(i);
    }

    public void pausa(int provenienza) {
        if (tasti != null) {
            tasti.pausa(StartGame.getInstance().getPausa());
        }
        StartGame.getInstance().pausa(provenienza);
    }

    @Override
    public int getLivelloAttuale() {
        return logic.getLivelloAttuale();
    }

    @Override
    public int getViteAttuali() {
        return logic.getViteAttuali();
    }

    public String getNuovaSkin(int i, int j) {
        return logic.getNuovaSkin(i, j);
    }

    public int getStartYGiocatore() {
        return logic.getStartYGiocatore();
    }

    public boolean giocoIniziato() {
        return logic.giocoIniziato();
    }

    public void setStartGioco(String nomeGiocatore) {
        finestra.abilitaClickInizioGioco();
        logic.setStartGioco(nomeGiocatore);
    }

    public void terminaGioco() {
        finestra.stopClickFineGioco();
        logic.terminaGioco();
    }

    @Override
    public void gameOver() {
        View.getInstance().log("Musica background in pausa per gameOver in View");
        utils.resetMusicaBackground(true);

        aggiornaClassifica(logic.getNomeGiocatore(), getPunteggioPartita());

        stopTastiView();
        finestra.gameOver(logic.getNomeGiocatore(), getPunteggioPartita());
    }
	
	public void resetGiocatore() {
        logic.resetGiocatore();
    }

    public int getColpiAlieni() {
        return logic.getColpiAlieni();
    }

    public void clearBattaglione() {
        logic.clearBattaglione();
    }

    @Override
    public void cambioLivello() {
        finestra.getAnimati().cambioLivello();
    }

    public void playMusicaBackground() {
        utils.playMusicaBackground();
    }

    public void pausaMusicaBackground() {
        utils.pausaMusicaBackground();
    }

    public void resetMusicaBackground(boolean isNewGame) {
        utils.resetMusicaBackground(isNewGame);
    }

    public void mutaBackground() {
        utils.mutaBackground();
    }

    public void togliMutoBackground() {
        utils.togliMutoBackground();
    }

    public float getVolumeBackground() {
        return utils.getVolumeBackground();
    }

    public void pausaEffettiSonori() {
        utils.pausaEffettiSonori();
    }

    public void terminaPausaEffettiSonori() {
        utils.terminaPausaEffettiSonori();
    }

    public void mutaEffettiSonori() {
        utils.mutaEffettiSonori();
    }

    public void togliMutoEffettiSonori() {
        utils.togliMutoEffettiSonori();
    }

    public float getVolumeEffettiSonori() {
        return utils.getVolumeEffettiSonori();
    }

    public void playPrimoClassificato() {
        utils.playPrimoClassificato();
    }

    public void playSecondoQuintoClassificato() {
        utils.playSecondoQuintoClassificato();
    }

    public void terminaMusicaClassifica() {
        utils.terminaMusicaClassifica();
    }

    public int getRowSpacing() {
        return logic.getRowSpacing();
    }

    public int getColSpacing() {
        return logic.getColSpacing();
    }

    public void stopTutti() {
        utils.stopTutti();
    }

    @Override
    public void inviaTutteImmaginiInCache() {
        logic.inviaTutteImmaginiInCache();
    }

}
