package Tools.Filters;

import Layer.Layer;
import Tools.LinearFilter;


public class GaussianFilter extends LinearFilter {

    /* DEFAULT VARIABLES */
    private static final double DEFAULT_SIGMA = 3;
    private double sigma = DEFAULT_SIGMA;

    // size = 2*pi*sigma
    private int size = (int) Math.round(2 * (int) Math.PI * sigma);

    @Override
    public double[][] getMask() {
        double sigma = getSigma();
        int size = getSize();
        
        double[][] mask = new double[size][size];

        double sum = 0;
        for (int yi = 0; yi < size; yi++) {
            for (int xi = 0; xi < size; xi++) {
                double distanceX = xi - size / 2;
                double distanceY = yi - size / 2;
                mask[yi][xi] = Math.exp(-(distanceX * distanceX + distanceY * distanceY) / (2 * sigma * sigma));
                sum += mask[yi][xi];
            }
        }

        /* normalize */

        for (int yi = 0; yi < size; yi++) {
            for (int xi = 0; xi < size; xi++) {
                mask[yi][xi] /= sum;
            }
        }
        LinearFilter.saveMaskToImage(mask, "test.png");
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
