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
     List<Vertex> toRootconnectedVertices;
     List<Vertex> toFirstLevelChildconnectedVertices;
     List<Vertex> toSecondLevelChildconnectedVertices;
     
   

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
        
        
        //urcime roota a oznacime ho visited
        Vertex root = allVertices.get(2);
        root.setisVisited(true);
        System.out.println("Toto je root "+ root.toString() + " a "+root.getisVisited());
        
        
        
        //najdeme potomkov a oznacime ich visited
        toRootconnectedVertices = new ArrayList<Vertex>();
        int i=0;
        for(Vertex rootsChild : network.getNeighbors(root))
        {
            toRootconnectedVertices.add(rootsChild); 
            rootsChild.setisVisited(true);
            i++;
        }
        orderVertices(toRootconnectedVertices);
      
        
        
       System.out.println("Uzol root"+root.toString()+" je "+ root.getisVisited() +" a je spojeny s uzlami: ");
       System.out.println();
       
      toFirstLevelChildconnectedVertices = new ArrayList<Vertex>();
       for(Vertex firstLevelChild: toRootconnectedVertices)
       {
           System.out.println("\t"+root.toString()+"-> "+firstLevelChild.toString()+", ktory dalej susedi s: ");
           for(Vertex asda : network.getNeighbors(firstLevelChild))
           {
               if(asda.getisVisited()== true)
               {    
                    System.out.println("\t\t"+firstLevelChild.toString()+" ->"+asda.toString()+" UZ JE VISITED");
               }
               else
               {
                   toFirstLevelChildconnectedVertices.add(asda);
                   asda.setisVisited(true);
                   System.out.println("\t\t"+firstLevelChild.toString()+" ->"+asda.toString()+", ktory je "+asda.getisVisited());
               }
               
               //tu zoradit
           }
           //System.out.println("     -"+v.toString()+", ktory je "+v.getisVisited());
           
       }
       
       
       
       
       
       
       
       
       
       
       
       System.out.println();
       for(Vertex v:allVertices)
       {
           System.out.println(v.toString()+" a ten je "+v.getisVisited());
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
