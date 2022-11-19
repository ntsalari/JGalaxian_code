package jgalaxian.Logic;

import jgalaxian.View.IView;
import jgalaxian.Utils.IUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.Closeable;

public class Logic implements ILogic {

    private IView view;
    private IUtils utils;

    private static Logic sinLogic = null;

    private String nomeGiocatore;
    private LAlieno[][] alieni;
    private LAlieno[] alieniAttacco;
    private LAlieno[] alieniFineAttacco;
    private int numAlieniAttacco;
    private int numAlieniVivi = Costanti.NUM_ALIENI_TOT;
    private LNavGiocatore giocatore;
    private LColpoAlieno[] colpiAlieni;
    private LColpoGiocatore colpoGiocatore;
    private LPunteggio punti;
    private LContVite vite;
    private LContLivello livello;
    private boolean musica = true;
    private boolean musicaBackground = true;
    private long tempoMorteGiocatore = 0;
    private int numAnimazione;
    private int[] curvaSx;
    private int diametroCurvaSinistra;
    private int[] arrayIndexCambiaSkinSinistra;
    private int[] secondaCurvaSx;
    private final int[][] segmentiSx;
    private int numeroPuntiFalsaCurvaSinistra;
    private int[] curvaDx;
    private int diametroCurvaDestra;
    private int[] arrayIndexCambiaSkinDestra;
    private final int[][] segmentiDx;
    private int[] secondaCurvaDx;
    private int numeroPuntiFalsaCurvaDestra;
    private int[] puntiFineAttaccoSinistra;
    private int[] puntiFineAttaccoDestra;
    private int[] puntiAttaccoLivello1;
    private int[] puntiAttaccoLivello2;
    private int[] puntiAttaccoLivello3;
    private boolean iniziato = false;
    private int countOrdineAlieniAttacco = 0;
    private boolean lasciapassarePrimoAlienoAttacco = false;
    private long tempoPrecedente = 0;
    private Random random = new Random();
    private ArrayList<LAlieno> alienoBoss;
    private ArrayList<LAlieno> alienoRossoSx; 
    private ArrayList<LAlieno> alienoRossoDx;
    private ArrayList<LAlieno> alienoViola;
    private ArrayList<LAlieno> alienoBlu;


    private Logic() {
        this.giocatore = new LNavGiocatore();
        this.colpiAlieni = new LColpoAlieno[Costanti.NUM_COLPI_ALIENI];
        this.colpoGiocatore = new LColpoGiocatore();
        this.vite = new LContVite();
        this.punti = new LPunteggio(this.vite);
        this.livello = new LContLivello();
        this.alieni = new LAlieno[Costanti.ROWS][Costanti.COLS];
        this.alieniAttacco = new LAlieno[5];
        this.alieniFineAttacco = new LAlieno[5];
        this.segmentiSx = new int[11][];
        this.segmentiDx = new int[11][];
        
        alienoBoss = new ArrayList<>();
        alienoRossoSx = new ArrayList<>();
        alienoRossoDx = new ArrayList<>();
        alienoViola = new ArrayList<>();
        alienoBlu = new ArrayList<>();


        int i = 0;
        alieni[i][3] = new LAlienoBoss(0, 0);
        alieni[i][6] = new LAlienoBoss(0, 0);
        alieni[i][3].id = 0;
        alieni[i][6].id = 1;

        i++;
        for (int j = 2; j < Costanti.COLS - 2; j++) {
            alieni[i][j] = new LAlienoRosso(0, 0);
            alieni[i][j].id = j;
        }

        i++;
        for (int j = 1; j < Costanti.COLS - 1; j++) {
            alieni[i][j] = new LAlienoViola(0, 0);
            alieni[i][j].id = j;
        }

        i++;
        while (i < Costanti.ROWS) {
            for (int j = 0; j < Costanti.COLS; j++) {
                alieni[i][j] = new LAlienoBlu(0, 0);
                alieni[i][j].id = j;
            }
            i++;
        }

        for (i = 0; i < colpiAlieni.length; i++) {
            colpiAlieni[i] = new LColpoAlieno();
        }

        for (i = 0; i < Costanti.ROWS; i++) {
            for (int j = 0; j < Costanti.COLS; j++) {
                if (alieni[i][j] != null && j % 2 == 1) {
                    alieni[i][j].cambiaSkin(Costanti.SCHIERAMENTO, 0);
                }
            }
        }
    }
    
    public static Logic getInstance() {
        if (sinLogic == null) {
            sinLogic = new Logic();
        }
        return sinLogic;
    }

    @Override
    public void setView(IView view) {
        this.view = view;
    }

    @Override
    public void setUtils(IUtils utils) {
        this.utils = utils;
    }

    public void log(String s) {
        view.log(s);
    }

    public void clearColpi() {
        for (LColpoAlieno a : colpiAlieni) {
            a.setVisibile(false);
        }
        colpoGiocatore.setVisibile(false);
    }

    public int[] getCurvaDx() {
        return this.curvaDx;
    }

    public int[][] getPuntiDx() {
        return this.segmentiDx;
    }

    public int[] getCurvaSx() {
        return this.curvaSx;
    }

    public int[] getArrayIndexCambiaSkinSinistra() {
        return this.arrayIndexCambiaSkinSinistra;
    }

    public int[] getArrayIndexCambiaSkinDestra() {
        return this.arrayIndexCambiaSkinDestra;
    }

    public int[][] getPuntiSx() {
        return this.segmentiSx;
    }

    public int[] getSecondaCurvaSx() {
        return secondaCurvaSx;
    }

    public int[] getSecondaCurvaDx() {
        return secondaCurvaDx;
    }

    public void esplodiGiocatore(int x, int y) {
        view.esplodiGiocatore(x, y);
    }

    public void esplodiAlieno(int x, int y) {
        view.esplodiAlieno(x, y);
    }

