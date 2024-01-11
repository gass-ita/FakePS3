package Tools.NonLinearFilters;

import java.util.Arrays;

import Layer.Layer;
import Tools.NonLinearFilter;
import Tools.Filters.BorderSolution;
import Utils.ColorConverter;
import Utils.Debugger;

public class MedianFilter extends NonLinearFilter{

    static final int DEFAULT_SIZE = 6;

    private int size = DEFAULT_SIZE;

    @Override
    public int[][] applyFilter(Layer layer, int color, int x, int y) throws Exception {
        int[][] image = layer.getPixels();
        int size = this.size;
        BorderSolution borderSolution = BorderSolution.PAD_WITH_CONSTANT;
        int[][] new_image = new int[image.length][image[0].length];

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j <image[0].length; j++) {
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

        int r[] = new int[c_image.length*c_image.length];
        int g[] = new int[c_image.length*c_image.length];
        int b[] = new int[c_image.length*c_image.length];

        int i = 0;
        for (int[] row : c_image) {
            for (int pixel : row) {
                r[i] = ColorConverter.getRed(pixel);
                g[i] = ColorConverter.getGreen(pixel);
                b[i] = ColorConverter.getBlue(pixel);
                i++;
            }
        }

        int median_r = getMedian(r);
        int median_g = getMedian(g);
        int median_b = getMedian(b);
        int alpha = ColorConverter.getAlpha(image[y][x]);

        return ColorConverter.argbToHex(alpha, median_r, median_g, median_b);


    }

    private int getMedian(int[] array) {
        int median = 0;
        int[] sorted = array.clone();
        Arrays.sort(sorted);
        if (sorted.length % 2 == 0) {
            median = (sorted[sorted.length / 2] + sorted[sorted.length / 2 - 1]) / 2;
        } else {
            median = sorted[sorted.length / 2];
        }
        return median;
    }
    
}
