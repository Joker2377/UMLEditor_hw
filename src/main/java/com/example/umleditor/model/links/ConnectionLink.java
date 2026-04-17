package com.example.umleditor.model.links;

import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.Port;
import java.awt.*;

public abstract class ConnectionLink extends GraphicObject{
    protected Port fromPort;
    protected Port toPort;

    public ConnectionLink(Port fromPort, Port toPort){
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.depth = 100;
    }

    @Override
    public void draw(Graphics2D g){
        int x1 = fromPort.getAbsoluteX();
        int y1 = fromPort.getAbsoluteY();
        int x2 = toPort.getAbsoluteX();
        int y2 = toPort.getAbsoluteY();

        g.setColor(Color.BLACK);
        g.drawLine(x1, y1, x2, y2);

        // the end mark design is determine by children
        drawEndpoint(g, x1, y1, x2, y2);
    }

    protected abstract void drawEndpoint(Graphics2D g, int x1, int y1, int x2, int y2);

    @Override
    public boolean contains(Point p) {
        return false;
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public void move(int dx, int dy) {}
}
