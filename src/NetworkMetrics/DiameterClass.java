/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkMetrics;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.algorithms.shortestpath.Distance;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class DiameterClass {
   //public DistanceStatistics(){}
   
    private Graph<Vertex, Edge> network;
    private double diamaterValue;

    public DiameterClass(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    public void count() {
       
         diamaterValue  = DistanceStatistics.diameter(network);
       
    }
    
   
    public double getDiameter()
    {
          System.out.println("Toto je hodnota ktoru posielam"+diamaterValue);
        return diamaterValue;
    }

}
