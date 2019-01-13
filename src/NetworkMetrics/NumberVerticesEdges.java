/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkMetrics;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;

/**
 *
 * @author pavol
 */
public class NumberVerticesEdges {
    private Graph<Vertex, Edge> network;
    private double numberOfVertices;
    private double numberOfEdges;
    

    public NumberVerticesEdges(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    public void count() {
       numberOfVertices = network.getVertexCount();
        numberOfEdges =network.getEdgeCount();
    
    }
    
   
    public double getNumberOfVertices()
    {
        return numberOfVertices;
    }
    
    public double getNumberOfEdges()
    {
        return numberOfEdges;
    }
    
}
