package Tools.Filters;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;

import javax.imageio.ImageIO;

import Layer.Layer;
import Tools.Filter;

public class GaussianFilter implements Filter {

    /* DEFAULT VARIABLES */
    private static final double DEFAULT_SIGMA = 0.1;

    private double sigma = DEFAULT_SIGMA;
    // size = 2*pi*sigma
    private int size = (int) Math.round(2 * (int) Math.PI * sigma);


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

    @Override
    public double[][] getMask(int size) {
        /* generate a mask based on the gaussian function */
        double[][] mask = new double[size][size];

        //size = 2*pi*sigma
        double sigma = size / (2 * Math.PI);

        //mask[i][j] = (1)/(2*pi*sigma^2) * e^(-1/2 * ((i^2 + j^2)/(sigma^2)))
        double sum = 0;
        for (int yi = 0; yi < size; yi++) {
            for (int xi = 0; xi < size; xi++) {
                double value = (1) / (2 * Math.PI * Math.pow(sigma, 2)) * Math.exp(-1 / 2 * ((Math.pow(xi, 2) + Math.pow(yi, 2)) / (Math.pow(sigma, 2))));
                mask[yi][xi] = value;
                sum += value;
            }
        }

        //normalize the mask, the sum of all values must be 1
        for (int yi = 0; yi < size; yi++) {
            for (int xi = 0; xi < size; xi++) {
                mask[yi][xi] /= sum;
            }
        }
        
        

        return mask;
    }
    
}
