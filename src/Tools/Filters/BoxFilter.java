package Tools.Filters;

import Layer.Layer;
import Tools.LinearFilter;

public class BoxFilter implements LinearFilter{
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

    @Override
    public void apply(Layer layer, int color, int x, int y) throws Exception {
        int[][] image = layer.getPixels();
        double[][] mask = getMask();
        int[][] result = LinearFilter.fullConvolution(image, mask);

        for (int yi = 0; yi < result.length; yi++) {
            for (int xi = 0; xi < result[0].length; xi++) {
                layer.setPixel(xi, yi, result[yi][xi]);
            }
        }
    }
}
