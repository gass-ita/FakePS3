package Tools.Filters;

import Layer.Layer;
import Tools.Filter;

public class Impulse implements Filter{

    /* DEFAULT VARIABLES */
    public static final int DEFAULT_SIZE = 3;

    private int size = DEFAULT_SIZE;

    public double[][] getMask(int size){
        double[][] mask = new double[size][size];
        /* the center will be 1 */
        mask[size/2][size/2] = 1;
        return mask;
    }

    @Override
    public void apply(Layer layer, int color, int x, int y) throws Exception {
        int[][] image = layer.getPixels();
        double[][] mask = getMask(size);
        int[][] result = Filter.convolution(image, mask);

        for (int yi = 0; yi < result.length; yi++) {
            for (int xi = 0; xi < result[0].length; xi++) {
                layer.setPixel(xi, yi, result[yi][xi]);
            }
        }
    }


}
