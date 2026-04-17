package com.example.umleditor.controller;

import com.example.umleditor.model.CanvasModel;
import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.Port;
import com.example.umleditor.model.links.*;
import java.awt.*;
import java.awt.event.MouseEvent;


public class CreateLinkState extends EditorState{
    private StateContext context;
    private CanvasModel model;
    private EditorMode mode;
    private Port startPort;
    private Point currentPoint;

    private GraphicObject currentHovered;

    public CreateLinkState(StateContext context, CanvasModel model, EditorMode mode) {
        this.context = context;
        this.model = model;
        this.mode = mode;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPort = model.getPortAt(e.getPoint());
        if(startPort!=null){
            currentPoint = e.getPoint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(startPort!=null){
            currentPoint = e.getPoint();
        }
        updateHover(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(startPort!=null){
            Port endPort = model.getPortAt(e.getPoint());

            if(endPort!=null && startPort.getOwner() != endPort.getOwner()){
                ConnectionLink link = null;
                switch (mode){
                    case ASSOCIATION:
                        link = new AssociationLink(startPort, endPort);
                        break;
                    case GENERALIZATION:
                        link = new GeneralizationLink(startPort, endPort);
                        break;
                    case COMPOSITION:
                        link = new CompositionLink(startPort, endPort);
                        break;
                    default: break;
                }
                if(link != null){
                    model.addObject(link);
                }
            }
        }

        startPort = null;
        currentPoint = null;
    }

    @Override
    public void draw(Graphics2D g) {
        // draw preview line
        if(startPort != null && currentPoint != null){
            g.setColor(Color.BLACK);
            g.drawLine(startPort.getAbsoluteX(), startPort.getAbsoluteY(), currentPoint.x, currentPoint.y);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        updateHover(e.getPoint());
    }

    private void updateHover(Point p){
        GraphicObject hit = model.getTopmostObjectAt(p);
        if(hit!=currentHovered){
            if(currentHovered!=null) currentHovered.setHovered(false);
            if(hit!=null)hit.setHovered(true);
            currentHovered = hit;
        }
    }
}
