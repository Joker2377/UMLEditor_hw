package com.example.umleditor.model.links;

import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.Port;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/** 連結兩個 port。端點座標永遠跟著 port 跑，所以自己不需要存座標、不需要 move。 */
public abstract class Link extends GraphicObject {

    protected final Port fromPort;
    protected final Port toPort;

    protected Link(Port fromPort, Port toPort) {
        this.fromPort = fromPort;
        this.toPort = toPort;
    }

    public Port getFromPort() { return fromPort; }
    public Port getToPort() { return toPort; }

    /** Template Method：共用「畫線」流程，終點箭頭交給子類別（多型，取代 view 的 switch）。 */
    @Override
    public void draw(Graphics2D g, boolean highlighted) {
        int x1 = fromPort.getAbsoluteX(), y1 = fromPort.getAbsoluteY();
        int x2 = toPort.getAbsoluteX(),   y2 = toPort.getAbsoluteY();
        g.setColor(Color.BLACK);
        g.drawLine(x1, y1, x2, y2);
        drawEndpoint(g, x1, y1, x2, y2);     // 各 link 的箭頭差異
    }

    /** 子類別畫終點符號（無 / 三角形 / 菱形）。 */
    protected abstract void drawEndpoint(Graphics2D g, int x1, int y1, int x2, int y2);

    // link 不被直接拖、不被框選，端點自動跟隨 → 幾何契約多半 no-op。
    @Override public boolean contains(Point p) { return false; }
    @Override public Rectangle getBounds() { return null; }
    @Override public void move(int dx, int dy) { /* no-op */ }
}
