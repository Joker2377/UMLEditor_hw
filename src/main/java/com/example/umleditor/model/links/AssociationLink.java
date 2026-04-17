package com.example.umleditor.model.links;

import com.example.umleditor.model.Port;
import java.awt.*;

public class AssociationLink extends ConnectionLink{
    public AssociationLink(Port fromPort, Port toPort) {
        super(fromPort, toPort);
    }

    @Override
    protected void drawEndpoint(Graphics2D g, int x1, int y1, int x2, int y2) {
        // no endpoint for association link, plain line
    }
}
