package com.example.umleditor.controller;

import com.example.umleditor.model.CanvasModel;
import java.awt.*;
import java.awt.event.MouseEvent;

public class StateContext {
    private CanvasModel canvasModel;
    private EditorMode currentMode;
    private EditorMode previousMode;
    private EditorState currentState;
    private Runnable modeChangeListener;

    public StateContext(CanvasModel canvasModel){
        this.canvasModel = canvasModel;
        this.currentMode = EditorMode.SELECT;
        this.previousMode = EditorMode.SELECT;
        this.currentState = new SelectState(this, canvasModel);
    }

    public void setCurrentMode(EditorMode mode){
        this.previousMode = this.currentMode;
        this.currentMode = mode;
        switch (mode){
            case SELECT:
                currentState = new SelectState(this, canvasModel);
                break;
            case RECT:
            case OVAL:
                currentState = new CreateObjectState(this, canvasModel, mode);
                break;
            case ASSOCIATION:
            case COMPOSITION:
            case GENERALIZATION:
                currentState = new CreateLinkState(this, canvasModel, mode);
                break;
        }
        // make sure button change accordingly
        if(modeChangeListener!=null){
            modeChangeListener.run();
        }
    }

    public EditorMode getCurrentMode() {return currentMode;}
    public EditorMode getPreviousMode() {return previousMode;}

    // update the state to front view (if internal change mode)
    public void setModeChangeListener(Runnable listener){
        this.modeChangeListener = listener;
    }

    public void mousePressed(MouseEvent e) { currentState.mousePressed(e); }
    public void mouseReleased(MouseEvent e) { currentState.mouseReleased(e); }
    public void mouseDragged(MouseEvent e) { currentState.mouseDragged(e); }
    public void mouseMoved(MouseEvent e) { currentState.mouseMoved(e); }
    public void draw(Graphics2D g) { currentState.draw(g); }
}
