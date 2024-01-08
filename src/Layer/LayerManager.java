package Layer;

import java.util.ArrayList;
import java.awt.image.*;

import Tools.Tool;
import Utils.ColorConverter;
import Utils.Debugger;



public class LayerManager {
    /* DEFAULT_VARIABLES */
    static final int DEFAULT_WIDTH = 800;
    static final int DEFAULT_HEIGHT = 800;
    static final int DEFAULT_ACTIVE_COLOR = 0xffffffff;
    static final Tool DEFAULT_ACTIVE_TOOL = new Tools.BetterBrush();

    /* VARIABLES */
    private ArrayList<Layer> LayerList = new ArrayList<>();
    private Layer activeLayer;
    private BufferedImage image;
    private int width, height;
    private int activeColor = DEFAULT_ACTIVE_COLOR;
    private Tool activeTool = DEFAULT_ACTIVE_TOOL;
    private int lastX = -1;
    private int lastY = -1;
    

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
        Layer layer = new Layer(width, height);
        if (activeLayer == null) {
            activeLayer = layer;
        }
        LayerList.add(layer);
    }

    public void addLayer(Layer layer) {
        addLayer(layer, false);
    }

    public void addLayer(Layer layer, boolean resize) {
        Layer newLayer = Layer.setDimension(layer.copy(), width, height, resize);
        if (activeLayer == null) {
            activeLayer = newLayer;
        }
        LayerList.add(newLayer);    
    }

    public void removeLayer(int index) {
        if (index < 0 || index >= LayerList.size()) {
            return;
        }

        if (activeLayer == LayerList.get(index)) {
            activeLayer = null;
        }

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
                /* int bgColor_alpha = (bgColor_pixel >> 24) & 0xff;
                int bgColor_red = (bgColor_pixel >> 16) & 0xff;
                int bgColor_green = (bgColor_pixel >> 8) & 0xff;
                int bgColor_blue = bgColor_pixel & 0xff; */
                int[] bgColor_argb = ColorConverter.hexToArgb(bgColor_pixel);
                int bgColor_alpha = bgColor_argb[0];
                int bgColor_red = bgColor_argb[1];
                int bgColor_green = bgColor_argb[2];
                int bgColor_blue = bgColor_argb[3];
                
                bgColor_alpha = (int) (bgColor_alpha * bg_layer.getOpacity());
                bgColor_red = (int) (bgColor_red * bg_layer.getChannelOpacity(ColorChannel.RED_CHANNEL));
                bgColor_green = (int) (bgColor_green * bg_layer.getChannelOpacity(ColorChannel.GREEN_CHANNEL));
                bgColor_blue = (int) (bgColor_blue * bg_layer.getChannelOpacity(ColorChannel.BLUE_CHANNEL));

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
                    int[] layer_argb = ColorConverter.hexToArgb(layer_pixel);
                    int layer_alpha = layer_argb[0];
                    int layer_red = layer_argb[1];
                    int layer_green = layer_argb[2];
                    int layer_blue = layer_argb[3];

                    layer_alpha = (int) (layer_alpha * layer.getOpacity());
                    layer_red = (int) (layer_red * layer.getChannelOpacity(ColorChannel.RED_CHANNEL));
                    layer_green = (int) (layer_green * layer.getChannelOpacity(ColorChannel.GREEN_CHANNEL));
                    layer_blue = (int) (layer_blue * layer.getChannelOpacity(ColorChannel.BLUE_CHANNEL));

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

    
    public void toolUsed(Tool tool, int x, int y){
        tool.apply(activeLayer, activeColor, x, y);
    }

    public void toolDragged(Tool tool, int x, int y){
        if (lastX == -1 || lastY == -1){
            lastX = x;
            lastY = y;
        }

        ArrayList<int[]> points = interpolatePoints(lastX, lastY, x, y);

        for (int[] point : points){
            tool.apply(activeLayer, activeColor, point[0], point[1]);
        }

        lastX = x;
        lastY = y;
    }

    public void toolReleased(){
        lastX = -1;
        lastY = -1;
    }


    /* PRIVATE_METHODS */

    /**
     * Returns an ArrayList of points that are interpolated between the two given points.
     * @param x0 The x coordinate of the first point.
     * @param y0 The y coordinate of the first point.  
     * @param x1 The x coordinate of the second point.
     * @param y1 The y coordinate of the second point.
     * @return
     */
    private static ArrayList<int[]> interpolatePoints(int x0, int y0, int x1, int y1){
        ArrayList<int[]> points = new ArrayList<>();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;

        while (true){
            points.add(new int[]{x0, y0});

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;

            if (e2 > -dy){
                err -= dy;
                x0 += sx;
            }

            if (e2 < dx){
                err += dx;
                y0 += sy;
            }
        }

        return points;
    }


   

    /* STATIC_METHODS */
    public static int calculateColor(int f, int b){
        /* // extract the color components from f and b
        int fa = (f >> 24) & 0xff;
        int fr = (f >> 16) & 0xff;
        int fg = (f >> 8) & 0xff;
        int fb = f & 0xff;

        int ba = (b >> 24) & 0xff;
        int br = (b >> 16) & 0xff;
        int bg = (b >> 8) & 0xff;
        int bb = b & 0xff; */

        int[] fa_argb = ColorConverter.hexToArgb(f);
        int fa = fa_argb[0];
        int fr = fa_argb[1];
        int fg = fa_argb[2];
        int fb = fa_argb[3];

        int[] ba_argb = ColorConverter.hexToArgb(b);
        int ba = ba_argb[0];
        int br = ba_argb[1];
        int bg = ba_argb[2];
        int bb = ba_argb[3];

        // calculate the generated color of f over b
        int ca = ba + fa - ba * fa / 255;
        int cr = (int) ((fr * fa + br * (255 - fa)) / 255);
        int cg = (int) ((fg * fa + bg * (255 - fa)) / 255);
        int cb = (int) ((fb * fa + bb * (255 - fa)) / 255);

        // pack the color components into a single int
        int c = ColorConverter.argbToHex(ca, cr, cg, cb);

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

    public BufferedImage exportSingleImage(int layer) {
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

    public Tool getActiveTool() {
        return activeTool;
    }

    public void setActiveTool(Tool activeTool) {
        this.activeTool = activeTool;
    }


    /**
     * Returns the active color of the LayerManager.
     * @return The active color of the LayerManager.
     */
    public int getActiveColor() {
        return activeColor;
    }

    /**
     * Sets the active color of the LayerManager.
     * @param activeColor The new active color of the LayerManager.
     */
    public void setActiveColor(int activeColor) {
        this.activeColor = activeColor;
    }


    /**
     * Returns the active layer of the LayerManager.
     * @param index The index of the layer to set as the active layer.
     */
    public void setActiveLayer(int index) {
        if (index < 0 || index >= LayerList.size()) {
            Debugger.err("Invalid index for setActiveLayer()");
            return;
        }
        activeLayer = LayerList.get(index);
    }
}
