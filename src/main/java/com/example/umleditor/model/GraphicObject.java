package com.example.umleditor.model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/** 畫布上所有東西的共同型別：領域資料 + 幾何契約 + 自己會畫自己（多型）。沒有選取狀態。 */
public abstract class GraphicObject {

    /** 深度 0~99，越小越上層、越優先吃滑鼠事件。 */
    protected int depth;

    /**
     * 把自己畫到畫布上。highlighted 由 view 算好傳入（被選取或 hover）→ 決定要不要畫 ports/外框。
     * 選取狀態仍在 controller，這裡只是「被告知」要不要高亮，不自己保存。
     */
    public abstract void draw(Graphics2D g, boolean highlighted);

    /** 點 p 是否落在自己範圍內（命中判定）。 */
    public abstract boolean contains(Point p);

    /** 完全包住自己的最小矩形（模型座標）。 */
    public abstract Rectangle getBounds();

    /** 把自己平移 (dx, dy)。 */
    public abstract void move(int dx, int dy);

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
