package com.example.umleditor.model.objects;

import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.Label;
import com.example.umleditor.model.Port;

import java.awt.*;

public abstract class BasicObject extends GraphicObject{
    // record the top-left coord of object
    protected int x; // x coord (leftmost)
    protected int y; // y coord (top)
    // tbd
    protected int width;
    protected int height;
    // tbd
    protected Label label;
    protected Port[] ports;

    public static final int MIN_SIZE = 20;

    // allow child objects to init number of ports and location
    protected abstract Port[] createPorts();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Port[] getPorts() {
        return ports;
    }

    public void setPorts(Port[] ports) {
        this.ports = ports;
    }

    public void resize(Port draggedPort, Point currentPoint, Point anchor){
        double rx = draggedPort.getRelativeX();
        double ry = draggedPort.getRelativeY();

        // the point that does not move during resize (the opposite point in the object)
        int anchorX = anchor.x;
        int anchorY = anchor.y;

        int newX = this.x;
        int newY = this.y;
        int newWidth = this.width;
        int newHeight = this.height;

        // its diagonal resize
        if(rx!=0.5){
            // if crossing -> still working since abs
            newWidth = Math.max(Math.abs(currentPoint.x-anchorX), MIN_SIZE);
            // if the new point cross the anchor point, make it new X (the top left point)
            // if the new point does not cross -> anchor does not change
            // else -> anchor - width/height = new anchor location (top left)
            newX = (currentPoint.x>=anchorX) ? anchorX : anchorX-newWidth;
        }
        if(ry!=0.5){
            newHeight = Math.max(Math.abs(currentPoint.y-anchorY), MIN_SIZE);
            newY = (currentPoint.y>=anchorY) ? anchorY : anchorY-newHeight;
        }
        this.x = newX;
        this.y = newY;
        this.width = newWidth;
        this.height = newHeight;
    }

}
