package Tools;

import Layer.Layer;
import Tools.Filters.BorderSolution;
import Utils.Debugger;

public abstract class NonLinearFilter extends LinearFilter {

    @Override
    public void apply(Layer layer, int color, int x, int y) throws Exception {

        Debugger.log("Applying " + getName() + "...");

        long startTime = System.nanoTime();

        int[][] image = layer.getPixels();
        int[][] result = applyFilter(layer, color, x, y);

        long endTime = System.nanoTime();
        Debugger.log("Done in " + ((endTime - startTime) / 1000000) + " ms" + "! Applying the result to the layer...");

        startTime = System.nanoTime();
        for (int yi = 0; yi < image.length; yi++) {
            for (int xi = 0; xi < image[0].length; xi++) {
                image[yi][xi] = result[yi][xi];
            }
        }
        endTime = System.nanoTime();

        Debugger.log("Applied " + getName() + " in " + ((endTime - startTime) / 1000000) + " ms");
    }

    public abstract int[][] applyFilter(Layer layer, int color, int x, int y) throws Exception;

    public abstract int convolution(int[][] image, int size, int x, int y, BorderSolution borderSolution)
            throws Exception;

    @Override
    public String getName() {
        try {
            return setName();
        } catch (UnsupportedOperationException e) {
            Debugger.warn("This non linear filter doesn't have a name, to set it override the method setName()");
            return "unknown non linear filter";
        }
    }

    public double[][] getMask() throws Exception {
        throw new UnsupportedOperationException(getName() + " don't fixed have masks");
    }

}
