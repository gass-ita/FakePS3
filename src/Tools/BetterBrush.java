package Tools;

import java.awt.image.BufferedImage;

import Layer.Layer;

public class BetterBrush extends Brush {

    
    public static int DEFAULT_MASK_SIZE = 128;
    public static int[][] DEFAULT_BRUSH_MASK = generateDefaultBrushMask(DEFAULT_MASK_SIZE);

    private int[][] brushMask;
    private int maskSize;


    public BetterBrush() {
        this(DEFAULT_SIZE, DEFAULT_BRUSH_MASK, DEFAULT_MASK_SIZE);
    }

    public BetterBrush(int size) {
        this(size, DEFAULT_BRUSH_MASK, DEFAULT_MASK_SIZE);
    }

    public BetterBrush(int[][] brushMask, int maskSize) {
        this(DEFAULT_SIZE, brushMask, maskSize);
    }

    public BetterBrush(int size, int[][] brushMask, int maskSize) {
        setSize(size);
        this.brushMask = brushMask;
        this.maskSize = maskSize;
    }

    @Override
    public void apply(Layer layer, int color, int x, int y) {
        // paint with the color using the brush mask
        int[][] resizedBrushMask = resizeMatrix(brushMask, getSize() * 2);
        for (int i = 0; i < resizedBrushMask.length; i++) {
            for (int j = 0; j < resizedBrushMask.length; j++) {
                if (resizedBrushMask[i][j] == 1) {
                    layer.setPixel(x + i - getSize() / 2, y + j - getSize() / 2, color);
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
                int color = brushMask[i][j] == 1 ? 0xffffffff : 0xff000000;
                brushMaskImage.setRGB(i, j, color);
            }
        }
        return brushMaskImage;
    }

    public void importBrushImage(BufferedImage brushMaskImage) {
        maskSize = brushMaskImage.getWidth();
        brushMask = new int[maskSize][maskSize];
        for (int i = 0; i < maskSize; i++) {
            for (int j = 0; j < maskSize; j++) {
                brushMask[i][j] = brushMaskImage.getRGB(i, j) == 0xffffffff ? 1 : 0;
            }
        }
    }

    /* PRIVATE_METHODS */

    private static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage resized_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        resized_image.getGraphics().drawImage(image, 0, 0, width, height, null);
        return resized_image;
    }

    private static int[][] resizeMatrix(int[][] matrix, int newSize) {
        BetterBrush b = new BetterBrush(matrix, matrix.length);
        BufferedImage brushMaskImage = b.exportBrushImage();
        BufferedImage resizedBrushMaskImage = resizeImage(brushMaskImage, newSize, newSize);
        b.importBrushImage(resizedBrushMaskImage);
        return b.brushMask;
    }




    private static int[][] generateDefaultBrushMask(int maskSize) {
        int[][] mask = new int[maskSize][maskSize];
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
