package Tools;



import Layer.Layer;

public class Brush implements Tool{
    public static int DEFAULT_SIZE = 10;

    private int size;
    

    public Brush() {
        this(DEFAULT_SIZE);
    }

    public Brush(int size) {
        this.size = size;
    }

    @Override
    public void apply(Layer layer, int color, int x, int y) {
        // use layer.setPixel to create a circle of pixels centered at (x, y) with radius size
        for (int i = x - getSize(); i < x + getSize(); i++) {
            for (int j = y - getSize(); j < y + getSize(); j++) {
                /* check that it's in the circle */
                if ((i - x) * (i - x) + (j - y) * (j - y) <= getSize() * getSize()) {
                    layer.setPixel(i, j, color);
                }
            }
        }
        

    }

    /* PUBLIC_METHODS */

    /* GETTERS_AND_SETTERS */

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    
}
