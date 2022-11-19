package jgalaxian.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class ReadCSV {

    public static LinkedList<String[]> getRows(String fileName, String charset, int numRigheDaLeggere) {
        //Inizializzo la lista 1stRows che conterrà poi i campi di ogni riga
        LinkedList<String[]> lstRows = null;

        try {
            Path path = Paths.get(fileName);
            Utils.getInstance().log("classifica.csv path: " + path);
            if (!Files.exists(path)) {
                File csvClassifica = new File(fileName);
                boolean isFileCreated = csvClassifica.createNewFile();
                Utils.getInstance().log("isFileCreated: " + isFileCreated);
            }
        } catch (IOException ex) {
            Utils.getInstance().exitGamePerErroreSerio(
                "Errore nella creazione del file di classifica.\nIl programma verrà chiuso.",
                "Errore SERIO"
            );
            System.exit(1);
        }

        try ( BufferedReader buffRead = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileName), charset))) {

            lstRows = new LinkedList<String[]>();
            String s = null;

            if (numRigheDaLeggere == 0) {
                while ((s = buffRead.readLine()) != null) {//Finchè ci sono cose da leggere
                    if (!s.isEmpty() && s.contains(";")) //E se s contiene il ";" e la riga non è vuota
                    {
                        lstRows.add(s.trim().split(";")); //Aggiungo il contenuto del buffer sulla linked list
                    }
                }
            } else {
                for (int i = 0; i < numRigheDaLeggere; i++) {
                    if ((s = buffRead.readLine()) != null)
                        lstRows.add(s.trim().split(";")); 
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        return lstRows;
    }
}