    public void sceltaAlieniAttacco() {

        //Aspetto che passi almeno un minimo di tempo tra morte giocatore e respawn
        if (vite.getVite() >= 1 && !giocatore.isVisibile()) {
            if (System.currentTimeMillis() > (tempoMorteGiocatore + Costanti.TEMPO_MINIMO_RESPAWN)) {

                //Aspetto che tutti gli alieni rientrino in schieramento prima di fare il respawn
                for (int i = 0; i < alieniFineAttacco.length; i++) {
                    if (alieniFineAttacco[i] != null && alieniFineAttacco[i].isVisibile()) {
                        return;
                    }
                }

                giocatore.LSpostabile(null, Costanti.LARGHEZZA / 2, Costanti.STARTGIOCATORE);
                giocatore.setGiocatoreMorto(false);
            } else {
                return;
            }
        }

        /********************************************************************************************
        *
        *   Nel livello 1 ho al massimo 3 alieni che mi attaccano insieme
        *   Non ci sono kamikaze
        *   Nel livello 2 ho al massimo 4 alieni che mi attaccano insieme
        *   C'è una bassa probabilità di essere attaccati da kamikaze
        *   La mira dei kamikaze non è precisa
        *   Nel livello 3 ho al massimo 5 alieni che mi attaccano insieme
        *   C'è una probabilità più alta del livello precedente di essere attaccati da kamikaze
        *   Questi kamikaze mirano direttamente alla posizione del giocatore
        *
        *********************************************************************************************/

        //Reset flag usati durante attacco alieno
        lasciapassarePrimoAlienoAttacco = false;
        countOrdineAlieniAttacco = 0;

        //Creo degli arrayList che riempo con alieni che possono essere scelti per attaccare
        alienoBoss.clear();
        alienoRossoSx.clear();
        alienoRossoDx.clear();
        alienoViola.clear();
        alienoBlu.clear();

        boolean isAlienoBossEmpty = false;
        boolean isAlienoRossoSxEmpty = false;
        boolean isAlienoRossoDxEmpty = false;
        boolean isAlienoViolaEmpty = false;
        boolean isAlienoBluEmpty = false;

        int countAlieniDisponibili = 0;
        
        //DISPONIBILI = Che non sono morti e non sono in fase di rientro dall'attacco

        //Riempimento arrayList alienoBoss con gli alieni DISPONIBILI per attacco
        if (alieni[0][3] != null && alieni[0][3].isVisibile() && !alieni[0][3].sceltoPerAttacco) {
            alienoBoss.add(alieni[0][3]);
            countAlieniDisponibili++;
            Logic.getInstance().log("Aggiunto alieno[0][3] ad arrayList alienoBoss");
        }
        if (alieni[0][6] != null && alieni[0][6].isVisibile() && !alieni[0][6].sceltoPerAttacco) {
            alienoBoss.add(alieni[0][6]);
            countAlieniDisponibili++;
            Logic.getInstance().log("Aggiunto alieno[0][6] ad arrayList alienoBoss");
        }

        Logic.getInstance().log("\n");
        int s;
        
        //Riempimento arrayList alienoRosso con gli alieni DISPONIBILI per attacco
        //Li prendo solo se BOSS morto o BOSS vivo e non è in fase di rientro dall'attacco
        s = 1;
        for (int j = 2; j < Costanti.COLS - 2; j++) {
            if (alieni[s][j] != null && alieni[s][j].isVisibile() && !alieni[s][j].sceltoPerAttacco) {
                if (alieni[s][j].id <= 4) { //Alieni rossi per boss id = 0
                    //Cerco se il boss è disponibile o meno
                    if (alieni[0][3] != null
                            && (!alieni[0][3].isVisibile()
                            || (alieni[0][3].isVisibile() && !alieni[0][3].sceltoPerAttacco))) {
                        alienoRossoSx.add(alieni[s][j]);
                        countAlieniDisponibili++;
                        Logic.getInstance().log("Aggiunto alieno[" + s + "][" + j + "] ad arrayList alienoRossoSx");
                    }
                } else {  //Alieni rossi per boss id = 1
                    //Cerco se il boss è disponibile o meno
                    if (alieni[0][6] != null
                            && (!alieni[0][6].isVisibile()
                            || (alieni[0][6].isVisibile() && !alieni[0][6].sceltoPerAttacco))) {
                        alienoRossoDx.add(alieni[s][j]);
                        countAlieniDisponibili++;
                        Logic.getInstance().log("Aggiunto alieno[" + s + "][" + j + "] ad arrayList alienoRossoDx");
                    }
                }
            }
        }

        Logic.getInstance().log("\n");

        //Riempimento arrayList alienoViola con gli alieni DISPONIBILI per attacco
        s = 2;
        for (int j = 1; j < Costanti.COLS - 1; j++) {
            if (alieni[s][j] != null && alieni[s][j].isVisibile() && !alieni[s][j].sceltoPerAttacco) {
                alienoViola.add(alieni[s][j]);
                countAlieniDisponibili++;
                Logic.getInstance().log("Aggiunto alieno[" + s + "][" + j + "] ad arrayList alienoViola");
            }
        }

        Logic.getInstance().log("\n");

        //Riempimento arrayList alienoBlu con gli alieni DISPONIBILI per attacco
        s = 3;
        for (; s < Costanti.ROWS; s++) {
            for (int j = 0; j < Costanti.COLS; j++) {
                if (alieni[s][j] != null && alieni[s][j].isVisibile() && !alieni[s][j].sceltoPerAttacco) {
                    alienoBlu.add(alieni[s][j]);
                    countAlieniDisponibili++;
                    Logic.getInstance().log("Aggiunto alieno[" + s + "][" + j + "] ad arrayList alienoBlu");
                }
            }
        }

        if (countAlieniDisponibili == 0) {
            return;
        }

        int numAlieni, iIndexAlieno, jIndexAlieno, numMaxAlieni;

        boolean kamikaze = false;
        final int PROB_KAMIKAZE_LIV_2 = 7; //Valore in percentuale
        final int PROB_KAMIKAZE_LIV_3 = 15; //Valore in percentuale

        numAnimazione = Costanti.MIXED;

        int livello = this.livello.getLivello();

        switch (livello) {
            case 1:
                numMaxAlieni = 3;
                break;
            case 2:
                numMaxAlieni = 4;
                break;
            default:
                numMaxAlieni = 5;
                break;
        }

        numAlieni = 1 + random.nextInt(numMaxAlieni); //Il bound è escluso
        
        Logic.getInstance().log("\nNumero alieni: " + numAlieni);

        //Metto in attacco tutti gli alieni disponibili
        if (this.numAlieniVivi <= 5) {
            int count = 0;
            numAlieni = 0;
            Logic.getInstance().log("\nCambio numAlieni in numAlieniVivi <= 5 : " + numAlieni);

            for (int i = 0; i < alienoBoss.size(); i++) {
                LAlienoBoss soldato = (LAlienoBoss) alienoBoss.get(i);
                alieniAttacco[count] = soldato;
                alieniAttacco[count].sceltoPerAttacco = true;
                LAlieno.decrementaCountAlieniSchieramento();

                alienoBoss.remove(i);
                count++;
                numAlieni = count;
                i--;

                Logic.getInstance().log("Scelto BOSS: " + soldato + " id " + soldato.id);
                Logic.getInstance().log("decrementaCountAlieniSchieramento - scelto alieno BOSS: " + LAlieno.getCountAlieniSchieramento());

                Logic.getInstance().log("\n\nMENO DI 5 ALIENI");
                Logic.getInstance().log("numAlieniVivi <= 5  --  numAlieni PRIMA di scegliere scorta boss: " + count);

                if (soldato.id == 0) {
                    int[] res = scegliScortaBoss(soldato, numAlieni, numAlieni + alienoRossoSx.size(), alienoRossoSx.size(), alienoRossoSx, isAlienoRossoSxEmpty, alienoRossoDx, isAlienoRossoDxEmpty);
                    isAlienoRossoSxEmpty = (res[0] == 1);
                    isAlienoRossoDxEmpty = (res[2] == 1);
                    count = numAlieni = res[1];
                    Logic.getInstance().log("alienoRossoSx.size() - dopo scelta SCORTA: " + alienoRossoSx.size());
                    Logic.getInstance().log("alienoRossoDx.size() - OPPOSTO dopo scelta SCORTA: " + alienoRossoDx.size());

                } else {
                    int[] res = scegliScortaBoss(soldato, numAlieni, numAlieni + alienoRossoDx.size(), alienoRossoDx.size(), alienoRossoDx, isAlienoRossoDxEmpty, alienoRossoSx, isAlienoRossoSxEmpty);
                    isAlienoRossoDxEmpty = (res[0] == 1);
                    isAlienoRossoSxEmpty = (res[2] == 1);
                    count = numAlieni = res[1];
                    Logic.getInstance().log("alienoRossoDx.size() - dopo scelta SCORTA: " + alienoRossoDx.size());
                    Logic.getInstance().log("alienoRossoSx.size() - OPPOSTO dopo scelta SCORTA: " + alienoRossoSx.size());
                }

                Logic.getInstance().log("numAlieniVivi <= 5  --  numAlieni DOPO di scegliere scorta boss: " + count + "\n\n");

            }

            Logic.getInstance().log("Size alieni boss : " + alienoBoss.size());

            for (int i = 0; i < alienoRossoSx.size(); i++) {
                alieniAttacco[count] = (LAlieno) alienoRossoSx.get(i);
                alieniAttacco[count].sceltoPerAttacco = true;
                LAlieno.decrementaCountAlieniSchieramento();
                Logic.getInstance().log("decrementaCountAlieniSchieramento - scelto alieno ROSSO SX: " + LAlieno.getCountAlieniSchieramento());

                alienoRossoSx.remove(i);
                count++;
                numAlieni = count;
                i--;
            }

            Logic.getInstance().log("Size alieni rossoSx : " + alienoRossoSx.size());

            for (int i = 0; i < alienoRossoDx.size(); i++) {
                alieniAttacco[count] = (LAlieno) alienoRossoDx.get(i);
                alieniAttacco[count].sceltoPerAttacco = true;
                LAlieno.decrementaCountAlieniSchieramento();
                Logic.getInstance().log("decrementaCountAlieniSchieramento - scelto alieno ROSSO DX: " + LAlieno.getCountAlieniSchieramento());
                
                alienoRossoDx.remove(i);
                count++;
                numAlieni = count;
                i--;
            }

            Logic.getInstance().log("Size alieni rossoDx : " + alienoRossoDx.size());

            for (int i = 0; i < alienoViola.size(); i++) {
                alieniAttacco[count] = (LAlieno) alienoViola.get(i);
                alieniAttacco[count].sceltoPerAttacco = true;
                LAlieno.decrementaCountAlieniSchieramento();
                Logic.getInstance().log("decrementaCountAlieniSchieramento - scelto alieno VIOLA: " + LAlieno.getCountAlieniSchieramento());
                
                alienoViola.remove(i);
                count++;
                numAlieni = count;
                i--;
            }

            Logic.getInstance().log("Size alieni viola : " + alienoViola.size());

            for (int i = 0; i < alienoBlu.size(); i++) {
                alieniAttacco[count] = (LAlieno) alienoBlu.get(i);
                alieniAttacco[count].sceltoPerAttacco = true;
                LAlieno.decrementaCountAlieniSchieramento();
                Logic.getInstance().log("decrementaCountAlieniSchieramento - scelto alieno BLU: " + LAlieno.getCountAlieniSchieramento());

                alienoBlu.remove(i);
                count++;
                numAlieni = count;
                i--;
            }

            Logic.getInstance().log("Size alieni blu : " + alienoBlu.size());
            Logic.getInstance().log("In numAlieniVivi <= 5  -->  numAlieni : " + numAlieni);
            Logic.getInstance().log(Arrays.toString(alieniAttacco));

        } else { //Attacco alieno con numAlieni > 5

            boolean errore;
            int alienoAttuale = 0;

            //Ci sono dei casi, quando sta per finire il livello, in cui numAlieni > 5 ma dato che alcuni 
            //alieni stanno ritornando dall'attacco, il numero di alieni disponibili è inferiori al numero
            //di alieni che dovrebbero partire per l'attacco
            if (countAlieniDisponibili < numAlieni) {
                numAlieni = countAlieniDisponibili;
            }

            if (livello == 2) {
                if ((random.nextInt(100) + 1) < PROB_KAMIKAZE_LIV_2) {
                    kamikaze = true;
                }
            } else if (livello >= 3) {
                if ((random.nextInt(100) + 1) < PROB_KAMIKAZE_LIV_3) {
                    kamikaze = true;
                }
            }

            //itero finchè non ho scelto tutti gli alieni per l'attacco
            
            do { 
                errore = false;
                iIndexAlieno = random.nextInt(Costanti.ROWS);
                Logic.getInstance().log("\nScelto iIndexAlieno : " + iIndexAlieno);

                if (alienoBoss.isEmpty()) {
                    isAlienoBossEmpty = true;
                    Logic.getInstance().log("isAlienoBossEmpty : " + isAlienoBossEmpty);
                }

                if (alienoRossoSx.isEmpty()) {
                    isAlienoRossoSxEmpty = true;
                    Logic.getInstance().log("isAlienoRossoSxEmpty : " + isAlienoRossoSxEmpty);
                }

                if (alienoRossoDx.isEmpty()) {
                    isAlienoRossoDxEmpty = true;
                    Logic.getInstance().log("isAlienoRossoDxEmpty : " + isAlienoRossoDxEmpty);
                }

                if (alienoViola.isEmpty()) {
                    isAlienoViolaEmpty = true;
                    Logic.getInstance().log("isAlienoViolaEmpty : " + isAlienoViolaEmpty);
                }

                if (alienoBlu.isEmpty()) {
                    isAlienoBluEmpty = true;
                    Logic.getInstance().log("isAlienoBluEmpty : " + isAlienoBluEmpty);
                }

                //Elimino la possibilità di cadere in un loop dovuto ad errori di arrayList vuote
                //Essendo che io prendo index a caso e se quell'index corrisponde ad un arrayList
                //di un tipo di Alieno che è stato ucciso del tutto, genero un errore che mi fa 
                //ripescare un nuovo index. Se sono sfortunato si verificherebbe in continuazione
                //ed ecco perchè effettuo una scelta "forzata"
                if (iIndexAlieno > 2 && isAlienoBluEmpty) {
                    if (isAlienoViolaEmpty) {
                        if (isAlienoRossoSxEmpty && isAlienoRossoDxEmpty) {
                            Logic.getInstance().log("Cambio iIndexAlieno - 0");
                            //Ci sono rimasti solo alieni Boss
                            iIndexAlieno = 0;
                        } else {
                            Logic.getInstance().log("Cambio iIndexAlieno - 1");
                            if (isAlienoBossEmpty) {
                                Logic.getInstance().log("Cambio iIndexAlieno - 1.1");
                                iIndexAlieno = 1;
                            } else {
                                Logic.getInstance().log("Cambio iIndexAlieno - 1.2");
                                iIndexAlieno = 0;
                            }
                        }
                    } else {
                        Logic.getInstance().log("Cambio iIndexAlieno - 2");
                        iIndexAlieno = 2;
                    }
                } else if (iIndexAlieno == 2 && isAlienoViolaEmpty) {
                    Logic.getInstance().log("Cambio iIndexAlieno - 3");
                    if (!isAlienoBossEmpty) {
                        iIndexAlieno = 0;
                    } else if (!isAlienoBluEmpty) {
                        iIndexAlieno = 3;
                    } else {
                        iIndexAlieno = 1;
                    }
                } else if (iIndexAlieno == 1 && isAlienoRossoSxEmpty && isAlienoRossoDxEmpty) {
                    Logic.getInstance().log("Cambio iIndexAlieno - 4");
                    if (!isAlienoBluEmpty) {
                        iIndexAlieno = 3;
                    } else if (!isAlienoViolaEmpty) {
                        iIndexAlieno = 2;
                    } else {
                        iIndexAlieno = 0;
                    }
                } else if (iIndexAlieno == 0 && isAlienoBossEmpty) {
                    Logic.getInstance().log("Cambio iIndexAlieno - 5");
                    if (!isAlienoViolaEmpty) {
                        iIndexAlieno = 2;
                    } else if (!isAlienoRossoSxEmpty || !isAlienoRossoDxEmpty) {
                        iIndexAlieno = 1;
                    } else {
                        iIndexAlieno = 3;
                    }
                }

                Logic.getInstance().log("Nuovo iIndexAlieno : " + iIndexAlieno);

                //Scelgo un alieno casuale dentro all'ArrayList della tipologia di alieno scelto
                switch (iIndexAlieno) {
                    //BOSS
                    case 0:
                        jIndexAlieno = random.nextInt(alienoBoss.size());
                        break;
                    //ROSSO
                    case 1:
                        if (isAlienoRossoSxEmpty) {
                            jIndexAlieno = random.nextInt(alienoRossoDx.size());
                        } else if (isAlienoRossoDxEmpty) {
                            jIndexAlieno = random.nextInt(alienoRossoSx.size());
                        } else {
                            int minSize = (alienoRossoSx.size() <= alienoRossoDx.size() ? alienoRossoSx.size() : alienoRossoDx.size());
                            jIndexAlieno = random.nextInt(minSize);
                        }
                        break;
                    //VIOLA
                    case 2:
                        jIndexAlieno = random.nextInt(alienoViola.size());
                        break;
                    //BLU
                    default:
                        jIndexAlieno = random.nextInt(alienoBlu.size());
                        break;
                }

                Logic.getInstance().log("\nalienoAttuale: " + alienoAttuale);
                Logic.getInstance().log("iIndexAlieno: " + iIndexAlieno);
                Logic.getInstance().log("jIndexAlieno: " + jIndexAlieno);

                
                /********************************** ALIENI BOSS **********************************/
                
                //Dato che gli attacchi boss hanno più probabilità di uccidere il giocatore,
                //abbasso la probabilità di scegliere un alieno boss  ->  1/6 * 6/10 * 100 = 10%
                if (iIndexAlieno == 0) {
                    if (random.nextInt(10) < 6) {

                        //Se numAlieni == 1 ed esiste almeno 1 alieno rosso non posso usare un boss
                        //Se scelta primo alieno e numAlieni == 2 ed esistono almeno 2 alieni rossi non posso usare un boss
                        //Se scelta secondo alieno e numAlieni == 2 ed esiste almeno 1 alieno rosso non posso usare un boss
                        //Se scelta secondo alieno e numAlieni == 3 ed esistono almeno 2 alieni rossi non posso usare un boss
                        //Se scelta terzo alieno e numAlieni == 3 ed esiste almeno 1 alieno rosso non posso usare un boss
                        //Se scelta terzo alieno e numAlieni == 4 ed esistono almeno 2 alieni rossi non posso usare un boss
                        //Se scelta quarto alieno e numAlieni == 4 ed esiste almeno 1 alieno rosso non posso usare un boss
                        //Se scelta quarto alieno e numAlieni == 5 ed esistono almeno 2 alieni rossi non posso usare un boss
                        //Se scelta quinto alieno e numAlieni == 5 ed esiste almeno 1 alieno rosso non posso usare un boss
                        
                        //Condizione speciale per la quale si crea una specie di "ATTESA CIRCOLARE":
                        //Succede a volte che quando si rimane con pochissimi alieni quali per esempio
                        //almeno 1 BOSS e almeno 1 o 2 ROSSO e alcuni VIOLA o BLU. Se la loro somma è > 5
                        //allora viene estratto un numero che indica quanti alieni vanno in attacco. Se quel
                        //numero fosse 3 per esempio e i primi due alieni sono viola e/o blu e togliendo essi
                        //rimangono solo gialli e rossi si ha che il numero di alieni rimanenti da scegliere è 1
                        //ma noi non possiamo scegliere un rosso perchè esiste almeno un BOSS ma non possiamo 
                        //scegliere neanche un BOSS perchè esiste almeno un ROSSO che dovrebbe fargli sa scorta.
                        //In questi casi devo quindi uscire dal do-while senza scegliere tutti gli alieni richiesti

                        int count;
                        int id;

                        LAlienoBoss bossScelto = (LAlienoBoss) alienoBoss.get(jIndexAlieno);
                        id = bossScelto.id;

                        if (id == 0) {
                            count = alienoRossoSx.size();
                        } else {
                            count = alienoRossoDx.size();
                        }

                        if ((numAlieni == 1 && count >= 1)
                                || (alienoAttuale == 0 && numAlieni == 2 && count >= 2)
                                || (alienoAttuale == 1 && numAlieni == 2 && count >= 1)
                                || (alienoAttuale == 1 && numAlieni == 3 && count >= 2)
                                || (alienoAttuale == 2 && numAlieni == 3 && count >= 1)
                                || (alienoAttuale == 2 && numAlieni == 4 && count >= 2)
                                || (alienoAttuale == 3 && numAlieni == 4 && count >= 1)
                                || (alienoAttuale == 3 && numAlieni == 5 && count >= 2)
                                || (alienoAttuale == 4 && numAlieni == 5 && count >= 1)) {
                            
                            if (isAlienoViolaEmpty && isAlienoBluEmpty) {
                                Logic.getInstance().log("\n\n\nSono entrato in attesa circolare BOSS con ROSSO\n\n\n");
                                numAlieni = alienoAttuale; //Perchè alienoAttuale è già incrementato
                                break; //exit DO-WHILE
                            }

                            Logic.getInstance().log("L'alieno boss non può essere scelto:");
                            Logic.getInstance().log("countRossi: " + count);
                            errore = true;
                            continue;
                        }

                        alieniAttacco[alienoAttuale] = bossScelto;
                        alieniAttacco[alienoAttuale].sceltoPerAttacco = true;
                        LAlieno.decrementaCountAlieniSchieramento();
                        Logic.getInstance().log("decrementaCountAlieniSchieramento - scelto alieno BOSS: " + LAlieno.getCountAlieniSchieramento());
                        alienoBoss.remove(jIndexAlieno);

                        Logic.getInstance().log("Rimosso BOSS - size : " + alienoBoss.size());

                        if (alienoBoss.isEmpty()) {
                            isAlienoBossEmpty = true;
                        }

                        if (numAlieni == 1) {
                            break; //exit do-while
                        } else {
                            alienoAttuale++;

                            if (id == 0) {
                                int[] res = scegliScortaBoss(bossScelto, alienoAttuale, numAlieni, count, alienoRossoSx, isAlienoRossoSxEmpty, alienoRossoDx, isAlienoRossoDxEmpty);
                                isAlienoRossoSxEmpty = (res[0] == 1);
                                isAlienoRossoDxEmpty = (res[2] == 1);
                                alienoAttuale = res[1];
                                Logic.getInstance().log("Alieno attuale DOPO scegliScortaBoss id = 0 : " + alienoAttuale);
                            } else {
                                int[] res = scegliScortaBoss(bossScelto, alienoAttuale, numAlieni, count, alienoRossoDx, isAlienoRossoDxEmpty, alienoRossoSx, isAlienoRossoSxEmpty);
                                isAlienoRossoDxEmpty = (res[0] == 1);
                                isAlienoRossoSxEmpty = (res[2] == 1);
                                alienoAttuale = res[1];
                                Logic.getInstance().log("Alieno attuale DOPO scegliScortaBoss id = 1 : " + alienoAttuale);
                            }
                        }
                    } else { //Se riga boss ma "sfortunato" 
                        Logic.getInstance().log("Ops, ritenta. Sarai più fortunato!");
                        errore = true;
                        continue;
                    }
                }
                

                /********************************** ALIENI ROSSI **********************************/
                
                if (iIndexAlieno == 1) {

                    ArrayList alienoRosso = null;
                    boolean isAlienoRossoEmpty;
                    int scelta = -1;

                    if (!isAlienoBossEmpty) {
                        if (alienoBoss.size() == 2) {
                            Logic.getInstance().log("L'alieno rosso non può essere scelto: esistono entrambi i boss");
                            errore = true;
                            continue;
                        } else if (alienoBoss.size() == 1 && ((LAlieno) alienoBoss.get(0)).id == 0) {
                            if (isAlienoRossoDxEmpty) {
                                Logic.getInstance().log("L'alieno rossoSx non può essere scelto: esiste il proprio boss");
                                Logic.getInstance().log("L'alieno rossoDx non può essere scelto: è vuoto");
                                errore = true;
                                continue;
                            } else {
                                alienoRosso = alienoRossoDx;
                                isAlienoRossoEmpty = isAlienoRossoDxEmpty;
                                scelta = 1;
                            }
                        } else if (alienoBoss.size() == 1 && ((LAlieno) alienoBoss.get(0)).id == 1) {
                            if (isAlienoRossoSxEmpty) {
                                Logic.getInstance().log("L'alieno rossoDx non può essere scelto: esiste il proprio boss");
                                Logic.getInstance().log("L'alieno rossoSx non può essere scelto: è vuoto");
                                errore = true;
                                continue;
                            } else {
                                alienoRosso = alienoRossoSx;
                                isAlienoRossoEmpty = isAlienoRossoSxEmpty;
                                scelta = 0;
                            }
                        }
                    } else { //Non ci sono alieni boss

                        if (!isAlienoRossoSxEmpty && !isAlienoRossoDxEmpty) {
                            scelta = random.nextInt(2);
                        }

                        if (scelta == 0 || (scelta == -1 && !isAlienoRossoSxEmpty)) {
                            alienoRosso = alienoRossoSx;
                            isAlienoRossoEmpty = isAlienoRossoSxEmpty;
                        } else if (scelta == 1 || (scelta == -1 && !isAlienoRossoDxEmpty)) {
                            alienoRosso = alienoRossoDx;
                            isAlienoRossoEmpty = isAlienoRossoDxEmpty;
                        }
                    }

                    alieniAttacco[alienoAttuale] = (LAlieno) alienoRosso.get(jIndexAlieno);
                    alieniAttacco[alienoAttuale].sceltoPerAttacco = true;
                    LAlieno.decrementaCountAlieniSchieramento();
                    Logic.getInstance().log("decrementaCountAlieniSchieramento - scelto alieno ROSSO: " + LAlieno.getCountAlieniSchieramento());
                    alienoRosso.remove(jIndexAlieno);

                    if (alienoRosso.isEmpty()) {
                        isAlienoRossoEmpty = true;

                        if (scelta == 0 || (scelta == -1 && !isAlienoRossoSxEmpty)) {
                            isAlienoRossoSxEmpty = isAlienoRossoEmpty;
                        } else if (scelta == 1 || (scelta == -1 && !isAlienoRossoDxEmpty)) {
                            isAlienoRossoDxEmpty = isAlienoRossoEmpty;
                        }
                    }

                }
                

                /********************************** ALIENI VIOLA **********************************/
                
                if (iIndexAlieno == 2) {

                    alieniAttacco[alienoAttuale] = (LAlieno) alienoViola.get(jIndexAlieno);
                    alieniAttacco[alienoAttuale].sceltoPerAttacco = true;
                    LAlieno.decrementaCountAlieniSchieramento();
                    Logic.getInstance().log("decrementaCountAlieniSchieramento - scelto alieno VIOLA: " + LAlieno.getCountAlieniSchieramento());
                    alienoViola.remove(jIndexAlieno);

                    if (alienoViola.isEmpty()) {
                        isAlienoViolaEmpty = true;
                    }

                }
                
                
                /********************************** ALIENI BLU **********************************/

                if (iIndexAlieno > 2) {

                    alieniAttacco[alienoAttuale] = (LAlieno) alienoBlu.get(jIndexAlieno);
                    alieniAttacco[alienoAttuale].sceltoPerAttacco = true;
                    LAlieno.decrementaCountAlieniSchieramento();
                    Logic.getInstance().log("decrementaCountAlieniSchieramento - scelto alieno BLU: " + LAlieno.getCountAlieniSchieramento());
                    alienoBlu.remove(jIndexAlieno);

                    if (alienoBlu.isEmpty()) {
                        isAlienoBluEmpty = true;
                    }
                }
                
                
                /*******************************************************************************/

                //Incremento in caso ci siano altri alieni da "aggiungere" all'attacco
                alienoAttuale++;

            } while (errore || alienoAttuale < numAlieni);
        }

        Logic.getInstance().log("\nLivello: " + livello);
        Logic.getInstance().log("numAlieni: " + numAlieni);
        Logic.getInstance().log("Truppe attacco alieno:");

        numAlieniAttacco = numAlieni;

        for (int i = 0; i < numAlieni; i++) {
            Logic.getInstance().log("\t" + alieniAttacco[i].t + "  id: " + alieniAttacco[i].id);
        }

        Logic.getInstance().log("Kamikaze: " + kamikaze + "\n");

        if (kamikaze) {
            scegliKamikaze();
        }

    }

