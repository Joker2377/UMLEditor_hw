package com.example.umleditor.controller;

import com.example.umleditor.model.CanvasModel;
import com.example.umleditor.model.Port;
import com.example.umleditor.model.links.AssociationLink;
import com.example.umleditor.model.links.CompositionLink;
import com.example.umleditor.model.links.GeneralizationLink;
import com.example.umleditor.model.links.Link;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

/** 建立連結（Use Case B）。 */
public class CreateLinkState extends EditorState {

    private final StateContext context;
    private final CanvasModel model;
    private final EditorMode mode;          // ASSOCIATION / GENERALIZATION / COMPOSITION
    private Port startPort;
    private Point currentPoint;             // 預覽線終點

    public CreateLinkState(StateContext c, CanvasModel m, EditorMode mode) {
        this.context = c;
        this.model = m;
        this.mode = mode;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPort = model.getPortAt(e.getPoint());   // B.1：起點不在 port 就無效
        currentPoint = e.getPoint();
    }

    @Override public void mouseDragged(MouseEvent e) { currentPoint = e.getPoint(); }

    @Override
    public void mouseReleased(MouseEvent e) {
        /*
        * Release operation
        * 1. If no starting port, do nothing
        * 2. If start port and end port valid
        * 2a. Add the link to model
        * */
        if (startPort != null) {
            Port endPort = model.getPortAt(e.getPoint());
            // B.2：終點要在 port、且不能同一個物件
            if (endPort != null && endPort.getOwner() != startPort.getOwner()) {
                model.addObject(createLink(startPort, endPort));
            }
        }
        startPort = null;
        currentPoint = null;
    }

    /** 依模式 new 出對應的 link 子類別（箭頭差異由各子類別的多型 draw 處理）。 */
    private Link createLink(Port from, Port to) {
        return switch (mode) {
            case ASSOCIATION    -> new AssociationLink(from, to);
            case GENERALIZATION -> new GeneralizationLink(from, to);
            case COMPOSITION    -> new CompositionLink(from, to);
            default -> throw new IllegalStateException("CreateLinkState 只處理連結模式");
        };
    }

    @Override
    public void drawOverlay(Graphics2D g) {
        if (startPort != null && currentPoint != null) {
            g.setColor(Color.BLACK);
            g.drawLine(startPort.getAbsoluteX(), startPort.getAbsoluteY(),
                    currentPoint.x, currentPoint.y);
        }
    }
}
