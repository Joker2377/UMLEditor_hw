package com.example.umleditor.model.objects;

import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.Label;
import com.example.umleditor.model.Port;
import java.awt.*;

public class OvalObject extends BasicObject{
    public static final int MIN_SIZE = 20;

    public OvalObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = Math.max(width, MIN_SIZE);
        this.height = Math.max(height, MIN_SIZE);
        this.label = new Label();
        this.ports = createPorts();
        this.depth = 99;
    }

    @Override
    protected Port[] createPorts(){
        return new Port[]{
                new Port(this, 0.5, 0.0),
                new Port(this, 0.5, 1.0),
                new Port(this, 0.0, 0.5),
                new Port(this, 1.0, 0.5),
        };
    }

    @Override
    public void draw(Graphics2D g){
        // draw body
        g.setColor(Color.WHITE);
        g.fillOval(x, y, width, height);
        // draw edge
        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);

        label.draw(g, x+width/2, y+height/2);

        if(selected || hovered){
            for(Port p : ports){
                p.draw(g);
            }
        }
    }

    @Override
    public boolean contains(Point p){
        // (x-cx)^2 / a^2 + (y-cy)^2 / b^2 <= 1
        // cx, cy = center coords
        double cx = x + width/2.0;
        double cy = y + height/2.0;
        double a = width/2.0;
        double b = height/2.0;

        return Math.pow(p.x-cx, 2) / Math.pow(a, 2) + Math.pow(p.y-cy, 2) / Math.pow(b, 2) <=1;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

}
