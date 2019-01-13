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
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author pavol
 */
public class ReccurenceNetwork {
     public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    private int lengthOfSegment = 5;
/*
    public void setLengthOfSegment(int length) {
        this.lengthOfSegment = length;
    }
*/
    public Graph<Vertex, Edge> createReccurenceNetwork(File file, int delay, int lengthOfSegment, double criticalValue) {

        System.out.println("Delay "+delay+" lengtofSegment"+lengthOfSegment);
        ArrayList<Double> values = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            while (br.ready()) {
                    values.add(Double.parseDouble(br.readLine()));
                }
        } catch (IOException ex) {
            System.err.println("File not found.");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    System.err.println("Error while closing file");
                }
            }
        }
        if (values.isEmpty()) {
            return null;
        }

        return createNet(values, delay,lengthOfSegment, criticalValue);
    }

    private Vertex[] vertices;
    private double[] segmentConstants;

    private Graph<Vertex, Edge> createNet(ArrayList<Double> series,int delay, int lengthOfSegment, double criticalValue) {
        //int edgeID=0;

        SparseGraph<Vertex, Edge> graph = new SparseGraph<>();
        

        //creating of vertices
        int numberOfVertices = series.size() - (lengthOfSegment*delay -delay);
        vertices = new Vertex[numberOfVertices];
        
        segmentConstants = new double[numberOfVertices]; //for each vertice represented by segment there is value of <Ti>

        for (int i = 0; i < numberOfVertices; i++) {
            vertices[i] = new Vertex(i);
            graph.addVertex(vertices[i]);
        }
        
        double[][] multi = new double[numberOfVertices][lengthOfSegment]; //pole pre kazdy uzol prislusne hodnoty casovej rady z ktorych vyskladam vektor
        
        for (int x=0; x<numberOfVertices; x++) {
            for (int y=0; y<multi[x].length; y++) {
                multi[x][y]=series.get(x+y*delay);//your value
     }
 }
        
      /*  for(int i = 0; i < numberOfVertices; i++)
   {
      for(int j = 0; j < lengthOfSegment; j++)
      {
         System.out.print(multi[i][j]+" ");
      }
      System.out.println();
   }*/
        int idEdge=0;
        for(int x=0; x<numberOfVertices; x++)
        {
            for(int y=x+1;y<numberOfVertices;y++)
            {
                double results = 0.0;
                for( int z=0;z<lengthOfSegment;z++)
                {
                    results += Math.abs(multi[x][z]-multi[y][z])*Math.abs(multi[x][z]-multi[y][z]);
                }
            
                double finalResult = Math.sqrt(results);
                //System.out.println("TOTO TY KOKOT "+finalResult);
                if(criticalValue-finalResult>0)
                { 
                    graph.addEdge(new Edge(idEdge), vertices[x],vertices[y]);
                    //System.out.println("Critical value je "+criticalValue+" - "+finalResult+ " co je viac ako nula vytvorim hranu");
                }
            }
        }
        
        return graph;
    }

}