    public void iniziaAttaccoAlieno(LAlieno soldato, int livello) {

        soldato.setFormazione(false);
        soldato.setPartitoInAttacco(true);

        //Scelta direzione attacco alieno
        int direzione = scegliDirezioneAttacco(soldato, -1);

        soldato.direzioneAttacco = direzione;

        //Se sto in attacco con BOSS e uno della scorta uscirebbe dallo schermo per fare la curva,
        //allora devo cambiare direzione in "FINTA_CURVA" per tutta la scorta e boss
        if (numAnimazione == Costanti.BOSS_1_SCORTA && soldato.t == Costanti.TipoAlieno.BOSS) {
            Logic.getInstance().log("99999 - LOGIC BOSS_1_SCORTA");

            LAlienoBoss boss = (LAlienoBoss) soldato;
            LAlieno membroScorta = (boss.getMembriScorta())[0];

            if (membroScorta != null && membroScorta.isVisibile()) {
                membroScorta.setFormazione(false);
                membroScorta.setPartitoInAttacco(true);
                Logic.getInstance().log("LOGIC, BOSS_1_SCORTA");
                int nuovaDirezione = scegliDirezioneAttacco(membroScorta, direzione);
                if (nuovaDirezione != direzione && (nuovaDirezione == Costanti.FALSA_DESTRA || nuovaDirezione == Costanti.FALSA_SINISTRA)) {
                    boss.direzioneAttacco = nuovaDirezione; //boss
                    membroScorta.direzioneAttacco = nuovaDirezione; //rosso scorta
                    direzione = nuovaDirezione;
                } else {
                    membroScorta.direzioneAttacco = direzione; //rosso scorta
                }

            } else {
                Logic.getInstance().log("LOGIC, BOSS_1_SCORTA CON BOSS SENZA SCORTA");
            }

        } else if (numAnimazione == Costanti.BOSS_2_SCORTA && soldato.t == Costanti.TipoAlieno.BOSS) {
            Logic.getInstance().log("99999 - LOGIC BOSS_2_SCORTA");

            LAlienoBoss boss = (LAlienoBoss) soldato;
            LAlieno[] membriScorta = boss.getMembriScorta();

            for (int s = 0; s < membriScorta.length; s++) {
                if (membriScorta[s] != null && membriScorta[s].isVisibile()) {
                    membriScorta[s].setFormazione(false);
                    membriScorta[s].setPartitoInAttacco(true);
                    Logic.getInstance().log("LOGIC, BOSS_2_SCORTA");
                    int nuovaDirezione = scegliDirezioneAttacco(membriScorta[s], direzione);
                    if (nuovaDirezione != direzione && (nuovaDirezione == Costanti.FALSA_DESTRA || nuovaDirezione == Costanti.FALSA_SINISTRA)) {
                        boss.direzioneAttacco = nuovaDirezione; //boss
                        membriScorta[s].direzioneAttacco = nuovaDirezione; //rosso scorta
                        if (s == 1 && membriScorta[s - 1] != null) { //seconda scorta
                            membriScorta[s - 1].direzioneAttacco = nuovaDirezione; //rosso scorta
                        }

                        direzione = nuovaDirezione;
                    } else {
                        membriScorta[s].direzioneAttacco = direzione; //rosso scorta
                    }

                    Logic.getInstance().log("LOGIC, Sto usando una scorta " + membriScorta[s].t + " con id: " + membriScorta[s].id + " che ha direzione: " + membriScorta[s].direzioneAttacco);

                } else {
                    Logic.getInstance().log("LOGIC, BOSS_2_SCORTA CON BOSS SENZA SCORTA");
                }
            }

            for (int s = 0; s < membriScorta.length; s++) {
                if (membriScorta[s] != null) {
                    Logic.getInstance().log("LOGIC, Sto usando scorta " + membriScorta[s].t + " con id: " + membriScorta[s].id + " che ha direzione: " + membriScorta[s].direzioneAttacco);
                }
            }

        }

        Logic.getInstance().log("LOGIC, 1 - Sto usando alieno " + soldato.t + " con id: " + soldato.id);
        Logic.getInstance().log("LOGIC, 1 - numAnimazione " + numAnimazione);
        playPartenzaAlieno();
        soldato.attaccoAnim(livello, direzione);
    }

    
    /**
     * Se si vuole trovare la direzione di un qualsiasi alieno scegliere {@code direzione = -1}. 
     * Se si vuole trovare la direzione di una scorta del boss {@code direzione = direzione del boss}.
     */
    public int scegliDirezioneAttacco(LAlieno soldato, int direzione) {

        //Per tutti gli alieni che non hanno a che fare con un BOSS
        if (direzione == -1) {
            if (soldato.getXPos() < (Costanti.LARGHEZZA / 2)) {
                direzione = Costanti.SINISTRA;
            } else {
                direzione = Costanti.DESTRA;
            }
        }

        //Per tutti gli alieni compresi quelli che hanno a che fare con un BOSS
        if (direzione == Costanti.SINISTRA) {
            if (soldato.getXPos() < diametroCurvaSinistra) {
                direzione = Costanti.FALSA_DESTRA;
            }
        } else if (direzione == Costanti.DESTRA) {
            if (soldato.getXPos() > Costanti.LARGHEZZA - diametroCurvaDestra) {
                direzione = Costanti.FALSA_SINISTRA;
            }
        }

        return direzione;
    }

