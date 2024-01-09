package Tools.Brush;

import java.awt.image.BufferedImage;

import Layer.Layer;
import Utils.ColorConverter;
import Utils.Debugger;

public class BetterBrush extends Brush {

    
    public static int DEFAULT_MASK_SIZE = 128;
    public static double[][] DEFAULT_BRUSH_MASK = generateDefaultBrushMask(DEFAULT_MASK_SIZE);

    private double[][] brushMask;
    private int maskSize;


    public BetterBrush() {
        this(DEFAULT_SIZE, DEFAULT_BRUSH_MASK, DEFAULT_MASK_SIZE);
    }

    public BetterBrush(int size) {
        this(size, DEFAULT_BRUSH_MASK, DEFAULT_MASK_SIZE);
    }

    public BetterBrush(double[][] brushMask, int maskSize) {
        this(DEFAULT_SIZE, brushMask, maskSize);
    }

    public BetterBrush(int size, double[][] brushMask, int maskSize) {
        this.brushMask = brushMask;
        this.maskSize = maskSize;
        setSize(maskSize);
    }

    @Override
    public void apply(Layer layer, int color, int x, int y) {
        // paint with the color using the brush mask
        Debugger.log("Applying brush " + "brushMask.length: " + brushMask.length + " brushMask[0].length: " + brushMask[0].length);
        for (int i = 0; i < brushMask.length; i++) {
            for (int j = 0; j < brushMask.length; j++) {
                if (brushMask[i][j] != 0) {
                    int r = ColorConverter.getRed(color);
                    int g = ColorConverter.getGreen(color);
                    int b = ColorConverter.getBlue(color);
                    int a = ColorConverter.getAlpha(color);

                    int newA = (int) Math.round(a * brushMask[i][j]);

                    layer.setPixel(x + i - getSize() / 2, y + j - getSize() / 2, ColorConverter.argbToHex(newA, r, g, b));
                }
            }
        }
    }

    /* PUBLIC_METHODS */

    /* GETTERS_AND_SETTERS */

    public BufferedImage exportBrushImage() {
        BufferedImage brushMaskImage = new BufferedImage(maskSize, maskSize, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < maskSize; i++) {
            for (int j = 0; j < maskSize; j++) {
                int opacity = (int) Math.round(brushMask[i][j] * 255);
                int color;
                if (opacity == 0){
                    color = 0x00000000;
                } else {
                    color = ColorConverter.argbToHex(255, opacity, opacity, opacity);
                } 
                brushMaskImage.setRGB(i, j, color);
            }
        }
        return brushMaskImage;
    }

    public void importBrushImage(BufferedImage brushMaskImage) {
        maskSize = brushMaskImage.getWidth();
        brushMask = new double[maskSize][maskSize];
        for (int i = 0; i < maskSize; i++) {
            for (int j = 0; j < maskSize; j++) {
                int color = brushMaskImage.getRGB(i, j);
                
                int r = ColorConverter.getRed(color);
                int g = ColorConverter.getGreen(color);
                int b = ColorConverter.getBlue(color);

                int opacity = (r + g + b) / 3;

                brushMask[i][j] = opacity / 255.0;                
            }
        }
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);
        brushMask = resizeMatrix(brushMask, size);
        maskSize = size;
        
    }

    /* PRIVATE_METHODS */

    private double[][] resizeMatrix(double[][] matrix, int newSize) {
        /* resize the matrix and copy the old matrix adapting it to the new dimension */
        double[][] newMatrix = new double[newSize][newSize];

        BufferedImage image = exportBrushImage();
        /* resize the image */
        BufferedImage resizedImage = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
        resizedImage.getGraphics().drawImage(image, 0, 0, newSize, newSize, null);

        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                int color = resizedImage.getRGB(i, j);
                
                int r = ColorConverter.getRed(color);
                int g = ColorConverter.getGreen(color);
                int b = ColorConverter.getBlue(color);

                int opacity = (r + g + b) / 3;

                newMatrix[i][j] = opacity / 255.0;                
            }
        }

        return newMatrix;       
    }




    private static double[][] generateDefaultBrushMask(int maskSize) {
        double[][] mask = new double[maskSize][maskSize];
        int radius = maskSize / 2;
        for (int i = 0; i < maskSize; i++) {
            for (int j = 0; j < maskSize; j++) {
                if ((i - radius) * (i - radius) + (j - radius) * (j - radius) <= radius * radius) {
                    mask[i][j] = 1;
                }
            }
        }
        return mask;        
    }
}
