package jgalaxian.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Classifica {

    public static boolean aggiornaClassifica(String nomeGiocatore, int punteggio) {

        String fileName = "classifica.csv";
        String charset = "UTF-8";

        //0 indica che leggo tutto il file
        LinkedList<String[]> lstRows = ReadCSV.getRows(fileName, charset, 0);

        //C'è stato un errore durante la read della classifica
        if (lstRows == null) {
            return false;
        }

        Utils.getInstance().log("Numero giocatori in classifica: " + lstRows.size() + "\n");
        Utils.getInstance().log("Classifica: " + lstRows + "\n");

        LinkedList<String[]> pData = new LinkedList<String[]>();

        int lenArray = lstRows.size();

        if (lenArray == 0) {
            pData.add(new String[]{"1", nomeGiocatore, String.valueOf(punteggio)});
            Utils.getInstance().log("Posizione: 1" + "\nNome: " + nomeGiocatore + "\nPunteggio: " + punteggio + "\n");
        } else {
            boolean inserito = false;
            for (int i = 0, add = 0; i < lenArray; i++) {

                String[] sArr = lstRows.get(i);

                if (Integer.valueOf(sArr[2]) < punteggio && !inserito) {
                    pData.add(new String[]{String.valueOf(i + 1), nomeGiocatore, String.valueOf(punteggio)});
                    Utils.getInstance().log("Posizione: " + String.valueOf(i + 1) + "\nNome: " + nomeGiocatore + "\nPunteggio: " + punteggio + "\n");
                    add++;
                    inserito = true;
                }

                pData.add(new String[]{String.valueOf(Integer.valueOf(sArr[0]) + add), sArr[1], sArr[2]});
                Utils.getInstance().log("Posizione: " + String.valueOf(Integer.valueOf(sArr[0]) + add) + "\nNome: " + sArr[1] + "\nPunteggio: " + Integer.valueOf(sArr[2]) + "\n");

                //Se è l'ultimo classificato me ne accorgo solo qua e lo aggiungo alla classifica
                if ((i == (lenArray - 1)) && (punteggio <= Integer.valueOf(sArr[2]))) {
                    i++;
                    pData.add(new String[]{String.valueOf(i + 1), nomeGiocatore, String.valueOf(punteggio)});
                    Utils.getInstance().log("Posizione: " + String.valueOf(i + 1) + "\nNome: " + nomeGiocatore + "\nPunteggio: " + punteggio + "\n");
                }
            }
        }

        try {
            Files.deleteIfExists(Paths.get(fileName));
            Utils.getInstance().log("Vecchia classifica eliminata con successo.");
        } catch (NoSuchFileException e) {
            Utils.getInstance().log("Non è stato trovato il file della vecchia classifica");
            return false;
        } catch (IOException e) {
            Utils.getInstance().log("Non hai i permessi per eliminare il file della vecchia classifica.");
            return false;
        }

        return WriteCSV.print(fileName, charset, pData);
    }
    
    public static int getPunteggioMigliore() {

        String fileName = "classifica.csv";
        String charset = "UTF-8";

        //0 indica che leggo tutto il file
        LinkedList<String[]> lstRows = ReadCSV.getRows(fileName, charset, 1);

        //C'è stato un errore durante la read della classifica
        if (lstRows == null) {
            return 0;
        }
        
        Utils.getInstance().log("Lunghezza stringa punteggio migliore: " + lstRows.size());
        String punteggio;
        
        try {
            punteggio = (lstRows.get(0))[2];            
        } catch (IndexOutOfBoundsException ioobe) {
            Utils.getInstance().log("Errore reperimento punteggio migliore");
            return 0;
        }

        Utils.getInstance().log("Punteggio migliore in classifica: " + punteggio);        
        return Integer.parseInt(punteggio);

    }
    
    public static String[][] getPrimeNRigheClassifica(int n) {

        String fileName = "classifica.csv";
        String charset = "UTF-8";

        //0 indica che leggo tutto il file
        LinkedList<String[]> lstRows = ReadCSV.getRows(fileName, charset, n);

        //C'è stato un errore durante la read della classifica
        if (lstRows == null) {
            Utils.getInstance().log("ERRORE: Errore reperimento prime " + n + " righe di classifica");
            return null;
        } else if (lstRows.size() == 0) {
            Utils.getInstance().log("La classifica per ora è vuota");
            return null;
        }
        
        int lenCampiClassifica = (lstRows.getFirst()).length;
        String[][] res = new String[lstRows.size()][lenCampiClassifica];

                
        try {
            for (int i = 0; i < lstRows.size(); i++) {
                String[] elemento = lstRows.get(i);
                for (int j = 0; j < lenCampiClassifica; j++) {
                    res[i][j] =  elemento[j];
                }
            }
        } catch (IndexOutOfBoundsException ioobe) {
            Utils.getInstance().log("ERRORE: Errore reperimento primeNRigheClassifica");
            return null;
        }

        return res;
    }
}
