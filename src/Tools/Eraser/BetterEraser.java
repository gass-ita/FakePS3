package Tools.Eraser;

import Layer.Layer;
import Tools.Brush.BetterBrush;

public class BetterEraser extends BetterBrush{

    public BetterEraser() {
        super();
    }

    public BetterEraser(int size) {
        super(size);
    }

    public BetterEraser(int[][] brushMask, int maskSize) {
        super(brushMask, maskSize);
    }

    @Override
    public void apply(Layer layer, int color, int x, int y) {
        super.apply(layer, 0x00000000, x, y);    
    }
    
}
