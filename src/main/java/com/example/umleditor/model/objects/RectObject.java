package com.example.umleditor.model.objects;

import com.example.umleditor.model.Label;
import com.example.umleditor.model.Port;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/** 矩形：8 個 port，矩形命中，本體畫成方框。 */
public class RectObject extends BasicObject {

    public RectObject(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = Math.max(w, MIN_SIZE);
        this.height = Math.max(h, MIN_SIZE);
        this.label = new Label();
        this.ports = createPorts();
    }

    @Override
    protected Port[] createPorts() {
        // 四角 + 四邊中點，共 8 個，relative 取 0 / 0.5 / 1
        return new Port[]{
                new Port(this, 0.0, 0.0),   // 左上
                new Port(this, 0.5, 0.0),   // 上中
                new Port(this, 1.0, 0.0),   // 右上
                new Port(this, 1.0, 0.5),   // 右中
                new Port(this, 1.0, 1.0),   // 右下
                new Port(this, 0.5, 1.0),   // 下中
                new Port(this, 0.0, 1.0),   // 左下
                new Port(this, 0.0, 0.5)    // 左中
        };
    }

    @Override
    protected void drawBody(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    @Override
    public boolean contains(Point p) {
        return p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
    }
}
