package com.example.umleditor.controller;

import com.example.umleditor.model.CanvasModel;
import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.objects.BasicObject;
import com.example.umleditor.model.objects.CompositeObject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/** 持有 model、目前模式、目前 state、共享 selection。委派滑鼠事件給當前 state，
 *  並提供 Edit 選單指令（group/ungroup/label）讓 view 只負責接線。 */
public class StateContext {

    private final CanvasModel model;
    private final Selection selection = new Selection();   // 所有 state 共用同一份選取
    private EditorMode currentMode;
    private EditorMode previousMode;     // 建立物件/連結後要還原的模式（Use Case A.6）
    private EditorState currentState;
    private Runnable modeChangeListener; // 通知 toolbar 同步按鈕

    public StateContext(CanvasModel model) {
        this.model = model;
        setCurrentMode(EditorMode.SELECT);
    }

    /** 切換模式 → 換成對應的 state（State pattern 的核心）。 */
    public void setCurrentMode(EditorMode mode) {
        this.previousMode = (this.currentMode == null) ? mode : this.currentMode;
        this.currentMode = mode;
        this.currentState = switch (mode) {
            case SELECT -> new SelectState(this, model);
            case RECT, OVAL -> new CreateObjectState(this, model, mode);
            case ASSOCIATION, GENERALIZATION, COMPOSITION -> new CreateLinkState(this, model, mode);
        };
        if (modeChangeListener != null) modeChangeListener.run();
    }

    public EditorMode getCurrentMode()  { return currentMode; }
    public EditorMode getPreviousMode() { return previousMode; }
    public Selection getSelection()     { return selection; }
    public void setModeChangeListener(Runnable l) { this.modeChangeListener = l; }

    // --- Edit 選單指令（商業邏輯在 controller，view 只接線）---

    /** Use Case D Case1：≥2 個選取 → 群組（含 composite 可遞迴巢狀）。 */
    public void groupSelected() {
        if (selection.size() >= 2) {
            CompositeObject c = model.group(new ArrayList<>(selection.items()));
            if (c != null) selection.selectOnly(c);
        }
    }

    /** Use Case D Case2：剛好 1 個 composite → 解開一層。 */
    public void ungroupSelected() {
        if (selection.size() == 1 && selection.items().iterator().next() instanceof CompositeObject comp) {
            selection.clear();
            for (GraphicObject child : model.ungroup(comp)) selection.add(child);
        }
    }

    /** Label：回傳唯一被選取的 BasicObject，否則 null（給 view 判斷要不要開對話框）。 */
    public BasicObject singleSelectedBasic() {
        if (selection.size() == 1 && selection.items().iterator().next() instanceof BasicObject b) return b;
        return null;
    }

    /** Label：view 收完對話框輸入後呼叫，由 controller 寫回 model（Use Case G）。 */
    public void applyLabel(BasicObject target, String text, Color color) {
        target.getLabel().setText(text);
        target.getLabel().setBackgroundColor(color);
        model.notifyChanged();   // 直接改 model 內容 → 主動通知 view 重畫
    }

    // 事件委派給當前 state
    public void mousePressed(MouseEvent e)  { currentState.mousePressed(e); }
    public void mouseReleased(MouseEvent e) { currentState.mouseReleased(e); }
    public void mouseDragged(MouseEvent e)  { currentState.mouseDragged(e); }
    public void mouseMoved(MouseEvent e)    { currentState.mouseMoved(e); }

    /** 讓 state 畫「暫時性」的東西（框選框、預覽圖形、預覽連線）。 */
    public void drawOverlay(Graphics2D g)   { currentState.drawOverlay(g); }
}
