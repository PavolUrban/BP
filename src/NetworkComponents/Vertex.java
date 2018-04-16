/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkComponents;

import java.util.Random;
import timeseriesanalysistool.GUI.Design;

/**
 *
 * @author pavol
 */
public class Vertex {
    private final int id;
    private final String label;
    private double positionX;
    private double positionY;
    private double size = 10;
    private Boolean isVisited;
    
    private double revValue = -1;
    private String revChange = "";

    public Vertex(int id) {
        this(id, "");
        
    }
    public Vertex(int id, String label){
        this.id = id;
        this.label = label;
        Random r = new Random();
        this.positionX = r.nextInt(Design.canvasWidth)-5; //este upravit
        this.positionY = r.nextInt(Design.canvasHeight)-5;
        this.isVisited =false;
    }
    
    public Vertex(int id, String label, double positionX, double positionY) {
        this.id = id;
        this.label = label;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getSize() {
        return size;
    }

    public double getRevValue() {
        return revValue;
    }

    public String getRevChange() {
        return revChange;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        if("".equals(label))
            return "Vertex " + id;
        return "Vertex " + id + ": " + label;
    }

    public void setRevValue(double revValue) {
        this.revValue = revValue;
    }

    public void setRevChange(String revChange) {
        this.revChange = revChange;
    }
    
    public void setisVisited(Boolean visited)
    {
        this.isVisited = visited;
    }
    
    public Boolean getisVisited()
    {
        
        return this.isVisited;
    }
    
}