package com.example.umleditor.model.objects;

import com.example.umleditor.model.GraphicObject;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 樹狀容器：children 可以是 BasicObject，也可以是另一個 CompositeObject（巢狀）。 */
public class CompositeObject extends GraphicObject {

    private final List<GraphicObject> children;

    public CompositeObject(List<GraphicObject> children) {
        this.children = new ArrayList<>(children);
    }

    /** 回傳唯讀檢視，外部不能直接動內部清單。 */
    public List<GraphicObject> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /** 遞迴畫 children；被選取/hover 時加一個虛線外框（Use Case C：composite 只顯示外框）。 */
    @Override
    public void draw(Graphics2D g, boolean highlighted) {
        for (GraphicObject child : children) child.draw(g, false);  // 子物件不各自高亮
        if (highlighted) {
            Rectangle b = getBounds();
            Stroke old = g.getStroke();
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10f, new float[]{5f}, 0f));
            g.drawRect(b.x, b.y, b.width, b.height);
            g.setStroke(old);
        }
    }

    @Override
    public Rectangle getBounds() {
        // 取所有 children bounds 的聯集，再轉成「完全包住的最小正方形」（需求 D 對 composite 範圍的定義）。
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (GraphicObject child : children) {
            Rectangle b = child.getBounds();
            if (b == null) continue;            // link 的 bounds 是 null，跳過
            minX = Math.min(minX, b.x);
            minY = Math.min(minY, b.y);
            maxX = Math.max(maxX, b.x + b.width);
            maxY = Math.max(maxY, b.y + b.height);
        }

        if (minX == Integer.MAX_VALUE) return new Rectangle(0, 0, 0, 0);  // 沒有可量測的 child

        int width = maxX - minX;
        int height = maxY - minY;
        int side = Math.max(width, height);     // 取較長邊 → 最小正方形邊長

        // 以聯集中心為中心，往外擴成正方形（確保完全包住所有 children）
        int centerX = minX + width / 2;
        int centerY = minY + height / 2;
        return new Rectangle(centerX - side / 2, centerY - side / 2, side, side);
    }

    @Override
    public boolean contains(Point p) {
        return getBounds().contains(p);
    }

    @Override
    public void move(int dx, int dy) {
        for (GraphicObject child : children) child.move(dx, dy);  // 整組（含巢狀）一起移動
    }
}
