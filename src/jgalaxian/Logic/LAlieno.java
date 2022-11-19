package jgalaxian.Logic;

import jgalaxian.Logic.Costanti.*;

import java.util.Arrays;

public abstract class LAlieno extends LSpostabile {

    private static int countAlieniSchieramento = Costanti.NUM_ALIENI_TOT;
    private static int aux = 0;
    private static boolean dir = true;
    
    private boolean formazione = true;
    protected int id;
    protected int indexCambiaSkinSinistra = 0;
    protected int indexCambiaSkinDestra = 0;
    protected int margineX;
    protected int margineY;

    protected int segmentoCorrente = 0;
    protected int indexCoordinate = 0;
    protected int hold = 0;
    protected int hold2 = -1;

    //Creo una retta finta temporanea (sicuramente maggiore della distanza tra primo alieno e prima linea di attacco)
    //E' indispensabile per avere un corretto cambio di coordinate finchè non posso calcolarmi la vera retta
    protected int[] rettaUnisciCurve = new int[Costanti.ALTEZZA / 2];
    protected int[] rettaUnisciCurve2 = rettaUnisciCurve;

    protected int segmentoSparo;
    protected int countSegmentoSparo;

    protected StringBuilder pathAnimazione;
    protected StringBuilder pathAnimazioneSchieramento;
    protected boolean primaAnimazione = true;
    protected boolean sceltoPerAttacco = false;
    protected boolean partitoInAttacco = false;
    protected int direzioneAttacco;
    protected int indexAttacco = -1;
    protected int indexSchieramento = -1;
    protected int countCicliSchieramento = 0;
    protected int inversione = 0;
    protected int tipoAnimazione;
    protected int lastTipoAnimazione = Costanti.SCHIERAMENTO;
    protected boolean kamikaze;
    protected boolean inRientro = false;
    protected boolean primoStepRientro = false;
    protected int[] coordinateDiRientro;
    protected int[] coordinateDiRientroIniziali;

    public TipoAlieno t;

    public LAlieno(TipoAlieno t, int x, int y) {
        super(TipoSp.ALIENO, 28, 28);
        b.setX(x);
        b.setY(y);
        coordinateDiRientro = new int[2];
        coordinateDiRientroIniziali = new int[2];
        this.t = t;
    }

    public void LAlieno(int x, int y) {
        super.LSpostabile(null, x, y);
        coordinateDiRientro[0] = coordinateDiRientroIniziali[0] = x;
        coordinateDiRientro[1] = coordinateDiRientroIniziali[1] = y;
        Logic.getInstance().log("LALIENO, coordinateDiRientro per alieno " + this.t + " con id: " + this.id + " sono:\nX: " + x + "  Y: " + y);
        formazione = true;
        indexCambiaSkinSinistra = 0;
        indexCambiaSkinDestra = 0;

        rettaUnisciCurve = new int[Costanti.ALTEZZA / 2];
        rettaUnisciCurve2 = rettaUnisciCurve;

        primaAnimazione = true;
        sceltoPerAttacco = false;
        partitoInAttacco = false;
        indexAttacco = -1;
        indexSchieramento = -1;
        countCicliSchieramento = 0;
        inversione = 0;
        hold = 0;
        hold2 = -1;
        segmentoCorrente = 0;
        indexCoordinate = 0;
        tipoAnimazione = Costanti.SCHIERAMENTO;
        lastTipoAnimazione = Costanti.SCHIERAMENTO;
        super.setVisibile(true);
        
        inRientro = false;
        primoStepRientro = false;

        if (this.t == Costanti.TipoAlieno.ROSSO) {
            LAlienoRosso soldato = (LAlienoRosso) this;
            //Reset flag scorta
            soldato.setScorta(0, -1);
            soldato.setMargineMorteBoss(0,0);
            soldato.setEeguitoRossoMorteBoss(false);
        }

        if (this.t == Costanti.TipoAlieno.BOSS) {
            ((LAlienoBoss) this).setMembriScorta(null, null);
        }
    }

    public boolean movimentoGruppo() {
        boolean cambioDir = false;
        //Quando gli alieni stanno ritornando in schieramento hanno settato true sia formazione che sceltoPerAttacco che partitoInAttacco
        if (formazione) {
            int step;
            if (dir) {
                step = Costanti.STEP_ANIM; //Direzione = destra
            } else {
                step = -Costanti.STEP_ANIM; //Direzione = sinistra
            }
            
            //Finchè un alieno non parte effettivamente per l'attacco, anche se è stato scelto per andare in attacco,
            //partitoInAttacco = false quindi l'alieno continua a far cambiare direzione all'intero battaglione se 
            //tocca uno dei lati dello schermo
            if(!partitoInAttacco) {
                if (super.b.getEndX() + step >= Costanti.LARGHEZZA
                        || super.b.getX() + step <= 0) {
                    cambioDir = true;
                }
            }
            
            super.b.setX(super.b.getX() + step);
            coordinateDiRientro[0] = coordinateDiRientro[0] + step;
            Logic.getInstance().log("Aggiornate coordinateDiRientro in movimentoGruppo per alieno : " + this.t + " con id " + this.id + " val: " + coordinateDiRientro[0]);
        }
        return cambioDir;
    }

    public static void cambiaDir() {
        dir = !dir;
    }
    
    public static boolean getDir() {
        return dir;
    }
    
    public void fintoMovimentoGruppo() {
        int step;
        if (dir) {
            step = Costanti.STEP_ANIM; //Direzione = destra
        } else {
            step = -Costanti.STEP_ANIM; //Direzione = sinistra
        }
        coordinateDiRientro[0] = coordinateDiRientro[0] + step;
        Logic.getInstance().log("Aggiornate coordinateDiRientro in fintoMovimentoGruppo per alieno : " + this.t + " con id " + this.id + " val: " + coordinateDiRientro[0]);
    }
    
    public void resetCoordinateDiRientro() {
        coordinateDiRientro[0] = coordinateDiRientroIniziali[0];
        coordinateDiRientro[1] = coordinateDiRientroIniziali[1];
    }

    public LColpoAlieno sparaColpo() {
        LColpoAlieno c = null;
        LColpoAlieno[] cx = Logic.getInstance().getColpi();
        synchronized (cx) {
            while (cx[aux].visibile == true) {
                aux = (aux + 1) % Costanti.NUM_COLPI_ALIENI;
            }
            c = cx[aux];
            c.visibile = true;
        }
        c.LColpo(this, this.b.getX(), this.b.getY());
        return c;
    }

    public void setXPos(int x) {
        b.setX(x);
    }

    public void setYPos(int y) {
        b.setY(y);
    }

    public void riceviColpo() {
        Logic logic = Logic.getInstance();

        this.visibile = false;
        logic.decrementaNumAlieniVivi();
        
        if (!sceltoPerAttacco) {
            decrementaCountAlieniSchieramento();
            Logic.getInstance().log("decrementaCountAlieniSchieramento per alieno " + this.t + " id " + this.id + " morto. decrementaCountAlieniSchieramento: " + this.getCountAlieniSchieramento());
        }
        
        logic.incrementaPunteggioAttuale(this.getPunti());
        logic.esplodiAlieno(this.getXPos(), this.getYPos());
        logic.aggiornaPunteggioPartita();

        Logic.getInstance().log("Num alieni vivi: " + logic.getNumAlieniVivi());
    }
    
    public void playColpitoAlieno() {
        Logic.getInstance().playColpitoAlienoComune();
    }
    
