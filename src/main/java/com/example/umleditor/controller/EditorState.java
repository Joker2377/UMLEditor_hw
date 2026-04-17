package com.example.umleditor.controller;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class EditorState {
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){}

    // tmp draw for state during dragging
    public void draw(Graphics2D g){}
}
