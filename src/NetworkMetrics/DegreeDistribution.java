/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkMetrics;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class DegreeDistribution {
    private Graph<Vertex, Edge> network;
    private ArrayList<Pair<Integer, Double>> results;
     private ArrayList<Pair<Integer, Double>> results2;
    private int max = 0;
   

    public DegreeDistribution(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
   
    
    public void countDegreeDistribution() {
        int numberOfVertices = network.getVertexCount();
        int degree[] = new int[numberOfVertices];
        int valueOfDegree[]= new int[numberOfVertices];
        for(Vertex v : network.getVertices()){
            int value = network.getOutEdges(v).size();
            degree[value]++;
        }
        results = new ArrayList<>();
        results2 =new ArrayList<>();
        for(int i = 0; i < numberOfVertices; i++){
            if(degree[i]> 0){
                max = i;
                //results.add(new Pair(i, degree[i]/(numberOfVertices - 1.0)));  
                results2.add(new Pair(max, degree[i]/(numberOfVertices - 1.0)));
            }
        }
        
        for(int i=0;i<results2.size();i++)
        {
            DecimalFormat dec = new DecimalFormat("#0.0000000");
            System.out.println("Degree "+results2.get(i).getKey()+" distribution"+dec.format(results2.get(i).getValue()));
        }
    }
    
    public ArrayList getResults()
    {
        return this.results2;
    }
}
