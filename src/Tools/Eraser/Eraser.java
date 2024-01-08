package Tools.Eraser;

import Layer.Layer;
import Tools.Brush.Brush;

public class Eraser extends Brush{
    public Eraser() {
        super();
    }

    public Eraser(int size) {
        super(size);
    }

    @Override
    public void apply(Layer layer, int color, int x, int y) {
        super.apply(layer, 0x00000000, x, y);
    }
    
}
