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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 *
 * @author pavol
 */
public class CorrelationNetwork {
    
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
   
    private int lengthOfSegment= 5;
    
    public void setLengthOfSegment(int length)
    {
        this.lengthOfSegment = length;
    }
    
    public Graph<Vertex,Edge> createCorrelationNetwork(File file){
        
        ArrayList<Double> values = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            try {
                while(br.ready())
                    values.add(Double.parseDouble(br.readLine()));
            } catch (IOException ex) {
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
        
        
        return createNet(values);
    }
    
    
    private Vertex[] vertices;
    private double[] segmentConstants;
    
    
    
    private Graph<Vertex,Edge> createNet(ArrayList<Double> series){
        //int edgeID=0;
        
        SparseGraph <Vertex, Edge> graph = new SparseGraph<>();   
        double segmentConstant=0.0;
       
        
        
        //creating of vertices
        int numberOfVertices = series.size()-lengthOfSegment+1;
        System.out.println("Pocet uzlov bude "+numberOfVertices);
        vertices = new Vertex[numberOfVertices];
        segmentConstants = new double[numberOfVertices]; //for each vertice represented by segment there is value of <Ti>
        
        for(int i = 0; i < numberOfVertices; i++){
            vertices[i] = new Vertex(i);
            graph.addVertex(vertices[i]);
        }
        
        
        
        
        
        
        
        int constantPointer=0; //to select from array Left segment <Ti> part
      
        for(int a=0;a<series.size()-lengthOfSegment;a++)
        {
           
            double [] pointsLeftSide= new double[lengthOfSegment];
            int iterationsCounter=0;
            for(int i=a;i<lengthOfSegment+a;i++)
            {
                double result1 =series.get(i)/lengthOfSegment; //counting <Ti> 
                segmentConstant += result1;
                
                pointsLeftSide[iterationsCounter]=series.get(i);//selecting Ti points
                iterationsCounter++;
            }
            segmentConstants[a] = segmentConstant;
           
            int myPoint = 0;
            
            
            double upside=0.0; //upside of formula result
            
            for(int as=a+1;as<=series.size()-lengthOfSegment;as++)
            {
                double segmentConstant2=0.0;
                
                
                //counting of <Tj>  - right side 
                 double [] pointsRightSide= new double[lengthOfSegment];
                for(int fasfasr=0;fasfasr<lengthOfSegment;fasfasr++)
                {
                    double result1 =series.get(as+fasfasr)/lengthOfSegment;
                    segmentConstant2 += result1;
                    
                    pointsRightSide[fasfasr]=series.get(as+fasfasr);//selecting Tj points
               
                }
               segmentConstants[as] = segmentConstant2;
               segmentConstant2 =0.0;
              
                
                myPoint=0;
                double downsideLeftPart =0.0;
                double downsideRightPart= 0.0;
                for(int safs =0;safs<lengthOfSegment;safs++)
                {
                    double addValuea=(pointsLeftSide[myPoint]-segmentConstants[constantPointer]) * (pointsRightSide[myPoint]-segmentConstants[as]);
                    upside += addValuea;
                    System.out.println("Horna strana [" + pointsLeftSide[myPoint]+ "-"+segmentConstants[constantPointer]+"]"+" * "+pointsRightSide[myPoint]+"-"+segmentConstants[as]);
                    //System.out.println("Pripocitavam k hornej strane "+ addValuea);
                    
                    downsideLeftPart += Math.pow(pointsLeftSide[myPoint]-segmentConstants[constantPointer],2);
                    downsideRightPart +=Math.pow(pointsRightSide[myPoint]-segmentConstants[as], 2);
                    
                   
                    
                    
                    
                    myPoint++;
                }
                
                /*System.out.println("Finalny zlomok:");
                System.out.println("->Horna strana" + upside);
                 System.out.println("->Lava dole "+downsideLeftPart+" pravo dole: "+downsideRightPart);*/
                double downsideResult = Math.sqrt(downsideLeftPart)*Math.sqrt(downsideRightPart);
                
                double finalResult = upside/downsideResult;
                if(finalResult>0 && finalResult<=1)
                {
                    System.out.println(ANSI_GREEN_BACKGROUND+ "Finalny vysledok"+ finalResult);
                }
                else if(finalResult<0&& finalResult>=-1)
                {
                    System.out.println(ANSI_RED_BACKGROUND+ "Finalny vysledok"+ finalResult);
                }
                       
                else
                    System.out.println("Finalny vysledok"+ finalResult);
                upside=0.0;
                
                
            }
            
            constantPointer++;
            segmentConstant=0.0;
    
        }
        
       
        
        
        
        
        
        
        
        return graph;
    }

}
