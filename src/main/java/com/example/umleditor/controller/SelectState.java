package com.example.umleditor.controller;

import com.example.umleditor.model.CanvasModel;
import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.Port;
import com.example.umleditor.model.objects.BasicObject;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

/** Use Case C/D/E/F 的互動核心。selection 由 context 共享。 */
public class SelectState extends EditorState {

    private final StateContext context;
    private final CanvasModel model;
    private final Selection selection;        // = context.getSelection()

    private enum Drag { NONE, MOVE, BOX, RESIZE }   // 一次只會處於一種拖曳
    private Drag drag = Drag.NONE;

    private Point lastPoint;                   // MOVE：算位移用
    private Point boxStart, boxCurrent;        // BOX：框選範圍
    private Port draggedPort;                   // RESIZE：把手
    private Point resizeAnchor;                 // RESIZE：對角不動點

    public SelectState(StateContext context, CanvasModel model) {
        this.context = context;
        this.model = model;
        this.selection = context.getSelection();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        selection.setHovered(model.getTopmostObjectAt(e.getPoint()));  // hover 顯示 ports/外框
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /*
        * Operation when mouse pressed
        * 1. Check if any valid port within clickable range
        * 1a. Locate the owner, take it to top and proceed with resize operation
        * 2. Check if point hit a object
        * 2a. Its either move or click, but move is superset, so consider it move operation
        * 2b. Clear the selection and select current hit object, take it to top
        * 2c. and record the current location to lastPoint for moving coords calculation
        * 3. If the hits are at blank location, consider it a box operation
        * 3a. clear the selection and record the coords for box selection
        * */
        Point p = e.getPoint();

        // (1) 先看是否按在某基本物件的 port → 進入 RESIZE（Use Case F）
        draggedPort = model.getPortAt(p);

        if (draggedPort != null) {
            BasicObject owner = draggedPort.getOwner();
            if(model.getTopmostObjectAt(p) == owner){
                selection.selectOnly(owner);
                model.bringToFront(owner);
                resizeAnchor = computeAnchor(owner, draggedPort);   // 對角不動點
                drag = Drag.RESIZE;
                return;
            }
        }

        // (2) 按在物件上 → 移動。關鍵：若已在選取集合內，保留整組（group move）；否則改單選
        GraphicObject hit = model.getTopmostObjectAt(p);
        if (hit != null) {
            if (!selection.contains(hit)) {        // 點到沒選的 → 變單選
                selection.selectOnly(hit);
                model.bringToFront(hit);
            }                                       // 點到已選的 → 維持整組選取
            lastPoint = p;
            drag = Drag.MOVE;
            return;
        }

        // (3) 按在空白處 → 框選（Use Case C.2）
        selection.clear();
        boxStart = p;
        boxCurrent = p;
        drag = Drag.BOX;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        /*
        * Define mouse dragging operation
        * 1. If the drag mode (set at mouse press) is resize
        * 1a. Do the resize operation
        * 2. If the drag mode is box selection
        * 2a. record current point for selection overlay drawing
        * 3. If operation is move
        * 3a. calcuate the moving vector and move the object in selection (group moving)
        * 3b. update the previous point for next move operation
        * */
        Point p = e.getPoint();
        switch (drag) {
            case RESIZE -> draggedPort.getOwner().resize(draggedPort, p, resizeAnchor);
            case BOX    -> boxCurrent = p;
            case MOVE   -> {                       // ★ group move：選取集合內每個都一起移動
                int dx = p.x - lastPoint.x, dy = p.y - lastPoint.y;
                for (GraphicObject obj : selection.items()) obj.move(dx, dy);
                lastPoint = p;
            }
            default -> {}
        }
        // 不必呼叫 model.notifyChanged()：滑鼠事件後 CanvasPanel 會自己 repaint。
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        /*
        * Operation for mouse releasing
        * 1. If its drag selection
        * 1a. Calculate the box rect area
        * 1b. Clear existing selection
        * 1c. Get the objects that falls within the selection bounds, and add them to selection
        * 2. Clear the drag operation
        * */
        if (drag == Drag.BOX && boxStart != null) {
            Rectangle area = rectOf(boxStart, boxCurrent);
            selection.clear();
            // getObjectsFullyInside 回傳 BasicObject 與 CompositeObject（框選 composite → 之後可遞迴群組）
            for (GraphicObject obj : model.getObjectsFullyInside(area)) selection.add(obj);
        }
        drag = Drag.NONE;
        boxStart = boxCurrent = lastPoint = null;
        draggedPort = null;
        resizeAnchor = null;
    }

    @Override
    public void drawOverlay(Graphics2D g) {
        /*
        * Draw the overlay (temporary view)
        * 1. If box selection is active
        * 2. Draw a transparent blue rect
        * */
        if (drag == Drag.BOX && boxStart != null && boxCurrent != null
                && !boxStart.equals(boxCurrent)) {
            Rectangle r = rectOf(boxStart, boxCurrent);
            // 半透明藍色底
            g.setColor(new Color(0, 0, 255, 50));
            g.fillRect(r.x, r.y, r.width, r.height);
            // 藍色虛線外框
            g.setColor(Color.BLUE);
            Stroke old = g.getStroke();
            g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10.0f, new float[]{5.0f}, 0.0f));
            g.drawRect(r.x, r.y, r.width, r.height);
            g.setStroke(old);
        }
    }

    /** 依被拖曳 port 的 relative 位置，算出對角不動點（owner 目前範圍內）。 */
    private Point computeAnchor(BasicObject owner, Port handle) {
        /*
        * Compute the anchor at owner for given handle port
        * 1. Calculate the mirror direction port
        * 2. return the anchor point
        * */
        double rx = handle.getRelativeX();
        double ry = handle.getRelativeY();
        int anchorX = owner.getX() + (int) (owner.getWidth()  * (1.0 - rx));
        int anchorY = owner.getY() + (int) (owner.getHeight() * (1.0 - ry));
        return new Point(anchorX, anchorY);
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
