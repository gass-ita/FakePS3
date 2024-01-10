package Tools;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import Layer.Layer;
import Tools.Filters.BorderSolution;
import Utils.ColorConverter;
import Utils.Debugger;

public interface LinearFilter extends Tool {

    static int[][] fullConvolution(int[][] image, double[][] mask) throws Exception {
        Debugger.log("Full convolution");
        /* chronometrate the fullConvolution */
        long startTime = System.nanoTime();
        int[][] result = new int[image.length][image[0].length];

        for (int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {
                result[y][x] = convolution(image, mask, x, y, BorderSolution.PAD_WITH_REFLECTION);
            }
        }
        long endTime = System.nanoTime();
        Debugger.log("Finished convolution in: " + ((endTime - startTime) / 1000000) + " ms");

        return result;

    }

    public static int convolution(int[][] image, double[][] mask, int x, int y, BorderSolution borderSolution)
            throws Exception {
        if (x < 0 || x >= image[0].length || y < 0 || y >= image.length) {
            Debugger.err("x or y out of bounds");
            throw new IllegalArgumentException("x or y out of bounds");
        }

        int[][] c_image = new int[mask.length][mask[0].length];

        /*
         * if x, y locate on a dangerous position, for example if the mask is 5x5 then
         * 
         */
        if (x < mask[0].length / 2 || y < mask.length / 2 || x >= (image[0].length - mask[0].length / 2)
                || y >= (image.length - mask.length / 2)) {

            /* copy the interested part of the image into the c_image */
            switch (borderSolution) {
                case IGNORE_BORDER:
                    return image[y][x];
                case PAD_WITH_CONSTANT:

                    /*
                     * put the image[y + yi - mask.length / 2][x + xi - mask[0].length / 2] where is
                     * possible, otherwise put 0
                     */
                    for (int yi = 0; yi < mask.length; yi++) {
                        for (int xi = 0; xi < mask[0].length; xi++) {
                            int x_index = x + xi - mask[0].length / 2;
                            int y_index = y + yi - mask.length / 2;
                            if (x_index < 0 || x_index >= image[0].length || y_index < 0
                                    || y_index >= image.length) {
                                c_image[yi][xi] = 0;
                            } else {
                                c_image[yi][xi] = image[y_index][x_index];
                            }
                        }
                    }
                    break;
                case PAD_WITH_REFLECTION:

                    /*
                     * put the image[y + yi - mask.length / 2][x + xi - mask[0].length / 2] where is
                     * possible, otherwise put the reflected value
                     */
                    for (int yi = 0; yi < mask.length; yi++) {
                        for (int xi = 0; xi < mask[0].length; xi++) {
                            int x_index = x + xi - mask[0].length / 2;
                            int y_index = y + yi - mask.length / 2;
                            if (x_index < 0 || x_index >= image[0].length || y_index < 0
                                    || y_index >= image.length) {
                                /* reflect the index */
                                if (x_index < 0) {
                                    x_index = -x_index;
                                }
                                if (x_index >= image[0].length) {
                                    x_index = 2 * image[0].length - x_index - 1;
                                }
                                if (y_index < 0) {
                                    y_index = -y_index;
                                }
                                if (y_index >= image.length) {
                                    y_index = 2 * image.length - y_index - 1;
                                }
                                c_image[yi][xi] = image[y_index][x_index];
                            } else {
                                c_image[yi][xi] = image[y_index][x_index];
                            }
                        }
                    }
                    break;
            }
        } else {
            /* copy the interested part of the image into the c_image */
            for (int yi = 0; yi < mask.length; yi++) {
                for (int xi = 0; xi < mask[0].length; xi++) {
                    c_image[yi][xi] = image[y + yi - mask.length / 2][x + xi - mask[0].length / 2];
                }
            }
        }

        double red = 0;
        double green = 0;
        double blue = 0;

        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[0].length; j++) {

                int color = c_image[i][j];

                double maskValue = mask[mask.length - 1 - i][mask[0].length - 1 - j];

                red += ColorConverter.getRed(color) * maskValue;
                green += ColorConverter.getGreen(color) * maskValue;
                blue += ColorConverter.getBlue(color) * maskValue;
            }
        }

        int newRed = (int) Math.round(red);
        int newGreen = (int) Math.round(green);
        int newBlue = (int) Math.round(blue);

        newRed = Math.min(newRed, 255);
        newGreen = Math.min(newGreen, 255);
        newBlue = Math.min(newBlue, 255);

        newRed = Math.max(newRed, 0);
        newGreen = Math.max(newGreen, 0);
        newBlue = Math.max(newBlue, 0);

        int newColor = (ColorConverter.getAlpha(c_image[mask.length / 2][mask[0].length / 2]) << 24) | (newRed << 16)
                | (newGreen << 8)
                | newBlue;

        return newColor;

    }

    public static void saveMaskToText(double[][] mask, String filename) {
        File outputfile = new File(filename);
        try {
            outputfile.createNewFile();
            Debugger.log("Created file");
        } catch (Exception e) {
            Debugger.log("Could not create file");
        }

        /* save to txt the matrix */
        try {
            String content = "";
            for (int i = 0; i < mask.length; i++) {
                for (int j = 0; j < mask[0].length; j++) {
                    content += mask[i][j] + " ";
                }
                content += "\n";
            }
            Debugger.log("Content: " + content);
            java.io.FileWriter fw = new java.io.FileWriter(outputfile);
            fw.write(content);
            fw.close();
        } catch (Exception e) {
            Debugger.log("Could not write to file");
        }

    }

    public static void saveMaskToImage(double[][] mask, String filename) {
        BufferedImage image = new BufferedImage(mask.length, mask[0].length, BufferedImage.TYPE_INT_RGB);

        /* find the minimum and map it to black and the maximum to white */
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[0].length; j++) {
                min = Math.min(min, mask[i][j]);
                max = Math.max(max, mask[i][j]);
            }
        }

        Debugger.log("Min: " + min + " Max: " + max);

        /* map the values to 0-255 */
        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[0].length; j++) {
                double value = mask[i][j];
                value = (value - min) / (max - min);
                int color = (int) Math.round(value * 255);
                image.setRGB(i, j, (color << 16) | (color << 8) | color);
            }
        }

        /* save the image */
        File outputFile = new File(filename);
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (Exception e) {
            Debugger.log("Could not write to file");
        }

        Debugger.log("Saved image");

    }

    @Override
    public void apply(Layer layer, int color, int x, int y) throws Exception;

    public double[][] getMask() throws Exception;

}
