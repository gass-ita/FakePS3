package Layer;

import java.awt.Color;
import java.awt.image.BufferedImage;


import Utils.ColorConverter;
import Utils.Debugger;



public class Layer {

    
    /* DEFAULT_VARIABLES */
    
    final static int DEFAULT_WIDTH = 800;
    final static int DEFAULT_HEIGHT = 800;
    final static Color DEFAULT_BG_COLOR = new Color(0, 0, 0, 0);
    final static String DEFAULT_LABEL = "New Layer";
    final static boolean DEFAULT_VISIBILITY = true;
    final static double DEFAULT_OPACITY = 1.0;
    final static double DEFAULT_RED_CHANNEL_OPACITY = 1.0;
    final static double DEFAULT_GREEN_CHANNEL_OPACITY = 1.0;
    final static double DEFAULT_BLUE_CHANNEL_OPACITY = 1.0;


    
    /* VARIABLES */
    private int width;
    private int height;
    private int[][] pixels;
    private BufferedImage image;
    private String label = DEFAULT_LABEL;
    private boolean isVisible = DEFAULT_VISIBILITY;
    double opacity = DEFAULT_OPACITY;
    double redChannel_opacity = DEFAULT_RED_CHANNEL_OPACITY;
    double greenChannel_opacity = DEFAULT_GREEN_CHANNEL_OPACITY;
    double blueChannel_opacity = DEFAULT_BLUE_CHANNEL_OPACITY;

    
    /* CONSTRUCTORS */
    public Layer(int width, int height, Color bgColor) {
        this.width = width;
        this.height = height;
        this.pixels = new int[height][width];
        this.fill(bgColor);
    }




