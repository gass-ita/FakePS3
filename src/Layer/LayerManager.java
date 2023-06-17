package Layer;

import java.util.ArrayList;
import java.awt.image.*;


public class LayerManager {
    /* DEFAULT_VARIABLES */
    static final int DEFAULT_WIDTH = 800;
    static final int DEFAULT_HEIGHT = 800;

    /* VARIABLES */
    public ArrayList<Layer> LayerList = new ArrayList<>();
    private BufferedImage image;
    private int width, height;

    /* CONSTRUCTORS */

    public LayerManager() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public LayerManager(int w, int h) {
        width = w;
        height = h;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    
    /* METHODS */

    public void createNewLayer() {
        LayerList.add(new Layer(width, height));
    }

    public void addLayer(Layer layer) {
        addLayer(layer, false);
    }

    public void addLayer(Layer layer, boolean resize) {
        LayerList.add(Layer.setDimension(layer.copy(), width, height, resize));
    }

    public void removeLayer(int index) {
        LayerList.remove(index);
    }


    public BufferedImage exportImage(){
        // create a new BufferedImage with the same dimensions as the LayerManager
        BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                
        // iterate over each pixel in the image
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                // get the background color of the image from the first layer
                Layer bg_layer = LayerList.get(0);
                int bgColor_pixel = bg_layer.isVisible() ? bg_layer.getPixelValue(x, y) : 0;

                // calculate the alpha value of the background color based on the layer's opacity
                int bgColor_alpha = (bgColor_pixel >> 24) & 0xff;
                int bgColor_red = (bgColor_pixel >> 16) & 0xff;
                int bgColor_green = (bgColor_pixel >> 8) & 0xff;
                int bgColor_blue = bgColor_pixel & 0xff;
                bgColor_alpha = (int) (bgColor_alpha * bg_layer.getOpacity());
                bgColor_red = (int) (bgColor_red * bg_layer.getChannelOpacity(Layer.RED_CHANNEL));
                bgColor_green = (int) (bgColor_green * bg_layer.getChannelOpacity(Layer.GREEN_CHANNEL));
                bgColor_blue = (int) (bgColor_blue * bg_layer.getChannelOpacity(Layer.BLUE_CHANNEL));

                // combine the alpha value and color value into a single pixel value
                bgColor_pixel = (bgColor_alpha << 24) | (bgColor_pixel & 0x00ffffff);
                bgColor_pixel = (bgColor_red << 16) | (bgColor_pixel & 0xff00ffff);
                bgColor_pixel = (bgColor_green << 8) | (bgColor_pixel & 0xffff00ff);
                bgColor_pixel = (bgColor_blue) | (bgColor_pixel & 0xffffff00);

                // iterate over each layer (excluding the background layer)
                for (int i = 1; i < LayerList.size(); i++){
                    Layer layer = LayerList.get(i);

                    // if the layer is not visible, skip it
                    if (!layer.isVisible()) continue;

                    // get the pixel value of the current layer at the current pixel location
                    int layer_pixel = layer.getPixelValue(x, y);

                    // calculate the alpha value of the layer based on its opacity
                    int layer_alpha = (layer_pixel >> 24) & 0xff;
                    int layer_red = (layer_pixel >> 16) & 0xff;
                    int layer_green = (layer_pixel >> 8) & 0xff;
                    int layer_blue = layer_pixel & 0xff;
                    layer_alpha = (int) (layer_alpha * layer.getOpacity());
                    layer_red = (int) (layer_red * layer.getChannelOpacity(Layer.RED_CHANNEL));
                    layer_green = (int) (layer_green * layer.getChannelOpacity(Layer.GREEN_CHANNEL));
                    layer_blue = (int) (layer_blue * layer.getChannelOpacity(Layer.BLUE_CHANNEL));

                    // combine the alpha value and color value into a single pixel value
                    layer_pixel = (layer_alpha << 24) | (layer_pixel & 0x00ffffff);
                    layer_pixel = (layer_red << 16) | (layer_pixel & 0xff00ffff);
                    layer_pixel = (layer_green << 8) | (layer_pixel & 0xffff00ff);
                    layer_pixel = (layer_blue) | (layer_pixel & 0xffffff00);

                    // combine the current layer's pixel value with the background color
                    bgColor_pixel = calculateColor(layer_pixel, bgColor_pixel);
                }

                // set the pixel value of the current pixel in the new BufferedImage
                tmp.setRGB(x, y, bgColor_pixel);
            }
        }

        // set the LayerManager's image to the new BufferedImage and return it
        image = tmp;
        return image;
    }








    /* PRIVATE_METHODS */




    private int calculateColor(int f, int b) {
        // extract the color components from f and b
        int fa = (f >> 24) & 0xff;
        int fr = (f >> 16) & 0xff;
        int fg = (f >> 8) & 0xff;
        int fb = f & 0xff;

        int ba = (b >> 24) & 0xff;
        int br = (b >> 16) & 0xff;
        int bg = (b >> 8) & 0xff;
        int bb = b & 0xff;

        // calculate the generated color of f over b
        int ca = ba + fa - ba * fa / 255;
        int cr = (int) ((fr * fa + br * (255 - fa)) / 255);
        int cg = (int) ((fg * fa + bg * (255 - fa)) / 255);
        int cb = (int) ((fb * fa + bb * (255 - fa)) / 255);

        // pack the color components into a single int
        int c = (ca << 24) | (cr << 16) | (cg << 8) | cb;

        return c;
    }


    /* GETTERS_AND_SETTERS */

    public BufferedImage getImage() {
        BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                tmp.setRGB(x, y, image.getRGB(x, y));
            }
        }
        return tmp;
    }

    public BufferedImage getSingleImage(int layer) {
        return LayerList.get(layer).exportImage();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Layer getLayer(int index) {
        return LayerList.get(index).copy();
    }

    public int getLayerCount() {
        return LayerList.size();
    }

    public void setWidth(int width, boolean stretch) {
        setDimension(width, height, stretch);
    }

    public void setHeight(int height, boolean stretch) {
        setDimension(width, height, stretch);
    }

    public void setWidth(int width) {
        setDimension(width, height);
    }

    public void setHeight(int height) {
        setDimension(width, height);
    }

    public void setDimension(int width, int height) {
        setDimension(width, height, false);
    }

    public void setDimension(int width, int height, boolean stretch) {
        for (Layer layer : LayerList) {
            layer.setDimension(width, height, stretch);
        }
        this.width = width;
        this.height = height;
    }
}
