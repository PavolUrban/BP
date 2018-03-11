/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseriesanalysistool.GUI;

import Algorithms.NVG;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import NetworkMetrics.Betweenness;
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class ChartMaker {

    Stage chartStage;
    //XYChart.Series series1;
    BarChart<String, Number> bc;

    int groups[];
    int borders[];

    public ChartMaker() {
    }

    public void createChart(String title, Graph<Vertex, Edge> network, double range) {

        ScrollPane scrollPane = new ScrollPane();

        startTask(network, range);

        chartStage = new Stage();
        chartStage.setTitle(title);
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("Betweenness centrality - Vertices summary");
        xAxis.setLabel("Vertices");
        xAxis.setPrefWidth(5000);

        yAxis.setLabel("Value");

        bc.setPrefWidth(2000);//todo
        bc.setMinWidth(2000);

        Scene scene = new Scene(scrollPane, 900, 700);
        scrollPane.setContent(bc);

        chartStage.setScene(scene);
        chartStage.show();
    }

    public void startTask(Graph<Vertex, Edge> network, double range) {
        // Create a Runnable
        Runnable task = new Runnable() {
            public void run() {
                runTask(network, range);
            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    public void runTask(Graph<Vertex, Edge> network, double range) {

        try {
                // Get the Status

            Betweenness b = new Betweenness(network);
            b.count();
            ArrayList<Pair<Vertex, Double>> data = b.getScores();
            double max = data.get(0).getValue();
            for (Pair<Vertex, Double> m : data) {
                double tmp = m.getValue();
                if (tmp > max) {
                    max = tmp;
                }
            }

            if ((max - (int) max) != 0) {
                double numberOfGroups = max / range;
                int numberOfGroupsInteger = (int) numberOfGroups + 1; //to get nearest upper integer value
                System.out.println("Cislo je desatinne a pocet skupin je" + numberOfGroups + " najblizsie vyssie cislo je " + numberOfGroupsInteger + " maximalna centralita ma hodnotu" + max);
                groups = new int[numberOfGroupsInteger];
                borders = new int[numberOfGroupsInteger];
                System.out.println("Pocet skupin je " + groups.length);
                resetGroupsCounters(range);
            } else {
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

            // Update the Label on the JavaFx Application Thread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    /* TODO Odstarnit praznde skupiny*/
                    int lowBorder = 0;
                    int rangeINT = (int) range;
                    
                    for (int i = 0; i < groups.length; i++) {
                        if (groups[i] == 0) {
                            lowBorder = lowBorder + rangeINT;
                            break;
                        } else {
                            XYChart.Series series1 = new XYChart.Series();
                            series1.setName("Group " + lowBorder + "-" + borders[i]); //opravit ..ked nevykreslujem vsetko tak je to napicu

                            series1.getData().add(new XYChart.Data("", groups[i]));
                            bc.getData().add(series1);
                            lowBorder = lowBorder + rangeINT;

                        }
                    }

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

}
