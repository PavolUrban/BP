/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import timeseriesanalysistool.GUI.Design;

/**
 *
 * @author pavol
 */
public class Actions {

    public void saveNetworkAsImage(Canvas canvas, Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage(Design.canvasWidth, Design.canvasHeight);
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                //Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void saveChartAsImage(Scene scene, Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage(Design.sceneWidth, Design.sceneHeight / 2);
                scene.snapshot(writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                //Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void saveChartDatas(Stage stage, int array[], double rangeValue) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");;
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {

            try {
                double lowBorder = 0;
                double upBorder = rangeValue;
                fw = new FileWriter(file, true);
                for(int i=0;i<array.length;i++)
                {
                    String lowBorders = String.valueOf(lowBorder);
                    String upBorders = String.valueOf(upBorder-1);
                    String arrayValueToText = String.valueOf(array[i]);
                    
                    fw.write(lowBorders+" - "+upBorders);
                    fw.write("\t");
                    fw.write(arrayValueToText);
                    fw.write("\n");
                    lowBorder = lowBorder+rangeValue;
                    upBorder= upBorder+rangeValue;
                }
                

                System.out.println("Done");

            } catch (IOException ex) {
                //Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

                try {

                    if (bw != null) {
                        bw.close();
                    }

                    if (fw != null) {
                        fw.close();
                    }

                } catch (IOException ex) {

                    ex.printStackTrace();

                }
            }

        }
    }
}
