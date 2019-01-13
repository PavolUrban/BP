/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkMetrics;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class AverageDegree {
     private Graph<Vertex, Edge> network;
    private double averageDegree;
    
    

    public AverageDegree(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    public void count() {
        double upside=0;
        System.out.println("Horna strana zlomku");
        for(Vertex v : network.getVertices()){
          
            upside += (double)network.getOutEdges(v).size();
              System.out.println((double)network.getOutEdges(v).size());
        }
        
        double numberOfVertices = network.getVertexCount();
        
        averageDegree = upside/numberOfVertices;
        
    
    }
    
   
    public double getAverageDegree()
    {
        return averageDegree;
    }
    
    
}
