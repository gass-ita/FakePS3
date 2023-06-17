package Tools;

import Layer.Layer;

public interface Tool {
    public void apply(Layer layer, int color, int x, int y);
}