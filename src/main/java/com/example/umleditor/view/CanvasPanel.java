package com.example.umleditor.view;

import com.example.umleditor.controller.StateContext;
import com.example.umleditor.model.CanvasModel;
import com.example.umleditor.model.GraphicObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CanvasPanel extends JPanel{
    private StateContext context;
    private CanvasModel model;

    public CanvasPanel(StateContext context, CanvasModel model){
        setBackground(Color.WHITE);
        this.context = context;
        this.model = model;

        MouseAdapter mouseAdapter = new MouseAdapter(){
            @Override
            public void mouseDragged(MouseEvent e) {
                context.mouseDragged(e); repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                context.mouseMoved(e); repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                context.mousePressed(e); repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                context.mouseReleased(e); repaint();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // smaller depth = on top = draw last
        List<GraphicObject> sortedObjects = new ArrayList<>(model.getObjects());
        sortedObjects.sort(Comparator.comparingInt(GraphicObject::getDepth).reversed());

        for(GraphicObject obj : sortedObjects){
            obj.draw(g2d);
        }

        context.draw(g2d);
    }
}
