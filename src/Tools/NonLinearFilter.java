package Tools;

import Layer.Layer;
import Tools.Filters.BorderSolution;
import Utils.Debugger;

public abstract class NonLinearFilter extends LinearFilter {

    @Override
    public  void apply(Layer layer, int color, int x, int y) throws Exception{
        Debugger.log("Applying non linear filter");
        int[][] image = layer.getPixels();
        int[][] result = applyFilter(layer, color, x, y);
        
        for (int yi = 0; yi < image.length; yi++) {
            for (int xi = 0; xi < image[0].length; xi++) {
                image[yi][xi] = result[yi][xi];
            }
        }
        Debugger.log("Applied non linear filter");
    }

    public abstract int[][] applyFilter(Layer layer, int color, int x, int y) throws Exception;

    public abstract int convolution(int[][] image, int size, int x, int y, BorderSolution borderSolution) throws Exception;

    public  double[][] getMask() throws Exception{
        throw new UnsupportedOperationException("non linear filters don't fixed have masks");
    }
    
}
