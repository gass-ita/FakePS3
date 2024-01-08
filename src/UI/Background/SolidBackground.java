package UI.Background;

import java.awt.Color;
import java.awt.Graphics;

public class SolidBackground implements Background{

    /* DEFAULT VARIABLES */
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.DARK_GRAY;

    private int width, height;
    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;

    public SolidBackground(int width, int height, Color backgroundColor) {
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
    }

    public SolidBackground(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public SolidBackground() {
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);
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
