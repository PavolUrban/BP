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
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class DegreeCentrality {
        private Graph<Vertex, Edge> network;
    private ArrayList<Pair<Vertex, Double>> scores;
    
    
    public DegreeCentrality(Graph<Vertex, Edge> network)
    {
        this.network=network;
    }
    
    public void count() {
        scores = new ArrayList<>();
        double numberOfNodesInNetwork= network.getVertexCount();
        for(Vertex v : network.getVertices()){
            double numberOfConnectedVertices = (double)network.getOutEdges(v).size();
            double result = numberOfConnectedVertices/numberOfNodesInNetwork;
            
            System.out.println("pocet pripojeni" +numberOfConnectedVertices+" pocet uzlov v sieti " +numberOfNodesInNetwork+", vysledok je "+result);
            scores.add(new Pair(v, result));
        }
    }
     
    public ArrayList<Pair<Vertex, Double>> getScores()
    {
        return scores;
    }
}
