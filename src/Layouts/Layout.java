/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Layouts;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import timeseriesanalysistool.GUI.Design;

/**
 *
 * @author pavol
 */
public abstract class Layout {
    
    public abstract void runLayout(Graph<Vertex, Edge> network);
    
    public void fitToScrean(Graph<Vertex,Edge> network){
        double minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
        for(Vertex v : network.getVertices()){
            if(v.getPositionX() < minX){
                minX = v.getPositionX();
            }
            if(v.getPositionX() > maxX){
                maxX = v.getPositionX();
            }
            if(v.getPositionY() < minY){
                minY = v.getPositionY();
            }
            if(v.getPositionY() > maxY){
                maxY = v.getPositionY();
            }
        }
        double difX = maxX - minX;
        double difY = maxY - minY;
        double xRange = Design.canvasWidth - 100;
        double yRange = Design.canvasHeight - 100;
        for(Vertex v : network.getVertices()){
            v.setPositionX(((v.getPositionX()-minX)/difX)*xRange + 50);
            v.setPositionY(((v.getPositionY()-minY)/difY)*yRange + 50);
        }
        System.out.println("Done");
    }
    
}
