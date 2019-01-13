/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import NetworkMetrics.Betweenness;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class ProgresBarGenerator {
     public ProgresBarGenerator()
     {}
     
     Stage loadingStage;
     public void run(Graph<Vertex, Edge>network)
     { 
          Group root = new Group();
        Scene scene = new Scene(root, 350, 350);
        loadingStage = new Stage();
        loadingStage.setScene(scene);
        loadingStage.setTitle("Progress Controls");
         Task<Void> task = new Task<Void>() {
      @Override public Void call() {
        for (int i = 0; i < 10; i++) {
          try {
               ArrayList<Pair<Vertex, Double>> data;
                Betweenness b = new Betweenness(network);
                b.count();
                data = b.getScores();
             Thread.sleep(100);
          } catch (InterruptedException e) {
            Thread.interrupted();
            break;
          }
          System.out.println(i + 1);
          updateProgress(i + 1, 10);
        }
        return null;
      }
    };

    ProgressBar updProg = new ProgressBar();
    updProg.progressProperty().bind(task.progressProperty());

    Thread th = new Thread(task);
    th.setDaemon(true);
    th.start();
final VBox vb = new VBox();
 vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(updProg);
    scene.setRoot(vb);
        loadingStage.initStyle(StageStyle.UNDECORATED);
        loadingStage.show();
  }
     }

