package jgalaxian.Utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

public class WriteCSV {

    public static boolean print(String fileName, String charset, LinkedList<String[]> lstSA) {
        try (PrintWriter printWriter = new PrintWriter (
                new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(fileName), charset)), true)) {
            
            for (String[] sArr : lstSA)
                for (int i = 0; i < sArr.length; i++)
                    if (i < (sArr.length - 1))
                        printWriter.print(sArr[i] + ";");
                    else
                        printWriter.print(sArr[i] + "\r\n");
            
            return true;
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return false;
        }   catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
}
