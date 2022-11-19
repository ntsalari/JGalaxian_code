package jgalaxian.Logic;

import java.util.ArrayList;

public class Retta {
    
    public static int[] retta(int x1, int y1, int x2, int y2) {
        int dx, dy;
        dx = Math.abs(x2 - x1);
        dy = Math.abs(y2 - y1);
        
        //i valori x1 & y1, x2 & y2, dx & dy vengono scambiati e passati alla funzione plotPixel così
        //può gestire entrambi i casi quando m >= 1 e m < 1

        
        //Se la pendenza è minore di uno
        if (dx > dy) {
            //Passiamo 0 per ottenere (x,y)
            Logic.getInstance().log("Chiamato primo metodo");
            return plotPixel(x1, y1, x2, y2, dx, dy, 0);
        } //Se la pendenza è maggiore o uguale a uno
        else {
            //Passiamo 1 per ottenere (y,x)
            Logic.getInstance().log("Chiamato secondo metodo");
            return plotPixel(y1, x1, y2, x2, dy, dx, 1);
        }
    }

    public static int[] plotPixel(int x1, int y1, int x2, int y2, int dx, int dy, int decide) {

        ArrayList<Integer> lin = new ArrayList<>(20);
        int pk = 2 * dy - dx;
        for (int i = 0; i <= dx; i++) {
            if (decide == 0) {
                //Logic.getInstance().log(x1 + "," + y1 + "\n");
            } else {
                //Logic.getInstance().log(y1 + "," + x1 + "\n");
            }
            lin.add(x1);
            lin.add(y1);

            if (x1 < x2) {
                x1++;
            } else {
                x1--;
            }
            if (pk < 0) {
                //il valore della decisione deciderà di tracciare
                //o x1 o y1 nella posizione di x
                if (decide == 0) {
                    pk = pk + 2 * dy;
                } else {
                    pk = pk + 2 * dy;
                }
            } else {
                if (y1 < y2) {
                    y1++;
                } else {
                    y1--;
                }
                pk = pk + 2 * dy - 2 * dx;
            }
        }
        int linea[] = new int[lin.size()];
        for (int i = 0; i < linea.length; i += 2) {
            if (decide == 0) {
                linea[i] = lin.get(i);
                linea[i + 1] = -lin.get(i + 1);
            } else {
                linea[i] = lin.get(i + 1);
                linea[i + 1] = -lin.get(i);
            }
        }
        Logic.getInstance().log("\nNumero punti segmento: " + Integer.toString(linea.length / 2));
        return linea;
    }
}
