/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseriesanalysistool.GUI;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author pavol
 */
public class MyAlerts {

    Stage alertStage;
    //XYChart.Series series1;

    int groups[];
    int borders[];

    public MyAlerts() {
    }
    
    private void whichCentralityisChosen(Graph<Vertex, Edge> network, int chosenCentrality)
    {
        System.out.println("------------------Centrality is "+chosenCentrality);
         if(chosenCentrality==1)
         {
         ChartMaker ch = new ChartMaker();
         ch.createChart("Betweenness centrality", network, 5000);
         }
         else if(chosenCentrality==2)
         {
             ChartMaker ch = new ChartMaker();
             ch.createChart("Closeness centrality", network, 100);
         }
         else if(chosenCentrality==3)
         {
                    ChartMaker ch = new ChartMaker();
                    ch.createChart("Degree", network, 2);
         }
         else if(chosenCentrality ==4)
         {
            ChartMaker ch= new ChartMaker();
            ch.createChart("Eigenvector centrality", network, 5);
         }
          else if(chosenCentrality == 5)
        {
            ChartMaker ch= new ChartMaker();
            ch.createChart("Degree centrality", network, 0.01);
        }
    }
    
    
    public void filesWasConverted()
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Success");
        alert.setContentText("Chosen files were converted!");

alert.showAndWait();
    }
    
    public void alertUnsupportedFileContent()
    {
         Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("An error here");
        alert.setContentText("Program is not able to convert this file.");

        alert.showAndWait();
    }
    
    public void displayDialog(Graph<Vertex, Edge> network, int chosenCentrality, List<File> filesGroup1, List<File> filesGroup2) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("There is both- network drawn and files in table.");
        alert.setContentText("Which one do you want to use for metrics?");

        ButtonType buttonTypeOne = new ButtonType("Network");
        ButtonType buttonTypeTwo = new ButtonType("Files in table");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            whichCentralityisChosen(network, chosenCentrality);
        } else if (result.get() == buttonTypeTwo) {
            System.out.println("Tu bude konverzia sieti v tabulkach + vypocet centralit");
        } else {
    // ... user chose CANCEL or closed the dialog
        }

    }

    public void displayAlert(String alertText) {
        /*
         Group root = new Group();
         Scene scene = new Scene(root, 350, 350, Color.GREY);
        
         final VBox vb = new VBox();
         alertStage = new Stage();
         alertStage.setTitle("Warning");
         alertStage.initStyle(StageStyle.UNDECORATED);
         Label l = new Label(alertText);
         l.setFont(new Font("Arial", 20.0));
         l.setMaxWidth(350);
         Button b = new Button("Ok");
         b.setOnAction((event) -> {

         alertStage.close();
         });
         vb.setSpacing(5);
         vb.setMaxWidth(350);
         vb.setAlignment(Pos.CENTER);
         vb.getChildren().addAll(l, b);
         root.getChildren().add(vb);
         alertStage.setScene(scene);
         alertStage.show();
         */

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("An error here");
        alert.setContentText(alertText);

        alert.showAndWait();
    }

    
    public void pleaseConvertSomeFileInTable()
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("An error here");
        alert.setContentText("There are some files in table, but no file was converted into network.");

        alert.showAndWait();
    }
    
    
     public void closenesTestDialog()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("ok");
        alert.setHeaderText("OK");
        alert.setContentText("Done");

        alert.showAndWait();
    }
     
       public void saveEror()
    {
         Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("An error here");
        alert.setContentText("There is no network to save.");

        alert.showAndWait();
    }
       
       
           public void NetworkNullProperty()
    {
         Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("An error here");
        alert.setContentText("You must create network first.");

        alert.showAndWait();
    }
}
