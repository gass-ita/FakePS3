package UI;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import Layer.LayerManager;
import UI.Background.*;
import Utils.Debugger;

public class Editor extends JPanel implements MouseMotionListener, MouseListener {

    
    static final Background DEFAULT_BACKGROUND_STYLE = new GridBackground();


    private LayerManager manager;
    private Background backgroundStyle = DEFAULT_BACKGROUND_STYLE;
    private int width, height;

    public Editor(LayerManager manager) {
        this.manager = manager;
        width = manager.getWidth();
        height = manager.getHeight();

        backgroundStyle.setWidth(width);
        backgroundStyle.setHeight(height);

        setSize(width, height);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        /* draw the background */
        if (backgroundStyle != null) {
            backgroundStyle.draw(g);
        }
        
        /* draw the image */
        g.drawImage(manager.exportImage(), 0, 0, null);
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        try {
            manager.toolDragged(manager.getActiveTool(), e.getX(), e.getY());
        } catch (Exception e1) {
            Debugger.err(e1.getMessage());
        }
        repaint();
    }



    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            manager.toolUsed(manager.getActiveTool(), e.getX(), e.getY());
        } catch (Exception e1) {
            Debugger.err(e1.getMessage());
        }
        repaint();
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
