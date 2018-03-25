/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Layouts;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import java.awt.Dimension;
import timeseriesanalysistool.GUI.Design;

/**
 *
 * @author pavol
 */
public class LayoutCircular extends Layout{

    @Override
    public void runLayout(Graph<Vertex, Edge> network) {
        
        CircleLayout layout = new CircleLayout(network);
        layout.initialize();
        DefaultVisualizationModel<Integer,Integer> model = new DefaultVisualizationModel<>(layout, new Dimension(Design.canvasWidth, Design.canvasHeight));
        model.fireStateChanged();
        layout.reset();
        
        for(Vertex v : network.getVertices()){
            v.setPositionX((float)layout.getX(v));
            v.setPositionY((float)layout.getY(v));
        }
        
    }
    }
    

