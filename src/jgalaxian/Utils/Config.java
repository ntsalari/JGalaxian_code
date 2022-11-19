package jgalaxian.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {

    //STATIC FIELDS
    private static Config config = null;

    //INSTANCE FIELDS
    private Properties properties;
    private String pathNomeFile;
    
    private Config() {
        BufferedReader buffRead = null;
        this.pathNomeFile = "/Resources/Config/configuration.txt";
        
        try {
            buffRead = new BufferedReader(
                new InputStreamReader(
                    this.getClass().getResourceAsStream("/Resources/Config/configuration.txt"), "ISO-8859-1"));

            this.properties = new Properties();
            this.properties.load(buffRead);
        }
        catch(FileNotFoundException | NullPointerException fnfe) {
            Utils.getInstance().exitGamePerErroreSerio(
                    "File di configurazione non trovato, il programma verrà chiuso.", 
                    "Errore SERIO"
            );
            System.exit(-1);
        }
        catch(IOException ioe) {

            Utils.getInstance().exitGamePerErroreSerio(
                    "Impossibile leggere il file di configurazione, il programma verrà chiuso.", 
                    "Errore SERIO"
            );            
            System.exit(-1);
        }
        finally {
            try {
                if (buffRead != null)
                    buffRead.close();
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    //STATIC METHODS - DESIGN PATTERN SINGLETON
    public static Config getInstance() {
        if (config == null)
            config = new Config();
        
        return config;
    }
    
    //In caso l'utente cambi profilo gioccatore rispetto quello scelto inizialmente            
    public void cambiaConfigFile(String path) {
        
        this.pathNomeFile = path;

        BufferedReader buffRead = null;
        try {
            //Se devo accedere al file di configurazione "generale" del game
            if (path.contains("Resources")) {
                buffRead = new BufferedReader(
                    new InputStreamReader(
                        this.getClass().getResourceAsStream(path), "ISO-8859-1"));
            } else {
                //Se devo accedere al file di configurazione di un utente
                buffRead = new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(path), "ISO-8859-1"));
            }            
            
            this.properties = new Properties();
            this.properties.load(buffRead);
        }
        catch(FileNotFoundException | NullPointerException fnfe) {
            
            Utils.getInstance().exitGamePerErroreSerio(
                    "File di configurazione non trovato, il programma verrà chiuso.", 
                    "Errore SERIO"
            );            
            System.exit(-1);

        }
        catch(IOException ioe) {
            
            Utils.getInstance().exitGamePerErroreSerio(
                    "Impossibile leggere il file di configurazione, il programma verrà chiuso.", 
                    "Errore SERIO"
            );            
            System.exit(-1);

        }
        finally {
            try {
                if (buffRead != null)
                    buffRead.close();
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }


    //Gli unici file di configurazione da creare sono quelli dei nuovi utenti
    //I file di configurazione generali non devono essere creati
    //Se il file che si tenta di creare è già esistente, questo è un errore grave in quanto
    //doveva essere fatto un controllo a monte sull'unicità del nome dell'utente in fase
    //di creazione
    public void creaConfigFile(String Nome, String Sparo, String Sinistra, String Destra) {
        
        File theDir = new File("ConfigUtenti");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        
        String nomeFile = Nome + ".txt";
        String path = "ConfigUtenti/" + nomeFile;
        
        File configFile = new File(path);
        try { 
            boolean isFileCreated = configFile.createNewFile();
            
            if (!isFileCreated) {
                Utils.getInstance().exitGamePerErroreSerio(
                        "Esiste già un file di configurazione con questo nome: " + nomeFile + " .\nIl programma verrà chiuso.", 
                        "Errore SERIO"
                );
                System.exit(-1);
            } 
            
            try {
                FileWriter myWriter = new FileWriter(path);
                myWriter.write("Nome = " + Nome + "\n");
                myWriter.write("Sparo = " + Sparo + "\n");
                myWriter.write("Sinistra = " + Sinistra + "\n");
                myWriter.write("Destra = " + Destra + "\n");
                myWriter.close();
                //Utils.getInstance().log("File di configurazione scritto con successo.");
            } catch (IOException e) {
                Utils.getInstance().exitGamePerErroreSerio(
                        "Errore nella scrittura del file di configurazione.\nIl programma verrà chiuso.", 
                        "Errore SERIO"
                );
                System.exit(-1);
            }
        } catch (IOException ex) {
            Utils.getInstance().exitGamePerErroreSerio(
                    "Errore nella scrittura del file di configurazione.\nIl programma verrà chiuso.", 
                    "Errore SERIO"
            );
            System.exit(-1);
        }
    }
    
    public String getNome() {
        return this.properties.getProperty("Nome");
    }
    
    public String getTastoSparo() {
        return this.properties.getProperty("Sparo");
    }
    
    public String getTastoMuoviSinistra() {
        return this.properties.getProperty("Sinistra");
    }

    public String getTastoMuoviDestra() {
        return this.properties.getProperty("Destra");
    }
    
        public String[] getCurvaSinistra() {
        return this.properties.getProperty("curvaSinistra").split(", "); 
    }
    
    public String getDiametroCurvaSinistra() {
        return this.properties.getProperty("diametroCurvaSinistra");
    }

    public String[] getCambiaSkinSinistra() {
        return this.properties.getProperty("cambiaSkinSinistra").split(", ");
    }
    
    public String getNumeroPuntiFalsaCurvaSinistra() {
        return this.properties.getProperty("numeroPuntiFalsaCurvaSinistra");
    }
        
    public String[] getCurvaDestra() {
        return this.properties.getProperty("curvaDestra").split(", ");
    }
    
    public String getDiametroCurvaDestra() {
        return this.properties.getProperty("diametroCurvaDestra");
    }
    
    public String[] getCambiaSkinDestra() {
        return this.properties.getProperty("cambiaSkinDestra").split(", ");
    }
    
    public String getNumeroPuntiFalsaCurvaDestra() {
        return this.properties.getProperty("numeroPuntiFalsaCurvaDestra");
    }
    
    public String[] getSecondaCurvaSinistra() {
        return this.properties.getProperty("curvaSinistraSeconda").split(",");
    }
    
    public String[] getSecondaCurvaDestra() {
        return this.properties.getProperty("curvaDestraSeconda").split(",");
    } 
            
    public String[] getPuntiFineAttaccoSinistra() {
        return this.properties.getProperty("puntiFineAttaccoSinistra").split(",");
    } 
    
    public String[] getPuntiFineAttaccoDestra() {
        return this.properties.getProperty("puntiFineAttaccoDestra").split(",");
    } 
    
    public String[] getPuntiAttaccoLivello1() {
        return this.properties.getProperty("puntiAttaccoLivello1").split(",");
    }
    
    public String[] getPuntiAttaccoLivello2() {
        return this.properties.getProperty("puntiAttaccoLivello2").split(",");
    }
    
    public String[] getPuntiAttaccoLivello3() {
        return this.properties.getProperty("puntiAttaccoLivello3").split(",");
    }
    
    public void setTastoSparo(String tasto) {
        this.properties.setProperty("Sparo", tasto);
        String pathFinale = "";
        
        try {
            if (this.pathNomeFile.contains("Resources")) {
                pathFinale = this.getClass().getResource(this.pathNomeFile).toString().replace("jar:file:/", "").replace("!", "");
                Utils.getInstance().log("URL CONFIG FILE: " + pathFinale);
                FileWriter writer = new FileWriter(pathFinale);
                this.properties.store(writer, "setTastoSparo");
                writer.close();
                

            } else {
                Utils.getInstance().log("URL CONFIG FILE: " + this.pathNomeFile);
                FileWriter writer = new FileWriter(this.pathNomeFile);
                this.properties.store(writer, "setTastoSparo");
                writer.close();
                
                pathFinale = this.pathNomeFile;
            }
        } catch (FileNotFoundException ex) {
            Utils.getInstance().log("Il file di configurazione " + pathFinale + " non esiste in setTastoSparo");
        } catch (IOException ex) {
            Utils.getInstance().log("Errore I/O per il file di configurazione " + pathFinale + " in setTastoSparo");
        }
    }
    
    public void setTastoMuoviSinistra(String tasto) {
        this.properties.setProperty("Sinistra", tasto);
        String pathFinale = "";
        
        try {
            if (this.pathNomeFile.contains("Resources")) {
                pathFinale = this.getClass().getResource(this.pathNomeFile).toString().replace("jar:file:/", "").replace("!", "");
                Utils.getInstance().log("URL CONFIG FILE: " + pathFinale);
                FileWriter writer = new FileWriter(pathFinale);
                this.properties.store(writer, "setTastoMuoviSinistra");
                writer.close();

            } else {
                Utils.getInstance().log("URL CONFIG FILE: " + this.pathNomeFile);
                FileWriter writer = new FileWriter(this.pathNomeFile);
                this.properties.store(writer, "setTastoMuoviSinistra");
                writer.close();
                
                pathFinale = this.pathNomeFile;
            }
        } catch (FileNotFoundException ex) {
            Utils.getInstance().log("Il file di configurazione " + pathFinale + " non esiste in setTastoMuoviSinistra");
        } catch (IOException ex) {
            Utils.getInstance().log("Errore I/O per il file di configurazione " + pathFinale + " in setTastoMuoviSinistra");
        }
    }

    public void setTastoMuoviDestra(String tasto) {
        this.properties.setProperty("Destra", tasto);
        String pathFinale = "";
    
            try {
            if (this.pathNomeFile.contains("Resources")) {
                pathFinale = this.getClass().getResource(this.pathNomeFile).toString().replace("jar:file:/", "").replace("!", "");
                Utils.getInstance().log("URL CONFIG FILE: " + pathFinale);
                FileWriter writer = new FileWriter(pathFinale);
                this.properties.store(writer, "setTastoMuoviDestra");
                writer.close();

            } else {
                Utils.getInstance().log("URL CONFIG FILE: " + this.pathNomeFile);
                FileWriter writer = new FileWriter(this.pathNomeFile);
                this.properties.store(writer, "setTastoMuoviDestra");
                writer.close();
                
                pathFinale = this.pathNomeFile;
           }
        } catch (FileNotFoundException ex) {
            Utils.getInstance().log("Il file di configurazione " + pathFinale + " non esiste in setTastoMuoviDestra");
        } catch (IOException ex) {
            Utils.getInstance().log("Errore I/O per il file di configurazione " + pathFinale + " in setTastoMuoviDestra");
        }
    }
        
    public String[] getNomiConfigFileProfiliUtente() {
        File theDir = new File("ConfigUtenti");
        if (!theDir.exists())
            return null;
        
        return theDir.list();
    }
    
}
