/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkMetrics;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class ClusteringCoefficients {
      private Graph<Vertex, Edge> network;
    private ArrayList<Pair<Vertex, Double>> results;

    public ClusteringCoefficients(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
   
    public void count() {
        Map<Vertex, Double> map = edu.uci.ics.jung.algorithms.metrics.Metrics.clusteringCoefficients(network);
        results = new ArrayList<>();
        
        double allTogether=0.0;
        for(Vertex v : network.getVertices()){
            System.out.println(map.get(v));
            //System.out.println("Hodnota clustering coeficientu je "+map.get(v));
            allTogether += map.get(v);
            results.add(new Pair(v, map.get(v)));
        }
        
        System.out.println("Vsetko dokopy je "+allTogether+" globalny clustering je"+(allTogether/network.getVertexCount()));
    }
    
    
  
    
    public ArrayList<Pair<Vertex, Double>> getScores()
    {
        
        return results;
    }

   
}
