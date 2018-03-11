/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkMetrics;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 *
 * @author pavol
 */
public class ThreeVerticesMotifs {
     private Graph<Vertex, Edge> network;
     Vertex vertex;
     List<Vertex> connectedVertices;
     
   

    public ThreeVerticesMotifs(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    public void countMotifs()
    {
        List<Vertex> allVertices = new ArrayList<>();
        for(Vertex v: network.getVertices())
        {
            allVertices.add(v);
        }
        orderVertices(allVertices);
       
        
        
        
        for(Vertex v : allVertices)
        {
            Collection<Edge> connectedEdges = network.getIncidentEdges(v);
            connectedVertices = new ArrayList<Vertex>();
            
            int i=0;
            for(Edge e: connectedEdges)
            {
                /*pozor je tu aj spojenie 1-0 aj 0-1 atd*/
                connectedVertices.add(network.getOpposite(v, e));
                //System.out.println("Uzol " +v.toString() +" je spojeny s uzlom "+connectedVertices.get(i).toString() + "hranou s ID"+ e.getId());
                i++;
            }
            
           
          orderVertices(connectedVertices);
          System.out.println("Uzol "+v.toString()+" je spojeny s uzlami: ");
           for(int sfasf=0;sfasf<connectedVertices.size();sfasf++)
          {
            System.out.println(sfasf+". ->"+connectedVertices.get(sfasf).toString());    
          }
           
           
           System.out.println();
          for(int j=0;j<connectedVertices.size();j++)
          {
              for(int k=j+1;k<connectedVertices.size();k++)
              {
                  Vertex first =connectedVertices.get(j);
                  Vertex second = connectedVertices.get(k);
                  if(network.isNeighbor(first, second))
                  {
                   System.out.println("Vznikol trojuholník "+v.toString()+"-"+ first.toString() + "-"+second.toString());
                  }
                  else
                  {
                      System.out.println("Tieto dva nie su spojene "+ first.toString() + " a "+second.toString());
                  }
              }
          }
            
          
            System.out.println("-------------------------------------------------");
            
        }
    }
    
    private void orderVertices(List<Vertex> vertices)
    {
         Collections.sort(vertices, new Comparator<Vertex>() {
                @Override
                public int compare(Vertex v1, Vertex v2) {
                    return v1.getId() - v2.getId();
                }
            });
    }
    
}
