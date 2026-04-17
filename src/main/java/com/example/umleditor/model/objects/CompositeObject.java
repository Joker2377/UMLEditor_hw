package com.example.umleditor.model.objects;

import com.example.umleditor.model.GraphicObject;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CompositeObject extends GraphicObject{
    private List<GraphicObject> children;


    public CompositeObject(List<GraphicObject> children) {
        this.children = new ArrayList<>(children);
        this.depth = 0;
    }

    public List<GraphicObject> getChildren() {
        return children;
    }

    @Override
    public Rectangle getBounds() {
        if(children.isEmpty()) return new Rectangle(0, 0, 0, 0);
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for(GraphicObject child:children){
            Rectangle bounds = child.getBounds();
            minX = Math.min(minX, bounds.x);
            minY = Math.min(minY, bounds.y);
            maxX = Math.max(maxX, bounds.x+ bounds.width);
            maxY = Math.max(maxY, bounds.y+ bounds.height);
        }

        int width = maxX - minX;
        int height = maxY - minY;
        int side = Math.max(width, height);

        // make least square
        int centerX = minX + width/2;
        int centerY = minY + height/2;

        return new Rectangle(centerX - side / 2, centerY - side / 2, side, side);
    }

    @Override
    public void draw(Graphics2D g) {
        for(GraphicObject child:children){
            child.draw(g);
        }

        if(selected || hovered){
            // the composite rect effect
            Rectangle bounds = getBounds();
            g.setColor(selected ? Color.RED : Color.DARK_GRAY);
            Stroke oldStroke = g.getStroke();
            float[] dash = {5.0f};
            g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.setStroke(oldStroke);
        }
    }

    @Override
    public boolean contains(Point p) {
        return getBounds().contains(p);
    }

    @Override
    public void move(int dx, int dy) {
        for(GraphicObject child:children){
            child.move(dx, dy);
        }
    }
}
