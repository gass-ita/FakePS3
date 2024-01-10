package Tools.Filters;

import Layer.Layer;
import Tools.LinearFilter;

public class ImpulseFilter extends LinearFilter{

    /* DEFAULT VARIABLES */
    public static final int DEFAULT_SIZE = 3;

    private int size = DEFAULT_SIZE;

    public double[][] getMask(){
        double[][] mask = new double[size][size];
        /* the center will be 1 */
        mask[size/2][size/2] = 1;
        return mask;
    }
}
