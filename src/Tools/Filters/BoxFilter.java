package Tools.Filters;

import Tools.LinearFilter;

public class BoxFilter extends LinearFilter{
    /* DEFAULT VARIABLES */
    public static final int DEFAULT_SIZE = 5;

    private int size = DEFAULT_SIZE;
    
    public double[][] getMask(){
        double[][] mask = new double[size][size];
        /* fill the mask except the borders */
        for (int yi = 1; yi < size-1; yi++) {
            for (int xi = 1; xi < size-1; xi++) {
                mask[yi][xi] = 1;
            }
        }
        return mask;
    }
}
