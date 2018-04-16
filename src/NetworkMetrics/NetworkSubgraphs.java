/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkMetrics;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.util.List;

/**
 *
 * @author pavol
 */
public class NetworkSubgraphs {
     private Graph<Vertex, Edge> network;
     Vertex vertex;
     List<Vertex> connectedVertices;
     
   

    public NetworkSubgraphs(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    public void enumerate()
    {
        
    }
    
}

