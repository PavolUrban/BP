/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseriesanalysistool.GUI;

import Actions.Actions;
import Algorithms.NVG;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import NetworkMetrics.Betweenness;
import NetworkMetrics.Closennes;
import NetworkMetrics.Degreee;
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class ChartMaker {

    int chosenCentrality;
    double maximumValue;
    double minimumValue;
    Stage chartStage;
    //XYChart.Series series1;
    LineChart<String, Number> bc;
    ArrayList<Pair<Vertex, Double>> dataToChangeRange;

    Label labelbiggestCentrality = new Label();
    Label labellowestCentrality = new Label();
    Label labelchooseRange = new Label("Choose range for x-axis");

    double rangeValueFromSlider = 2;
    int numberOfGroupss=0;
    int groups[];
    int borders[];
    int arrayToPrint[];
    public ChartMaker() {
    }

    public void createChart(String title, Graph<Vertex, Edge> network, double range) {

        Group root = new Group();
        Scene scene = new Scene(root, Design.sceneWidth, Design.sceneHeight);

        // ScrollPane scrollPane = new ScrollPane();
        startTask(network, range, title);

        chartStage = new Stage();
        chartStage.setTitle(title);
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        bc = new LineChart<String, Number>(xAxis, yAxis);
        if (new String("Closeness centrality").equals(title)) {
            System.out.println("Uvodne porovnanie- ide o closenes");
            bc.setTitle("Closeness centrality - Vertices summary");
        } else if (new String("Betweenness centrality").equals(title)) {

            bc.setTitle("Betweenness centrality - Vertices summary");
        } else {
            bc.setTitle("Degree - Vertices summary");
        }

        bc.setMinWidth(Design.sceneWidth);
        bc.setMinHeight(Design.sceneHeight / 2);
        bc.setAnimated(false);

        xAxis.setLabel("Range of centrality value");
        yAxis.setLabel("Number of vertices");

        Slider slider = new Slider(0, 50, 2);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(5);
        slider.setBlockIncrement(5);
        
        slider.valueProperty().addListener((obs, oldval, newVal) -> {
            rangeValueFromSlider = slider.getValue();
            slider.setValue(Math.round(newVal.doubleValue()));
            System.out.println("Value from slider " + rangeValueFromSlider);
        });
        
        Button saveChart = new Button("Save chart as PNG");
  saveChart.setOnAction((event) -> {
            // Button was clicked, do something...
            Actions a = new Actions();
           a.saveChartAsImage(scene, chartStage);
           
        });
  
  Button saveChartData = new Button("Save chart data");
  saveChartData.setOnAction((event) -> {
            // Button was clicked, do something...
            Actions a = new Actions();
           a.saveChartDatas(chartStage, arrayToPrint, rangeValueFromSlider);
           
        });
  
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, 0)); //TODO dynamicky


        final VBox vbox1 = new VBox();
        final VBox leftCornerBox = new VBox();
        Button recount = new Button("Change!");

        recount.setOnAction((event) -> {

            if (!bc.getData().isEmpty()) {
                bc.getData().remove(0);
                bc.getData().remove(0);
                createBounds(maximumValue, rangeValueFromSlider, dataToChangeRange);
            }

        });

        labellowestCentrality.setMinWidth(Region.USE_PREF_SIZE);
        labellowestCentrality.setMaxWidth(Region.USE_PREF_SIZE);
        labelbiggestCentrality.setMinWidth(Region.USE_PREF_SIZE);
        labelbiggestCentrality.setMaxWidth(Region.USE_PREF_SIZE);
        labelchooseRange.setMinWidth(Region.USE_PREF_SIZE);
        labelchooseRange.setMaxWidth(Region.USE_PREF_SIZE);
        saveChart.setMinWidth(Region.USE_PREF_SIZE);
          saveChart.setMaxWidth(Region.USE_PREF_SIZE);
        labelchooseRange.setFont(new Font("Arial", 20.0));
        vbox1.setPadding(new Insets(50, 0, 0, 560));
        leftCornerBox.setPadding(new Insets(120, 0, 0, 810));
        leftCornerBox.getChildren().addAll(saveChart,saveChartData);
        vbox1.getChildren().addAll(labellowestCentrality, labelbiggestCentrality, labelchooseRange, slider, recount, leftCornerBox);
        vbox1.setMaxWidth(500);
        slider.setMaxWidth(500);
        recount.setMaxWidth(500);
        vbox1.setMinWidth(500);
        slider.setMinWidth(500);
        recount.setMinWidth(500);
        vbox1.setSpacing(5);
        //vbox1.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(bc, vbox1);

        root.getChildren().addAll(vbox);
        chartStage.setScene(scene);
        chartStage.show();
    }

    public void startTask(Graph<Vertex, Edge> network, double range, String title) {
        // Create a Runnable zmenene na lambda expresion
        Runnable task = () -> {
            runTask(network, range, title);

        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    public void runTask(Graph<Vertex, Edge> network, double range, String title) {

        try {
            ArrayList<Pair<Vertex, Double>> data;
            if (new String("Closeness centrality").equals(title)) {
                System.out.println();
                System.out.println("Idem riesit closeness");
                System.out.println();

                Closennes c = new Closennes(network);
                c.count();
                data = c.getScores();
            } else if (new String("Betweenness centrality").equals(title)) {
                System.out.println();
                System.out.println("Idem riesit betweenes");
                System.out.println();
                Betweenness b = new Betweenness(network);
                b.count();
                data = b.getScores();
            } else {
                System.out.println();
                System.out.println("Idem riesit degree");
                System.out.println();

                Degreee d = new Degreee(network);
                d.count();
                data = d.getScores();
            }

            dataToChangeRange = new ArrayList<>(data);// copy of original data

            double max = data.get(0).getValue();
            double min = data.get(0).getValue();
            for (Pair<Vertex, Double> m : data) {
                double tmp = m.getValue();
                double tmp2 = m.getValue();
                if (tmp > max) {
                    max = tmp;
                }

                if (tmp < min) {
                    min = tmp;
                }
            }

            maximumValue = max;
            minimumValue = min;
            if ((max - (int) max) != 0) {
                double numberOfGroups = max / range;
                int numberOfGroupsInteger = (int) numberOfGroups + 1; //to get nearest upper integer value
                numberOfGroupss = numberOfGroupsInteger;
                System.out.println("Hodnota maximalnej centrality je " + max + " a range je " + range);
                System.out.println("Cislo je desatinne a pocet skupin je" + numberOfGroups + " najblizsie vyssie cislo je " + numberOfGroupsInteger + " maximalna centralita ma hodnotu" + max);
                groups = new int[numberOfGroupsInteger];
                borders = new int[numberOfGroupsInteger];
                 arrayToPrint= new int[numberOfGroupsInteger];
                System.out.println("Pocet skupin je " + groups.length);
                resetGroupsCounters(range);
            } else {
                double numberOfGroups = max / range;
                int numberOfGroupsInteger = (int) numberOfGroups + 1; //to get nearest upper integer value
                numberOfGroupss = numberOfGroupsInteger;
                System.out.println("Hodnota maximalnej centrality je " + max + " a range je " + range);
                System.out.println("Cislo je desatinne a pocet skupin je" + numberOfGroups + " najblizsie vyssie cislo je " + numberOfGroupsInteger + " maximalna centralita ma hodnotu" + max);
                groups = new int[numberOfGroupsInteger];
                borders = new int[numberOfGroupsInteger];
                 arrayToPrint= new int[numberOfGroupsInteger];
                System.out.println("Pocet skupin je " + groups.length);
                resetGroupsCounters(range);
                System.out.println("decimal value is not there");
            }

            for (Pair<Vertex, Double> m : data) {
                for (int i = 0; i < groups.length; i++) {
                    if (m.getValue() < borders[i]) {

                        groups[i]++;
                        System.out.println("Hodnota centrality je " + m.getValue() + " a to je menej ako " + borders[i] + " pricitavam k skupine " + i + " ta ma hodnotu" + groups[i]);
                        break;
                    }
                }
                //System.out.println("kluc "+ m.getKey()+" hodnota"+ m.getValue());

            }
            int lowBorder = 0;
            int rangeINT = (int) range;

            XYChart.Series series1 = new XYChart.Series();

            series1.setName("Group 1");

            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Group 2");
            int upBorder = lowBorder + rangeINT;
            for (int i = 0; i < groups.length; i++) {

                //series1.setName("Group " + lowBorder + "-" + borders[i]); //opravit ..ked nevykreslujem vsetko tak je to napicu
                series1.getData().add(new XYChart.Data(lowBorder + "-" + (upBorder - 1), groups[i]));
                series2.getData().add(new XYChart.Data(lowBorder + "-" + (upBorder - 1), groups[i] + 3));
                System.out.println("Pridavam tuto hodnotu" + groups[i]);
                lowBorder = lowBorder + rangeINT;
                upBorder = lowBorder + rangeINT;
            }

        System.arraycopy( groups, 0, arrayToPrint, 0, groups.length );
            // Update the Label on the JavaFx Application Thread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    /* XYChart.Series series1 = new XYChart.Series();
                     series1.setName("GROUP 1");*/
                    /* XYChart.Series series2 = new XYChart.Series();
                     series2.setName("GROUP 2");*/
                    bc.getData().addAll(series1, series2);
                    labelbiggestCentrality.setText("Biggest value: " + maximumValue);
                    labellowestCentrality.setText("Lowest value: " + minimumValue);

                    /*
                     for (int i = 0; i < series1.getData().size(); i++) {
                     System.out.println(series1.getData().get(i));
                     }
                     System.out.println(bc.getData().toString());*/
                }
            });

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    private void createBounds(double max, double range, ArrayList<Pair<Vertex, Double>> data) {
        if ((max - (int) max) != 0) {
            double numberOfGroups = max / range;
            int numberOfGroupsInteger = (int) numberOfGroups + 1; //to get nearest upper integer value
            numberOfGroupss = numberOfGroupsInteger;
            System.out.println("Hodnota maximalnej centrality je " + max + " a range je " + range);
            System.out.println("Cislo je desatinne a pocet skupin je" + numberOfGroups + " najblizsie vyssie cislo je " + numberOfGroupsInteger + " maximalna centralita ma hodnotu" + max);
            groups = new int[numberOfGroupsInteger];
            arrayToPrint= new int[numberOfGroupsInteger];
            borders = new int[numberOfGroupsInteger];
            System.out.println("Pocet skupin je " + groups.length);
            resetGroupsCounters(range);
        } else {
            double numberOfGroups = max / range;
            int numberOfGroupsInteger = (int) numberOfGroups + 1; //to get nearest upper integer value
            numberOfGroupss = numberOfGroupsInteger;
            System.out.println("Hodnota maximalnej centrality je " + max + " a range je " + range);
            System.out.println("Cislo je desatinne a pocet skupin je" + numberOfGroups + " najblizsie vyssie cislo je " + numberOfGroupsInteger + " maximalna centralita ma hodnotu" + max);
            groups = new int[numberOfGroupsInteger];
            borders = new int[numberOfGroupsInteger];
             arrayToPrint= new int[numberOfGroupsInteger];
            System.out.println("Pocet skupin je " + groups.length);
            resetGroupsCounters(range);
            System.out.println("decimal value is not there");
        }

        for (Pair<Vertex, Double> m : data) {
            for (int i = 0; i < groups.length; i++) {
                if (m.getValue() < borders[i]) {

                    groups[i]++;
                    System.out.println("Hodnota centrality je " + m.getValue() + " a to je menej ako " + borders[i] + " pricitavam k skupine " + i + " ta ma hodnotu" + groups[i]);
                    break;
                }
            }
            //System.out.println("kluc "+ m.getKey()+" hodnota"+ m.getValue());

        }
        int lowBorder = 0;
        int rangeINT = (int) range;

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Group 1");

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Group 2");
        int upBorder = lowBorder + rangeINT;
        for (int i = 0; i < groups.length; i++) {

            //series1.setName("Group " + lowBorder + "-" + borders[i]); //opravit ..ked nevykreslujem vsetko tak je to napicu
            series1.getData().add(new XYChart.Data(lowBorder + "-" + (upBorder - 1), groups[i]));
            series2.getData().add(new XYChart.Data(lowBorder + "-" + (upBorder - 1), groups[i] + 3));
            System.out.println("Pridavam tuto hodnotu" + groups[i]);
            lowBorder = lowBorder + rangeINT;
            upBorder = lowBorder + rangeINT;
        }
       
        System.arraycopy( groups, 0, arrayToPrint, 0, groups.length );
        
        
        
        bc.getData().addAll(series1, series2);
    }

    private void kokotinz() {

    }

}
