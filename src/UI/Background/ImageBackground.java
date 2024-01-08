package UI.Background;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import Utils.Debugger;
import java.awt.Image;

public class ImageBackground implements Background {

    /* DEFAULT VARIABLES */
    public static final BufferedImage DEFAULT_BACKGROUND_IMAGE = null;

    private int width, height;
    private BufferedImage backgroundImage = DEFAULT_BACKGROUND_IMAGE;

    public ImageBackground(int width, int height, BufferedImage backgroundImage) {
        this.width = width;
        this.height = height;
        this.backgroundImage = backgroundImage;
    }

    public ImageBackground(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ImageBackground() {
    }

    public ImageBackground(String path) {
        File file = new File(path);
        try {
            backgroundImage = javax.imageio.ImageIO.read(file);
        } catch (Exception e) {
            Debugger.warn("Error reading image file");
            backgroundImage = DEFAULT_BACKGROUND_IMAGE;
        }

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, null);
    }

    @Override
    public void setWidth(int width) {
        this.width = width;

        resizeImage();

    }

    @Override
    public void setHeight(int height) {
        this.height = height;
        resizeImage();

    }

    private void resizeImage() {
        if (width <= 0 || height <= 0) {
            Debugger.warn("Invalid width or height");
            return;
        }

        if (backgroundImage == null) {
            Debugger.warn("No image to resize");
            return;
        }

        Image scaledImage = backgroundImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = resizedImage.getGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();

        backgroundImage = resizedImage;

    }

}
