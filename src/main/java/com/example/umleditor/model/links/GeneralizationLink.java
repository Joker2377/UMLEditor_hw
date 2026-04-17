package com.example.umleditor.model.links;

import com.example.umleditor.model.Port;
import java.awt.*;

public class GeneralizationLink extends ConnectionLink{
    public GeneralizationLink(Port fromPort, Port toPort) {
        super(fromPort, toPort);
    }

    @Override
    protected void drawEndpoint(Graphics2D g, int x1, int y1, int x2, int y2) {
        // draw triangle
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowLength = 15;
        int arrowWidth = 10;

        int[] xPoints = {
                x2,
                x2 - (int)(arrowLength * Math.cos(angle) - arrowWidth * Math.sin(angle)),
                x2 - (int)(arrowLength * Math.cos(angle) + arrowWidth * Math.sin(angle))
        };
        int[] yPoints = {
                y2,
                y2 - (int)(arrowLength * Math.sin(angle) + arrowWidth * Math.cos(angle)),
                y2 - (int)(arrowLength * Math.sin(angle) - arrowWidth * Math.cos(angle))
        };

        g.setColor(Color.WHITE);
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 3);
    }
}
