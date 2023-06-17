package Layer;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;



public class Layer {

    public final static int RED_CHANNEL = 0;
    public final static int GREEN_CHANNEL = 1;
    public final static int BLUE_CHANNEL = 2;
    /* DEFAULT_VARIABLES */
    
    final static int DEFAULT_WIDTH = 800;
    final static int DEFAULT_HEIGHT = 800;
    final static Color DEFAULT_BG_COLOR = new Color(255, 255, 255, 0);
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
        pixels[y][x] = (a << 24) | (r << 16) | (g << 8) | b;
    }

    public void setPixel(int x, int y, Color color) {
        setPixel(x, y, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public void setPixel(int x, int y, int color) {
        pixels[y][x] = color;    
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

    public ArrayList<Point> floodSelection(int x, int y, int tolerance){
        // Get the color of the pixel at the given coordinates.
        int color = this.getPixelValue(x, y);
        // Create a binary map of the layer, where pixels with a color within the tolerance range are set to 1 and all others are set to 0.
        int[][] binary_map = binaryMap(color, tolerance);
        // Perform a flood fill on the binary map starting from the given coordinates.
        // This will select all pixels that are connected to the starting pixel and have a color within the tolerance range.
        ArrayList<Point> points = floodFill(binary_map, x, y);
        // Return the selected pixels.
        return points;
    }

    public ArrayList<Point> floodSelection(int x, int y){
        return floodSelection(x, y, 0);
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
        return this.pixels[y][x];
    }

    public Color getPixelColor(int x, int y) {
        int r = (this.pixels[y][x] >> 16) & 0xFF;
        int g = (this.pixels[y][x] >> 8) & 0xFF;
        int b = (this.pixels[y][x] >> 0) & 0xFF;
        int a = (this.pixels[y][x] >> 24) & 0xFF;
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

    /**
     * This method creates a binary map of a given image, where pixels with a color within the tolerance range are set to 1 and all others are set to 0.
     * 
     * @param pixels the 2D array of pixels to create the binary map from
     * @param color the color to compare the pixels to
     * @param tolerance the tolerance range to use when comparing the pixels
     * @return the binary map
     */
    private static int[][] binaryMap(int[][] pixels, int color, int tolerance){
        // Create a new 2D array to store the binary map
        int[][] binary_map = new int[pixels.length][pixels[0].length];
        // Loop through each pixel in the original pixel array
        for(int x = 0; x < pixels.length; x++){
            for(int y = 0; y < pixels[0].length; y++){
                // Extract the color channels from the pixel
                int image_r = (pixels[y][x] >> 16) & 0xFF;
                int image_g = (pixels[y][x] >> 8) & 0xFF;
                int image_b = (pixels[y][x] >> 0) & 0xFF;
                int image_a = (pixels[y][x] >> 24) & 0xFF;

                // Extract the color channels from the given color
                int color_r = (color >> 16) & 0xFF;
                int color_g = (color >> 8) & 0xFF;
                int color_b = (color >> 0) & 0xFF;
                int color_a = (color >> 24) & 0xFF;

                // Check if the pixel is within the tolerance range of the given color
                if(Math.abs(image_r - color_r) <= tolerance && Math.abs(image_g - color_g) <= tolerance && Math.abs(image_b - color_b) <= tolerance && Math.abs(image_a - color_a) <= tolerance){
                    // If it is, set the corresponding pixel in the binary map to 1
                    binary_map[y][x] = 1;
                }else{
                    // Otherwise, set it to 0
                    binary_map[y][x] = 0;
                }
            }
        }
        // Return the binary map
        return binary_map;
    }

    private int[][] binaryMap(int color, int tolerance){
        return binaryMap(this.pixels, color, tolerance);
    }


    /**
    * Performs a flood fill algorithm on a binary map.
    * 
    * @param binaryMap the binary map to perform the flood fill on
    * @param x the starting x coordinate
    * @param y the starting y coordinate
    * @param points an ArrayList of points to store the result
    * @return the ArrayList of points that were filled
    */
    private static ArrayList<Point> floodFill(int[][] binaryMap, int x, int y, ArrayList<Point> points){
        // Check if the current coordinates are out of bounds
        if(x < 0 || x >= binaryMap[0].length || y < 0 || y >= binaryMap.length){
            // If they are, return the current ArrayList of points
            return points;
        }
        // Check if the current pixel is set to 1 (meaning it hasn't been filled yet)
        if(binaryMap[y][x] == 1){
            // If it is, add the current point to the ArrayList of points
            points.add(new Point(x, y));
            // Set the current pixel to 0 (meaning it has been filled)
            binaryMap[y][x] = 0;
            // Recursively call the floodFill method on the neighboring pixels
            points = floodFill(binaryMap, x + 1, y, points);
            points = floodFill(binaryMap, x - 1, y, points);
            points = floodFill(binaryMap, x, y + 1, points);
            points = floodFill(binaryMap, x, y - 1, points);
        }
        // Return the ArrayList of points that were filled
        return points;
    }
    private ArrayList<Point> floodFill(int[][] binaryMap, int x, int y){
        return floodFill(binaryMap, x, y, new ArrayList<Point>());
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
        System.out.println("setting opacity to " + opacity);
        this.opacity = opacity;
    }

    public void setChannelOpacity(int channel, double opacity){
        
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

    public double getChannelOpacity(int channel){
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