    public Layer() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_BG_COLOR);
    }
    
    public Layer(int width, int height) {
        this(width, height, DEFAULT_BG_COLOR);
    }

    public Layer(int size) {
        this(size, size, DEFAULT_BG_COLOR);
    }



    /* METHODS */

    public void fill(Color color) {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                setPixel(x, y, color);
            }
        }
    }

    public void fill(int r, int g, int b, int a) {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                setPixel(x, y, r, g, b, a);
            }
        }
    }

    public void fill(int color) {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                setPixel(x, y, color);
            }
        }
    }

    public void fill(int[] color) {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                setPixel(x, y, color);
            }
        }
    }

    public void fill(int[] color, int a) {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                setPixel(x, y, color, a);
            }
        }
    }


    public void setPixel(int x, int y, int r, int g, int b, int a) {
        int color = ColorConverter.argbToHex(a, r, g, b);
        setPixel(x, y, color);
    }

    public void setPixel(int x, int y, Color color) {
        setPixel(x, y, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public void setPixel(int x, int y, int color) {
        if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
            this.pixels[y][x] = color;
        } else {
            Debugger.warn("Pixel out of bounds: (" + x + ", " + y + ")");
        }
    }

    public void setPixel(int x, int y, int[] color) {
        setPixel(x, y, color[0], color[1], color[2], color[3]);
    }

    public void setPixel(int x, int y, int[] color, int a) {
        setPixel(x, y, color[0], color[1], color[2], a);
    }

    
    


    /**
     * Exports the current layer as a BufferedImage.
     * @return The exported BufferedImage.
     */
    public BufferedImage exportImage() {
        // Create a new BufferedImage with the same dimensions as the current layer.
        BufferedImage tmp = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        // Create a new BufferedImage with the same dimensions as the current layer.
        this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        // Iterate over each pixel in the layer and set the corresponding pixel in the new BufferedImages.
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.image.setRGB(x, y, this.pixels[y][x]);
                tmp.setRGB(x, y, this.pixels[y][x]);
            }
        }

        // Return the exported BufferedImage.
        return tmp;
    }

    /**
     * Imports a BufferedImage into the current layer.
     * @param image The BufferedImage to import.
     */
    public void importImage(BufferedImage image){
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = new int[this.height][this.width];
        // Iterate over each pixel in the imported image and set the corresponding pixel in the current layer's pixels array.
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.pixels[y][x] = image.getRGB(x, y);
            }
        }
    }

    public Layer copy(){
        return Layer.copy(this);
    }


    


    /* GETTERS_AND_SETTERS */

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int[][] getPixels() {
        return this.pixels;
    }

    public int getPixelValue(int x, int y) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            return ColorConverter.argbToHex(DEFAULT_BG_COLOR.getAlpha(), DEFAULT_BG_COLOR.getRed(), DEFAULT_BG_COLOR.getGreen(), DEFAULT_BG_COLOR.getBlue());
        }
        return this.pixels[y][x];
    }

    public Color getPixelColor(int x, int y) {
        int color = getPixelValue(x, y);
        int[] argb = ColorConverter.hexToArgb(color);
        int a = argb[0];
        int r = argb[1];
        int g = argb[2];
        int b = argb[3];
        return new Color(r, g, b, a);
    }

    

    public BufferedImage getImage() {
        /* copy of the image */
        BufferedImage tmp = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < this.width; x++){
            for (int y = 0; y < this.height; y++){
                tmp.setRGB(x, y, this.pixels[y][x]);
            }
        }
        return tmp;
    }

    public void setDimension(int width, int height) {
        Layer.setDimension(this, width, height);
    }

    public void setDimension(int width, int height, boolean stretch) {
       Layer.setDimension(this, width, height, stretch);
    }


    @Override
    public String toString() {
        return "Layer [height=" + height + ", image=" + image + ", label=" + label + ", pixels=" + pixels + ", visible="
                + isVisible + ", width=" + width + "]";
    }


    /* PRIVATE_METHODS */

    private static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage resized_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        resized_image.getGraphics().drawImage(image, 0, 0, width, height, null);
        return resized_image;
    }

    

    /* STATIC */

    public static Layer setDimension(Layer layer, int width, int height) {
        if(width > 0){
            layer.width = width;
            layer.pixels = new int[layer.getHeight()][width];
        }
        if(height > 0){
            layer.height = height;
            layer.pixels = new int[height][layer.getWidth()];
        }
        return layer;
    }

    /**
    * This method sets the dimensions of a Layer object, and optionally stretches its image to fit the new dimensions.
    * 
    * @param layer The Layer object to modify.
    * @param width The new width of the Layer object.
    * @param height The new height of the Layer object.
    * @param stretch A boolean indicating whether to stretch the image to fit the new dimensions.
    * 
    * @return The modified Layer object.
    */
    public static Layer setDimension(Layer layer, int width, int height, boolean stretch) {
        // Export the current image to a BufferedImage object
        BufferedImage current_image = layer.exportImage();
        // Set the new dimensions of the Layer object
        Layer.setDimension(layer, width, height);
        if(stretch){
            // If the stretch flag is set, resize the current image to fit the new dimensions
            layer.image = resizeImage(current_image, width, height);
        } else {
            // Otherwise, create a new BufferedImage object with the new dimensions
            layer.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            
            // Copy the pixels from the current image to the new image, up to the minimum width and height
            int min_width = Math.min(current_image.getWidth(), width);
            int min_height = Math.min(current_image.getHeight(), height);

            for (int x = 0; x < min_width; x++){
                for (int y = 0; y < min_height; y++){
                    layer.image.setRGB(x, y, current_image.getRGB(x, y));
                }
            }

            int max_width = Math.max(current_image.getWidth(), width);
            int max_height = Math.max(current_image.getHeight(), height);

            Debugger.log(current_image.getWidth() + " " + current_image.getHeight() + " " + width + " " + height + " " + min_width + " " + min_height + " " + max_width + " " + max_height);

            /* check if its needed to add the border pixels */
            if (width >= max_width || height >= max_height){
                Debugger.log("need to add the border pixels");
                /* set the other pixel values to the default bg value */
                for (int x = min_width; x < max_width; x++){
                    for (int y = 0; y < max_height; y++){
                        if (x < current_image.getWidth() && y < current_image.getHeight()){
                            continue;
                        } else {
                            layer.image.setRGB(x, y, ColorConverter.argbToHex(DEFAULT_BG_COLOR.getAlpha(), DEFAULT_BG_COLOR.getRed(), DEFAULT_BG_COLOR.getGreen(), DEFAULT_BG_COLOR.getBlue()));
                        }
                    }
                }

                for (int x = 0; x < max_width; x++){
                    for (int y = min_height; y < max_height; y++){
                        if (x < current_image.getWidth() && y < current_image.getHeight()){
                            continue;
                        } else {
                            layer.image.setRGB(x, y, ColorConverter.argbToHex(DEFAULT_BG_COLOR.getAlpha(), DEFAULT_BG_COLOR.getRed(), DEFAULT_BG_COLOR.getGreen(), DEFAULT_BG_COLOR.getBlue()));
                        }
                    }
                }
            }
        }     
        // Import the modified image into the Layer object
        layer.importImage(layer.image);
        // Return the modified Layer object
        return layer;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean toggleVisibility() {
        this.isVisible = !this.isVisible;
        return this.isVisible;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        /* make the opacity between 0 and 1 */
        /* if(opacity < 0){
            opacity = 0;
        } else if(opacity > 1){
            opacity = 1;
        } */
        Debugger.log("setting opacity to " + opacity);
        this.opacity = opacity;
    }

    public void setChannelOpacity(ColorChannel channel, double opacity){
        
        if (opacity < 0){
            opacity = 0;
        } else if (opacity > 1){
            opacity = 1;
        }

        switch (channel) {
            case RED_CHANNEL:
                redChannel_opacity = opacity;
                break;
            case GREEN_CHANNEL:
                greenChannel_opacity = opacity;
                break;
            case BLUE_CHANNEL:
                blueChannel_opacity = opacity;
                break;
            default:
                break;
        }
    }

    public double getChannelOpacity(ColorChannel channel){
        switch (channel) {
            case RED_CHANNEL:
                return redChannel_opacity;
            case GREEN_CHANNEL:
                return greenChannel_opacity;
            case BLUE_CHANNEL:
                return blueChannel_opacity;
            default:
                return -1;
        }
    }

    public static Layer copy(Layer layer){
        Layer new_layer = new Layer(layer.width, layer.height);
        new_layer.label = layer.label;
        new_layer.isVisible = layer.isVisible;
        new_layer.opacity = layer.opacity;
        new_layer.redChannel_opacity = layer.redChannel_opacity;
        new_layer.greenChannel_opacity = layer.greenChannel_opacity;
        new_layer.blueChannel_opacity = layer.blueChannel_opacity;
        for (int x = 0; x < layer.width; x++){
            for (int y = 0; y < layer.height; y++){
                new_layer.pixels[y][x] = layer.pixels[y][x];
            }
        }
        return new_layer;
    }
}
