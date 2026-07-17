package com.example.umleditor.controller;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/** 每種模式的行為。預設空實作，子類別只覆寫需要的事件。 */
public abstract class EditorState {
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

    /** 互動中的暫時繪圖（非 model 內容），例如框選框、建立預覽。 */
    public void drawOverlay(Graphics2D g) {}
}