    /**
     * @param alienoAttuale   -->     index alieno da scegliere in attacco
     *
     * @param numAlieni       -->     numero di alieni da scegliere per attacco
     * 
     * @param count           -->     numero alieni (size) dentro ArrayList alieni ROSSO riferita al boss corrente
     * 
     * @param alienoRosso           -->     Giusta ArrayList da cui estrarre alieni di scorta
     * 
     * @param isAlienoRossoEmpty      -->     Per aggiornare il boolean anche in scelta alieno
     * 
     * @param alienoRossoOpposto    -->     ArrayList ROSSO del BOSS opposto a quello attuale
     * 
     * @param isAlienoRossoOppostoEmpty      -->     Per aggiornare il boolean anche in scelta alieno
     * 
     * @return new int[] {isAlienoRossoEmpty, alienoAttuale, isAlienoRossoOppostoEmpty}
    **/
    public int[] scegliScortaBoss(LAlienoBoss boss, int alienoAttuale, int numAlieni, int count, ArrayList<LAlieno> alienoRosso, boolean isAlienoRossoEmpty, ArrayList<LAlieno> alienoRossoOpposto, boolean isAlienoRossoOppostoEmpty) {

        Logic.getInstance().log("Alieno ROSSO size - PRIMA FOR: " + alienoRosso.size());

        LAlienoRosso soldato = null;
        int aggiunti = 0;

        for (int j = 0; j < alienoRosso.size(); j++) {
            if (alienoAttuale < numAlieni && aggiunti < 2) {
                soldato = (LAlienoRosso) alienoRosso.get(j);
                alieniAttacco[alienoAttuale] = soldato;
                soldato.sceltoPerAttacco = true;
                LAlieno.decrementaCountAlieniSchieramento();
                //soldato.setFormazione(false);
                soldato.setScorta(1, boss.id);
                boss.setMembriScorta(aggiunti, soldato);
                alienoRosso.remove(j);
                aggiunti++;
                j--;

                Logic.getInstance().log("Per BOSS - Alieno ROSSO id : " + soldato.id);
                Logic.getInstance().log("Membri scorta BOSS id " + boss.id + Arrays.toString(boss.getMembriScorta()));
                Logic.getInstance().log("Alieno ROSSO size : " + alienoRosso.size());
                Logic.getInstance().log("j : " + j);
                Logic.getInstance().log("alienoAttuale : " + alienoAttuale);
                Logic.getInstance().log("aggiunti : " + aggiunti);
                Logic.getInstance().log("count : " + count);
                Logic.getInstance().log("decrementaCountAlieniSchieramento - trova scorta BOSS : " + LAlieno.getCountAlieniSchieramento());

                if (alienoRosso.isEmpty()) {
                    isAlienoRossoEmpty = true;
                }

                if ((count == 1 && aggiunti == 1) || aggiunti == 2) {

                    if (this.numAlieniVivi <= 5) {
                        alienoAttuale++;
                    }

                    break;
                }

                alienoAttuale++;
            }
        }

        //Ci sono dei casi rari in cui può capitare di avere 5 alieni di cui, insieme, 2 BOSS con
        //due alieni ROSSO da una parte e 1 alieno ROSSO dall'altra. In questi casi si avrebbero due
        //animazioni (Costanti.BOSS_1_SCORTA e Costanti.BOSS_2_SCORTA) che si sovrappongono in base
        //all'ordine dei boss selezionati. In questi casi quindi è necessario che l'animazione sia
        //"la più bassa" ovvero Costanti.BOSS_1_SCORTA così da non creare problemi nella scelta dei
        //punti di attacco dei vari alieni
        if (numAnimazione != Costanti.BOSS_1_SCORTA) {
            int tmp_nuovaAnimazione = numAnimazione;

            switch (count) {
                case 1:
                    tmp_nuovaAnimazione = Costanti.BOSS_1_SCORTA;
                    break;
                case 2:
                case 3:
                    tmp_nuovaAnimazione = Costanti.BOSS_2_SCORTA;
                    break;
                default:
                    break;
            }

            //Devo togliere un alieno dalla scorta precedente
            if (numAnimazione == Costanti.BOSS_2_SCORTA && tmp_nuovaAnimazione == Costanti.BOSS_1_SCORTA) {
                int tmp_alienoAttuale;

                if (this.numAlieniVivi <= 5) {
                    Logic.getInstance().log("QUI 2324 -  numAlieniVivi <= 5");
                    tmp_alienoAttuale = alienoAttuale - 1;
                    Logic.getInstance().log("QUI 2324 -  tmp_alienoAttuale : " + tmp_alienoAttuale);
                } else {
                    tmp_alienoAttuale = alienoAttuale;
                    Logic.getInstance().log("QUI 2325 -  tmp_alienoAttuale : " + tmp_alienoAttuale);
                }

                //Ho messo + 1 perchè li devo scorrere tutti
                for (int i = 0; i < tmp_alienoAttuale + 1; i++) {
                    if (alieniAttacco[i].t == Costanti.TipoAlieno.BOSS && alieniAttacco[i].id != boss.id) {
                        LAlienoRosso scortaDaRimuovere = (LAlienoRosso) alieniAttacco[i + 2];
                        Logic.getInstance().log("Il soldato della PRECEDENTE scorta " + scortaDaRimuovere + " id " + scortaDaRimuovere.id + " è stato rimosso dalla scorta");
                        scortaDaRimuovere.sceltoPerAttacco = false;
                        LAlieno.incrementaCountAlieniSchieramento();
                        Logic.getInstance().log("incrementaCountAlieniSchieramento - tolta scorta da BOSS* : " + LAlieno.getCountAlieniSchieramento());
                        //scortaDaRimuovere.setFormazione(true);
                        scortaDaRimuovere.setScorta(0, -1);
                        ((LAlienoBoss) alieniAttacco[i]).setMembriScorta(1, null);
                        alienoRossoOpposto.add(scortaDaRimuovere);
                        Logic.getInstance().log("alieno rosso opposto size: " + alienoRossoOpposto.size());
                        isAlienoRossoOppostoEmpty = false;

                        //Devo far slittare tutti i posti
                        i = i + 2; //Posto scorta rimossa
                        Logic.getInstance().log("alieni in attacco - PRIMA SPOSTAMENTO: " + Arrays.toString(alieniAttacco));
                        for (; i < tmp_alienoAttuale; i++) {
                            alieniAttacco[i] = alieniAttacco[i + 1];
                        }
                        break;
                    }
                }

                alienoAttuale = tmp_alienoAttuale;
                Logic.getInstance().log("QUI 2326 -  tmp_alienoAttuale : " + tmp_alienoAttuale);
                Logic.getInstance().log("alieni in attacco - PRIMA: " + Arrays.toString(alieniAttacco));
                alieniAttacco[alienoAttuale] = null;

                if (this.numAlieniVivi > 5) {
                    alienoAttuale--;
                    Logic.getInstance().log("Ho eliminato una scorta dall'altro boss ed ho numAlieniVivi > 5  -  alienoAttuale : " + alienoAttuale);
                }

                Logic.getInstance().log("alieni in attacco - DOPO: " + Arrays.toString(alieniAttacco));

            }

            numAnimazione = tmp_nuovaAnimazione;
        } else {
            //Se siamo in BOSS_2_SCORTA ma non posso settarlo ho già BOSS_1_SCORTA, devo eliminare una scorta
            if (aggiunti == 2) {
                if (soldato != null) {
                    if (this.numAlieniVivi <= 5) {
                        alienoAttuale--;
                        alieniAttacco[alienoAttuale] = null;
                    } else {
                        alieniAttacco[alienoAttuale] = null;
                        alienoAttuale--;
                    }
                    soldato.sceltoPerAttacco = false;
                    LAlieno.incrementaCountAlieniSchieramento();
                    soldato.setScorta(0, -1);
                    boss.setMembriScorta(1, null);
                    alienoRosso.add(soldato);
                    isAlienoRossoEmpty = false;

                    Logic.getInstance().log("Il soldato di scorta " + soldato + " id " + soldato.id + " è stato rimosso dalla scorta");
                    Logic.getInstance().log("incrementaCountAlieniSchieramento - tolta scorta da BOSS** : " + LAlieno.getCountAlieniSchieramento());
                    Logic.getInstance().log("Attuali alieni in attacco: " + Arrays.toString(alieniAttacco));
                }
            }
        }

        return new int[]{(isAlienoRossoEmpty ? 1 : 0), alienoAttuale, (isAlienoRossoOppostoEmpty ? 1 : 0)};
    }

    public void scegliKamikaze() {
        int index = random.nextInt(5); //Valore tra 0 e 4
        int copyIndex = index;

        //Dato che non voglio che il kamikaze sia sempre il primo soldato, pesco a caso un'index ma se non
        //va bene per diventare kamikaze scorro l'intero array di attacco in cerca di un alieno adatto
        do {
            LAlieno soldato = alieniAttacco[index];

            if (soldato != null) {
                if (soldato.t == Costanti.TipoAlieno.BLU || soldato.t == Costanti.TipoAlieno.VIOLA) {
                    soldato.kamikaze = true;
                    break;
                }
            }

            index = (index + 1) % 5;
            if (index == copyIndex) {
                return;
            }

        } while (true);
    }

    public void resetCoordinateDiRientro() {
        Logic.getInstance().log("RESET coordinateDiRientro");
        for (int i = 0; i < alieni.length; i++) {
            for (int j = 0; j < alieni[0].length; j++) {
                if (alieni[i][j] != null) {
                    alieni[i][j].resetCoordinateDiRientro();
                }
            }
        }
    }

    public void initCurve() {
        String[] dx = utils.getProprietaCurvaDestra();
        String[] sx = utils.getProprietaCurvaSinistra();
        String[] ddx = utils.getProprietaSecondaCurvaDestra();
        String[] ssx = utils.getProprietaSecondaCurvaSinistra();
        String[] iskinSx = utils.getProprietaCambiaSkinSinistra();
        String[] iskinDx = utils.getProprietaCambiaSkinDestra();
        String[] pfas = utils.getProprietaPuntiFineAttaccoSinistra();
        String[] pfad = utils.getProprietaPuntiFineAttaccoDestra();
        String[] pal1 = utils.getProprietaPuntiAttaccoLivello1();
        String[] pal2 = utils.getProprietaPuntiAttaccoLivello2();
        String[] pal3 = utils.getProprietaPuntiAttaccoLivello3();

        this.curvaSx = new int[sx.length];
        this.curvaDx = new int[dx.length];
        this.secondaCurvaDx = new int[ddx.length];
        this.secondaCurvaSx = new int[ssx.length];
        this.arrayIndexCambiaSkinSinistra = new int[iskinSx.length];
        this.arrayIndexCambiaSkinDestra = new int[iskinDx.length];
        this.puntiFineAttaccoSinistra = new int[pfas.length];
        this.puntiFineAttaccoDestra = new int[pfad.length];
        this.puntiAttaccoLivello1 = new int[pal1.length];
        this.puntiAttaccoLivello2 = new int[pal2.length];
        this.puntiAttaccoLivello3 = new int[pal3.length];
        this.diametroCurvaSinistra = Integer.parseInt(utils.getProprietaDiametroCurvaSinistra());
        this.diametroCurvaDestra = Integer.parseInt(utils.getProprietaDiametroCurvaDestra());
        this.numeroPuntiFalsaCurvaSinistra = Integer.parseInt(utils.getProprietaNumeroPuntiFalsaCurvaSinistra());
        this.numeroPuntiFalsaCurvaDestra = Integer.parseInt(utils.getProprietaNumeroPuntiFalsaCurvaDestra());

        //Ho messo questi controlli solo nel caso in cui volessimo cambiare 
        //prima o poi qualcosa alle curve
        if (curvaSx.length == curvaDx.length) {
            for (int i = 0; i < curvaSx.length; i++) {
                this.curvaSx[i] = Integer.parseInt(sx[i]);
                this.curvaDx[i] = Integer.parseInt(dx[i]);
            }
        } else {
            for (int i = 0; i < curvaSx.length; i++) {
                this.curvaSx[i] = Integer.parseInt(sx[i]);
            }

            for (int i = 0; i < curvaDx.length; i++) {
                this.curvaDx[i] = Integer.parseInt(dx[i]);
            }
        }

        if (iskinSx.length == iskinDx.length) {
            for (int i = 0; i < iskinSx.length; i++) {
                this.arrayIndexCambiaSkinSinistra[i] = Integer.parseInt(iskinSx[i]);
                this.arrayIndexCambiaSkinDestra[i] = Integer.parseInt(iskinDx[i]);
            }
        } else {
            for (int i = 0; i < iskinSx.length; i++) {
                this.arrayIndexCambiaSkinSinistra[i] = Integer.parseInt(iskinSx[i]);
            }

            for (int i = 0; i < iskinDx.length; i++) {
                this.arrayIndexCambiaSkinDestra[i] = Integer.parseInt(iskinDx[i]);
            }
        }

        if (secondaCurvaSx.length == secondaCurvaDx.length) {
            for (int i = 0; i < secondaCurvaDx.length; i++) {
                secondaCurvaSx[i] = Integer.parseInt(ssx[i]);
                secondaCurvaDx[i] = Integer.parseInt(ddx[i]);
            }
        } else {
            for (int i = 0; i < secondaCurvaSx.length; i++) {
                secondaCurvaSx[i] = Integer.parseInt(ssx[i]);
            }

            for (int i = 0; i < secondaCurvaDx.length; i++) {
                secondaCurvaDx[i] = Integer.parseInt(ddx[i]);
            }
        }

        if (puntiFineAttaccoSinistra.length == puntiFineAttaccoDestra.length) {
            for (int i = 0; i < puntiFineAttaccoSinistra.length; i++) {
                puntiFineAttaccoSinistra[i] = Integer.parseInt(pfas[i]);
                puntiFineAttaccoDestra[i] = Integer.parseInt(pfad[i]);
            }
        } else {
            for (int i = 0; i < puntiFineAttaccoSinistra.length; i++) {
                puntiFineAttaccoSinistra[i] = Integer.parseInt(pfas[i]);
            }

            for (int i = 0; i < puntiFineAttaccoDestra.length; i++) {
                puntiFineAttaccoDestra[i] = Integer.parseInt(pfad[i]);
            }
        }

        //puntiAttaccoLivello1.length == puntiAttaccoLivello2.length == puntiAttaccoLivello3.length
        for (int i = 0; i < puntiAttaccoLivello1.length; i++) {
            puntiAttaccoLivello1[i] = Integer.parseInt(pal1[i]);
            puntiAttaccoLivello2[i] = Integer.parseInt(pal2[i]);
            puntiAttaccoLivello3[i] = Integer.parseInt(pal3[i]);
        }

        Logic.getInstance().log(this.toString());
    }

