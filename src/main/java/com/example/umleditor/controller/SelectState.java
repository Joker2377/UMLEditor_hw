package com.example.umleditor.controller;

import com.example.umleditor.model.CanvasModel;
import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.Port;
import com.example.umleditor.model.objects.BasicObject;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SelectState extends EditorState{
    private StateContext context;
    private CanvasModel model;
    private GraphicObject currentHovered;

    private Point startPoint;
    private Point currentPoint;
    private boolean isDraggingBox = false;

    private GraphicObject targetObject;
    private Point lastPoint;

    private Port draggedPort;
    private Point resizeAnchor;

    public SelectState(StateContext context, CanvasModel model){
        this.context = context;
        this.model = model;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        GraphicObject hit = model.getTopmostObjectAt(e.getPoint());

        if(hit != currentHovered){
            if(currentHovered!=null){
                currentHovered.setHovered(false);
            }
            if(hit!=null){
                hit.setHovered(true);
            }
            currentHovered = hit;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        draggedPort = model.getPortAt(e.getPoint());
        if(draggedPort!=null){
            isDraggingBox = false;
            targetObject = null;

            BasicObject owner = draggedPort.getOwner();
            model.unselectAll();
            owner.setSelected(true);
            model.bringToFront(owner);

            // set the anchor for resizing
            double rx = draggedPort.getRelativeX();
            double ry = draggedPort.getRelativeY();
            int anchorX = owner.getX() + (int) (owner.getWidth() * (1.0 - rx));
            int anchorY = owner.getY() + (int) (owner.getHeight() * (1.0 - ry));
            resizeAnchor = new Point(anchorX, anchorY);
        }
        else{
            GraphicObject hit = model.getTopmostObjectAt(e.getPoint());

            if (hit != null) {
                model.unselectAll();
                hit.setSelected(true);
                model.bringToFront(hit);
                isDraggingBox = false;
                targetObject = hit;
                lastPoint = e.getPoint();
            } else {
                model.unselectAll();
                isDraggingBox = true;
                startPoint = e.getPoint();
                currentPoint = startPoint;
                targetObject = null;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(draggedPort != null){
            draggedPort.getOwner().resize(draggedPort, e.getPoint(), resizeAnchor);
        }
        else if(isDraggingBox){
            currentPoint = e.getPoint();
        }else if(targetObject!=null && lastPoint != null){
            int dx = e.getPoint().x - lastPoint.x;
            int dy = e.getPoint().y - lastPoint.y;
            targetObject.move(dx, dy);
            lastPoint = e.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isDraggingBox && startPoint!=null && currentPoint!=null)
        {
            if (!startPoint.equals(currentPoint)) {
                int x = Math.min(startPoint.x, currentPoint.x);
                int y = Math.min(startPoint.y, currentPoint.y);
                int width = Math.abs(currentPoint.x - startPoint.x);
                int height = Math.abs(currentPoint.y - startPoint.y);
                Rectangle selectionBounds = new Rectangle(x, y, width, height);

                model.selectObjectsInBounds(selectionBounds);
            }
        }
        isDraggingBox = false;
        startPoint = null;
        currentPoint = null;
        targetObject = null;
        lastPoint = null;
        draggedPort = null;
        resizeAnchor = null;
    }

    @Override
    public void draw(Graphics2D g) {
        if (isDraggingBox && startPoint != null && currentPoint != null
                && !startPoint.equals(currentPoint)) {
            int x = Math.min(startPoint.x, currentPoint.x);
            int y = Math.min(startPoint.y, currentPoint.y);
            int width = Math.abs(currentPoint.x - startPoint.x);
            int height = Math.abs(currentPoint.y - startPoint.y);

            g.setColor(new Color(0, 0, 255, 50));
            g.fillRect(x, y, width, height);

            // draw edges
            g.setColor(Color.BLUE);
            Stroke oldStroke = g.getStroke();
            float[] dash = {5.0f};
            g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            g.drawRect(x, y, width, height);
            g.setStroke(oldStroke);

        }
    }
}
