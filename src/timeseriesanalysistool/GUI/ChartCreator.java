/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseriesanalysistool.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author pavol
 */


public class ChartCreator {
    
     Stage chartStage;


    int groups[];
    int borders[];

    public ChartCreator() {
    }
    
    public void createChart()
    {
        StackPane root = new StackPane();
        Scene scene = new Scene (root,330,250);
    
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
    
        ScatterChart chart = new ScatterChart(xAxis, yAxis, getChartData());
        chart.setTitle("Speculations");
        root.getChildren().add(chart);
        chart.setAnimated(false);
        chartStage.setScene(scene);
        chartStage.show();
    }
    
    private ObservableList<Series<String,Double>> getChartData()
    {
        double javaValue=17.56;
        double cValue=24.4;
        double cppValue=32.1;
        ObservableList<XYChart.Series<String, Double>> data = FXCollections.observableArrayList();
        Series<String, Double> java = new Series<>();
        Series<String, Double> c = new Series<>();
        Series<String, Double> cpp = new Series<>();
        
        java.setName("Java");
        c.setName("c");
        cpp.setName("c++");
        
        for(int i=2011;i<2021;i++)
        {
            java.getData().add(new XYChart.Data(Integer.toString(i),javaValue));
            javaValue= javaValue+1;
            
            c.getData().add(new XYChart.Data(Integer.toString(i),cValue));
            cValue= cValue-1;
            
            cpp.getData().add(new XYChart.Data(Integer.toString(i),cppValue));
            cppValue= cppValue+1;
        }
        
        data.addAll(java,c, cpp);
        return data;
        
    }
    
    
    
}