    public boolean getInRientro() {
        return this.inRientro;
    }

    public boolean getPrimoStepRientro() {
        return this.primoStepRientro;
    }
    
    public void setInRientro(boolean inRientro) {
        this.inRientro = inRientro;
    }

    public void setPrimoStepRientro(boolean primoStepRientro) {
        this.primoStepRientro = primoStepRientro;
    }
    
    public int[] getCoordinateDiRientro() {
        return this.coordinateDiRientro;
    }
    
    public static void incrementaCountAlieniSchieramento() {
        countAlieniSchieramento++;
    }
    
    public static void decrementaCountAlieniSchieramento() {
        countAlieniSchieramento--;
    }
    
    public static int getCountAlieniSchieramento() {
        return countAlieniSchieramento;
    }
    
    public static void resetCountAlieniSchieramento() {
        countAlieniSchieramento = Costanti.NUM_ALIENI_TOT;
    }

    public boolean navettaInFormazione() {
        return this.formazione;
    }

    public String getSkinAttuale() {
        return pathAnimazione.toString();
    }
    
    public void resetPathAnimazione() {
        Logic.getInstance().log("RESET PATH ANIMAZIONE");
        Logic.getInstance().log("pathAnimazione.length : " + this.pathAnimazione.length());
        Logic.getInstance().log("pathAnimazione.toString : " + this.pathAnimazione.toString());
        Logic.getInstance().log("pathAnimazioneSchieramento.length : " + this.pathAnimazioneSchieramento.length());
        Logic.getInstance().log("pathAnimazioneSchieramento.toString : " + this.pathAnimazioneSchieramento.toString());
        this.pathAnimazione.replace(0, this.pathAnimazione.length(), this.pathAnimazioneSchieramento.toString());
        Logic.getInstance().log("NUOVO PATH ANIMAZIONE : " + this.pathAnimazione.toString());
    }
    
    public void resetAlienoFineAttacco(boolean attaccoDiretto) {
        kamikaze = false;
        hold = 0;
        hold2 = -1;
        inversione = 0;
        segmentoCorrente = 0;
        indexCoordinate = 0;
        indexCambiaSkinSinistra = 0;
        indexCambiaSkinDestra = 0;
        
        if (!attaccoDiretto) {
            //Setto variabili per inizio rientro in schieramento
            setInRientro(true);
            setPrimoStepRientro(true);
            setFormazione(true);
        }
    }
    
    public void resetBaseAlienoFineAttacco(boolean attaccoDiretto) {
        kamikaze = false;
        hold = 0;
        hold2 = -1;
        inversione = 0;
        segmentoCorrente = 0;
        indexCoordinate = 0;
        indexCambiaSkinSinistra = 0;
        indexCambiaSkinDestra = 0;
        
        if (!attaccoDiretto) {
            //Setto variabili per inizio rientro in schieramento
            setInRientro(true);
            setPrimoStepRientro(true);
            setFormazione(true);
        }
    }

    
    public void resetAlienoFineRientro() {
        setInRientro(false);
        sceltoPerAttacco = false;
        setFormazione(true);
        setPartitoInAttacco(false);
    }

    public void resetAlienoFineRientroPerAttaccoDiretto() {
        setInRientro(false);
        sceltoPerAttacco = false;
    }

    public boolean checkCollisione(LSpostabile giocatore) {
        boolean collis = false;
        if (b.inBound(giocatore.b)) {
            collis = true;
        }
        return collis;
    }
    
    public int trovaCostante() {
    
        //Queste costanti sono i numeri che appaiono in fondo al path della prima immagine 
        //di ogni tipologia di alieno
        switch (this.t) {
            case BOSS:
                return 0;
            case BLU:
                return 10;
            case ROSSO:
                return 30;
            case VIOLA:
                return 50;
            default:
                return 0;
        }
    }

