package com.example.umleditor.controller;

import com.example.umleditor.model.CanvasModel;
import com.example.umleditor.model.objects.BasicObject;
import com.example.umleditor.model.objects.OvalObject;
import com.example.umleditor.model.objects.RectObject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/** 建立 Rect / Oval（Use Case A）。 */
public class CreateObjectState extends EditorState {

    private final StateContext context;
    private final CanvasModel model;
    private final EditorMode mode;          // RECT 或 OVAL
    private Point start, current;           // 拖曳出範圍

    public CreateObjectState(StateContext c, CanvasModel m, EditorMode mode) {
        this.context = c;
        this.model = m;
        this.mode = mode;
    }

    @Override public void mousePressed(MouseEvent e) { start = current = e.getPoint(); }
    @Override public void mouseDragged(MouseEvent e) { current = e.getPoint(); }

    @Override
    public void mouseReleased(MouseEvent e) {
        Rectangle r = rectOf(start, current);
        BasicObject obj = switch (mode) {            // 明確列出每個模式，不用 else/三元
            case RECT -> new RectObject(r.x, r.y, r.width, r.height);
            case OVAL -> new OvalObject(r.x, r.y, r.width, r.height);
            default   -> throw new IllegalStateException("CreateObjectState 只處理 RECT/OVAL");
        };
        model.addObject(obj);
        context.setCurrentMode(context.getPreviousMode());   // Use Case A.6：還原模式
    }

    @Override
    public void drawOverlay(Graphics2D g) {
        if (start == null || current == null) return;
        Rectangle r = rectOf(start, current);
        g.setColor(Color.GRAY);
        switch (mode) {
            case RECT -> g.drawRect(r.x, r.y, r.width, r.height);
            case OVAL -> g.drawOval(r.x, r.y, r.width, r.height);
            default   -> throw new IllegalStateException("CreateObjectState 只處理 RECT/OVAL");
        }
    }

    /** 兩點 → 正規化矩形（左上角 + 正寬高）。 */
    private Rectangle rectOf(Point a, Point b) {
        int x = Math.min(a.x, b.x);
        int y = Math.min(a.y, b.y);
        int w = Math.abs(b.x - a.x);
        int h = Math.abs(b.y - a.y);
        return new Rectangle(x, y, w, h);
    }
}
