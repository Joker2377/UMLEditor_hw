package com.example.umleditor.view;

import com.example.umleditor.controller.Selection;
import com.example.umleditor.controller.StateContext;
import com.example.umleditor.model.CanvasModel;
import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.ModelListener;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** 畫布：畫 + 收事件 + 當觀察者。沒有 renderer，靠物件自己多型 draw。 */
public class CanvasPanel extends JPanel implements ModelListener {

    private final StateContext context;
    private final CanvasModel model;

    public CanvasPanel(StateContext context, CanvasModel model) {
        this.context = context;
        this.model = model;
        setBackground(Color.WHITE);
        model.addListener(this);                 // ★ 觀察者：model 一變就重畫

        MouseAdapter mouse = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e)  { context.mousePressed(e);  repaint(); }
            @Override public void mouseReleased(MouseEvent e) { context.mouseReleased(e); repaint(); }
            @Override public void mouseDragged(MouseEvent e)  { context.mouseDragged(e);  repaint(); }
            @Override public void mouseMoved(MouseEvent e)    { context.mouseMoved(e);    repaint(); }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    @Override public void modelChanged() { repaint(); }   // 來自 ModelListener

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // 依 depth 由大到小排序（depth 大的先畫，被 depth 小的蓋住 → 小的在最上層）
        List<GraphicObject> ordered = new ArrayList<>(model.getObjects());
        ordered.sort(Comparator.comparingInt(GraphicObject::getDepth).reversed());

        Selection selection = context.getSelection();
        for (GraphicObject obj : ordered) {
            boolean highlighted = selection.contains(obj) || obj == selection.getHovered();
            obj.draw(g2, highlighted);
        }
        context.drawOverlay(g2);                  // 互動暫時圖（框選框 / 建立預覽）
    }
}