    public String cambiaSkin(int tipoAnimazione, int direzione) {

        final int COSTANTE = trovaCostante();    
        boolean fuoriCostruttoreLogic = true;
        
        //Se è esplosa non deve fare animazioni
        if (!this.isVisibile()) {
            return null;
        }

        
        if (primaAnimazione) {
            
            if (pathAnimazione == null)
                fuoriCostruttoreLogic = false;
            
            pathAnimazione = new StringBuilder(getPrimaImmagine());
            pathAnimazioneSchieramento = new StringBuilder(getPrimaImmagine());
            primaAnimazione = false;
            
            if (fuoriCostruttoreLogic) {
                Logic.getInstance().log("CREAZIONE STRINGBUILDER PER PRIMA ANIMAZIONE");
                Logic.getInstance().log("pathAnimazione.length : " + this.pathAnimazione.length());
                Logic.getInstance().log("pathAnimazione.toString : " + this.pathAnimazione.toString());
                Logic.getInstance().log("pathAnimazioneSchieramento.length : " + this.pathAnimazioneSchieramento.length());
                Logic.getInstance().log("pathAnimazioneSchieramento.toString : " + this.pathAnimazioneSchieramento.toString());
            }
        }
        
        if (tipoAnimazione != Costanti.RIENTRO)
            this.tipoAnimazione = tipoAnimazione;        

        if (tipoAnimazione == Costanti.RIENTRO) {
            indexSchieramento++;
            indexSchieramento = indexSchieramento % 3; //Schieramento è composto da 3 foto

            if (indexSchieramento == 1) {
                countCicliSchieramento++;

                if (countCicliSchieramento == 2) {
                    indexSchieramento++;
                    countCicliSchieramento = 0;
                }
            } else if (indexSchieramento == 2) {
                indexSchieramento = 0;
            }

            if (fuoriCostruttoreLogic) {            
                Logic.getInstance().log("PRIMA - cambiaSkin in logic su fintoMovimentoGruppo: " + pathAnimazioneSchieramento.toString() + "  --  alieno " + this.t + " con id: " + this.id);
                Logic.getInstance().log("000001 - Scambio char in posizione : " + String.valueOf(pathAnimazioneSchieramento.length() - 5) + " con valore int : " + indexSchieramento + " che sarebbe char : " + Character.forDigit(indexSchieramento, 10));
            }
            
            pathAnimazioneSchieramento.setCharAt(pathAnimazioneSchieramento.length() - 5, Character.forDigit(indexSchieramento, 10));

            return pathAnimazioneSchieramento.toString();
        } else if (lastTipoAnimazione == Costanti.SCHIERAMENTO && tipoAnimazione == Costanti.SCHIERAMENTO) {
            indexSchieramento++;
            indexSchieramento = indexSchieramento % 3; //Schieramento è composto da 3 foto

            if (indexSchieramento == 1) {
                countCicliSchieramento++;

                if (countCicliSchieramento == 2) {
                    indexSchieramento++;
                    countCicliSchieramento = 0;
                }
            } else if (indexSchieramento == 2) {
                indexSchieramento = 0;
            }

            
            if (fuoriCostruttoreLogic) {
                Logic.getInstance().log("000002 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + indexSchieramento + " che sarebbe char : " + Character.forDigit(indexSchieramento, 10));
            }
            
            pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexSchieramento, 10));

            return pathAnimazione.toString();
        } else if (lastTipoAnimazione == Costanti.ATTACCO && tipoAnimazione == Costanti.SCHIERAMENTO) {
            //Reset variabili attacco per la prossima animazione di attacco
            indexAttacco = -1;
            
            //Setto il path per partire dall'attuale animazione di schieramento
            if (fuoriCostruttoreLogic) {
                Logic.getInstance().log("000003 - Chiamato reset path animazione");
            }
            this.resetPathAnimazione();
            
            return pathAnimazione.toString();
        } //Entro sia se passo da schieramento ad attacco, sia da attacco ad attacco
        else {
            //Setto il path per partire dalla prima animazione di attacco
            if (direzione == Costanti.SINISTRA) {
                if (inversione == 0) {
                    if (indexAttacco == -1) {
                        indexAttacco = COSTANTE + 10;
                    } else if (indexAttacco == COSTANTE + 2) {
                        indexAttacco = COSTANTE + 19;
                    }

                    if (indexAttacco < COSTANTE + 10) {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000004 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf(COSTANTE / 10) + " che sarebbe char : " + Character.forDigit(COSTANTE / 10, 10));
                            Logic.getInstance().log("000004 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - COSTANTE) + " che sarebbe char : " + Character.forDigit(indexAttacco - COSTANTE, 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit(COSTANTE / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - COSTANTE, 10));
                    } else {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000005 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf((COSTANTE + 10) / 10) + " che sarebbe char : " + Character.forDigit((COSTANTE + 10) / 10, 10));
                            Logic.getInstance().log("000005 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - (COSTANTE + 10)) + " che sarebbe char : " + Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit((COSTANTE + 10) / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                    }

                    if (indexAttacco == COSTANTE + 15) {
                        indexAttacco = -1;
                        inversione = (inversione + 1) % 2;
                    } else {
                        indexAttacco--;
                    }
                } else { //Ritorno da destra verso sinistra  -  SECONDA CURVA
                    if (indexAttacco == -1) {
                        indexAttacco = COSTANTE + 16;
                    } else if (indexAttacco == COSTANTE + 20) {
                        indexAttacco = COSTANTE + 3;
                    }

                    if (indexAttacco < COSTANTE + 10) {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000006 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf(COSTANTE / 10) + " che sarebbe char : " + Character.forDigit(COSTANTE / 10, 10));
                            Logic.getInstance().log("000006 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - COSTANTE) + " che sarebbe char : " + Character.forDigit(indexAttacco - COSTANTE, 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit(COSTANTE / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - COSTANTE, 10));
                    } else {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000007 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf((COSTANTE + 10) / 10) + " che sarebbe char : " + Character.forDigit((COSTANTE + 10) / 10, 10));
                            Logic.getInstance().log("000007 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - (COSTANTE + 10)) + " che sarebbe char : " + Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit((COSTANTE + 10) / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                    }

                    if (indexAttacco == COSTANTE + 6) {
                        indexAttacco = -1;
                        inversione = (inversione + 1) % 2;
                    } else {
                        indexAttacco++;
                    }
                }

            } else if (direzione == Costanti.DESTRA) {
                if (inversione == 0) {
                    if (indexAttacco == -1) {
                        indexAttacco = COSTANTE + 11;
                    } else if (indexAttacco == COSTANTE + 20) {
                        indexAttacco = COSTANTE + 3;
                    }

                    if (indexAttacco < COSTANTE + 10) {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000008 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf(COSTANTE / 10) + " che sarebbe char : " + Character.forDigit(COSTANTE / 10, 10));
                            Logic.getInstance().log("000008 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - COSTANTE) + " che sarebbe char : " + Character.forDigit(indexAttacco - COSTANTE, 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit(COSTANTE / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - (COSTANTE), 10));
                    } else {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000009 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf((COSTANTE + 10) / 10) + " che sarebbe char : " + Character.forDigit((COSTANTE + 10) / 10, 10));
                            Logic.getInstance().log("000009 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - (COSTANTE + 10)) + " che sarebbe char : " + Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit((COSTANTE + 10) / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                    }

                    if (indexAttacco == COSTANTE + 6) {
                        indexAttacco = -1;
                        inversione = (inversione + 1) % 2;
                    } else {
                        indexAttacco++;
                    }
                } else { //Ritorno da sinistra verso destra  -  SECONDA CURVA
                    if (indexAttacco == -1) {
                        indexAttacco = COSTANTE + 5;
                    } else if (indexAttacco == COSTANTE + 2) {
                        indexAttacco = COSTANTE + 19;
                    }

                    if (indexAttacco < COSTANTE + 10) {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000010 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf(COSTANTE / 10) + " che sarebbe char : " + Character.forDigit(COSTANTE / 10, 10));
                            Logic.getInstance().log("000010 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - COSTANTE) + " che sarebbe char : " + Character.forDigit(indexAttacco - COSTANTE, 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit(COSTANTE / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - COSTANTE, 10));
                    } else {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000011 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf((COSTANTE + 10) / 10) + " che sarebbe char : " + Character.forDigit((COSTANTE + 10) / 10, 10));
                            Logic.getInstance().log("000011 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - (COSTANTE + 10)) + " che sarebbe char : " + Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit((COSTANTE + 10) / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                    }

                    if (indexAttacco == COSTANTE + 15) {
                        indexAttacco = -1;
                        inversione = (inversione + 1) % 2;
                    } else {
                        indexAttacco--;
                    }
                }
            } else if (direzione == Costanti.FALSA_DESTRA) { //Curva a Sinistra impossibile per grandezza diametro curva dal bordo grafica
                if (inversione == 0) {
                    if (indexAttacco == -1) {
                        indexAttacco = COSTANTE + 11;
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000012 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf((COSTANTE + 10) / 10) + " che sarebbe char : " + Character.forDigit((COSTANTE + 10) / 10, 10));
                        }
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit((COSTANTE + 10) / 10, 10));
                    }
        
                    if (fuoriCostruttoreLogic) {
                        Logic.getInstance().log("000012 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - (COSTANTE + 10)) + " che sarebbe char : " + Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                    }
                    
                    pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - (COSTANTE + 10), 10));

                    if (indexAttacco == COSTANTE + 16) {
                        indexAttacco = -1;
                        inversione = (inversione + 1) % 2;
                    } else {
                        indexAttacco++;
                    }
                } else { //Ritorno da destra verso sinistra  -  SECONDA CURVA
                    if (indexAttacco == -1) {
                        indexAttacco = COSTANTE + 16;
                    } else if (indexAttacco == COSTANTE + 20) {
                        indexAttacco = COSTANTE + 3;
                    }

                    if (indexAttacco < COSTANTE + 10) {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000013 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf(COSTANTE / 10) + " che sarebbe char : " + Character.forDigit(COSTANTE / 10, 10));
                            Logic.getInstance().log("000013 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - COSTANTE) + " che sarebbe char : " + Character.forDigit(indexAttacco - COSTANTE, 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit(COSTANTE / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - COSTANTE, 10));
                    } else {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000014 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf((COSTANTE + 10) / 10) + " che sarebbe char : " + Character.forDigit((COSTANTE + 10) / 10, 10));
                            Logic.getInstance().log("000014 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - (COSTANTE + 10)) + " che sarebbe char : " + Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit((COSTANTE + 10) / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                    }

                    if (indexAttacco == COSTANTE + 6) {
                        indexAttacco = -1;
                        inversione = (inversione + 1) % 2;
                    } else {
                        indexAttacco++;
                    }
                }
            } else if (direzione == Costanti.FALSA_SINISTRA) { //Curva a Destra impossibile per grandezza diametro curva dal bordo grafica
                if (inversione == 0) {
                    if (indexAttacco == -1) {
                        indexAttacco = COSTANTE + 10;
                    }

                    if (indexAttacco < COSTANTE + 10) {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000015 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf(COSTANTE / 10) + " che sarebbe char : " + Character.forDigit(COSTANTE / 10, 10));
                            Logic.getInstance().log("000015 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - COSTANTE) + " che sarebbe char : " + Character.forDigit(indexAttacco - COSTANTE, 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit(COSTANTE / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - COSTANTE, 10));
                    } else {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000016 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf((COSTANTE + 10) / 10) + " che sarebbe char : " + Character.forDigit((COSTANTE + 10) / 10, 10));
                            Logic.getInstance().log("000016 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - (COSTANTE + 10)) + " che sarebbe char : " + Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit((COSTANTE + 10) / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                    }

                    if (indexAttacco == COSTANTE + 5) {
                        indexAttacco = -1;
                        inversione = (inversione + 1) % 2;
                    } else {
                        indexAttacco--;
                    }
                } else { //Ritorno da sinistra verso destra  -  SECONDA CURVA
                    if (indexAttacco == -1) {
                        indexAttacco = COSTANTE + 5;
                    } else if (indexAttacco == COSTANTE + 2) {
                        indexAttacco = COSTANTE + 19;
                    }

                    if (indexAttacco < COSTANTE + 10) {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000017 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf(COSTANTE / 10) + " che sarebbe char : " + Character.forDigit(COSTANTE / 10, 10));
                            Logic.getInstance().log("000017 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - COSTANTE) + " che sarebbe char : " + Character.forDigit(indexAttacco - COSTANTE, 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit(COSTANTE / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - COSTANTE, 10));
                    } else {
                        if (fuoriCostruttoreLogic) {
                            Logic.getInstance().log("000018 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 6) + " con valore int : " + String.valueOf((COSTANTE + 10) / 10) + " che sarebbe char : " + Character.forDigit((COSTANTE + 10) / 10, 10));
                            Logic.getInstance().log("000018 - Scambio char in posizione : " + String.valueOf(pathAnimazione.length() - 5) + " con valore int : " + String.valueOf(indexAttacco - (COSTANTE + 10)) + " che sarebbe char : " + Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                        }
                        
                        pathAnimazione.setCharAt(pathAnimazione.length() - 6, Character.forDigit((COSTANTE + 10) / 10, 10));
                        pathAnimazione.setCharAt(pathAnimazione.length() - 5, Character.forDigit(indexAttacco - (COSTANTE + 10), 10));
                    }

                    if (indexAttacco == COSTANTE + 15) {
                        indexAttacco = -1;
                        inversione = (inversione + 1) % 2;
                    } else {
                        indexAttacco--;
                    }
                }
            }

            return pathAnimazione.toString();
        }
    }

    public int attaccoAnim(int livello, int direzione) {
        Logic logic = Logic.getInstance();
        int numMembriScorta = 0;

        int[] curvaDx = logic.getCurvaDx();
        int[] curvaSx = logic.getCurvaSx();
        int[] secondaCurvaDx = logic.getSecondaCurvaDx();
        int[] secondaCurvaSx = logic.getSecondaCurvaSx();
        int[] arrayIndexCambiaSkinSinistra = logic.getArrayIndexCambiaSkinSinistra();
        int[] arrayIndexCambiaSkinDestra = logic.getArrayIndexCambiaSkinDestra();

        if (hold2 < 0) {
            margineX = b.getX();
            margineY = b.getY();
            hold2++;
        }

        switch (direzione) {
            case Costanti.SINISTRA:
                numMembriScorta = corpoAttaccoAnim(livello, direzione, curvaSx, curvaSx.length, secondaCurvaSx, arrayIndexCambiaSkinSinistra, indexCambiaSkinSinistra, logic);
                break;

            case Costanti.DESTRA:
                numMembriScorta = corpoAttaccoAnim(livello, direzione, curvaDx, curvaDx.length, secondaCurvaDx, arrayIndexCambiaSkinDestra, indexCambiaSkinDestra, logic);
                break;

            case Costanti.FALSA_DESTRA:
                numMembriScorta = corpoAttaccoAnim(livello, direzione, curvaDx, getNumeroPuntiFalsaCurva(direzione), secondaCurvaSx, arrayIndexCambiaSkinDestra, indexCambiaSkinDestra, logic);
                break;

            case Costanti.FALSA_SINISTRA:
                numMembriScorta = corpoAttaccoAnim(livello, direzione, curvaSx, getNumeroPuntiFalsaCurva(direzione), secondaCurvaDx, arrayIndexCambiaSkinSinistra, indexCambiaSkinSinistra, logic);
                break;

            default:
                break;
        }

        return numMembriScorta;
    }

    public int corpoAttaccoAnim(int livello, int direzione, int[] curva, int puntiCurva, int[] secondaCurva, int[] arrayIndexCambiaSkin, int indexCambiaSkin, Logic logic) {

        boolean isBoss = false;
        boolean isRosso = false;
        LAlienoRosso rosso = null;
        
        //Mi serve perchè finchè il Boss è morto e non ho dovuto calcolare un nuovo punto (attacco o uscita)
        //allora devo usare il margine che questo alieno rosso aveva con il boss, ma dal primo momento in 
        //cui mi ricalcolo un nuovo punto, tale margine costituirebbe un errore perchè il punto è calcolato
        //per l'alieno rosso di per se e non per il boss
        boolean eseguitoRossoMorteBoss = false;
        int numMembriScorta = 0;

        if (this.t == Costanti.TipoAlieno.BOSS) {
            isBoss = true;
        } else if (this.t == Costanti.TipoAlieno.ROSSO) {
            isRosso = true;
            rosso = (LAlienoRosso) this;
            eseguitoRossoMorteBoss = rosso.getEeguitoRossoMorteBoss();
        } 

        int numAnimazione = logic.getNumAnimazione();
        LAlienoRosso[] membriScorta = null;

        if (numAnimazione == Costanti.BOSS_1_SCORTA) {
            if (isBoss) {

                LAlienoBoss boss = (LAlienoBoss) this;
                membriScorta = boss.getMembriScorta();

                //Teoricamente non dovrebbe mai succedere che la seconda scorta != null ma per sicurezza
                //la pongo io come tale
                membriScorta[1] = null;

                //Controllo che sia il boss "giusto" dato che magari davanti al boss giusto che ha la scorta
                //è stato scelto un alieno boss che non ha più alieni rossi di scorta
                if (membriScorta[0] == null || !membriScorta[0].isVisibile()) {
                    isBoss = false;
                } else {
                    numMembriScorta = 1;
                }
            }
        } else if (numAnimazione == Costanti.BOSS_2_SCORTA) {
            if (isBoss) {

                LAlienoBoss boss = (LAlienoBoss) this;
                membriScorta = boss.getMembriScorta();

                int j = 0;
                for (int s = 0; s < membriScorta.length; s++) {
                    //Controllo che sia il boss "giusto" dato che magari davanti al boss giusto che ha la scorta
                    //è stato scelto un alieno boss che non ha più alieni rossi di scorta
                    if (membriScorta[s] != null && membriScorta[s].isVisibile()) {
                        j++;
                    }
                }
                if (j == 0) {
                    isBoss = false;
                    membriScorta = null;
                } else {
                    numMembriScorta = j;
                }
            }
        }

        //Se sto facendo ancora la prima curva
        if (hold2 < (puntiCurva / 2)) {

            int newX = curva[indexCoordinate];
            int newY = curva[indexCoordinate + 1];
            
            if (isRosso) {
                int[] margine = rosso.getMargineMorteBoss();
                
                Logic.getInstance().log("1 - eseguitoRossoMorteBoss : " + eseguitoRossoMorteBoss);
                Logic.getInstance().log("1 - margine[0]: " + margine[0] + " , margine[1] : " + margine[1]);
                
                this.setXPos(newX + margineX + margine[0]);
                this.setYPos(newY + margineY + margine[1]);
            } else {
                this.setXPos(newX + margineX);
                this.setYPos(newY + margineY);
            }


            //SETTO POSIZIONE SCORTA SE ESISTE
            if (isBoss && membriScorta != null) {
                muoviScorta(membriScorta, newX + margineX, newY + margineY, logic);
            }

            if ((indexCoordinate / 2) == arrayIndexCambiaSkin[indexCambiaSkin]
                    || (indexCoordinate / 2 + 1) == arrayIndexCambiaSkin[indexCambiaSkin]) {
                String s = this.cambiaSkin(Costanti.ATTACCO, direzione);
                Logic.getInstance().log("Parte 1 : " + s + "   , alieno " + this.t + " con id : " + this.id);

                //CAMBIO SKIN SCORTA SE ESISTE
                if (isBoss && membriScorta != null) {
                    cambiaSkinScorta(membriScorta, Costanti.ATTACCO, direzione, logic);
                }

                Logic.getInstance().log("arrayIndexCambiaSkin[indexCambiaSkin] : " + arrayIndexCambiaSkin[indexCambiaSkin]);
                incrementaIndexCambiaSkin(direzione);
            }

            indexCoordinate += 2;
            
        } else { //Finito di fare la prima curva

            if (hold2 == (puntiCurva / 2)) { //Inizio congiunzione curve attacco

                Logic.getInstance().log("\nAlieno attuale : " + this);
                Logic.getInstance().log("Primo Alieno : " + logic.getPrimoAlienoAttacco());
                Logic.getInstance().log("puntiCurva : " + puntiCurva);
                Logic.getInstance().log("direzione : " + direzione);
                Logic.getInstance().log("\nhold2 : " + hold2);

                indexCoordinate = 0;

                int[] res = trovaPuntoAttacco(livello, direzione, logic);

                rettaUnisciCurve = Retta.retta(this.getXPos(), this.getYPos(), res[0], res[1]);
                Logic.getInstance().log(Arrays.toString(rettaUnisciCurve));
                Logic.getInstance().log("rettaUnisciCurve.length: " + rettaUnisciCurve.length);
                
                //Da ora in poi mi calcolo punti rispetto l'alieno stesso e non al BOSS a cui faceva riferimento
                //prima che morisse (se era una scorta ovviamente)
                if (isRosso) {
                    if ((rosso.getScorta())[0] == 1) {
                        rosso.setEeguitoRossoMorteBoss(true);
                        eseguitoRossoMorteBoss = true;
                    }
                }
                
                Logic.getInstance().log("1 - eseguitoRossoMorteBoss : " + eseguitoRossoMorteBoss);

                //CALCO PUNTI IN CUI FAR SPARARE ALIENI
                segmentoSparo = (rettaUnisciCurve.length / 2) / 4;
                countSegmentoSparo = 1;
            }

            //RETTA DI CONGIUNZIONE TRA LE DUE CURVE
            if (hold2 < ((puntiCurva / 2) + (rettaUnisciCurve.length / 2))) {
                int newX = rettaUnisciCurve[indexCoordinate];
                int newY = rettaUnisciCurve[indexCoordinate + 1];

                if (isRosso) {
                    Logic.getInstance().log("2 PRIMA - eseguitoRossoMorteBoss : " + eseguitoRossoMorteBoss);
                    
                    if (eseguitoRossoMorteBoss)
                        rosso.setMargineMorteBoss(0, 0);
                    
                    int[] margine = rosso.getMargineMorteBoss();
                
                    Logic.getInstance().log("2 DOPO - eseguitoRossoMorteBoss : " + eseguitoRossoMorteBoss);
                    Logic.getInstance().log("2 - margine[0]: " + margine[0] + " , margine[1] : " + margine[1]);

                    this.setXPos(newX + margine[0]);
                    this.setYPos(Math.abs(newY) + margine[1]);
                } else {
                    this.setXPos(newX);
                    this.setYPos(Math.abs(newY));
                }

                //SETTO POSIZIONE SCORTA SE ESISTE
                if (isBoss && membriScorta != null) {
                    muoviScorta(membriScorta, newX, Math.abs(newY), logic);
                }

                //SPARI ALIENI
                int puntoSparo = (puntiCurva / 2) + (segmentoSparo * countSegmentoSparo);

                Logic.getInstance().log("SPARI ALIENI");
                Logic.getInstance().log("hold2 : " + hold2);
                Logic.getInstance().log("segmentoSparo : " + segmentoSparo);
                Logic.getInstance().log("countSegmentoSparo : " + countSegmentoSparo);
                Logic.getInstance().log("Punto in cui sparare : " + puntoSparo);

                if (hold2 == puntoSparo) {
                    //Posso sparare solo 3 volte. Faccio gli spari a intervalli regolari
                    countSegmentoSparo = (countSegmentoSparo + 1) % 4;
                    this.sparaColpo();
                    Logic.getInstance().log("HO SPARATO UN COLPO!");

                    //SPARA SCORTA SE ESISTE
                    if (isBoss && membriScorta != null) {
                        sparaScorta(membriScorta, logic);
                    }

                }

                int tmp_somma = (rettaUnisciCurve.length / 2) + (puntiCurva / 2);
                indexCoordinate = indexCoordinate + 2;

            } else { //Finita retta di congiunzione curve

                if (hold2 == ((puntiCurva / 2) + (rettaUnisciCurve.length / 2))) { //Inizio seconda curva
                    indexCoordinate = 0;
                    
                    //Da ora in poi mi calcolo punti rispetto l'alieno stesso e non al BOSS a cui faceva riferimento
                    //prima che morisse (se era una scorta ovviamente)
                    if (isRosso) {
                        if ((rosso.getScorta())[0] == 1) {
                            rosso.setEeguitoRossoMorteBoss(true);
                            eseguitoRossoMorteBoss = true;
                        }
                    }
                    Logic.getInstance().log("1 - eseguitoRossoMorteBoss : " + eseguitoRossoMorteBoss);
                }

                if (hold2 < ((puntiCurva / 2) + (rettaUnisciCurve.length / 2) + (secondaCurva.length / 2))) {
                    Logic.getInstance().log("\n\n\n\nINIZIO SECONDA CURVA\n\n\n\n");

                    int newX = secondaCurva[indexCoordinate];
                    int newY = secondaCurva[indexCoordinate + 1];

                    if (indexCoordinate == 0) {
                        margineX = this.getXPos();
                        margineY = this.getYPos();
                        
                        if (isRosso) {
                            Logic.getInstance().log("3 PRIMA - eseguitoRossoMorteBoss : " + eseguitoRossoMorteBoss);

                            if (eseguitoRossoMorteBoss)
                                rosso.setMargineMorteBoss(0, 0);
                            else if ((rosso.getScorta())[0] == 1) {
                                rosso.setEeguitoRossoMorteBoss(true);
                                eseguitoRossoMorteBoss = true;
                            }   

                            int[] margine = rosso.getMargineMorteBoss();
                    
                            Logic.getInstance().log("3 DOPO - eseguitoRossoMorteBoss : " + eseguitoRossoMorteBoss);
                            Logic.getInstance().log("3 - margine[0]: " + margine[0] + " , margine[1] : " + margine[1]);

                            this.setXPos(newX + margineX + margine[0]);
                            this.setYPos(newY + margineY + margine[1]);
                        } else {
                            this.setXPos(newX + margineX);
                            this.setYPos(newY + margineY);
                        }
                        
                        //SETTO POSIZIONE SCORTA SE ESISTE
                        if (isBoss && membriScorta != null) {
                            muoviScorta(membriScorta, newX + margineX, newY + margineY, logic);
                        }

                    } else {
                        if (isRosso) {
                            int[] margine = rosso.getMargineMorteBoss();
                     
                            if ((rosso.getScorta())[0] == 1) {
                                rosso.setEeguitoRossoMorteBoss(true);
                                eseguitoRossoMorteBoss = true;
                            }
                    
                            Logic.getInstance().log("4 - eseguitoRossoMorteBoss : " + eseguitoRossoMorteBoss);
                            Logic.getInstance().log("4 - margine[0]: " + margine[0] + " , margine[1] : " + margine[1]);

                            this.setXPos(newX + margineX + margine[0]);
                            this.setYPos(newY + margineY + margine[1]);
                        } else {
                            this.setXPos(newX + margineX);
                            this.setYPos(newY + margineY);
                        }

                        //SETTO POSIZIONE SCORTA SE ESISTE
                        if (isBoss && membriScorta != null) {
                            muoviScorta(membriScorta, newX + margineX, newY + margineY, logic);
                        }

                    }

                    String s = this.cambiaSkin(Costanti.ATTACCO, direzione);
                    Logic.getInstance().log("Parte 2 : " + s + "   , alieno " + this.t + " con id : " + this.id);

                    //CAMBIO SKIN SCORTA SE ESISTE
                    if (isBoss && membriScorta != null) {
                        cambiaSkinScorta(membriScorta, Costanti.ATTACCO, direzione, logic);
                    }

                    //Logic.getInstance().log("Parte 2 : " + s);
                    //Logic.getInstance().log("newX2: " + newX + " , newY2: " + newY);
                    //Logic.getInstance().log("indexCoordinate: " + indexCoordinate);
                    //Logic.getInstance().log("secondaCurva.length: " + secondaCurva.length);
                    indexCoordinate = indexCoordinate + 2;

                    Logic.getInstance().log("direzione : " + direzione);
                    Logic.getInstance().log("puntiCurva : " + puntiCurva);
                    Logic.getInstance().log("rettaUnisciCurve : " + rettaUnisciCurve.length);
                    Logic.getInstance().log("secondaCurva : " + secondaCurva.length);
                    Logic.getInstance().log("somma / 2 : " + String.valueOf((puntiCurva / 2) + (rettaUnisciCurve.length / 2) + (secondaCurva.length / 2)));
                    Logic.getInstance().log("posX: " + this.getXPos() + " , posY: " + this.getYPos());

                } else { //Finita seconda curva
                    
                    //Inizio retta finisci attacco
                    if (hold2 == ((puntiCurva / 2) + (rettaUnisciCurve.length / 2) + (secondaCurva.length / 2))) {
                        indexCoordinate = 0;

                        Logic.getInstance().log("\nAlieno attuale : " + this);
                        Logic.getInstance().log("Primo Alieno : " + logic.getPrimoAlienoAttacco());
                        Logic.getInstance().log("\nhold2 : " + hold2);

                        int posXalieno = this.getXPos();
                        int posYalieno = this.getYPos();

                        int posXfinale = posXalieno;
                        int posYfinale = posYalieno;

                        if (this.kamikaze) {
                            int[] res = kamikazeAnim(livello, direzione, posXalieno, posYalieno, logic);
                            posXfinale = res[0];
                            posYfinale = res[1];

                        } else if (direzione == Costanti.SINISTRA || direzione == Costanti.FALSA_DESTRA) {
                            //Scelgo un punto casuale di uscita dallo schermo (tra le coppie di punti possibili)
                            //Se paro ho pescato X e recupero la Y
                            //Se disparo ho pescato Y e recupero la X
                            int index = logic.getNextRandomInt(logic.getPuntiFineAttaccoSinistra().length);
                            if ((index % 2) == 0) { //Se paro
                                posXfinale = (logic.getPuntiFineAttaccoSinistra())[index];
                                posYfinale = (logic.getPuntiFineAttaccoSinistra())[index + 1];
                            } else { //Se disparo
                                posXfinale = (logic.getPuntiFineAttaccoSinistra())[index - 1];
                                posYfinale = (logic.getPuntiFineAttaccoSinistra())[index];
                            }

                        } else if (direzione == Costanti.DESTRA || direzione == Costanti.FALSA_SINISTRA) {
                            int index = logic.getNextRandomInt(logic.getPuntiFineAttaccoDestra().length);
                            if ((index % 2) == 0) { //Se paro
                                posXfinale = (logic.getPuntiFineAttaccoDestra())[index];
                                posYfinale = (logic.getPuntiFineAttaccoDestra())[index + 1];
                            } else { //Se disparo
                                posXfinale = (logic.getPuntiFineAttaccoDestra())[index - 1];
                                posYfinale = (logic.getPuntiFineAttaccoDestra())[index];
                            }
                        }

                        Logic.getInstance().log("In corpoAttaccoAnim :");
                        Logic.getInstance().log("numAnimazione = " + logic.getNumAnimazione());
                        Logic.getInstance().log("Alieno " + this.t + " , id : " + this.id);
                        Logic.getInstance().log("posXalieno: " + posXalieno + " , posYalieno: " + posYalieno);
                        Logic.getInstance().log("posXfinale: " + posXfinale + " , posYfinale: " + posYfinale);

                        rettaUnisciCurve2 = Retta.retta(posXalieno, posYalieno, posXfinale, posYfinale);
                        Logic.getInstance().log("rettaUnisciCurve2 :\n" + Arrays.toString(rettaUnisciCurve2));
                        
                        //Da ora in poi mi calcolo punti rispetto l'alieno stesso e non al BOSS a cui faceva riferimento
                        //prima che morisse (se era una scorta ovviamente)

                        if (isRosso) {
                            if ((rosso.getScorta())[0] == 1) {
                                rosso.setEeguitoRossoMorteBoss(true);
                                eseguitoRossoMorteBoss = true;
                            }
                    
                            Logic.getInstance().log("4.5 - eseguitoRossoMorteBoss : " + eseguitoRossoMorteBoss);
                        }                        
                    }
                    
                    if (isRosso) {
                        Logic.getInstance().log("5 PRIMA - eseguitoRossoMorteBoss : " + eseguitoRossoMorteBoss);
                        
                        if (eseguitoRossoMorteBoss)
                            rosso.setMargineMorteBoss(0, 0);

                        int[] margine = rosso.getMargineMorteBoss();
                    
                        Logic.getInstance().log("5 DOPO - eseguitoRossoMorteBoss : " + eseguitoRossoMorteBoss);
                        Logic.getInstance().log("5 - margine[0]: " + margine[0] + " , margine[1] : " + margine[1]);

                        this.setXPos(rettaUnisciCurve2[indexCoordinate] + margine[0]);
                        this.setYPos(Math.abs(rettaUnisciCurve2[indexCoordinate + 1]) + margine[1]);
                    } else {
                        this.setXPos(rettaUnisciCurve2[indexCoordinate]);
                        this.setYPos(Math.abs(rettaUnisciCurve2[indexCoordinate + 1]));
                    }

                    //SETTO POSIZIONE SCORTA SE ESISTE
                    if (isBoss && membriScorta != null) {
                        muoviScorta(membriScorta, rettaUnisciCurve2[indexCoordinate], Math.abs(rettaUnisciCurve2[indexCoordinate + 1]), logic);
                    }

                    indexCoordinate = indexCoordinate + 2;
                }
            }
        }

        hold2 = hold2 + 1;
        Logic.getInstance().log("Ho incrementato hold2 : " + hold2 + " di " + this.t + " id " + this.id);

        return numMembriScorta;
    }

    public void sparaScorta(LAlieno[] membriScorta, Logic logic) {
        for (int s = 0; s < membriScorta.length; s++) {
            if (membriScorta[s] != null && membriScorta[s].isVisibile()) {
                membriScorta[s].sparaColpo();
            }
        }
    }

    public void cambiaSkinScorta(LAlieno[] membriScorta, int tipoFase, int direzione, Logic logic) {
        for (int s = 0; s < membriScorta.length; s++) {
            if (membriScorta[s] != null) {
                String skin = membriScorta[s].cambiaSkin(tipoFase, direzione);
                Logic.getInstance().log("Skin scorta : " + skin + "   , alieno " + membriScorta[s].t + " con id : " + membriScorta[s].id);
            }
        }
    }

    public void muoviScorta(LAlienoRosso[] membriScorta, int xBoss, int yBoss, Logic logic) {
        int altezza = (logic.getDimensioneImmagine(this.pathAnimazione.toString()))[0];
        int margine;

        for (int s = 0; s < membriScorta.length; s++) {
            if (membriScorta[s] != null) {
                margine = calcolaMargine(membriScorta[s], logic);

                membriScorta[s].setXPos(xBoss + margine);
                membriScorta[s].setYPos(yBoss + altezza);
                Logic.getInstance().log("\nIn muoviScorta:");
                Logic.getInstance().log("xBoss: " + xBoss + " , yBoss: " + yBoss + " , margine alieno da boss: " + margine);
                Logic.getInstance().log("Settata posizione scorta id " + membriScorta[s].id + "  -  PosX: " + membriScorta[s].getXPos() + "   PosY: " + membriScorta[s].getYPos());
                Logic.getInstance().log("Alieno " + membriScorta[s].t + " con id " + membriScorta[s].id + " è scorta " + (membriScorta[s].getScorta())[0] + " per boss id: " + (membriScorta[s].getScorta())[1] + "\n");
            }
        }
    }

    
    /**
     * Serve a calcolare la distanza sull'asse X tra un boss e il <b> soldato </b> passato come parametro
     */
    public int calcolaMargine(LAlieno soldato, Logic logic) {
        int margine = 0;
        int larghezza = (logic.getDimensioneImmagine(soldato.pathAnimazione.toString()))[1];
        switch (soldato.id) {
            //Le scorte con id 2 e 5 stanno alla sinistra dei rispettivi boss (0 e 1)
            case 2:
            case 5:
                margine = larghezza * -1;
                break;
            //Le scorte con id 4 e 7 stanno alla destra dei rispettivi boss (0 e 1)
            case 4:
            case 7:
                margine = larghezza;
                break;
            //Le scorte con id 3 e 6 stanno nella stessa posizione dei rispettivi boss (0 e 1)
            default:
                break;
        }

        return margine;
    }

    /**
     * Cerca un punto sulla mappa che sarà usato come punto di arrivo di una retta di congiunzione tra 
     * il punto finale della prima curva e il punto iniziale della seconda curva dell'animazione di attacco. <br>
     * Tale punto sarà scelto all'interno di alcuni array di posizioni che a loro volta vengono scelti
     * in base al livello a cui è arrivato il giocatore e la direzione di attacco dell'alieno.
     */
    public int[] trovaPuntoAttacco(int livello, int direzione, Logic logic) {

        int x2 = 0;
        int y2 = 0;
        int[] res;

        Logic.getInstance().log("In trovaPuntoAttacco :");
        Logic.getInstance().log("Alieno " + this.t + " , id : " + this.id);
        Logic.getInstance().log("numAnimazione = " + logic.getNumAnimazione());
        Logic.getInstance().log("direzione = " + direzione);

        switch (livello) {
            case 1:

                switch (direzione) {
                    case Costanti.SINISTRA:
                    case Costanti.FALSA_DESTRA:
                        res = corpoTrovaPuntoAttacco(logic.getPuntiAttaccoLivello1(), 4, 6, logic);
                        x2 = res[0];
                        y2 = res[1];
                        break;

                    case Costanti.DESTRA:
                    case Costanti.FALSA_SINISTRA:
                        res = corpoTrovaPuntoAttacco(logic.getPuntiAttaccoLivello1(), 2, 0, logic);
                        x2 = res[0];
                        y2 = res[1];
                        break;
                }

                break; //End livello 1

            case 2:

                switch (direzione) {
                    case Costanti.SINISTRA:
                    case Costanti.FALSA_DESTRA:
                        res = corpoTrovaPuntoAttacco(logic.getPuntiAttaccoLivello2(), 4, 6, logic);
                        x2 = res[0];
                        y2 = res[1];
                        break;

                    case Costanti.DESTRA:
                    case Costanti.FALSA_SINISTRA:
                        res = corpoTrovaPuntoAttacco(logic.getPuntiAttaccoLivello2(), 2, 0, logic);
                        x2 = res[0];
                        y2 = res[1];
                        break;
                }

                break; //End livello 2
                
            default: //livello >= 3                
                switch (direzione) {
                    case Costanti.SINISTRA:
                    case Costanti.FALSA_DESTRA:
                        res = corpoTrovaPuntoAttacco(logic.getPuntiAttaccoLivello3(), 4, 6, logic);
                        x2 = res[0];
                        y2 = res[1];
                        break;

                    case Costanti.DESTRA:
                    case Costanti.FALSA_SINISTRA:
                        res = corpoTrovaPuntoAttacco(logic.getPuntiAttaccoLivello3(), 2, 0, logic);
                        x2 = res[0];
                        y2 = res[1];
                        break;
                }

                break; //End livello >= 3

        }

        Logic.getInstance().log("getXpos: " + this.getXPos() + " , getYpos: " + this.getYPos());
        Logic.getInstance().log("X: " + x2 + " , Y: " + y2);

        return new int[]{x2, y2};
    }

    
    /**
     * 
     * @param puntiAttaccoLivello 
     * <br>&nbsp;&nbsp;Array di coordinate (da prendere a coppie) per il livello attuale.
     * <br>&nbsp;&nbsp;L'array ha lunghezza 8 di cui:
     * <br>&nbsp;&nbsp;&nbsp;&nbsp; - le prime due coppie (index 0 e 2) sono per direzione DESTRA e FALSA_SINISTRA
     * <br>&nbsp;&nbsp;&nbsp;&nbsp; - le altre due coppie (index 4 e 6) sono per direzione SINISTRA e FALSA_DESTRA
     * <br>
     * 
     * @param x 
     * <br>&nbsp;&nbsp;Index dell'array di coordinate da cui prendere la posizione.
     * <br>&nbsp;&nbsp;Indica una posizione più <b>interna</b>. Usata per alieni BLU, BOSS, ROSSI
     * <br>
     * @param j
     * <br>&nbsp;&nbsp;Index dell'array di coordinate da cui prendere la posizione.
     * <br>&nbsp;&nbsp;Indica una posizione più <b>esterna</b>. Usata per alieni BLU, BOSS, ROSSI

     */
    public int[] corpoTrovaPuntoAttacco(int[] puntiAttaccoLivello, int x, int j, Logic logic) {

        int x2 = 0, y2 = 0;

        if (this.t == Costanti.TipoAlieno.BLU) {
            x2 = puntiAttaccoLivello[x];      //4 ; 2
            y2 = puntiAttaccoLivello[x + 1];  //5 ; 3
        } else if (this.t == Costanti.TipoAlieno.VIOLA) {
            x2 = puntiAttaccoLivello[j];      //6 ; 0
            y2 = puntiAttaccoLivello[j + 1];  //7 ; 1
        } else { //Alieni ROSSO e BOSS

            //Per far funzionare il codice sia per Costanti.SINISTRA che Costanti.DESTRA 
            //devo invertire ora x e j ma solo se x == 2
            if (x == 2) {
                int tmp = x;
                x = j;
                j = tmp;
            }

            int randomPoint = (logic.getNextRandomInt(2) == 0 ? x : j); // 4 : 6 ; 0 : 2
            x2 = puntiAttaccoLivello[randomPoint];
            y2 = puntiAttaccoLivello[randomPoint + 1];
        }

        return new int[]{x2, y2};
    }

    public void setFormazione(boolean scelta) {
        this.formazione = scelta;
    }
    
    public void setPartitoInAttacco(boolean scelta) {
        this.partitoInAttacco = scelta;
    }
    
    public boolean getPartitoInAttacco() {
        return this.partitoInAttacco;
    }

    public void incrementaIndexCambiaSkinSinistra() {
        this.indexCambiaSkinSinistra = (this.indexCambiaSkinSinistra + 1) % Logic.getInstance().getArrayIndexCambiaSkinSinistra().length;
    }

    public void incrementaIndexCambiaSkinDestra() {
        this.indexCambiaSkinDestra = (this.indexCambiaSkinDestra + 1) % Logic.getInstance().getArrayIndexCambiaSkinDestra().length;
    }

    public void incrementaIndexCambiaSkin(int direzione) {
        if (direzione == Costanti.SINISTRA || direzione == Costanti.FALSA_SINISTRA) {
            incrementaIndexCambiaSkinSinistra();
        } else if (direzione == Costanti.DESTRA || direzione == Costanti.FALSA_DESTRA) {
            incrementaIndexCambiaSkinDestra();
        }
    }

    public void resetIndexCambiaSkin(int direzione) {
        if (direzione == Costanti.FALSA_SINISTRA) {
            indexCambiaSkinSinistra = 0;
        } else if (direzione == Costanti.FALSA_DESTRA) {
            indexCambiaSkinDestra = 0;
        }
    }

    public int getNumeroPuntiFalsaCurva(int direzione) {
        if (direzione == Costanti.FALSA_SINISTRA) {
            return Logic.getInstance().getNumeroPuntiFalsaCurvaSinistra();
        } else if (direzione == Costanti.FALSA_DESTRA) {
            return Logic.getInstance().getNumeroPuntiFalsaCurvaDestra();
        }

        return 0;
    }

    public int[] kamikazeAnim(int livello, int direzione, int posXalieno, int posYalieno, Logic logic) {
        int posXgiocatore = logic.getPosizioneGiocatore();
        int posYgiocatore = Costanti.STARTGIOCATORE;

        int posXfinale = 0;
        int posYfinale = 0;

        int spostamento = (livello >= 3 ? 60 : 90);
        int marginePeggioraMira = (livello >= 3 ? 0 : 90);

        if ((direzione == Costanti.SINISTRA || direzione == Costanti.FALSA_DESTRA) && (posXgiocatore >= posXalieno)) {
            //Se livello >= 3, ad altezza giocatore, l'alieno è a 20 px a sinistra della posizione di partenza (fine seconda curva)
            //Se livello == 2 aggiungo un margine di disturbo verso sinistra
            posXfinale = posXalieno - spostamento;
            posYfinale = ((posYgiocatore - posYalieno) * 3) + posYalieno;
        } else if ((direzione == Costanti.SINISTRA || direzione == Costanti.FALSA_DESTRA) && (posXgiocatore < posXalieno)) {
            //Se livello >= 3 colpisco giocatore 
            //Se livello == 2 aggiungo un margine di disturbo (positivo o negativo) in base a dove sta il giocatore

            if (posXgiocatore < (Costanti.LARGHEZZA / 4)) {
                posXgiocatore += marginePeggioraMira;
            } else {
                posXgiocatore -= marginePeggioraMira;
            }

            posXfinale = ((posXgiocatore - posXalieno) * 3) + posXalieno;
            posYfinale = ((posYgiocatore - posYalieno) * 3) + posYalieno;
        } else if ((direzione == Costanti.DESTRA || direzione == Costanti.FALSA_SINISTRA) && (posXgiocatore <= posXalieno)) {
            //Se livello >= 3, ad altezza giocatore, l'alieno è a 20 px a destra della posizione di partenza (fine seconda curva)
            //Se livello == 2 aggiungo un margine di disturbo verso destra
            posXfinale = posXalieno + spostamento;
            posYfinale = ((posYgiocatore - posYalieno) * 3) + posYalieno;
        } else if ((direzione == Costanti.DESTRA || direzione == Costanti.FALSA_SINISTRA) && (posXgiocatore > posXalieno)) {
            //Se livello >= 3 colpisco giocatore 
            //Se livello == 2 aggiungo un margine di disturbo (positivo o negativo) in base a dove sta il giocatore

            if (posXgiocatore > (Costanti.LARGHEZZA * 3 / 4)) {
                posXgiocatore -= marginePeggioraMira;
            } else {
                posXgiocatore += marginePeggioraMira;
            }

            posXfinale = ((posXgiocatore - posXalieno) * 3) + posXalieno;
            posYfinale = ((posYgiocatore - posYalieno) * 3) + posYalieno;
        }

        return new int[]{posXfinale, posYfinale};
    }

    @Override
    public void setImmaginiInCache() {

        //Tutte le immagini devono avere nome con struttura: nome + numero + .estensione
        //Mi scorro quindi le foto modificando di volta in volta il numero della foto 
        //Inoltre le foto di ogni alieno sono divise in due gruppi da 10 foto l'uno
        //Tutte iniziano da uno specifico numero ed ecco perchè uso una costante per distinguerli        
        Logic logic = Logic.getInstance();
        int numFotoAlieno = 20;

        final int COSTANTE = trovaCostante();

        StringBuilder path = new StringBuilder(this.getPrimaImmagine());

        for (int i = 0; i < numFotoAlieno; i++) {
            if (i < 10) {
                path.setCharAt(path.length() - 5, Character.forDigit(i, 10));
            } else {
                if (i == 10) {
                    path.setCharAt(path.length() - 6, Character.forDigit((COSTANTE + 10) / 10, 10));
                }
                path.setCharAt(path.length() - 5, Character.forDigit(i - 10, 10));
            }

            Logic.getInstance().log("Ho mandato in cache: " + path.toString());
            logic.getDimensioneImmagine(path.toString());
        }
    }
    
    public abstract int getPunti();
}
