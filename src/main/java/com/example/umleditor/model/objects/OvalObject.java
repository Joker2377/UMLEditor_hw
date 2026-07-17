package com.example.umleditor.model.objects;

import com.example.umleditor.model.Label;
import com.example.umleditor.model.Port;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/** 橢圓：4 個 port，橢圓方程式命中，本體畫成橢圓。 */
public class OvalObject extends BasicObject {

    public OvalObject(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = Math.max(w, MIN_SIZE);
        this.height = Math.max(h, MIN_SIZE);
        this.label = new Label();
        this.ports = createPorts();
    }

    @Override
    protected Port[] createPorts() {
        // 上下左右 4 個中點
        return new Port[]{
                new Port(this, 0.5, 0.0),   // 上
                new Port(this, 1.0, 0.5),   // 右
                new Port(this, 0.5, 1.0),   // 下
                new Port(this, 0.0, 0.5)    // 左
        };
    }

    @Override
    protected void drawBody(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);
    }

    @Override
    public boolean contains(Point p) {
        // (x-cx)^2/a^2 + (y-cy)^2/b^2 <= 1（cx,cy=中心；a,b=半寬,半高）
        double cx = x + width / 2.0;
        double cy = y + height / 2.0;
        double a = width / 2.0;
        double b = height / 2.0;
        if (a <= 0 || b <= 0) return false;
        double nx = (p.x - cx) / a;
        double ny = (p.y - cy) / b;
        return nx * nx + ny * ny <= 1.0;
    }
}
