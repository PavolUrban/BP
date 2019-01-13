/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author pavol
 */
public class KNearestNeighborGraph {
    private Vertex[] vertices;

    private Graph<Vertex, Edge> initVertices(int size) {
        vertices = new Vertex[size];
        SparseGraph <Vertex, Edge> graph = new SparseGraph<>();
        for(int i = 0; i < size; i++){
            vertices[i] = new Vertex(i);
            graph.addVertex(vertices[i]);
            
        }
        return graph;
    }
    
    
   
    
    public Graph<Vertex, Edge> readCSV() throws FileNotFoundException
    {
        String[] tempArray;
        Scanner scanner = new Scanner(new File("C:\\kidsTestingData.txt"));
        scanner.useDelimiter(",");
        
        Map<Integer, String[]> map = new HashMap<Integer, String[]>();
        Map<Integer, Double> topDistances = new HashMap<Integer, Double>();
        Map<Integer, Integer> topNeighbor = new HashMap<Integer, Integer>();
        int numberOfVertices = 0;
        
        while(scanner.hasNext())
        {
            String record = scanner.next();
            tempArray = record.split(";");
            map.put(numberOfVertices, tempArray);
            topDistances.put(numberOfVertices, 88888.8);
            topNeighbor.put(numberOfVertices, numberOfVertices);
            numberOfVertices++;
        
        }
       
        scanner.close();
        
        Graph<Vertex,Edge> graph = initVertices(numberOfVertices);
      //  System.out.println("Pocet uzlov bude "+numberOfVertices);
        int edgeID=0;
        for (Map.Entry<Integer, String[]> firstObject : map.entrySet()) {
            for (Map.Entry<Integer, String[]> secondObject : map.entrySet()){
                 ArrayList<Double> values1 = new ArrayList<>();
                 ArrayList<Double> values2 = new ArrayList<>();
                
                 if(firstObject.getKey() != secondObject.getKey() && secondObject.getKey()>firstObject.getKey())
                {
                    for(String singleValue : firstObject.getValue())
                    {
                        values1.add(Double.parseDouble(singleValue));
                    }
                    
                    for(String singleValue : secondObject.getValue())
                    {
                        values2.add(Double.parseDouble(singleValue));
                    }
                   
                    
                    double distance = countEuclideanDistance(values1, values2);
                    
                  
                    if(distance< topDistances.get(firstObject.getKey()))
                    {
                        System.out.println("Menim na hodnotu "+ distance);
                        int secondID = secondObject.getKey();
                        topNeighbor.replace(firstObject.getKey(), topNeighbor.get(firstObject.getKey()), secondID);
                        topDistances.replace(firstObject.getKey(), topDistances.get(firstObject.getKey()), distance);
                    }
                    else
                    {
                        System.out.println("Zostavam na hodnote"+ topDistances.get(firstObject.getKey()));
                    }
                   // System.out.println("Vzdialenost medzi "+firstObject.getKey()+" "+secondObject.getKey()+" je "+countEuclideanDistance(values1, values2));
                }
                 
                 
            }
            
            graph.addEdge(new Edge(edgeID), vertices[firstObject.getKey()], vertices[topNeighbor.get(firstObject.getKey())]);
            
        }
        
        
        return graph;
    }


    public double countEuclideanDistance( ArrayList<Double> values1,  ArrayList<Double> values2)
    {
        double sum = 0.0;
        for (int x=0; x<values1.size(); x++)//nezacinam 0 aby sa ignoroval prvy riadok - premysliet
        {
            
            sum += Math.pow(values1.get(x)-values2.get(x),2);
        }

        double result = Math.sqrt(sum);
        
        return result;
    }
    
}
