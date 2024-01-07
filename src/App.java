import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Layer.Layer;
import Layer.LayerManager;
import Tools.BetterBrush;
import Tools.Brush;
import Tools.Fill;
import UI.Editor;
import Utils.Debugger;

public class App {
    public static void main(String[] args) throws Exception {

        Debugger.log("Starting the app");

        Layer layer = new Layer();
        /* BufferedImage image = ImageIO.read(new File("test.jpg")); */
        /* layer.importImage(image); */
        layer.setVisible(true); 
        LayerManager manager = new LayerManager(800, 800);

        manager.addLayer(layer);
        

        Editor editor = new Editor(manager);
        JFrame frame = new JFrame();
        frame.add(editor);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BufferedImage test_image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        //make test_image like a checkboard
        for (int i = 0; i < test_image.getWidth(); i++) {
            for (int j = 0; j < test_image.getHeight(); j++) {
                if ((i + j) % 2 == 0) {
                    test_image.setRGB(i, j, 0xffffffff);
                } else {
                    test_image.setRGB(i, j, 0xff000000);
                }
            }
        }



        BetterBrush brush = new BetterBrush();
        brush.importBrushImage(test_image);
        brush.setSize(100);
        manager.toolUsed(brush, 500, 500);

        Brush brush2 = new Brush();
        manager.toolUsed(brush2, 300, 300);

        /* BufferedImage image_to_save = manager.exportImage();
        File outputfile = new File("saved.png");
        ImageIO.write(image_to_save, "png", outputfile); */


        
        

        
     


        
        

    }
}
