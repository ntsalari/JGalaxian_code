package jgalaxian.Utils;

import java.io.Closeable;

public class MusicManager implements Closeable {

    private static MusicManager sinMusicManager = null;
    private MusicBackground[] musicBackground;
    private UEffect[] ueffect;

    private final int numMusicBackgroundFile = 3;
    private final int numUEffectFile = 10;

    private int indexMusicBackground = -1;

    private final int LENTO = 0;
    private final int VELOCE = 1;
    private final int FINALE = 2;

    private final int COLPITO_ALIENO_COMUNE = 0;
    private final int COLPITO_ALIENO_BOSS = 1;
    private final int COLPITO_ALIENO_ROSSO = 2;
    private final int COLPITO_GIOCATORE = 3;
    private final int PARTENZA_ALIENO = 4;
    private final int PRIMO_CLASSIFICATO = 5;
    private final int SECONDO_QUINTO_CLASSIFICATO = 6;
    private final int SPARO_GIOCATORE = 7;
    private final int SET_VITE = 8;
    private final int START_GAME = 9;

    private MusicManager() {
        musicBackground = new MusicBackground[numMusicBackgroundFile];
        ueffect = new UEffect[numUEffectFile];
        Utils.getInstance().registraAtExit(this);
    }

    public static MusicManager getInstance() {
        if (sinMusicManager == null) {
            sinMusicManager = new MusicManager();
        }
        return sinMusicManager;
    }

    public void preparaMusica() {
        musicBackground[LENTO] = new MusicBackground("Background_Galaxian_Lento_Lungo.mp3");
        musicBackground[VELOCE] = new MusicBackground("Background_Galaxian_Veloce_Lungo.mp3");
        musicBackground[FINALE] = new MusicBackground("Background_Galaxian_Finale_Lungo.mp3");

        ueffect[COLPITO_ALIENO_COMUNE] = new UEffect("colpito_alieno_comune.wav");
        ueffect[COLPITO_ALIENO_BOSS] = new UEffect("colpito_alieno_giallo.wav");
        ueffect[COLPITO_ALIENO_ROSSO] = new UEffect("colpito_alieno_rosso.wav");
        ueffect[COLPITO_GIOCATORE] = new UEffect("morte_navicella.wav");
        ueffect[PARTENZA_ALIENO] = new UEffect("partenza_alieno.wav");
        ueffect[PRIMO_CLASSIFICATO] = new UEffect("primo_classificato.wav");
        ueffect[SECONDO_QUINTO_CLASSIFICATO] = new UEffect("secondo_quinto_classificato.wav");
        ueffect[SPARO_GIOCATORE] = new UEffect("sparo_navicella_amica.wav");
        ueffect[SET_VITE] = new UEffect("stage_flag_appaerance.wav");
        ueffect[START_GAME] = new UEffect("start_game.wav");
    }

    //Essendo che la musica in background non può saltare da un file all'altro ma deve per forza
    //seguire il flusso lento, veloce, finale, io so sempre quale file è in esecuzione    
    public void playMusicaBackground() {
        if (indexMusicBackground == -1) {
            indexMusicBackground++;
        }
        musicBackground[indexMusicBackground].play();
        Utils.getInstance().log("Sto riproducendo la musica background index: " + indexMusicBackground);
    }

    public void replayMusicaBackground() {
        musicBackground[indexMusicBackground].stop();
        musicBackground[indexMusicBackground].play();
    }

    public void resetMusicaBackground(boolean isNewGame) {
        musicBackground[indexMusicBackground].stop();
        indexMusicBackground = -1;
        if (!isNewGame) {
            playMusicaBackground();
        }
    }

    public void pausaMusicaBackground() {
        Utils.getInstance().log("Sto per stoppare la musica background index: " + indexMusicBackground);
        musicBackground[indexMusicBackground].pausa();
    }

    //Essendo che la musica in background non può saltare da un file all'altro ma deve per forza
    //seguire il flusso lento, veloce, finale, io so sempre quale file è in esecuzione
    public void cambiaMusicaBackground() {
        if (indexMusicBackground == -1) {
            indexMusicBackground++;
            playMusicaBackground();
        } else {
            musicBackground[indexMusicBackground].stop();
            indexMusicBackground++;
            indexMusicBackground = indexMusicBackground % numMusicBackgroundFile;
            playMusicaBackground();
        }
    }

    public void mutaBackground() {
        musicBackground[LENTO].setVolume(0);
        musicBackground[VELOCE].setVolume(0);
        musicBackground[FINALE].setVolume(0);
    }

    public void togliMutoBackground() {
        musicBackground[LENTO].setVolume(1);
        musicBackground[VELOCE].setVolume(1);
        musicBackground[FINALE].setVolume(1);
    }

    public float getVolumeBackground() {
        return MusicBackground.getVolume();
    }

