package com.example.umleditor.model.objects;

import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.Label;
import com.example.umleditor.model.Port;
import org.w3c.dom.css.Rect;

import java.awt.*;

public class RectObject extends BasicObject {
    public static final int MIN_SIZE = 20;

    public RectObject(int x, int y, int width, int height) {
        // top left coords
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
        // create and gain ownership for 8 port
        return new Port[]{
                new Port(this, 0.0, 0.0),new Port(this, 0.5, 0.0),new Port(this, 1.0, 0.0),
                new Port(this, 0.0, 0.5),new Port(this, 1.0, 0.5),
                new Port(this, 0.0, 1.0),new Port(this, 0.5, 1.0),new Port(this, 1.0, 1.0),
        };
    }

    @Override
    public void draw(Graphics2D g){
        // draw body
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
        // draw edge
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        label.draw(g, x+width/2, y+height/2);

        if(selected || hovered){
            for(Port p : ports){
                p.draw(g);
            }
        }
    }

    @Override
    public boolean contains(Point p){
        return p.x>=x && p.x<=x+width && p.y>=y && p.y<=y+height;
    }

    @Override
    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void move(int dx, int dy){
        this.x += dx;
        this.y += dy;
    }
}
