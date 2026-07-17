package com.example.umleditor.model.objects;

import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.Label;
import com.example.umleditor.model.Port;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/** 基本物件共同骨架：位置、大小、ports、label、resize 規則 + 「模板 draw」。 */
public abstract class BasicObject extends GraphicObject {

    /** 最小尺寸，整個專案唯一來源（Use Case F.3）。 */
    public static final int MIN_SIZE = 20;

    protected int x, y, width, height;   // 模型座標：左上角 + 寬高
    protected Label label;               // 擁有一個 label
    protected Port[] ports;              // 擁有一組 port

    /** 每種形狀自己決定 port 的數量與相對位置。 */
    protected abstract Port[] createPorts();

    /** 只有「本體形狀」交給子類別畫（矩形 vs 橢圓）。其餘共用流程在 draw 模板裡。 */
    protected abstract void drawBody(Graphics2D g);

    /** Template Method：共用繪圖流程在這，子類別只負責 drawBody，用多型取代 view 的 instanceof。 */
    @Override
    public void draw(Graphics2D g, boolean highlighted) {
        drawBody(g);                                    // 1. 本體（子類別決定）
        label.draw(g, x + width / 2, y + height / 2);   // 2. 標籤
        if (highlighted) {                              // 3. 被選取/hover 才顯示 ports（Use Case C/F）
            for (Port p : ports) p.draw(g);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * 從 GraphicObject 繼承下來、BasicObject 不負責實作的抽象方法，在此「明確再宣告一次」，
     * 讓讀者一眼看出「這層不實作、交給子類別（RectObject/OvalObject）」，而不是被默默繼承。
     * contains 在矩形 / 橢圓判定不同，故留給子類別。
     */
    @Override
    public abstract boolean contains(Point p);

    /**
     * 依被拖曳的 port 與放開座標重算大小（Use Case F）。
     * anchor = 對角不動點；需處理反向拖曳(F.2)與最小尺寸(F.3)。
     */
    public void resize(Port handle, Point release, Point anchor) {
        double rx = handle.getRelativeX();
        double ry = handle.getRelativeY();

        // 對角不動點座標
        int anchorX = anchor.x;
        int anchorY = anchor.y;

        int newX = this.x;
        int newY = this.y;
        int newWidth = this.width;
        int newHeight = this.height;
        /*
        * Basically keeping the anchor always the top-left point
        * */
        // 1. 依 handle 的相對位置決定要改寬：relativeX 不在中點(0.5) 才動寬
        if (rx != 0.5) {
            // 2. 用 |release - anchor| 算新寬，並 clamp 到 MIN_SIZE
            newWidth = Math.max(Math.abs(release.x - anchorX), MIN_SIZE);
            // 3. 若 release 越過 anchor（反向拖曳 F.2），重算左上角 x
            newX = (release.x >= anchorX) ? anchorX : anchorX - newWidth;
        }
        if (ry != 0.5) {
            newHeight = Math.max(Math.abs(release.y - anchorY), MIN_SIZE);
            newY = (release.y >= anchorY) ? anchorY : anchorY - newHeight;
        }

        this.x = newX;
        this.y = newY;
        this.width = newWidth;
        this.height = newHeight;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Label getLabel() { return label; }
    public Port[] getPorts() { return ports; }
}
