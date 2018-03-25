/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Layouts;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import java.awt.Dimension;
import timeseriesanalysistool.GUI.Design;

/**
 *
 * @author pavol
 */
public class LayoutKK  extends Layout{
     @Override
    public void runLayout(Graph<Vertex, Edge> network) {
       
        int requiredOperations = 60;
        KKLayout layout = new KKLayout(network);
        layout.initialize();
        DefaultVisualizationModel<Integer,Integer> model = new DefaultVisualizationModel<>(layout, new Dimension(Design.canvasWidth,Design.canvasHeight));
        layout.reset();
        for(int doneOperations = 0; doneOperations < requiredOperations; doneOperations++){
            layout.step();
        }
        
        for(Vertex v : network.getVertices()){
            v.setPositionX((float)layout.getX(v));
            v.setPositionY((float)layout.getY(v));
        }
        
        
    }
}
