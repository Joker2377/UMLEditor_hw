package com.example.umleditor.model.links;

import com.example.umleditor.model.Port;
import java.awt.Graphics2D;

/** 關聯：純線、無箭頭。 */
public class AssociationLink extends Link {
    public AssociationLink(Port from, Port to) {
        super(from, to);
    }

    @Override
    protected void drawEndpoint(Graphics2D g, int x1, int y1, int x2, int y2) {
        // 無箭頭，純線
    }
}
