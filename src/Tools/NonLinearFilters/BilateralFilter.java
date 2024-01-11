package Tools.NonLinearFilters;

import Layer.Layer;
import Tools.NonLinearFilter;
import Tools.Filters.BorderSolution;
import Tools.Filters.GaussianFilter;
import Utils.ColorConverter;
import Utils.Debugger;

public class BilateralFilter extends NonLinearFilter {

    static final double DEFAULT_SIGMA_S = 4;
    static final double DEFAULT_SIGMA_B = 35;

    private double sigma_s = DEFAULT_SIGMA_S;
    private double sigma_b = DEFAULT_SIGMA_B;

    private int size_s = (int) Math.round(2 * (int) Math.PI * sigma_s);
    private int size_b = (int) Math.round(2 * (int) Math.PI * sigma_b);

    private double[][] gaussian_s;
    private double[][] gaussian_b;

    private String name = "Bilateral Filter";

    public BilateralFilter() {
        super();
        if (size_s % 2 == 0)
            size_s++;
        if (size_b % 2 == 0)
            size_b++;

        gaussian_s = GaussianFilter.generateGaussian(sigma_s, size_s);
        gaussian_b = GaussianFilter.generateGaussian(sigma_b, size_b);
    }

    @Override
    public int[][] applyFilter(Layer layer, int color, int x, int y) throws Exception {
        int[][] image = layer.getPixels();
        int size = this.size_s;
        BorderSolution borderSolution = BorderSolution.PAD_WITH_REFLECTION;
        int[][] new_image = new int[image.length][image[0].length];

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                new_image[i][j] = convolution(image, size, j, i, borderSolution);
            }
        }

        return new_image;
    }

    @Override
    public int convolution(int[][] image, int size, int x, int y, BorderSolution borderSolution) throws Exception {
        if (x < 0 || x >= image[0].length || y < 0 || y >= image.length) {
            Debugger.err("x or y out of bounds of the image");
            throw new IllegalArgumentException("x or y out of bounds of the image");
        }

        if (borderSolution == BorderSolution.IGNORE_BORDER)
            if (x < size / 2 || y < size / 2 || x >= (image[0].length - size / 2)
                    || y >= (image.length - size / 2))
                return image[y][x];

        int[][] c_image = generate_c_image(image, x, y, size, size, borderSolution);

        int center_pixel = image[y][x];

        double Wsb = 0;

        double newRed = 0;
        double newGreen = 0;
        double newBlue = 0;

        for (int m = 0; m < c_image.length; m++) {
            for (int n = 0; n < c_image[0].length; n++) {
                int pixel = c_image[m][n];
                int r = ColorConverter.getRed(pixel);
                int g = ColorConverter.getGreen(pixel);
                int b = ColorConverter.getBlue(pixel);

                int r_center = ColorConverter.getRed(center_pixel);
                int g_center = ColorConverter.getGreen(center_pixel);
                int b_center = ColorConverter.getBlue(center_pixel);

                double gaussian_s = this.gaussian_s[size - 1 - m][size - 1 - n];
                /*
                 * calculate the brightness difference between the central color and the actual
                 * color
                 */
                double brightness_difference = Math
                        .sqrt(Math.pow(r - r_center, 2) + Math.pow(g - g_center, 2) + Math.pow(b - b_center, 2)) / ((255 * Math.sqrt(3)));

                if (brightness_difference > 1)
                    brightness_difference = 1;

                /* map the brightness difference between 0 and size_b / 2 */
                brightness_difference = brightness_difference * (size_b / 2);

                /* if (brightness_difference != 0)
                    Debugger.log("brightness_difference: " + brightness_difference + " gaussian_b: " + this.gaussian_b[(int) brightness_difference][(int) brightness_difference]); */

                /* calculate the bilateral filter */
                double gaussian_b = this.gaussian_b[(int) brightness_difference + size_b / 2][(int) brightness_difference + size_b / 2];
                double bilateral_filter = gaussian_s * gaussian_b;

                Wsb += bilateral_filter;

                newRed += r * bilateral_filter;
                newGreen += g * bilateral_filter;
                newBlue += b * bilateral_filter;

            }
        }

        newRed /= Wsb;
        newGreen /= Wsb;
        newBlue /= Wsb;

        newRed = Math.min(newRed, 255);
        newGreen = Math.min(newGreen, 255);
        newBlue = Math.min(newBlue, 255);

        newRed = Math.max(newRed, 0);
        newGreen = Math.max(newGreen, 0);
        newBlue = Math.max(newBlue, 0);

        int newColor = (ColorConverter.getAlpha(center_pixel) << 24) | ((int) newRed << 16)
                | ((int) newGreen << 8)
                | (int) newBlue;

        return newColor;
    }

    @Override
    public String setName() {
        return name;
    }

   

}
