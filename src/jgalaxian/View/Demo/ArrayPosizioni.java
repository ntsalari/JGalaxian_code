package jgalaxian.View.Demo;

import java.util.ArrayList;

public class ArrayPosizioni {

    private ArrayList<int[]> coordinate;
    private int indice;
    private int massimo;
    private static ArrayPosizioni singleton;

    private ArrayPosizioni() {
        coordinate = new ArrayList<>(300);
        indice = 0;
        massimo = 300;
        for (int i = 0; i < massimo; i++) {
            coordinate.add(new int[2]);
        }
    }

    public static ArrayPosizioni getInstance() {
        if (singleton == null) {
            singleton = new ArrayPosizioni();
        }
        return singleton;
    }

    public int[] getArray() {
        indice = (indice + 1) % massimo;
        return coordinate.get(indice);
    }

    public int[] getArray(int x, int y) {
        indice = (indice + 1) % massimo;
        int[] xy = coordinate.get(indice);
        xy[0] = x;
        xy[1] = y;
        return xy;
    }
}
