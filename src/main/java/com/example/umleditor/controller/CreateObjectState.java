package com.example.umleditor.controller;

import com.example.umleditor.model.CanvasModel;
import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.objects.OvalObject;
import com.example.umleditor.model.objects.RectObject;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.event.MouseEvent;

public class CreateObjectState extends EditorState{
    private StateContext context;
    private CanvasModel model;
    private EditorMode mode;

    private Point startPoint;
    private Point currentPoint;

    public CreateObjectState(StateContext context, CanvasModel model, EditorMode mode) {
        this.context = context;
        this.model = model;
        this.mode = mode;
    }

    @Override
    public void mousePressed(MouseEvent e){
        startPoint = e.getPoint();
        currentPoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e){
        currentPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e){
        int x = Math.min(startPoint.x, currentPoint.x);
        int y = Math.min(startPoint.y, currentPoint.y);
        int width = Math.abs(currentPoint.x - startPoint.x);
        int height = Math.abs(currentPoint.y - startPoint.y);

        if(mode == EditorMode.RECT){
            model.addObject(new RectObject(x, y, width, height));
        }else if(mode == EditorMode.OVAL){
            model.addObject(new OvalObject(x, y, width, height));
        }

        context.setCurrentMode(context.getPreviousMode());
    }

    @Override
    public void draw(Graphics2D g){
        // preview graph
        if(startPoint != null && currentPoint != null){
            int x = Math.min(startPoint.x, currentPoint.x);
            int y = Math.min(startPoint.y, currentPoint.y);
            int width = Math.abs(currentPoint.x - startPoint.x);
            int height = Math.abs(currentPoint.y - startPoint.y);
            g.setColor(Color.GRAY);
            if(mode == EditorMode.RECT){
                g.drawRect(x, y, width, height);
            }else if(mode == EditorMode.OVAL){
                g.drawOval(x, y, width, height);
            }
        }
    }
}
