package Tools.Filters;

import Tools.LinearFilter;

public class LaplacianFilter extends LinearFilter {
    /* DEFAULT VARIABLES */
    public static final int DEFAULT_SIZE = 3;

    private int size = DEFAULT_SIZE;

    private String name = "Laplacian Filter";

    @Override
    public double[][] getMask() {
        /* Shift and subtract */
        double[][] mask = new double[size][size];
        for (int yi = 0; yi < size; yi++) {
            for (int xi = 0; xi < size; xi++) {
                mask[yi][xi] = -1;
            }
        }
        mask[size / 2][size / 2] = size * size - 1;
        return mask;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String setName() {
        return name;
    }
}
