package Tools;

import Layer.Layer;
import Tools.Filters.BorderSolution;
import Utils.ColorConverter;
import Utils.Debugger;

public abstract class LinearFilter implements Tool {

    /**
     * Applies a convolution filter to an entire image using a specified mask
     * (kernel).
     * The convolution operation is performed on each pixel of the image, producing
     * a new filtered image.
     * Border handling is managed by padding with a constant value.
     *
     * <p>
     * Each pixel in the output image is calculated using the convolution method:
     * </p>
     * 
     * <pre>
     * result[y][x] = convolution(image, mask, x, y, BorderSolution.PAD_WITH_CONSTANT);
     * </pre>
     *
     * @param image The original 2D array representing the image to be filtered.
     * @param mask  The convolution mask (or kernel), a 2D array of doubles used for
     *              filtering.
     * @return A new 2D array representing the filtered image.
     * @throws Exception If an error occurs during the convolution process.
     */
    protected static int[][] applyFilter(int[][] image, double[][] mask) throws Exception {
        int[][] result = new int[image.length][image[0].length];

        for (int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {
                result[y][x] = convolution(image, mask, x, y, BorderSolution.PAD_WITH_CONSTANT);
            }
        }

        return result;

    }

    /**
     * Applies a convolution operation to a specific pixel in the image using a
     * given mask.
     * The convolution operation takes into account different border solutions when
     * the mask
     * extends beyond the image boundaries.
     *
     * <p>
     * Mathematical notation for discrete convolution:
     * </p>
     * 
     * <pre>
     * Convolution result at (x, y) = Σ Σ (image[y + i][x + j] * mask[maskH - 1 - i][maskW - 1 - j])
     * where the summation is over the dimensions of the mask.
     * </pre>
     *
     * @param image          The original 2D array representing the image.
     * @param mask           The convolution mask (or kernel), a 2D array of
     *                       doubles.
     * @param x              The x-coordinate of the central pixel around which the
     *                       convolution is applied.
     * @param y              The y-coordinate of the central pixel around which the
     *                       convolution is applied.
     * @param borderSolution The solution for handling borders when the mask extends
     *                       beyond the image boundaries.
     * @return The new color value for the pixel at (x, y) after applying the
     *         convolution.
     * @throws IllegalArgumentException if the (x, y) coordinates are out of bounds
     *                                  of the image.
     */
    public static int convolution(int[][] image, double[][] mask, int x, int y, BorderSolution borderSolution)
            throws Exception {

        if (x < 0 || x >= image[0].length || y < 0 || y >= image.length) {
            Debugger.err("x or y out of bounds of the image");
            throw new IllegalArgumentException("x or y out of bounds of the image");
        }

        if (borderSolution == BorderSolution.IGNORE_BORDER)
            if (x < mask[0].length / 2 || y < mask.length / 2 || x >= (image[0].length - mask[0].length / 2)
                    || y >= (image.length - mask.length / 2))
                return image[y][x];

        int[][] c_image = generate_c_image(image, x, y, mask[0].length, mask.length, borderSolution);

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

    /**
     * Generates a sub-image (c_image) centered around a specific pixel (x, y) from
     * the input image.
     * The sub-image has the dimensions specified by the mask width (maskW) and
     * height (maskH).
     * Border cases are handled based on the provided BorderSolution.
     *
     * @param image          The original 2D array representing the image.
     * @param x              The x-coordinate of the central pixel around which the
     *                       sub-image is to be generated.
     * @param y              The y-coordinate of the central pixel around which the
     *                       sub-image is to be generated.
     * @param maskW          The width of the mask/sub-image.
     * @param maskH          The height of the mask/sub-image.
     * @param borderSolution The solution for handling borders when the mask extends
     *                       beyond the image boundaries.
     * @return A 2D array representing the generated sub-image.
     * @throws IllegalArgumentException if the border solution is IGNORE_BORDER.
     */
    public static int[][] generate_c_image(int[][] image, int x, int y, int maskW, int maskH,
            BorderSolution borderSolution) {
        int[][] c_image = new int[maskH][maskW];
        /*
         * if x, y locate on a dangerous position, for example if the mask is 5x5 then
         * the dangerous positions are (0,0), (0,1), (0,2), (0,3), (0,4), (1,0), (2,0),
         */
        if (x < maskW / 2 || y < maskH / 2 || x >= (image[0].length - maskW / 2)
                || y >= (image.length - maskH / 2)) {

            /* copy the interested part of the image into the c_image */
            switch (borderSolution) {
                case IGNORE_BORDER:
                    throw new IllegalArgumentException("border solution must be different than IGNORE_BORDER");
                case PAD_WITH_CONSTANT:
                    /*
                     * put the image[y + yi - mask.length / 2][x + xi - mask[0].length / 2] where is
                     * possible, otherwise put 0
                     */
                    for (int yi = 0; yi < maskH; yi++) {
                        for (int xi = 0; xi < maskW; xi++) {
                            int x_index = x + xi - maskW / 2;
                            int y_index = y + yi - maskH / 2;
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
                    for (int yi = 0; yi < maskH; yi++) {
                        for (int xi = 0; xi < maskW; xi++) {
                            int x_index = x + xi - maskW / 2;
                            int y_index = y + yi - maskH / 2;
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
            for (int yi = 0; yi < maskH; yi++) {
                for (int xi = 0; xi < maskW; xi++) {
                    c_image[yi][xi] = image[y + yi - maskH / 2][x + xi - maskW / 2];
                }
            }
        }

        return c_image;
    }

    @Override
    public void apply(Layer layer, int color, int x, int y) throws Exception {
        Debugger.log("Applying " + getName() + "...");

        long startTime = System.nanoTime();

        int[][] image = layer.getPixels();
        double[][] mask = getMask();

        int[][] result = applyFilter(image, mask);
        long endTime = System.nanoTime();
        Debugger.log("Done in " + ((endTime - startTime) / 1000000) + " ms" + "! Applying the result to the layer...");

        startTime = System.nanoTime();
        for (int yi = 0; yi < result.length; yi++) {
            for (int xi = 0; xi < result[0].length; xi++) {
                layer.setPixel(xi, yi, result[yi][xi]);
            }
        }
        endTime = System.nanoTime();

        Debugger.log("Applied " + getName() + " in " + ((endTime - startTime) / 1000000) + " ms");
    }

    public abstract double[][] getMask() throws Exception;

    public String setName() {
        throw new UnsupportedOperationException("This linear filter doesn't have a name");
    }

    public String getName() {
        try {
            return setName();
        } catch (UnsupportedOperationException e) {
            Debugger.warn("This linear filter doesn't have a name, to set it override the method setName()");
            return "Unknown linear filter";
        }
    }

}