    @Override
    public void setMusica(boolean scelta) {
        this.musica = scelta;
    }

    @Override
    public boolean getMusica() {
        return this.musica;
    }

    @Override
    public void setMusicaBackground(boolean scelta) {
        this.musicaBackground = scelta;
    }

    @Override
    public boolean getMusicaBackground() {
        return this.musicaBackground;
    }

    public LColpoAlieno[] getColpi() {
        return colpiAlieni;
    }

    public LAlieno[][] getNavette() {
        return alieni;
    }

    public LNavGiocatore getNaveGiocatore() {
        return giocatore;
    }

    @Override
    public int getPosizioneGiocatore() {
        return this.giocatore.getXPos();
    }

    int holdAnim = 0;
    int holdStep = 0;

    @Override
    public void avanzaAnimazioni() {

        //Avanzamento colpi alieni
        for (int i = 0; i < colpiAlieni.length; i++) {
            if (colpiAlieni[i].isVisibile()) {
                colpiAlieni[i].muoviti();
                colpiAlieni[i].checkTarget();
            }
        }

        //Avanzamento colpo giocatore        
        if (colpoGiocatore.isVisibile()) {
            colpoGiocatore.muoviti();
            colpoGiocatore.checkTarget();
        }

        
        //Check collisione tra alieno e giocatore
        for (int i = 0; i < Costanti.ROWS; i++) {
            for (int j = 0; j < Costanti.COLS; j++) {
                LAlieno a = alieni[i][j];
                if (a != null && a.isVisibile()) {
                    if (!a.navettaInFormazione()) {
                        if (giocatore.isVisibile() && a.checkCollisione(giocatore)) {
                            giocatore.riceviColpo();
                            a.riceviColpo();
                        }
                    }
                }
            }
        }

        //Avanzamento animazioni alieni
        for (int w = 0; countOrdineAlieniAttacco < alieniAttacco.length;) {

            Logic.getInstance().log("***************************************************************");

            LAlieno soldato = alieniAttacco[countOrdineAlieniAttacco];

            if (soldato != null) {
                Logic.getInstance().log("Alieno " + soldato.t + " , id : " + soldato.id + " , visibile: " + soldato.isVisibile() + " , formazione: " + soldato.navettaInFormazione() + " , inRientro: " + soldato.inRientro);
                if (soldato.t == Costanti.TipoAlieno.ROSSO) {
                    LAlienoRosso rosso = (LAlienoRosso) soldato;
                    Logic.getInstance().log("E' una scorta: " + (rosso.getScorta())[0] + " per Boss id " + (rosso.getScorta())[1]);
                }
            } else {
                Logic.getInstance().log("Alieno == null");
            }

            //Se il giocatore è morto o è una scorta messa in rientro o è un alieno che è uscito dallo schermo
            //incremento un contatore e se il contatore diventa uguale al numero alieni in attacco allora chiamo
            //la scelta di nuovi alieni da mettere in attacco
            if (soldato == null || !soldato.visibile || soldato.inRientro || cercaESettaFineAttaccoAlieno(soldato)) {
                w++;

                if (numAlieniAttacco == 0) {
                    countOrdineAlieniAttacco = (countOrdineAlieniAttacco + 1) % alieniAttacco.length;
                } else {
                    countOrdineAlieniAttacco = (countOrdineAlieniAttacco + 1) % numAlieniAttacco;
                }

                Logic.getInstance().log("w = " + w);
                Logic.getInstance().log("numAlieniAttacco = " + numAlieniAttacco);
                Logic.getInstance().log("countOrdineAlieniAttacco = " + countOrdineAlieniAttacco);

                if (w == numAlieniAttacco || w == alieniAttacco.length) {
                    this.sceltaAlieniAttacco();
                    break;
                } else if (countOrdineAlieniAttacco == 0) {
                    break;
                }
            } //Se non è NULL, se è VIVO, se non è una scorta in rientro e se è nello schermo
            else {
                //Se l'alieno è stato scelto per l'attacco, aspetto un minimo per farlo partire così da
                //scaglionare la partenza dei vari alieni invece di mandarli tutti insieme
                long tempoAttuale = System.currentTimeMillis();
                int attesa = Costanti.TEMPO_MINIMO_ATTESA + random.nextInt(Costanti.TEMPO_MASSIMO_ATTESA - Costanti.TEMPO_MINIMO_ATTESA);
                int livello = this.livello.getLivello();

                LAlieno primoAlienoAttacco = getPrimoAlienoAttacco();

                if (primoAlienoAttacco != null) {
                    Logic.getInstance().log("LOGIC -- Primo alieno: " + primoAlienoAttacco.t + " id: " + primoAlienoAttacco.id);
                } else {
                    Logic.getInstance().log("LOGIC -- Primo alieno: NULL");
                }

                Logic.getInstance().log("lasciapassarePrimoAlienoAttacco: " + lasciapassarePrimoAlienoAttacco);

                if (soldato == primoAlienoAttacco || lasciapassarePrimoAlienoAttacco) {
                    if (soldato == primoAlienoAttacco) {
                        //Alieno non ancora partito in attacco
                        if (soldato.hold2 < 1) {
                            lasciapassarePrimoAlienoAttacco = true;

                            //Fa partire anche la scorta se è un BOSS
                            iniziaAttaccoAlieno(soldato, livello);

                            //AGGIORNO tempoPrecedente dato che potrei aver eseguito più attaccoAnim per le scorte
                            tempoPrecedente = System.currentTimeMillis();
                            Logic.getInstance().log("Attacco con alieno " + soldato.t + " con id: " + soldato.id);
                            int numMembriScorta = soldato.attaccoAnim(livello, soldato.direzioneAttacco);

                            countOrdineAlieniAttacco += numMembriScorta;
                            countOrdineAlieniAttacco = (countOrdineAlieniAttacco + 1) % numAlieniAttacco;

                            if (countOrdineAlieniAttacco == 0) {
                                break;
                            }

                        } else { //hold2 > 1  --  Soldato già partito per l'attacco

                            int numMembriScorta = soldato.attaccoAnim(livello, soldato.direzioneAttacco);

                            countOrdineAlieniAttacco += numMembriScorta;
                            countOrdineAlieniAttacco = (countOrdineAlieniAttacco + 1) % numAlieniAttacco;

                            if (countOrdineAlieniAttacco == 0) {
                                break;
                            }
                        }
                    } else { //Per gli altri alieni
                        if (soldato.hold2 < 1 && tempoAttuale > (tempoPrecedente + attesa)) {

                            //Se sono una scorta non dovrei mai entrare qui dato che vengo eseguito insieme al BOSS
                            //quindi prendo e mando avanti il ciclo for
                            if (soldato.t == Costanti.TipoAlieno.ROSSO) {
                                if ((((LAlienoRosso) soldato).getScorta())[0] == 1) {
                                    countOrdineAlieniAttacco = (countOrdineAlieniAttacco + 1) % numAlieniAttacco;
                                    Logic.getInstance().log("Una scorta ha tentato di partire in attacco prima del BOSS");

                                    if (countOrdineAlieniAttacco == 0) {
                                        break;
                                    } else {
                                        continue;
                                    }
                                }
                            }

                            //Fa partire anche la scorta se è un BOSS
                            iniziaAttaccoAlieno(soldato, livello);

                            //AGGIORNO tempoPrecedente dato che potrei aver eseguito più attaccoAnim per le scorte
                            tempoPrecedente = System.currentTimeMillis();
                            int numMembriScorta = soldato.attaccoAnim(livello, soldato.direzioneAttacco);
                            countOrdineAlieniAttacco += numMembriScorta;
                            countOrdineAlieniAttacco = (countOrdineAlieniAttacco + 1) % numAlieniAttacco;

                            if (countOrdineAlieniAttacco == 0) {
                                break;
                            }

                        } else if (soldato.hold2 > 1) {

                            int numMembriScorta = soldato.attaccoAnim(livello, soldato.direzioneAttacco);

                            countOrdineAlieniAttacco += numMembriScorta;
                            countOrdineAlieniAttacco = (countOrdineAlieniAttacco + 1) % numAlieniAttacco;

                            if (countOrdineAlieniAttacco == 0) {
                                break;
                            }

                        } else { //Non è ancora passato il tempo necessario per far partire l'alieno

                            //Logic.getInstance().log("Non è ancora passato il tempo necessario per l'alieno " + soldato.t + " id: " + soldato.id);
                            countOrdineAlieniAttacco = (countOrdineAlieniAttacco + 1) % numAlieniAttacco;

                            if (countOrdineAlieniAttacco == 0) {
                                break;
                            }

                        }
                    }
                } else { //Attendo di trovare il primo alieno
                    countOrdineAlieniAttacco = (countOrdineAlieniAttacco + 1) % numAlieniAttacco;

                    if (countOrdineAlieniAttacco == 0) {
                        break;
                    }
                }
            }
        }


        //Movimento battaglione schieramento
        boolean cambioDir = false;
        if (holdAnim == 0) {
            for (int i = 0; i < Costanti.ROWS; i++) {
                for (int j = 0; j < Costanti.COLS; j++) {
                    if (alieni[i][j] != null && alieni[i][j].isVisibile()) {
                        //Quando gli alieni stanno ritornando in schieramento hanno settato a TRUE sia formazione che sceltoPerAttacco che partitoInAttacco
                        if (alieni[i][j].navettaInFormazione()) {
                            if (alieni[i][j].movimentoGruppo()) {
                                cambioDir = true;
                            }
                            if (holdStep == 0) {
                                String s = alieni[i][j].cambiaSkin(Costanti.SCHIERAMENTO, 0);
                                Logic.getInstance().log("cambiaSkin in logic su movimentoGruppo: " + s + "  --  alieno " + alieni[i][j].t + " con id: " + alieni[i][j].id);

                            }
                        } else if (alieni[i][j].sceltoPerAttacco) {
                            //Serve a mantenere la posizione di rientro in schieramento
                            alieni[i][j].fintoMovimentoGruppo();
                            if (holdStep == 0) {
                                String s = alieni[i][j].cambiaSkin(Costanti.RIENTRO, 0);
                                Logic.getInstance().log("DOPO - cambiaSkin in logic su fintoMovimentoGruppo: " + s + "  --  alieno " + alieni[i][j].t + " con id: " + alieni[i][j].id);
                            }
                        }
                    }
                }
            }
        }
        if (cambioDir) {
            LAlieno.cambiaDir();
        }

        holdAnim = (holdAnim + 1) % Costanti.MODULO_HOLD_ANIM;
        holdStep = (holdStep + 1) % Costanti.MODULO_HOLD_STEP;

        
        
        //Ritorno alla formazione/Schieramento per alieni in attacco sopravvissuti
        for (int i = 0; i < alieniFineAttacco.length; i++) {
            LAlieno soldato = alieniFineAttacco[i];
            if (soldato != null) {
                if (soldato.isVisibile()) {
                    Logic.getInstance().log("--------------------------------------------------------------------------------------");
                    Logic.getInstance().log("Alieno " + soldato.t + " , id : " + soldato.id + " , visibile: " + soldato.isVisibile() + " , formazione: " + soldato.navettaInFormazione() + " , sceltoPerAttacco: " + soldato.sceltoPerAttacco + " , Xpos: " + soldato.getXPos());
                    Logic.getInstance().log("countAlieniSchieramento : " + LAlieno.getCountAlieniSchieramento());

                    if (soldato.getPrimoStepRientro()) {
                        setPosizioneRientro(soldato, false);
                    }

                    LAlieno[] scorta = null;
                    if (soldato.t == Costanti.TipoAlieno.BOSS) {
                        LAlienoBoss boss = (LAlienoBoss) soldato;
                        scorta = boss.getMembriScorta();
                    }

                    //CONTROLLO DI SICUREZZA in caso si sia già passati tutto lo schermo senza trovare posto
                    //Faccio ripartire dall'inizio il rientro degli alieni
                    if (soldato.t == Costanti.TipoAlieno.BOSS) {
                        boolean reset = false;

                        for (int j = 0; j < scorta.length; j++) {
                            if (scorta[j] != null && scorta[j].isVisibile()) {
                                int posAttualeScorta = scorta[j].getXPos();

                                if (soldato.id == 0) {
                                    if (posAttualeScorta < soldato.getXPos()) {
                                        if (posAttualeScorta > Costanti.LARGHEZZA) {
                                            setPosizioneRientro(soldato, false);
                                            Logic.getInstance().log("WARNING: La scorta " + soldato.t + " con id " + soldato.id + " ha percorso tutto lo schermo senza trovare posto. RESET COORDINATE");
                                            reset = true;
                                            break;
                                        }
                                    }
                                } else {
                                    if (posAttualeScorta > soldato.getXPos()) {
                                        if (posAttualeScorta < (Costanti.SPAZIO_LARGHEZZA_ALIENO * -1)) {
                                            setPosizioneRientro(soldato, false);
                                            Logic.getInstance().log("WARNING: La scorta " + soldato.t + " con id " + soldato.id + " ha percorso tutto lo schermo senza trovare posto. RESET COORDINATE");
                                            reset = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if (!reset) {
                            if (soldato.id == 0) {
                                if (soldato.getXPos() > Costanti.LARGHEZZA) {
                                    setPosizioneRientro(soldato, false);
                                    Logic.getInstance().log("WARNING: L'alieno " + soldato.t + " con id " + soldato.id + " ha percorso tutto lo schermo senza trovare posto. RESET COORDINATE");
                                }
                            } else {
                                if (soldato.getEndXPos() < 0) {
                                    setPosizioneRientro(soldato, false);
                                }
                            }
                        }
                    } else {
                        if (soldato.id <= 4 && soldato.getXPos() > Costanti.LARGHEZZA) {
                            Logic.getInstance().log("WARNING: L'alieno " + soldato.t + " con id " + soldato.id + " ha percorso tutto lo schermo senza trovare posto. RESET COORDINATE");
                            setPosizioneRientro(soldato, false);
                        } else if (soldato.id > 4 && soldato.getEndXPos() < 0) {
                            setPosizioneRientro(soldato, false);
                        }
                    }

                    /*
                    * Se non ci sono più alieni in schieramento ed ho degli alieni in rientro si potrebbe avere che la
                    * loro posFinale è fuori dallo schermo e non rientrerebbero mai. Per questo, in questi casi, viene 
                    * attivata una funzione che fa il reset delle coordinate di rientro riportando tutte le posFinali
                    * dentro lo schermo
                    */
                    if (LAlieno.getCountAlieniSchieramento() == 0) {
                        resetCoordinateDiRientro();
                    }
                    
                    
                    int posAttuale = soldato.getXPos();
                    int posFinale = (soldato.getCoordinateDiRientro())[0];

                    Logic.getInstance().log("LOGIC, posAttuale per alieno " + soldato.t + " con id: " + soldato.id + " è: " + posAttuale);
                    Logic.getInstance().log("LOGIC, posFinale per alieno " + soldato.t + " con id: " + soldato.id + " è: " + posFinale);

                    //Se l'alieno ha raggiunto la sua posizione di rientro
                    if (posAttuale == posFinale
                            || (posAttuale >= (posFinale - Costanti.STEP_ANIM)
                            && posAttuale <= (posFinale + Costanti.STEP_ANIM))) {

                        /*
                        * Potrebbe succedere che se l'alieno ha la scorta e il suo puntoFinale si trova 
                        * nei pressi dei lati dello schermo, una delle sue scorte (con Xpos > o < di quella
                        * del boss) potrebbe risultare parzialmente o totalmente fuori dallo schermo e in quel
                        * caso l'alieno non può essere messo in formazione sennò bloccherebbe l'avanzamento 
                        * dell'intero schieramento. A questo punto gli faccio saltare il turno.
                        *
                        * Stessa cosa vale per gli alieni "singoli" se sono tagliati sullo schermo
                        */
                        
                        /*
                        * Potrebbe succedere che se ci sono poco più di 5 alieni e l'alieno entra con un verso 
                        * opposto a quello dello schieramento, il puntoFinale si potrebbe trovare fuori dallo 
                        * schermo, dalla parte di rientro dell'alieno, e quindi dobbiamo aspettare finchè
                        * la posFinale rientra nello schermo
                        */

                        if (posFinale <= 0 || posFinale >= Costanti.LARGHEZZA - Costanti.SPAZIO_LARGHEZZA_ALIENO) {
                            continue; //Passo al prossimo alieno in rientro
                        } else if (soldato.t == Costanti.TipoAlieno.BOSS) {

                            boolean fuoriSchermo = false;
                            for (int j = 0; j < scorta.length; j++) {
                                if (scorta[j] != null && scorta[j].isVisibile()) {
                                    int posFinaleScorta = (scorta[j].getCoordinateDiRientro())[0];

                                    Logic.getInstance().log("posFinaleScorta per id: " + scorta[j].id + " è: " + posFinaleScorta);

                                    if (posFinaleScorta <= 0 || posFinaleScorta >= Costanti.LARGHEZZA - Costanti.SPAZIO_LARGHEZZA_ALIENO) {
                                        fuoriSchermo = true;
                                        break;
                                    }
                                }
                            }

                            if (fuoriSchermo) {
                                continue;
                            }
                        }

                        Logic.getInstance().log("LOGIC, SONO DENTRO IF 00001");
                        soldato.setXPos(posFinale);
                        alieniFineAttacco[i] = null;

                        if (soldato.t == Costanti.TipoAlieno.BOSS) {

                            for (int j = 0; j < scorta.length; j++) {
                                if (scorta[j] != null && scorta[j].isVisibile()) {
                                    scorta[j].resetAlienoFineRientro();
                                    LAlieno.incrementaCountAlieniSchieramento();
                                    Logic.getInstance().log("incrementaCountAlieniSchieramento per fine rientro scorta: " + LAlieno.getCountAlieniSchieramento());
                                }
                            }
                        }

                        soldato.resetAlienoFineRientro();
                        LAlieno.incrementaCountAlieniSchieramento();
                        Logic.getInstance().log("incrementaCountAlieniSchieramento per fine rientro: " + LAlieno.getCountAlieniSchieramento());

                    } else { //ancora non ho raggiunto la posizione finale

                        Logic.getInstance().log("LOGIC, SONO DENTRO IF 00002 - PT1");

                        if (soldato.t == Costanti.TipoAlieno.BOSS) {

                            if (soldato.id == 0) {
                                soldato.setXPos(posAttuale + Costanti.STEP_ANIM);
                            } else {
                                soldato.setXPos(posAttuale - Costanti.STEP_ANIM);
                            }

                            for (int j = 0; j < scorta.length; j++) {
                                if (scorta[j] != null && scorta[j].isVisibile()) {
                                    if (soldato.id == 0) {
                                        scorta[j].setXPos(scorta[j].getXPos() + Costanti.STEP_ANIM);
                                    } else {
                                        scorta[j].setXPos(scorta[j].getXPos() - Costanti.STEP_ANIM);
                                    }
                                }
                            }
                        } else {
                            if (soldato.id <= 4) {
                                soldato.setXPos(posAttuale + Costanti.STEP_ANIM);
                            } else {
                                soldato.setXPos(posAttuale - Costanti.STEP_ANIM);
                            }
                        }
                    }

                } else { //E' stato ucciso mentre ritornava in posizione
                    soldato.setInRientro(false);
                    alieniFineAttacco[i] = null;

                    if (soldato.t == Costanti.TipoAlieno.BOSS) {

                        //Inserisco gli alieni rossi al posto del boss in alieniFineAttacco
                        LAlienoBoss boss = (LAlienoBoss) soldato;
                        LAlieno[] scorta = boss.getMembriScorta();

                        int z = 0;
                        for (int j = 0; j < scorta.length; j++) {
                            if (scorta[j] != null && scorta[j].isVisibile()) {
                                for (; z < alieniFineAttacco.length; z++) {
                                    if (alieniFineAttacco[z] == null) {
                                        alieniFineAttacco[z] = scorta[j];
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        Logic.getInstance().log("\n\n\n\nFUORI AVANZA COLPI\n\n\n\n");

    }


    /**
     * Serve a posizionare gli alieni che sono usciti dallo schermo nella posizione più vicina
     * al loro punto di rientro (ma pur sempre fuori dallo schermo)
     * 
     * @param attaccoDiretto TRUE se alieno non deve ritornare in schieramento/formazione
     */
    public void setPosizioneRientro(LAlieno soldato, boolean attaccoDiretto) {
        if (soldato.t == Costanti.TipoAlieno.BOSS) {
            LAlienoBoss boss = (LAlienoBoss) soldato;
            LAlieno[] scorta = boss.getMembriScorta();

            boss.resetPathAnimazione();

            int[] tmpPosXscorta = new int[]{0, 0};

            for (int j = 0; j < scorta.length; j++) {
                if (scorta[j] != null && scorta[j].isVisibile()) {
                    scorta[j].resetPathAnimazione();
                    tmpPosXscorta[j] = boss.calcolaMargine(scorta[j], this);
                }
            }

            //Trovo il min e il max per poter settare correttamente la posizione che devono 
            //avere boss e scorta al momento del rientro
            int min, max;

            if (tmpPosXscorta[0] <= tmpPosXscorta[1]) {
                min = tmpPosXscorta[0]; // -> indexScortaMin = 0
                max = tmpPosXscorta[1]; // -> indexScortaMax = 1

                setPosizioneRientroBossEScorta(boss, max, min, 1, 0, scorta, attaccoDiretto);

            } else {
                min = tmpPosXscorta[1]; // -> indexScortaMin = 1
                max = tmpPosXscorta[0]; // -> indexScortaMax = 0

                setPosizioneRientroBossEScorta(boss, max, min, 0, 1, scorta, attaccoDiretto);
            }
        } else {
            soldato.resetPathAnimazione();

            int margine = 0;
            //Li faccio partire da uno step dentro lo schermo se devono ripartire subito per l'attacco
            //Questo step in dentro garantisce che l'alieno non venga riconosciuto come "fuori dallo schermo"
            //nella funzione cercaEsettaFineAttaccoAlieno e permette di continuare ad attaccare
            if (numAlieniVivi <= 5) {
                margine = Costanti.STEP_ANIM;
            }

            if (soldato.id <= 4) //Entro da sinistra
            {
                soldato.setXPos((view.getDimensioneImmagine(soldato.pathAnimazione.toString())[1]) * -1 + margine);
            } else { //Entro da destra
                soldato.setXPos(Costanti.LARGHEZZA - margine);
            }
        }

        soldato.setYPos((soldato.getCoordinateDiRientro())[1]);
        Logic.getInstance().log("LOGIC, coordinateDiRientro per alieno " + soldato.t + " con id: " + soldato.id + " sono:\nX: " + soldato.getXPos() + "  Y: " + (soldato.getCoordinateDiRientro())[1]);
        soldato.setPrimoStepRientro(false);
    }

    private void setPosizioneRientroBossEScorta(LAlienoBoss boss, int max, int min, int indexScortaMax, int indexScortaMin, LAlieno[] scorta, boolean attaccoDiretto) {

        Logic.getInstance().log("Sono dentro setPosizioneRientroBossEScorta con max: " + max + " , min: " + min + " , indexScortaMax: " + indexScortaMax + " , indexScortaMin: " + indexScortaMin);

        int margine = 0;

        //Li faccio partire da uno step dentro se devono ripartire subito per l'attacco
        if (numAlieniVivi <= 5) {
            margine = Costanti.STEP_ANIM;
        }

        if (boss.id == 0) {
            if (max > 0) {

                Logic.getInstance().log("Sono in BOSS id 0 e max > 0");

                int posXscorta1 = max * -1 + margine;
                corpoSetPosizioneRientroBossEScorta(scorta[indexScortaMax], posXscorta1, (scorta[indexScortaMax].getCoordinateDiRientro())[1]); 
                
                Logic.getInstance().log("settata posX: " + posXscorta1 + " per scorta id " + scorta[indexScortaMax].id);
                int posXboss;
                if (attaccoDiretto) {
                    posXboss = ((view.getDimensioneImmagine(boss.pathAnimazione.toString())[1]) * -1) + posXscorta1;
                } else {
                    posXboss = Costanti.SPAZIO_LARGHEZZA_ALIENO * -1 + posXscorta1;
                }

                boss.setXPos(posXboss);
                Logic.getInstance().log("settata posX: " + posXboss + " per boss id " + boss.id);
                //La sua posizione può essere solo o negativa o uguale a 0
                if (scorta[indexScortaMin] != null) {
                    if (attaccoDiretto) {
                        scorta[indexScortaMin].setXPos(posXboss + (min == 0 ? 0 : ((view.getDimensioneImmagine(scorta[indexScortaMin].pathAnimazione.toString())[1]) * -1)));
                    } else {
                        scorta[indexScortaMin].setXPos(posXboss + (min == 0 ? 0 : Costanti.SPAZIO_LARGHEZZA_ALIENO * -1));
                    }

                    scorta[indexScortaMin].setYPos((scorta[indexScortaMin].getCoordinateDiRientro())[1]);
                    scorta[indexScortaMin].setPrimoStepRientro(false);
                    Logic.getInstance().log("settata posX: " + scorta[indexScortaMin].getXPos() + " per scorta id " + scorta[indexScortaMin].id);
                }
            } else {

                Logic.getInstance().log("Sono in BOSS id 0 e max <= 0");

                int posXboss = (view.getDimensioneImmagine(boss.pathAnimazione.toString())[1]) * -1 + margine;
                boss.setXPos(posXboss);

                //Se < 0 potrei avere 2 scorte, altrimenti solo una o nessuna perchè non possono stare entrambe in 0
                if (min < 0) {

                    Logic.getInstance().log("Sono in BOSS id 0 e max <= 0 , min < 0");

                    if (attaccoDiretto) {
                        scorta[indexScortaMin].setXPos(((view.getDimensioneImmagine(scorta[indexScortaMin].pathAnimazione.toString())[1]) * -1) + posXboss);
                    } else {
                        scorta[indexScortaMin].setXPos(Costanti.SPAZIO_LARGHEZZA_ALIENO * -1 + posXboss);
                    }

                    scorta[indexScortaMin].setYPos((scorta[indexScortaMin].getCoordinateDiRientro())[1]);
                    scorta[indexScortaMin].setPrimoStepRientro(false);
                    Logic.getInstance().log("settata posX: " + scorta[indexScortaMin].getXPos() + " per scorta id " + scorta[indexScortaMin].id);

                    //max = 0 -> Stessa posizione del boss
                    if (scorta[indexScortaMax] != null) {
                        corpoSetPosizioneRientroBossEScorta(scorta[indexScortaMax], posXboss, (scorta[indexScortaMax].getCoordinateDiRientro())[1]); 
                        Logic.getInstance().log("settata posX: " + scorta[indexScortaMax].getXPos() + " per scorta id " + scorta[indexScortaMax].id);
                    }
                } else {

                    Logic.getInstance().log("Sono in BOSS id 0 e max <= 0 , min >= 0");

                    //min e max = 0 quindi uno dei due o entrambi non esistono
                    if (scorta[indexScortaMax] != null) {
                        corpoSetPosizioneRientroBossEScorta(scorta[indexScortaMax], posXboss, (scorta[indexScortaMax].getCoordinateDiRientro())[1]); 
                        Logic.getInstance().log("settata posX: " + scorta[indexScortaMax].getXPos() + " per scorta id " + scorta[indexScortaMax].id);

                    } else if (scorta[indexScortaMin] != null) {
                        corpoSetPosizioneRientroBossEScorta(scorta[indexScortaMin], posXboss, (scorta[indexScortaMin].getCoordinateDiRientro())[1]); 
                        Logic.getInstance().log("settata posX: " + scorta[indexScortaMin].getXPos() + " per scorta id " + scorta[indexScortaMin].id);
                    }
                }
            }

        } else { //boss.id == 1
            if (min < 0) {

                Logic.getInstance().log("Sono in BOSS id 1 e min < 0");

                int posXscorta0 = Costanti.LARGHEZZA - margine;
                corpoSetPosizioneRientroBossEScorta(scorta[indexScortaMin], posXscorta0, (scorta[indexScortaMin].getCoordinateDiRientro())[1]); 
                Logic.getInstance().log("settata posX: " + scorta[indexScortaMin].getXPos() + " per scorta id " + scorta[indexScortaMin].id);

                int posXboss;
                if (attaccoDiretto) {
                    posXboss = (view.getDimensioneImmagine(boss.pathAnimazione.toString())[1]) + posXscorta0;
                } else {
                    posXboss = Costanti.SPAZIO_LARGHEZZA_ALIENO + posXscorta0;
                }

                boss.setXPos(posXboss);
                //La sua posizione può essere solo o positiva o uguale a 0
                if (scorta[indexScortaMax] != null) {
                    if (attaccoDiretto) {
                        scorta[indexScortaMax].setXPos(posXboss + (max == 0 ? 0 : (view.getDimensioneImmagine(scorta[indexScortaMax].pathAnimazione.toString())[1])));
                    } else {
                        scorta[indexScortaMax].setXPos(posXboss + (max == 0 ? 0 : Costanti.SPAZIO_LARGHEZZA_ALIENO));
                    }

                    scorta[indexScortaMax].setYPos((scorta[indexScortaMax].getCoordinateDiRientro())[1]);
                    scorta[indexScortaMax].setPrimoStepRientro(false);
                    Logic.getInstance().log("settata posX: " + scorta[indexScortaMax].getXPos() + " per scorta id " + scorta[indexScortaMax].id);
                }
            } else {

                Logic.getInstance().log("Sono in BOSS id 1 e min >= 0");

                int posXboss = Costanti.LARGHEZZA - margine;
                boss.setXPos(posXboss);

                //Se > 0 potrei avere 2 scorte, altrimenti solo una o nessuna perchè non possono stare entrambe in 0
                if (max > 0) {

                    Logic.getInstance().log("Sono in BOSS id 1 e min >= 0 , max > 0");

                    if (attaccoDiretto) {
                        scorta[indexScortaMax].setXPos((view.getDimensioneImmagine(scorta[indexScortaMax].pathAnimazione.toString())[1]) + posXboss);
                    } else {
                        scorta[indexScortaMax].setXPos(Costanti.SPAZIO_LARGHEZZA_ALIENO + posXboss);
                    }

                    scorta[indexScortaMax].setYPos((scorta[indexScortaMax].getCoordinateDiRientro())[1]);
                    scorta[indexScortaMax].setPrimoStepRientro(false);
                    Logic.getInstance().log("settata posX: " + scorta[indexScortaMax].getXPos() + " per scorta id " + scorta[indexScortaMax].id);

                    //min = 0 -> Stessa posizione del boss
                    if (scorta[indexScortaMin] != null) {
                        corpoSetPosizioneRientroBossEScorta(scorta[indexScortaMin], posXboss, (scorta[indexScortaMin].getCoordinateDiRientro())[1]); 
                        Logic.getInstance().log("settata posX: " + scorta[indexScortaMin].getXPos() + " per scorta id " + scorta[indexScortaMin].id);
                    }
                } else {

                    Logic.getInstance().log("Sono in BOSS id 1 e min >= 0 , max <= 0");

                    //min e max = 0 quindi uno dei due o entrambi non esistono
                    if (scorta[indexScortaMax] != null) {
                        corpoSetPosizioneRientroBossEScorta(scorta[indexScortaMax], posXboss, (scorta[indexScortaMax].getCoordinateDiRientro())[1]); 
                        Logic.getInstance().log("settata posX: " + scorta[indexScortaMax].getXPos() + " per scorta id " + scorta[indexScortaMax].id);

                    } else if (scorta[indexScortaMin] != null) {
                        corpoSetPosizioneRientroBossEScorta(scorta[indexScortaMin], posXboss, (scorta[indexScortaMin].getCoordinateDiRientro())[1]); 
                        Logic.getInstance().log("settata posX: " + scorta[indexScortaMin].getXPos() + " per scorta id " + scorta[indexScortaMin].id);
                    }
                }
            }
        }
    }
    
    private void corpoSetPosizioneRientroBossEScorta(LAlieno soldato, int posX, int posY) {
        soldato.setXPos(posX);
        soldato.setYPos(posY);
        soldato.setPrimoStepRientro(false);
    }

    public LAlieno muoviScorta(LAlieno soldato, int livello) {
        if (numAnimazione == Costanti.BOSS_1_SCORTA) {
            if (soldato.t == Costanti.TipoAlieno.BOSS) {

                LAlienoBoss boss = (LAlienoBoss) soldato;
                LAlieno membroScorta = (boss.getMembriScorta())[0];

                if (membroScorta != null && membroScorta.isVisibile()) {
                    Logic.getInstance().log("Sono in BOSS_1_SCORTA con alieno " + soldato.t + " con id: " + soldato.id);
                    soldato.attaccoAnim(livello, soldato.direzioneAttacco);
                    countOrdineAlieniAttacco++;
                    soldato = membroScorta;
                } else {
                    Logic.getInstance().log("Sono in BOSS_1_SCORTA con BOSS senza scorta");
                }
            }
        } else if (numAnimazione == Costanti.BOSS_2_SCORTA) {
            if (soldato.t == Costanti.TipoAlieno.BOSS) {

                LAlienoBoss boss = (LAlienoBoss) soldato;
                LAlieno[] membriScorta = boss.getMembriScorta();

                for (int s = 0; s < membriScorta.length; s++) {
                    if (membriScorta[s] != null && membriScorta[s].isVisibile()) {
                        Logic.getInstance().log("Sono in BOSS_2_SCORTA con alieno " + soldato.t + " con id: " + soldato.id);
                        soldato.attaccoAnim(livello, soldato.direzioneAttacco);
                        countOrdineAlieniAttacco++;
                        soldato = membriScorta[s];
                    } else {
                        Logic.getInstance().log("Sono in BOSS_2_SCORTA con BOSS senza scorta");
                    }
                }
            }
        }
        return soldato;
    }

    public void gameOver() {
        utils.stopTutti();
        //Reset colpo giocatore a fine game in quanto potrebbe essere visibile
        colpoGiocatore.visibile = false;
        view.gameOver();
    }

    
    /**
     * Controlla se l'alieno che gli è stato passato è uscito dallo schermo.<br>
     * Se è uscito dallo schermo e non è un boss o è un boss senza scorta, allora gli resetto i 
     * parametri di attacco e lo metto o in rientro sullo schieramento o se numAlieniVivi {@code <}= 5
     * lo rimetto subito in attacco. <br><br>.
     * Se l'alieno passato è un boss ed ha la scorta, tale alieno risulterà dentro lo schermo (e quindi
     * in attacco) finchè tutte i suoi membri della scorta non sono usciti dallo schermo.
     * 
     * @return 
     * - TRUE se l'alieno è uscito dallo schermo <br>
     * - FALSE se l'alieno è ancora dentro lo schermo
     */
    public boolean cercaESettaFineAttaccoAlieno(LAlieno soldato) {
        if (soldato != null && soldato.visibile
                && (soldato.getEndXPos() < 0
                || soldato.getXPos() > Costanti.LARGHEZZA
                || soldato.getYPos() > Costanti.ALTEZZA)) {

            if (soldato.t == Costanti.TipoAlieno.BOSS) {
                LAlienoBoss boss = (LAlienoBoss) soldato;
                LAlieno[] membriScorta = boss.getMembriScorta();

                int numMembriScorta = 0;
                int numMembriScortaTerminatoAttacco = 0;

                for (int s = 0; s < membriScorta.length; s++) {
                    if (membriScorta[s] != null) {
                        numMembriScorta++;
                        if (cercaESettaFineAttaccoScorta(membriScorta[s])) {
                            numMembriScortaTerminatoAttacco++;
                        }
                    }
                }

                //Se non hanno terminato l'attacco quelli della scorta continuo a far avanzare l'attacco
                if (numMembriScorta != numMembriScortaTerminatoAttacco) {
                    return false;
                } else {
                    for (int s = 0; s < membriScorta.length; s++) {
                        if (membriScorta[s] != null) {
                            Logic.getInstance().log("numAlieniVivi prima di reset attacco: " + numAlieniVivi);
                            if (numAlieniVivi <= 5 && giocatore.isVisibile()) {
                                membriScorta[s].resetBaseAlienoFineAttacco(true); //Rincomincia l'attacco 
                            } else {
                                membriScorta[s].resetBaseAlienoFineAttacco(false); //Va in rientro schieramento
                                alieniAttacco[countOrdineAlieniAttacco + s + 1] = null;
                            }
                        }
                    }
                }
            }

            //Le scorte vengono resettate dai boss quindi se arrivano prima non le devo calcolare
            //a meno che il boss non sia appena morto e numAlieniVivi <= 5
            boolean bossMorto = false;
            if (soldato.t == Costanti.TipoAlieno.ROSSO) {
                LAlienoRosso rosso = (LAlienoRosso) soldato;
                int[] res = rosso.getScorta();

                if (res[0] == 1) {
                    int idAlienoBoss = res[1];

                    if (idAlienoBoss == 0) {
                        if (alieni[0][3] != null) {
                            if (alieni[0][3].isVisibile()) {
                                return false;
                            } else {
                                bossMorto = true;
                            }
                        }
                    } else {
                        if (alieni[0][6] != null) {
                            if (alieni[0][6].isVisibile()) {
                                return false;
                            } else {
                                bossMorto = true;
                            }
                        }
                    }
                }
            }

            if (numAlieniVivi <= 5 && giocatore.isVisibile()) {
                if (bossMorto) {
                    soldato.resetAlienoFineAttacco(true); //Reset boss per alieno rosso e rincomincia l'attacco
                } else {
                    soldato.resetBaseAlienoFineAttacco(true); //Rincomincia l'attacco 
                }
                setPosizioneRientro(soldato, true);
                return false;
            } else {
                resetAlienoFineAttacco(soldato, false);

                //Inserisco i nuovi alieni che devono ritornare in formazione
                for (int i = 0; i < alieniFineAttacco.length; i++) {
                    if (alieniFineAttacco[i] == null) {
                        alieniFineAttacco[i] = soldato;
                        break;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean cercaESettaFineAttaccoScorta(LAlieno soldato) {
        if (!soldato.visibile || soldato.visibile
                && (soldato.getEndXPos() < 0
                || soldato.getXPos() > Costanti.LARGHEZZA
                || soldato.getYPos() > Costanti.ALTEZZA)) {
            Logic.getInstance().log("L'alieno " + soldato.t + " con id: " + soldato.id + " ha finito l'attacco in posX: " + soldato.getXPos());
            return true;
        } else {
            Logic.getInstance().log("L'alieno " + soldato.t + " con id: " + soldato.id + " NON ha finito l'attacco");
            Logic.getInstance().log("getXPos() " + soldato.getXPos() + "  --  getYPos(): " + soldato.getYPos());
            return false;
        }
    }

    public void resetAlienoFineAttacco(LAlieno soldato, boolean attaccoDiretto) {
        Logic.getInstance().log("L'alieno " + soldato.t + " con id: " + soldato.id + " è stato eliminato da alieniAttacco[" + countOrdineAlieniAttacco + "]");
        alieniAttacco[countOrdineAlieniAttacco] = null;
        soldato.resetAlienoFineAttacco(attaccoDiretto);
    }

    @Override
    public boolean giocatoreEsploso() {
        return !giocatore.isVisibile();
    }

    @Override
    public String getPrimaImmagine(String oggetto) {
        switch (oggetto) {
            case "AlienoBlu":
                return this.alieni[3][0].getPrimaImmagine();
            case "AlienoRosso":
                return this.alieni[1][2].getPrimaImmagine();
            case "AlienoViola":
                return this.alieni[2][2].getPrimaImmagine();
            case "AlienoBoss":
                return this.alieni[0][3].getPrimaImmagine();
            case "Giocatore":
                return this.giocatore.getPrimaImmagine();
            case "ColpoAlieno":
                return this.colpiAlieni[0].getPrimaImmagine();
            case "ColpoGiocatore":
                return this.colpoGiocatore.getPrimaImmagine();
            case "BandieraLivello":
                return this.livello.getImmagine();
            case "Vita":
                return this.vite.getImmagine();
            default:
                return null;
        }
    }

    @Override
    public void inviaTutteImmaginiInCache() {
        //Sfrutto in maniera impropria getDimensioneImmagine che manda tutto in cache

        //AlienoBlu
        this.alieni[3][0].setImmaginiInCache();
        //AlienoRosso
        this.alieni[1][2].setImmaginiInCache();
        //AlienoViola
        this.alieni[2][2].setImmaginiInCache();
        //AlienoBoss
        this.alieni[0][3].setImmaginiInCache();
        //Giocatore
        this.giocatore.setImmaginiInCache();
        //ColpoAlieno
        this.colpiAlieni[0].setImmaginiInCache();
        //ColpoGiocatore
        this.colpoGiocatore.setImmaginiInCache();
        //BandieraLivello
        this.livello.setImmaginiInCache();
        //Vita
        this.vite.setImmaginiInCache();
        //Esplosioni
        view.setImmaginiEsplosioniInCache();
    }

    /**
     * @return
     * return[0] = Height <br>
     * return[1] = Width
    */
    public int[] getDimensioneImmagine(String path) {
        return this.view.getDimensioneImmagine(path);
    }

    @Override
    public int getCostanteALTEZZA() {
        return Costanti.ALTEZZA;
    }

    @Override
    public int getCostanteLARGHEZZA() {
        return Costanti.LARGHEZZA;
    }

    @Override
    public void spostaGiocatoreDx() {
        this.giocatore.spostaDx();
    }

    @Override
    public void spostaGiocatoreSx() {
        this.giocatore.spostaSx();
    }

    @Override
    public void sparaColpoG() {
        this.giocatore.sparaColpo();
    }

    @Override
    public int[] getCoordColpoG() {
        return this.colpoGiocatore.getCord();
    }

    @Override
    public boolean colpoGVisibile() {
        return this.colpoGiocatore.isVisibile();
    }

    LColpoGiocatore getColpo() {
        return this.colpoGiocatore;
    }

    @Override
    public int[] getCoordAlieno(int i, int j) {
        int[] xy = new int[2];
        LAlieno a = alieni[i][j];
        xy[0] = a.getXPos();
        xy[1] = a.getYPos();
        return xy;
    }

    @Override
    public boolean alienoEsploso(int i, int j) {
        return !alieni[i][j].isVisibile();
    }

    @Override
    public boolean alienoInFormazione(int i, int j) {
        return alieni[i][j].navettaInFormazione();
    }

    @Override
    public int[] getCoordColpoAlieno(int i) {
        int[] xy = new int[2];
        xy[0] = colpiAlieni[i].getXPos();
        xy[1] = colpiAlieni[i].getYPos();
        return xy;
    }

    @Override
    public boolean colpoAlienoVisibile(int i) {
        return colpiAlieni[i].isVisibile();
    }

    public void resetNumAlieniVivi() {
        numAlieniVivi = Costanti.NUM_ALIENI_TOT;
    }

    public void decrementaNumAlieniVivi() {
        numAlieniVivi--;
        switch (numAlieniVivi) {
            case 0:
				if(vite.getVite() >= 1 && !giocatore.getGiocatoreMorto()){
					livello.incrementaLivello();
				}
                break;
            case 25:
                utils.cambiaMusicaBackground();
                break;
            case 10:
                utils.cambiaMusicaBackground();
                break;
            default:
                break;
        }
    }

    public int getNumAlieniVivi() {
        return numAlieniVivi;
    }

    @Override
    public int getLivelloAttuale() {
        return this.livello.getLivello();
    }

    @Override
    public int getViteAttuali() {
        return this.vite.getVite();
    }

    @Override
    public int getPunteggioPartita() {
        return this.punti.getPunteggio();
    }

    public void incrementaPunteggioAttuale(int punteggio) {
        this.punti.incrementaPunteggio(punteggio);
    }

    public int getNumMembriScortaVivi() {
        if (this.numAnimazione == Costanti.BOSS_1_SCORTA || this.numAnimazione == Costanti.BOSS_2_SCORTA) {
            int count = 0;
            for (int i = 0; i < alieniAttacco.length; i++) {
                if (alieniAttacco[i] != null) {
                    if (alieniAttacco[i].t == Costanti.TipoAlieno.BOSS) {
                        //Potrebbe succedere che in attacco ho contemporaneamente due boss di cui uno con scorta e uno no
                        //quindi devo verificare di aver preso il boss giusto
                        if (this.numAnimazione == Costanti.BOSS_1_SCORTA) {
                            if (alieniAttacco[i + 1] == null || alieniAttacco[i + 1].t != Costanti.TipoAlieno.ROSSO) {
                                Logic.getInstance().log("WARNING: Dichiarato BOSS_1_SCORTA ma l'alieno dopo BOSS non è ROSSO");
                            } else if (alieniAttacco[i + 1].isVisibile()) {
                                count++;
                                break;
                            }
                        } else { //BOSS_2_SCORTA
                            //Potrebbe succedere che in attacco ho contemporaneamente due boss di cui uno con scorta e uno no
                            //quindi devo verificare di aver preso il boss giusto
                            if (alieniAttacco[i + 1] == null || alieniAttacco[i + 2] == null || alieniAttacco[i + 1].t != Costanti.TipoAlieno.ROSSO || alieniAttacco[i + 2].t != Costanti.TipoAlieno.ROSSO) {
                                Logic.getInstance().log("WARNING: Dichiarato BOSS_2_SCORTA ma uno dei due alieni dopo il BOSS non è ROSSO");
                            } else {
                                if (alieniAttacco[i + 1].isVisibile()) { 
                                    count++;
                                }

                                if (alieniAttacco[i + 2].isVisibile()) {
                                    count++;
                                }

                                break;
                            }
                        }
                    }
                }
            }
            return count;
        }
        return -1;
    }

    public int getNumAnimazione() {
        return this.numAnimazione;
    }

    @Override
    public String getNuovaSkin(int i, int j) {
        return alieni[i][j].getSkinAttuale();
    }

    @Override
    public int getStartYGiocatore() {
        return Costanti.STARTGIOCATORE;
    }

    public int getNumeroPuntiFalsaCurvaSinistra() {
        return this.numeroPuntiFalsaCurvaSinistra;
    }

    public int getNumeroPuntiFalsaCurvaDestra() {
        return this.numeroPuntiFalsaCurvaDestra;
    }

    public int[] getPuntiAttaccoLivello1() {
        return this.puntiAttaccoLivello1;
    }

    public int[] getPuntiAttaccoLivello2() {
        return this.puntiAttaccoLivello2;
    }

    public int[] getPuntiAttaccoLivello3() {
        return this.puntiAttaccoLivello3;
    }

    public int[] getPuntiFineAttaccoSinistra() {
        return this.puntiFineAttaccoSinistra;
    }

    public int[] getPuntiFineAttaccoDestra() {
        return this.puntiFineAttaccoDestra;
    }

    public int getNextRandomInt(int bound) {
        return this.random.nextInt(bound);
    }

    public LAlieno getPrimoAlienoAttacco() {
        for (int i = 0; i < numAlieniAttacco; i++) {
            if (alieniAttacco[i] != null && alieniAttacco[i].isVisibile()) {
                return alieniAttacco[i];
            }
        }

        Logic.getInstance().log("\n\nERRORE: Si è tentato di cercare il primo alieno di attacco ma sono tutti stati distrutti\n\n");
        return null;
    }

    @Override
    public boolean giocoIniziato() {
        return iniziato;
    }

    @Override
    public void clearBattaglione() {
        int rowspacing = Costanti.SPAZIO_ALTEZZA_ALIENO;
        int colspacing = Costanti.SPAZIO_LARGHEZZA_ALIENO;
        //int startPosiz = (Costanti.LARGHEZZA / 2) - (colspacing * (Costanti.COLS / 2)); //startPosiz = 60
        int startPosiz = Costanti.START_X_SCHIERAMENTO;

        int i = 0, x, y = 55;
        alieni[i][3].LAlieno(startPosiz + 3 * colspacing, y);
        alieni[i][6].LAlieno(startPosiz + 6 * colspacing, y);
        i++;
        y += rowspacing;
        x = 2 * colspacing + startPosiz;
        for (int j = 2; j < Costanti.COLS - 2; j++) {
            alieni[i][j].LAlieno(x, y);
            x += colspacing;
        }
        x = colspacing + startPosiz;
        i++;
        y += rowspacing;
        for (int j = 1; j < Costanti.COLS - 1; j++) {
            alieni[i][j].LAlieno(x, y);
            x += colspacing;
        }
        i++;
        y += rowspacing;
        while (i < Costanti.ROWS) {
            x = startPosiz;
            for (int j = 0; j < Costanti.COLS; j++) {
                alieni[i][j].LAlieno(x, y);
                x += colspacing;
            }
            y += rowspacing;
            i++;
        }

        for (i = 0; i < colpiAlieni.length; i++) {
            colpiAlieni[i] = new LColpoAlieno();
        }
        for (i = 0; i < Costanti.ROWS; i++) {
            for (int j = 0; j < Costanti.COLS; j++) {
                if (alieni[i][j] != null && j % 2 == 1) {
                    alieni[i][j].cambiaSkin(Costanti.SCHIERAMENTO, 0);
                }
            }
        }

        giocatore.LSpostabile(null, Costanti.LARGHEZZA / 2, Costanti.STARTGIOCATORE);
        giocatore.setVisibile(true);

        resetNumAlieniVivi();
        LAlieno.resetCountAlieniSchieramento();

        for (i = 0; i < alieniAttacco.length; i++) {
            alieniAttacco[i] = null;
        }

        if (livello.getLivello() == 1) {
            utils.playMusicaBackground();
        }
    }

    @Override
    public void setStartGioco(String nomeGiocatore) {
        this.nomeGiocatore = nomeGiocatore;
        vite.resetVite();
        punti.resetPunteggio();
        view.aggiornaPunteggioPartita(0);
        livello.resetLivello();
        clearBattaglione();
        iniziato = true;
    }

    public void aggiornaPunteggioPartita() {
        view.aggiornaPunteggioPartita(this.getPunteggioPartita());
    }

    @Override
    public void terminaGioco() {
        iniziato = false;
        view.stopTastiView();
    }
	
	@Override
    public void resetGiocatore() {
        vite.decrementaVite();
    }

    @Override
    public int getColpiAlieni() {
        return Costanti.NUM_COLPI_ALIENI;
    }

    public void cambioLivello() {
        giocatore.setVisibile(false);
        colpoGiocatore.setVisibile(false);
        view.cambioLivello();
    }

    @Override
    public int getRowSpacing() {
        return Costanti.SPAZIO_ALTEZZA_ALIENO;
    }

    @Override
    public int getColSpacing() {
        return Costanti.SPAZIO_LARGHEZZA_ALIENO;
    }

    public void playColpitoAlienoComune() {
        utils.playColpitoAlienoComune();
    }

    public void playColpitoAlienoBoss() {
        utils.playColpitoAlienoBoss();
    }

    public void playColpitoAlienoRosso() {
        utils.playColpitoAlienoRosso();
    }

    public void playColpitoGiocatore() {
        utils.playColpitoGiocatore();
    }

    public void playPartenzaAlieno() {
        utils.playPartenzaAlieno();
    }

    public void playSparoGiocatore() {
        utils.playSparoGiocatore();
    }

    public void playSetVite() {
        utils.playSetVite();
    }

    public void registraAtExit(Closeable c) {
        view.registraAtExit(c);
    }

    public void setGiocatoreMorto(boolean morto) {
        giocatore.setGiocatoreMorto(morto);
    }

    @Override
    public boolean getGiocatoreMorto() {
        return giocatore.getGiocatoreMorto();
    }

    @Override
    public String getNomeGiocatore() {
        return this.nomeGiocatore;
    }

    public void setTempoMorteGiocatore(long tempo) {
        tempoMorteGiocatore = tempo;
    }

    public long getTempoMorteGiocatore() {
        return tempoMorteGiocatore;
    }

}
