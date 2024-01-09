package Tools;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import Layer.Layer;
import Utils.ColorConverter;
import Utils.Debugger;

public interface Filter extends Tool {

    static int[][] convolution(int[][] image, double[][] mask) {
        /*
         * the image will be a matrix of integers[y][x] where the colors are stored like
         * 0xaarrggbb, the mask will be a matrix of double[y][x] where the values are
         * bounded from 0 to 1 (0 black, 1 white)
         */
        /* lesson: https://youtu.be/-LD9MxBUFQo */

        int[][] result = new int[image.length][image[0].length];
        int maskSize = mask.length;

        for (int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {

                double red = 0;
                double green = 0;
                double blue = 0;

                for (int i = 0; i < maskSize; i++) {
                    for (int j = 0; j < maskSize; j++) {
                        int imageX = x + i - maskSize / 2;
                        int imageY = y + j - maskSize / 2;

                        if (imageX < 0 || imageX >= image[0].length || imageY < 0 || imageY >= image.length) {
                            continue;
                        }

                        int color = image[imageY][imageX];

                        /* remember to reflect the mask */
                        double maskValue = mask[maskSize - 1 - i][maskSize - 1 - j];

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

                /* lets keep the old alpha */
                int newColor = (ColorConverter.getAlpha(image[y][x]) << 24) | (newRed << 16) | (newGreen << 8)
                        | newBlue;
                result[y][x] = newColor;
            }
        }

        return result;
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

    public double[][] getMask();

}
