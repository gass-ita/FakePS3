import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Layer.Layer;
import Layer.LayerManager;
import Tools.Brush.BetterBrush;
import Tools.Brush.Brush;
import Tools.Eraser.BetterEraser;
import Tools.Fill.Fill;
import Tools.Filters.BoxFilter;
import Tools.Filters.GaussianFilter;
import Tools.Filters.ImpulseFilter;
import Tools.Filters.LaplacianFilter;
import Tools.NonLinearFilters.BilateralFilter;
import Tools.NonLinearFilters.MedianFilter;
import UI.Editor;
import Utils.ColorConverter;
import Utils.Debugger;

public class App {
    public static void main(String[] args) throws Exception {

        Debugger.log("Starting the app");

        Layer layer = new Layer();
        BufferedImage image = ImageIO.read(new File("noisy.png"));
        Layer l2 = new Layer();
        l2.importImage(image);
        /* set half of the l2 black and the other half white */
        /* for (int i = 0; i < l2.getHeight(); i++) {
            for (int j = 0; j < l2.getWidth(); j++) {
                if (j < l2.getWidth() / 2) {
                    l2.setPixel(j, i, 0xff000000);
                } else {
                    l2.setPixel(j, i, 0xffffffff);
                }
            }
        } */
        l2.setVisible(true);
        /* layer.importImage(image); */
        layer.setVisible(true); 
        LayerManager manager = new LayerManager(800, 800);

        manager.addLayer(layer);
        manager.addLayer(l2);
        manager.setActiveLayer(1);
        

        Editor editor = new Editor(manager);
        JFrame frame = new JFrame();
        frame.add(editor);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* BufferedImage test_image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        //make test_image like a checkboard
        for (int i = 0; i < test_image.getWidth(); i++) {
            for (int j = 0; j < test_image.getHeight(); j++) {
                if ((i + j) % 2 == 0) {
                    test_image.setRGB(i, j, 0xffffffff);
                } else {
                    test_image.setRGB(i, j, 0xff000000);
                }
            }
        } */

        BufferedImage test_image = ImageIO.read(new File("brush.png"));
        BetterBrush brush = new BetterBrush();
        brush.importBrushImage(test_image);
        brush.setSize(25);
        manager.setActiveTool(brush);
        


       /*  Brush brush2 = new Brush();
        manager.toolUsed(brush2, 300, 300); */
        

        BilateralFilter impulse = new BilateralFilter();
        /* impulse.setSize(5); */
        manager.toolUsed(impulse, 0, 0);
        editor.repaint();

        /* Layer l2 = new Layer();
        l2.setVisible(true);
        l2.fill(0xaa0000ff);
        manager.addLayer(l2);
        editor.repaint(); */

        
        /* Saving the final image */
        /* BufferedImage image_to_save = manager.exportSingleImage(1);
        File outputFile = new File("saved.png");
        ImageIO.write(image_to_save, "png", outputFile); */         
        

    }
}
