/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import Algorithms.CorrelationNetwork;
import Algorithms.NVG;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import NetworkMetrics.Betweenness;
import NetworkMetrics.DegreeCentrality;
import NetworkMetrics.Degreee;
import edu.uci.ics.jung.graph.Graph;
import static edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
import edu.uci.ics.jung.graph.util.Pair;
import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import timeseriesanalysistool.GUI.Design;
import timeseriesanalysistool.GUI.MyAlerts;

/**
 *
 * @author pavol
 */
public class Actions {

    Label labelWaitPlease;
    Label labelToDo;
    Label labelFrom;
    Stage loadingStage;
    int filesToConvert;
    int actualFile=1;
    Graph<Vertex, Edge> network;
    int chosenCentrality=1;
    
    Stage chartStage;
    //XYChart.Series series1;
    LineChart<String, Number> bc;
    double groups[];
    double borders[];
    double groups2[];
    double borders2[];
    Boolean isItGroupOne;
   
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
                WritableImage writableImage = new WritableImage(Design.canvasWidth, 600);
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
                WritableImage writableImage = new WritableImage(Design.sceneWidth, 569);
                scene.snapshot(writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                //Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void saveChartDatas(Stage stage, ArrayList<javafx.util.Pair<Vertex, Double>> dataToPrint) {
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
              
                orderVerticesInPair(dataToPrint);
                //TODO order vertices
                fw = new FileWriter(file, true);
                for(int i=0; i< dataToPrint.size();i++)
                {
                    fw.write(dataToPrint.get(i).getKey().toString()+ ": "+dataToPrint.get(i).getValue());
                    fw.write("\n");
                }
                
            
                    
                

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
    
    
     public void saveChartDatasDistributon(Stage stage, ArrayList<javafx.util.Pair<Integer, Double>> results2 ) {
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
              
                
                //TODO order vertices
                fw = new FileWriter(file, true);
                for(int i=0; i< results2.size();i++)
                {
                    fw.write("Degree "+results2.get(i).getKey().toString()+ ": "+results2.get(i).getValue());
                    fw.write("\n");
                }
                
            
                    
                

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
    
    
    
    public void displayProgress( List<File> files,  List<File> files2, Graph<Vertex, Edge> network, int chosenCent)
    {
        chosenCentrality = chosenCent;
        final ProgressIndicator pin = new ProgressIndicator();
        pin.setProgress(-1.0f);
        pin.setVisible(false);

        final VBox vb = new VBox();
        //labelWaitPlease = new Label("Choosen file is converted right now. Wait, please");
       // labelWaitPlease.setVisible(false);
        labelToDo = new Label("Working on file: ");
        labelFrom = new Label("from: ");
        labelToDo.setVisible(false);
        labelFrom.setVisible(false);
        Button start = new Button("Start conversion");

        start.setOnAction((event) -> {
            pin.setVisible(true);
            labelFrom.setVisible(true);
            labelToDo.setVisible(true);
            
            
            filesToConvert = files.size()+files2.size();
            
            
            
            startTask(files, files2);
            
            
           //System.out.println("Toto je pocet uzlov v sieti "+network.getVertexCount());
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction((event) -> {
           System.out.println("Cancelled");
           loadingStage.hide();
            
        });

        
        Group root = new Group();
        Scene scene = new Scene(root, 350, 350);
        loadingStage = new Stage();
        loadingStage.setScene(scene);
        loadingStage.setTitle("Progress Controls");
        start.setMinWidth(150);
        start.setMaxWidth(150); //dynamicky
        cancel.setMaxWidth(150);
        cancel.setMinWidth(150);
        
         labelToDo.setMinWidth(Region.USE_PREF_SIZE);
        labelToDo.setMaxWidth(Region.USE_PREF_SIZE);
        labelFrom.setMinWidth(Region.USE_PREF_SIZE);
        labelFrom.setMaxWidth(Region.USE_PREF_SIZE);
        
        HBox optionButtons = new HBox();
        optionButtons.setSpacing(5);
        optionButtons.getChildren().addAll(start, cancel);
        optionButtons.setAlignment(Pos.CENTER);
        vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(pin,labelToDo,labelFrom, optionButtons);
        scene.setRoot(vb);
        loadingStage.initStyle(StageStyle.UNDECORATED);
        loadingStage.show();
    }
    
    
    public void startTask(List<File> files, List<File> files2) {
        // Create a Runnable
        Runnable task = new Runnable() {
            public void run() {

                runTask(files, files2);

            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();

    }
    
     public void runTask(List<File> files,List<File> files2) {

        try {
          
            if(!files.isEmpty())
            {
                isItGroupOne=true;
                network= new NVG().createNVGNetwork(files.get(0));
                startTaskForMetrics(network,isItGroupOne);
                
               System.out.println("Vytvoril som siet vo vlakne a idem prec. Siet ma tolkoto uzlov"+network.getVertexCount());
               files.remove(0);
               if(!files.isEmpty())
               {
                   startTask(files, files2);
               }
               else if(!files2.isEmpty())
               {
                   startTask(files,files2);
               }
            }
            else if(!files2.isEmpty())
            {
               isItGroupOne=false;
               network= new NVG().createNVGNetwork(files2.get(0));
               startTaskForMetrics(network,isItGroupOne);
               System.out.println("Vytvoril som siet vo vlakne a idem prec. Siet ma tolkoto uzlov"+network.getVertexCount());
               files2.remove(0);
               if(!files2.isEmpty())
               {
                   startTask(files, files2);
               }
            }
            
            // Update the Label on the JavaFx Application Thread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //loadingStage.hide();
                    //loadingStage.close();
                  labelToDo.setText("Converted files: "+Design.a);
                   labelFrom.setText("From: "+filesToConvert);
                   if(filesToConvert==Design.a)
            {
                loadingStage.hide();
                MyAlerts ma = new MyAlerts();
                ma.filesWasConverted();
                Design.a=0;
            }
                }
            });

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
     
     
     
     public void startTaskForMetrics(Graph<Vertex, Edge> network, Boolean isItGroup1) {
        // Create a Runnable
        Runnable task = new Runnable() {
            public void run() {

                runTaskForMetrics(network, isItGroup1);

            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();

    }
     
     public void createchart()
     {
         
        Group root = new Group();
        Scene scene = new Scene(root, Design.sceneWidth, Design.sceneHeight);

        // ScrollPane scrollPane = new ScrollPane();
       
 final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, 0)); //TODO dyn
        chartStage = new Stage();
        chartStage.setTitle("Titulok");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        bc = new LineChart<String, Number>(xAxis, yAxis);
         vbox.getChildren().addAll(bc);
          bc.setAnimated(false);
 bc.setMinWidth(Design.sceneWidth);
        bc.setMinHeight(Design.sceneHeight / 2);
        root.getChildren().addAll(vbox);
        chartStage.setScene(scene);
        chartStage.show();
     }
     
     
     
     
     
     private void resetGroupsCounters(double range) {
        int times = 1;
        int rangeINT = (int) range;
        for (int i = 0; i < groups.length; i++) {
            groups[i] = 0;
            borders[i] = rangeINT * times;
            times++;
        }
    }
    
     
          private void resetGroupsCounters2(double range) {
        int times = 1;
        int rangeINT = (int) range;
        for (int i = 0; i < groups2.length; i++) {
            groups2[i] = 0;
            borders2[i] = rangeINT * times;
            times++;
        }
    }
     
     
     int i=0;
     int howmanytimes=0;
     public void runTaskForMetrics(Graph<Vertex, Edge> network, Boolean isItGroup1) {

        try {
         // System.out.println("Som v runtusk for metrics");
                howmanytimes++;
                System.out.println("Tolkotokrat "+ howmanytimes);
                ArrayList<javafx.util.Pair<Vertex, Double>> data;
                Degreee d = new Degreee(network);
                d.count();
                data = d.getScores();
                int numberOfGroupsInteger=20;
                int range=3;
                groups = new double[numberOfGroupsInteger];
                borders = new double[numberOfGroupsInteger];
                groups2 = new double[numberOfGroupsInteger];
                borders2 = new double[numberOfGroupsInteger];
                resetGroupsCounters(range);
                resetGroupsCounters2(range);
                
                
         
            for (javafx.util.Pair<Vertex, Double> m : data) {
                
                for (int i = 0; i < groups.length; i++) {
                    if (m.getValue() < borders[i]) {
                        if(isItGroup1)
                        {
                            //System.out.println("Ide o skupinu 1");
                            groups[i]++;
                            break;
                        }
                        else
                        {
                            //System.out.println("Ide o skupinu 2");
                            groups2[i]++;
                            break;
                        }
                    }
                    
                    if(m.getValue()> borders[numberOfGroupsInteger-1])
                    {
                         if(isItGroup1)
                        {
                            //System.out.println("Ide o skupinu 1");
                           groups[numberOfGroupsInteger-1]++;
                            break;
                        }
                        else
                        {
                            //System.out.println("Ide o skupinu 2");
                            groups2[numberOfGroupsInteger-1]++;
                            break;
                        }
                        
                    }
                }
              

            }
            double lowBorder = 0;
           

            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Group 1");
            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Group 2");
            double upBorder = lowBorder + range;
            for (int i = 0; i < groups.length; i++) {

                //series1.setName("Group " + lowBorder + "-" + borders[i]); //opravit ..ked nevykreslujem vsetko tak je to napicu
                series1.getData().add(new XYChart.Data(lowBorder + "-" + upBorder, groups[i]));
                series2.getData().add(new XYChart.Data(lowBorder + "-" + upBorder, groups2[i]));
                //System.out.println("Pridavam tuto hodnotu pre seriu1: " + groups[i]+" a tuto pre seriu 2: "+groups2[i]);
                lowBorder = lowBorder + range;
                upBorder = lowBorder + range;
            }
                
                
            // Update the Label on the JavaFx Application Thread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(false)
                    {
                        createchart();
                        bc.getData().addAll(series1, series2);
                    }
                }
            });

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
     
     
     
     
     
    public void saveNetworkAsConnectedVerticesList(Stage stage, Graph<Vertex, Edge> network) {
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
                fw = new FileWriter(file, true);
                
                for(Edge e: network.getEdges())
                {
                    Pair<Vertex> p = network.getEndpoints(e);
                    Vertex v = p.getFirst();
                    Vertex v2 = p.getSecond();
                    String firstVertex = String.valueOf(v.getId());
                    String secondVertex = String.valueOf(v2.getId());
                    
                    fw.write(firstVertex+" "+secondVertex);
                     fw.write("\n");
                    
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
     
 private void orderVertices(List<Vertex> vertices)
    {
         Collections.sort(vertices, new Comparator<Vertex>() {
                @Override
                public int compare(Vertex v1, Vertex v2) {
                    return v1.getId() - v2.getId();
                }
            });
    }
 
 
 private void orderVerticesInPair(ArrayList<javafx.util.Pair<Vertex, Double>> dataToPrint)
    {
  
       // Collections.sort(dataToPrint, Comparator.comparing(p -> p.getKey().getId()));
        Collections.sort(dataToPrint, Comparator.comparing(p -> p.getValue()));
    }
    
    
}
