package Tools.Fill;

import java.util.ArrayList;

import Layer.Layer;
import Tools.Tool;
import Utils.ColorConverter;
import Utils.Debugger;

public class Fill implements Tool{

    private int tollerance = 0;

    public Fill(int tollerance){
        this.tollerance = tollerance;
    }

    private int[][] binaryMap(int[][] colorMap, int x, int y){
        Debugger.log("binaryMap");
        int color = colorMap[y][x];
        /* create a binary map (composed of 0s and 1s) of the color map using the tollerance, if the color in [y][x] is within color - t and color + t then will be 1 otherwise it will be black */
        int [][] binaryMap = new int[colorMap.length][colorMap[0].length];
        for (int i = 0; i < binaryMap.length; i++) {
            for (int j = 0; j < binaryMap[0].length; j++) {
                binaryMap[i][j] = isPixelConsidered(colorMap, j, i, color) ? 1 : 0;
            }
        }

        

        return binaryMap;
    }

    private boolean isPixelConsidered(int[][] colorMap, int x, int y, int color){
        int c = colorMap[y][x];
        
        int cargb[] = ColorConverter.hexToArgb(c);

        int ca = cargb[0];
        int cr = cargb[1];
        int cg = cargb[2];
        int cb = cargb[3];

        int[] color_argb = ColorConverter.hexToArgb(color);
        int a = color_argb[0];
        int r = color_argb[1];
        int g = color_argb[2];
        int b = color_argb[3];

        return (ca >= a - tollerance && ca <= a + tollerance) && (cr >= r - tollerance && cr <= r + tollerance) && (cg >= g - tollerance && cg <= g + tollerance) && (cb >= b - tollerance && cb <= b + tollerance);
    }

    private ArrayList<int[]> floodFill(int[][] binaryMap, int x, int y){
        Debugger.log("floodFill");
        ArrayList<int[]> floodFill = new ArrayList<int[]>();
        ArrayList<int[]> queue = new ArrayList<int[]>();
        queue.add(new int[]{x, y});
        while (queue.size() > 0) {
            int[] p = queue.remove(0);
            floodFill.add(p);
            int px = p[0];
            int py = p[1];
            if (px > 0 && binaryMap[py][px - 1] == 1) {
                queue.add(new int[]{px - 1, py});
                binaryMap[py][px - 1] = 0;
            }
            if (px < binaryMap[0].length - 1 && binaryMap[py][px + 1] == 1) {
                queue.add(new int[]{px + 1, py});
                binaryMap[py][px + 1] = 0;
            }
            if (py > 0 && binaryMap[py - 1][px] == 1) {
                queue.add(new int[]{px, py - 1});
                binaryMap[py - 1][px] = 0;
            }
            if (py < binaryMap.length - 1 && binaryMap[py + 1][px] == 1) {
                queue.add(new int[]{px, py + 1});
                binaryMap[py + 1][px] = 0;
            }
        }
        return floodFill;
    }

    


    @Override
    public void apply(Layer layer, int color, int x, int y) {
        int[][] colorMap = layer.getPixels();
        int[][] binaryMap = binaryMap(colorMap, x, y);
        ArrayList<int[]> floodFill = floodFill(binaryMap, x, y);
        for (int[] p : floodFill) {
            layer.setPixel(p[0], p[1], color);
        }
    }
    
}