    public float getVolumeEffettiSonori() {
        return UEffect.getVolume();
    }

    public void playColpitoAlienoComune() {
        ueffect[COLPITO_ALIENO_COMUNE].play();
    }

    public void playColpitoAlienoBoss() {
        ueffect[COLPITO_ALIENO_BOSS].play();
    }

    public void playColpitoAlienoRosso() {
        ueffect[COLPITO_ALIENO_ROSSO].play();
    }

    public void playColpitoGiocatore() {
        ueffect[COLPITO_GIOCATORE].play();
    }

    public void playPartenzaAlieno() {
        ueffect[PARTENZA_ALIENO].play();
    }

    public void playPrimoClassificato() {
        Utils.getInstance().log("PLAY: playPrimoClassificato");
        ueffect[PRIMO_CLASSIFICATO].play();
    }

    public void playSecondoQuintoClassificato() {
        Utils.getInstance().log("PLAY: playSecondoQuintoClassificato");
        ueffect[SECONDO_QUINTO_CLASSIFICATO].play();
    }

    public void playSparoGiocatore() {
        ueffect[SPARO_GIOCATORE].play();
    }

    public void playSetVite() {
        ueffect[SET_VITE].play();
    }

    public void playStartGame() {
        ueffect[START_GAME].play();
    }

    public void mutaEffettiSonori() {
        ueffect[SPARO_GIOCATORE].setVolume(0);
        ueffect[PARTENZA_ALIENO].setVolume(0);
        ueffect[COLPITO_ALIENO_COMUNE].setVolume(0);
        ueffect[COLPITO_ALIENO_ROSSO].setVolume(0);
        ueffect[COLPITO_ALIENO_BOSS].setVolume(0);
        ueffect[COLPITO_GIOCATORE].setVolume(0);
        ueffect[PRIMO_CLASSIFICATO].setVolume(0);
        ueffect[SECONDO_QUINTO_CLASSIFICATO].setVolume(0);
        ueffect[SET_VITE].setVolume(0);
        ueffect[START_GAME].setVolume(0);
    }

    public void togliMutoEffettiSonori() {
        ueffect[SPARO_GIOCATORE].setVolume(1);
        ueffect[PARTENZA_ALIENO].setVolume(1);
        ueffect[COLPITO_ALIENO_COMUNE].setVolume(1);
        ueffect[COLPITO_ALIENO_ROSSO].setVolume(1);
        ueffect[COLPITO_ALIENO_BOSS].setVolume(1);
        ueffect[COLPITO_GIOCATORE].setVolume(1);
        ueffect[PRIMO_CLASSIFICATO].setVolume(1);
        ueffect[SECONDO_QUINTO_CLASSIFICATO].setVolume(1);
        ueffect[SET_VITE].setVolume(1);
        ueffect[START_GAME].setVolume(1);
    }

    public void pausaEffettiSonori() {
        ueffect[SPARO_GIOCATORE].pausa();
        ueffect[PARTENZA_ALIENO].pausa();
        ueffect[COLPITO_ALIENO_COMUNE].pausa();
        ueffect[COLPITO_ALIENO_ROSSO].pausa();
        ueffect[COLPITO_ALIENO_BOSS].pausa();
        ueffect[COLPITO_GIOCATORE].pausa();
        ueffect[PRIMO_CLASSIFICATO].pausa();
        ueffect[SECONDO_QUINTO_CLASSIFICATO].pausa();
        ueffect[SET_VITE].pausa();
        ueffect[START_GAME].pausa();
    }

    public void terminaPausaEffettiSonori() {
        ueffect[SPARO_GIOCATORE].unpause();
        ueffect[PARTENZA_ALIENO].unpause();
        ueffect[COLPITO_ALIENO_COMUNE].unpause();
        ueffect[COLPITO_ALIENO_ROSSO].unpause();
        ueffect[COLPITO_ALIENO_BOSS].unpause();
        ueffect[COLPITO_GIOCATORE].unpause();
        ueffect[PRIMO_CLASSIFICATO].unpause();
        ueffect[SECONDO_QUINTO_CLASSIFICATO].unpause();
        ueffect[SET_VITE].unpause();
        ueffect[START_GAME].unpause();
    }

    public void terminaMusicaClassifica() {
        ueffect[PRIMO_CLASSIFICATO].stop();
        ueffect[SECONDO_QUINTO_CLASSIFICATO].stop();
    }

    public void stopTutti() {
        for (MusicBackground m : musicBackground) {
            if (m != null) {
                m.stop();
            }
        }
        for (UEffect u : ueffect) {
            if (u != null) {
                u.stop();
            }
        }
    }

    @Override
    public void close() {
        for (MusicBackground m : musicBackground) {
            if (m != null) {
                m.stop();
                m.close();
            }
        }
        for (UEffect u : ueffect) {
            if (u != null) {
                u.stop();
                u.close();
            }
        }
    }
}
