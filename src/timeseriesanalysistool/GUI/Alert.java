/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseriesanalysistool.GUI;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
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
public class Alert {
     Stage alertStage;
    //XYChart.Series series1;
  

    int groups[];
    int borders[];

    public Alert() {
    }

    public void displayAlert(String alertText) {

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
    }

}
