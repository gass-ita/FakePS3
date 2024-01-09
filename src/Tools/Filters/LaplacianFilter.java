package Tools.Filters;

import Layer.Layer;
import Tools.Filter;

public class LaplacianFilter implements Filter {
    /* DEFAULT VARIABLES */
    public static final int DEFAULT_SIZE = 3;

    private int size = DEFAULT_SIZE;

    @Override
    public void apply(Layer layer, int color, int x, int y) throws Exception {
        int[][] image = layer.getPixels();
        double[][] mask = getMask(image, x, y);
        int[][] result = Filter.fullConvolution(image, mask);

        for (int yi = 0; yi < result.length; yi++) {
            for (int xi = 0; xi < result[0].length; xi++) {
                layer.setPixel(xi, yi, result[yi][xi]);
            }
        }
    }

    @Override
    public double[][] getMask(int[][] image, int x, int y) {
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
}