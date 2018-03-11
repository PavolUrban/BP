/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkMetrics;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class Betweenness {
    private Graph<Vertex, Edge> network;
    private ArrayList<Pair<Vertex, Double>> scores;

    public Betweenness(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    public void count() {
        BetweennessCentrality centrality = new BetweennessCentrality(network);
        scores = new ArrayList<>();
        for(Vertex v : network.getVertices()){
            scores.add(new Pair(v, centrality.getVertexScore(v)));
        }
    }
    
   
    public ArrayList<Pair<Vertex, Double>> getScores()
    {
        return scores;
    }
    
    
}
