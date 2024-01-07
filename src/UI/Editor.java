package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import Layer.LayerManager;

public class Editor extends JPanel implements MouseMotionListener, MouseListener {

    
    static final BackgroundStyle DEFAULT_BACKGROUND_STYLE = BackgroundStyle.GRID_BACKGROUND_STYLE;
    
    /* BACKGROUND_STYLE DEFAULT VARIABLES */
    static final Color DEFAULT_BACKGROUND_COLOR = new Color(255, 255, 255, 255);
    static final Color DEFAULT_BACKGROUND_COLOR_2 = new Color(0, 0, 0, 255);
    static final int DEFAULT_BACKGROUND_GRID_SIZE = 10;

    
    /* BACKGROUND_STYLE VARIABLES */
    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private Color backgroundColor2 = DEFAULT_BACKGROUND_COLOR_2;
    private int backgroundGridSize = DEFAULT_BACKGROUND_GRID_SIZE;


    private LayerManager manager;
    private BackgroundStyle backgroundStyle = DEFAULT_BACKGROUND_STYLE;
    private int width, height;

    public Editor(LayerManager manager) {
        this.manager = manager;
        width = manager.getWidth();
        height = manager.getHeight();
        setSize(width, height);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        /* make the classic gray and white bg for the transparent */
        switch (backgroundStyle) {
            case SOLID_BACKGROUND_STYLE:
                solidBackground(g);
                break;
            case GRID_BACKGROUND_STYLE:
                gridBackground(g);
                break;
            case TRANSPARENT_BACKGROUND_STYLE:
                break;
        }
        /* draw the image */
        g.drawImage(manager.exportImage(), 0, 0, null);
    }

    /* PRIVATE_METHODS */

    private void solidBackground(Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);
    }

    private void gridBackground(Graphics g) {
        for (int i = 0; i < width; i += backgroundGridSize) {
            for (int j = 0; j < height; j += backgroundGridSize) {
                if ((i / backgroundGridSize + j / backgroundGridSize) % 2 == 0) {
                    g.setColor(backgroundColor);
                } else {
                    g.setColor(backgroundColor2);
                }
                g.fillRect(i, j, backgroundGridSize, backgroundGridSize);
            }
        }
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        manager.toolDragged(manager.getActiveTool(), e.getX(), e.getY());
        repaint();
    }



    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        manager.toolReleased();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}
