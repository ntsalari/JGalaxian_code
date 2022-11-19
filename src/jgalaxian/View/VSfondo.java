package jgalaxian.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JPanel;

public class VSfondo extends JPanel {

    private byte matrix[][];
    private Color[] grigi;
    private final int rows;
    private final int cols;

    public VSfondo() {
        super();
        View view = View.getInstance();
        Dimension d = new Dimension(view.getCostanteLARGHEZZA(), view.getCostanteALTEZZA());
        this.setPreferredSize(d);
        this.setSize(d);
        rows = view.getCostanteALTEZZA() / 2;
        cols = view.getCostanteLARGHEZZA() / 2;
        matrix = new byte[rows][cols];
        grigi = new Color[8];
        int k = 0;
        for (int i = 0; i < 7; i++) {
            grigi[i] = new Color(255, 255, 255, k);
            k += 32;
        }
        grigi[7] = new Color(255, 255, 255, 255);
        this.setOpaque(true);
        this.setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] != 0) {
                    g.setColor(grigi[abs(matrix[i][j]) - 1]);
                    g.fillRect(j * 2, i * 2, 2, 2);
                }
            }
        }
    }

    public void initMatrix() {
        matrix = new byte[rows][cols];
        Random r = new Random();
        int val;
        int k = rows;
        int l = cols;
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < l; j++) {
                if (r.nextInt(512) == 256) {
                    val = (1 + r.nextInt(7));
                    if (r.nextInt(10000) <= 5000) {
                        matrix[i][j] = (byte) val;
                    } else {
                        matrix[i][j] = (byte) (-val);
                    }
                } else {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public void setMatrix(byte[][] matrix) {
        this.matrix = matrix;
    }

    public byte[][] getMatrix() {
        return this.matrix;
    }

    public void nextAnim() {
        int val;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                val = matrix[i][j];
                if (val != 0) {
                    if (val > 0) {
                        if (val >= 8) {
                            val = -1;
                        } else {
                            val++;
                        }
                    } else {
                        if (val <= -8) {
                            val = 1;
                        } else {
                            val--;
                        }
                    }
                    matrix[i][j] = (byte) val;
                }
            }
        }
        byte row[] = matrix[rows - 1];
        for (int i = rows - 1; i > 0; i--) {
            matrix[i] = matrix[i - 1];
        }
        matrix[0] = row;
    }

    private static int abs(int b) {
        if (b < 0) {
            return -b;
        }
        return b;
    }
}
