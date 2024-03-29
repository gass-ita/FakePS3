package Tools.Filters;

import Tools.LinearFilter;

public class GaussianFilter extends LinearFilter {

    /* DEFAULT VARIABLES */
    private static final double DEFAULT_SIGMA = 5;
    private double sigma = DEFAULT_SIGMA;

    private String name = "Gaussian Filter";

    // size = 2*pi*sigma
    private int size = (int) Math.round(2 * (int) Math.PI * sigma);

    public GaussianFilter() {
        super();
        if (size % 2 == 0)
            size++;
    }

    @Override
    public double[][] getMask() {
        double sigma = getSigma();
        int size = getSize();
        
        double[][] mask = generateGaussian(sigma, size);
        return mask;
    }

    public static double[][] generateGaussian(double sigma, int size) {
        if (size % 2 == 0)
            size++;
            
        double[][] gaussianMatrix = new double[size][size];

        // Calculate the center of the matrix
        int centerX = size / 2;
        int centerY = size / 2;

        double sum = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                    
                    gaussianMatrix[i][j] = Math.exp(-((i - centerX) * (i - centerX) + (j - centerY) * (j - centerY))
                            / (2 * sigma * sigma));
    
                    sum += gaussianMatrix[i][j];
            }
        }

        // Normalize the matrix
        for (int i = 0; i < gaussianMatrix.length; i++) {
            for (int j = 0; j < gaussianMatrix[0].length; j++) {
                gaussianMatrix[i][j] /= sum;
            }
        }

        return gaussianMatrix;
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

    @Override
    public String setName() {
        return name;
    }

}
