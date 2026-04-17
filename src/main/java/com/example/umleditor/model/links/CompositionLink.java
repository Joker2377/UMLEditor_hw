package com.example.umleditor.model.links;

import com.example.umleditor.model.Port;
import java.awt.*;

public class CompositionLink extends ConnectionLink{
    public CompositionLink(Port fromPort, Port toPort) {
        super(fromPort, toPort);
    }

    @Override
    protected void drawEndpoint(Graphics2D g, int x1, int y1, int x2, int y2) {
        // draw diamond shape
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int d = 15;

        int[] xPoints = {
                x2,
                x2 - (int)(d * Math.cos(angle) - (d/2.0) * Math.sin(angle)),
                x2 - (int)(2 * d * Math.cos(angle)),
                x2 - (int)(d * Math.cos(angle) + (d/2.0) * Math.sin(angle))
        };
        int[] yPoints = {
                y2,
                y2 - (int)(d * Math.sin(angle) + (d/2.0) * Math.cos(angle)),
                y2 - (int)(2 * d * Math.sin(angle)),
                y2 - (int)(d * Math.sin(angle) - (d/2.0) * Math.cos(angle))
        };

        g.setColor(Color.BLACK);
        g.fillPolygon(xPoints, yPoints, 4);
    }
}
