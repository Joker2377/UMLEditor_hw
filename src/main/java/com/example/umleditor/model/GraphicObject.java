package com.example.umleditor.model;

import java.awt.*;

public abstract class GraphicObject {
    protected int depth; // order of the graphic objects: smaller = upper
    protected boolean selected; // if the object is selected
    protected boolean hovered; // if the object is hovered (the mouse pointer is within the area of the graphic, and the graphic is on top)

    // abstract method for drawing Graphic (left for polymorphism)
    public abstract void draw(Graphics2D g);
    // if P is within range of graphic
    public abstract boolean contains(Point p);
    // get the rectangle bounds of the graphic (smallest possible rect)
    public abstract Rectangle getBounds();
    // move the Graphic by (dx, dy)
    public abstract void move(int dx, int dy);

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
}
