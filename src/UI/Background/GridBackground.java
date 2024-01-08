package UI.Background;

import java.awt.Graphics;
import java.awt.Color;

public class GridBackground implements Background{

    /* DEFAULT VARIABLES */
    public static final int DEFAULT_BACKGROUND_GRID_SIZE = 10;
    public static final Color DEFAULT_PRIMARY_COLOR = new Color(0xff141414);
    public static final Color DEFAULT_SECONDARY_COLOR = new Color(0xff1f1f1f);


    /* PRIVATE VARIABLES */
    private int width, height;
    private int backgroundGridSize = DEFAULT_BACKGROUND_GRID_SIZE;
    private Color primaryColor = DEFAULT_PRIMARY_COLOR;
    private Color secondaryColor = DEFAULT_SECONDARY_COLOR;

    /* CONSTRUCTORS */

    public GridBackground(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public GridBackground(int width, int height, int backgroundGridSize, Color primaryColor, Color secondaryColor) {
        this.width = width;
        this.height = height;
        this.backgroundGridSize = backgroundGridSize;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public GridBackground(int width, int height, int backgroundGridSize) {
        this.width = width;
        this.height = height;
        this.backgroundGridSize = backgroundGridSize;
    }

    public GridBackground(int width, int height, Color primaryColor, Color secondaryColor) {
        this.width = width;
        this.height = height;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public GridBackground(int width, int height, int backgroundGridSize, Color primaryColor) {
        this.width = width;
        this.height = height;
        this.backgroundGridSize = backgroundGridSize;
        this.primaryColor = primaryColor;
    }

    public GridBackground() {
    }

    @Override
    public void draw(Graphics g) {
        for (int i = 0; i < width; i += backgroundGridSize) {
            for (int j = 0; j < height; j += backgroundGridSize) {
                if ((i / backgroundGridSize + j / backgroundGridSize) % 2 == 0) {
                    g.setColor(primaryColor);
                } else {
                    g.setColor(secondaryColor);
                }
                g.fillRect(i, j, backgroundGridSize, backgroundGridSize);
            }
        }
    }

    public void setBackgroundGridSize(int backgroundGridSize) {
        this.backgroundGridSize = backgroundGridSize;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height; 
    }
    
}
