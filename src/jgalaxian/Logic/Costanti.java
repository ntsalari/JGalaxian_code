package jgalaxian.Logic;

public final class Costanti {

    public static final int ALTEZZA = 571; //571
    public static final int LARGHEZZA = 500; //500
    public static final int STARTGIOCATORE = ALTEZZA - 112;
    public static final int ALTEZZA_START_ALIENI = 60;
    public static final int START_X_SCHIERAMENTO = 30;
    //Altezza da cui deve partire il proiettile sparato dal giocatore
    public static final int MOV_NAV = 3;
    public static final int VEL_COLPO = 6;
    public static final int STEP_ANIM = 1;
    public static final int ROWS = 6;
    public static final int COLS = 10;
    public static final int VITE = 3;
    public static final int ONEUP = 10000;
    public static final int NUM_MAX_ONEUP = 1; 
    public static final int MODULO_HOLD_ANIM = 4;
    public static final int MODULO_HOLD_STEP = 12;
    
    public static final int NUM_ALIENI_TOT = 46;
    
    //Questi spazi non sono da intendersi come spazi veri e propri ma come spazi dati dalla
    //somma tra bounding box dell'alieno e il margine che vogliamo dare. Per esempio essendo
    //che tutte le immagini di alieno hanno dimensione 28x28 px si ha che se 
    //SPAZIO_ALTEZZA_ALIENO == 28 allora l'unico "margine" in altezza che vediamo graficamente
    //tra gli alieni è quello intrinseco che vi è nelle foto (ovvero 6px per ogni foto per un
    //totale quindi di 12 px di margine in altezza) e non viene aggiunto ulteriore spazio.
    //Per SPAZIO_LARGHEZZA_ALIENO invece si hanno 10px di margine aggiuntivo tra ogni 
    //alieno (dati da SPAZIO_LARGHEZZA_ALIENO - larghezza foto alieno = 38 - 28) più poi vi è
    //anche lo spazio intrinseco dentro la foto stessa che è di 3px a foto per un totale di 6px
    //intrinseci tra ogni alieno
    public static final int SPAZIO_ALTEZZA_ALIENO = 28;
    public static final int SPAZIO_LARGHEZZA_ALIENO = 38;

    //Numero colpi alieni = 5 navicelle max * 3 colpi max ad attacco + n colpi jolly
    //I colpi jolly servono ad avere sempre a disposizione dei colpi da sparare senza doverli
    //ricreare a runtime e senza correre il rischio di ricerca prolungata a causa di colpi
    //tutti attualmente visibili a schermo e quindi non disponibili per un nuovo sparo
    public static final int NUM_COLPI_ALIENI = 30;
    public static final int NUM_FOTO_ALIENO = 20;

    //numAnimazione
    public static final int MIXED = 0;
    public static final int BOSS_1_SCORTA = 1;
    public static final int BOSS_2_SCORTA = 2;
    
    //tipoAnimazione
    public static final int SCHIERAMENTO = 0;
    public static final int ATTACCO = 1;
    public static final int RIENTRO = 2;
    
    //direzione
    public static final int SINISTRA = 2;
    public static final int DESTRA = 3;
    public static final int FALSA_DESTRA = 4;
    public static final int FALSA_SINISTRA = 5;
    
    //dir 
    public static final boolean BOOL_SINISTRA = false;
    public static final boolean BOOL_DESTRA = true;

    
    //Tempo tra partenza singoli alieni in attacco (all'interno della stessa "onda di attacco")
    //TEMPO_MASSIMO_ATTESA deve essere sempre maggiore (ALMENO DI 1) di TEMPO_MINIMO_ATTESA
    public static final int TEMPO_MINIMO_ATTESA = 1500;
    public static final int TEMPO_MASSIMO_ATTESA = 2500;
    
    //Tempo minimo di respown giocatore dopo essere stato colpito (in millisecondi)
    public static final int TEMPO_MINIMO_RESPAWN = 2000;    
    
    public static final int PUNTI_BLU_SCHIERAMENTO = 30;
    public static final int PUNTI_BLU_ATTACCO = 60;
    public static final int PUNTI_VIOLA_SCHIERAMENTO = 40;
    public static final int PUNTI_VIOLA_ATTACCO = 80;
    public static final int PUNTI_ROSSO_SCHIERAMENTO = 50;
    public static final int PUNTI_ROSSO_ATTACCO = 100;
    public static final int PUNTI_BOSS_SCHIERAMENTO = 60;
    public static final int PUNTI_BOSS_ATTACCO_SINGOLO = 150;
    public static final int PUNTI_BOSS_ATTACCO_1_SCORTA_VIVA = 200;
    public static final int PUNTI_BOSS_ATTACCO_1_SCORTA_DISTRUTTA = 250;
    public static final int PUNTI_BOSS_ATTACCO_2_SCORTE_VIVE = 300;
    public static final int PUNTI_BOSS_ATTACCO_2_SCORTE_DISTRUTTE = 800;

    
    public enum TipoSp {
        GIOCATORE,
        ALIENO,
        COLPOALIENO,
        COLPOGIOCATORE
    }

    public enum TipoAlieno {
        BOSS,
        ROSSO,
        VIOLA,
        BLU
    }
}