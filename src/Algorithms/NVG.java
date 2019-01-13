/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

//import NetworkComponents.Edge;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import timeseriesanalysistool.GUI.Design;
import timeseriesanalysistool.GUI.MyAlerts;

/**
 *
 * @author pavol
 */
public class NVG {
    
    
    public Graph<Vertex,Edge> createNVGNetwork(File file){
        ArrayList<Double> values = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            try {
                while(br.ready())
                    values.add(Double.parseDouble(br.readLine()));
            } catch (Exception ex) {
                System.err.println("File error");
            }
        } catch (FileNotFoundException ex) {
            System.err.println("File not found.");
        } finally{
            if(br != null)
                try {
                    br.close();
            } catch (IOException ex) {
                System.err.println("Error while closing file");
            }
        }
        if(values.isEmpty())
            return null;
        return createNet(values,file);
    }
  

    
    private Graph<Vertex,Edge> createNet(ArrayList<Double> series, File f){
        int edgeID=0;
        Graph<Vertex,Edge> graph = initVertices(series.size());
        for(int i = 0; i < series.size(); i++){
            for(int j = i+1; j < series.size(); j++){
                boolean edge = true;
                for(int k = i+1; k < j; k++){
                    if(series.get(i) + ((series.get(j) - series.get(i))/(j - i))*(k-i) <= series.get(k)){
                       edge = false;
                       break;
                    }
                }
                if(edge){    
                    
                    graph.addEdge(new Edge(edgeID), vertices[i], vertices[j]);
                    System.out.println("Som v triede NVG, pracujem na subore"+f.getName() + "Hrana medzi uzlami: "+ vertices[i].toString()+" a "+vertices[j].toString()+" a id hrany je "+edgeID);
                    edgeID++;
                    
                }
            }
        }
        
        
        
        Design.a++;
        return graph;
    }
    
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
}
