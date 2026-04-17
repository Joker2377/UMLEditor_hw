package com.example.umleditor.model;

import com.example.umleditor.model.objects.BasicObject;
import java.awt.*;

public class Port {
    public static final int SIZE = 6;
    public static final int HIT_RADIUS = 15;

    private BasicObject owner;
    // 0~1
    private double relativeX;
    private double relativeY;

    public Port(BasicObject owner, double relativeX, double relativeY) {
        this.owner = owner;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }

    public BasicObject getOwner() {
        return owner;
    }

    // to make sure the port will auto update location according to object move
    public int getAbsoluteX(){
        return owner.getX() + (int)(owner.getWidth()*relativeX);
    }

    public int getAbsoluteY(){
        return owner.getY() + (int)(owner.getHeight()*relativeY);
    }

    public double getRelativeX() {
        return relativeX;
    }

    public double getRelativeY() {
        return relativeY;
    }

    // draw small black cube -> port loc
    public void draw(Graphics2D g){
        int cx = getAbsoluteX();
        int cy = getAbsoluteY();
        g.setColor(Color.BLACK);
        g.fillRect(cx - SIZE/2, cy - SIZE/2, SIZE, SIZE);
    }

    // check if a point is within range of hit radius
    public boolean contains(Point p){
        int cx = getAbsoluteX();
        int cy = getAbsoluteY();
        return Math.pow(p.x - cx, 2) + Math.pow(p.y - cy, 2) <= Math.pow(HIT_RADIUS, 2);
    }
}
