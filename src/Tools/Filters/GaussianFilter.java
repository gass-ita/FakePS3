package Tools.Filters;

import Layer.Layer;
import Tools.Filter;


public class GaussianFilter implements Filter {

    /* DEFAULT VARIABLES */
    private static final double DEFAULT_SIGMA = 10;
    private double sigma = DEFAULT_SIGMA;

    // size = 2*pi*sigma
    private int size = (int) Math.round(2 * (int) Math.PI * sigma);


    @Override
    public void apply(Layer layer, int color, int x, int y) throws Exception {
        int[][] image = layer.getPixels();
        double[][] mask = getMask();

        int[][] result = Filter.convolution(image, mask);

        for (int yi = 0; yi < result.length; yi++) {
            for (int xi = 0; xi < result[0].length; xi++) {
                layer.setPixel(xi, yi, result[yi][xi]);
            }
        }
    }

    @Override
    public double[][] getMask() {
        double sigma = getSigma();
        int size = getSize();
        
        double[][] mask = new double[size][size];

        double sum = 0;
        for (int yi = 0; yi < size; yi++) {
            for (int xi = 0; xi < size; xi++) {
                double x = xi - size / 2;
                double y = yi - size / 2;
                mask[yi][xi] = Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
                sum += mask[yi][xi];
            }
        }

        /* normalize */

        for (int yi = 0; yi < size; yi++) {
            for (int xi = 0; xi < size; xi++) {
                mask[yi][xi] /= sum;
            }
        }
        Filter.saveMaskToImage(mask, "test.png");
        return mask;

    }


    

    

    public void setSigma(double sigma) {
        this.sigma = sigma;
        this.size = (int) Math.round(2 * (int) Math.PI * sigma);
    }

    public double getSigma() {
        return sigma;
    }

    public int getSize() {
        return size;
    }
    
}
