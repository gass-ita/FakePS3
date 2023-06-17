import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Layer.Layer;
import Layer.LayerManager;
import UI.Editor;

public class App {
    public static void main(String[] args) throws Exception {
        Layer layer = new Layer();
        BufferedImage image = ImageIO.read(new File("test.jpg"));
        layer.importImage(image);
        layer.setVisible(true);
        LayerManager manager = new LayerManager(800, 800);

        manager.addLayer(layer);
        

        Editor editor = new Editor(manager);
        JFrame frame = new JFrame();
        frame.add(editor);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* BufferedImage image_to_save = manager.exportImage();
        File outputfile = new File("saved.png");
        ImageIO.write(image_to_save, "png", outputfile); */


        
        

        
     


        
        

    }
}
