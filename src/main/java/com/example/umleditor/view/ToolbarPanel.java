package com.example.umleditor.view;

import com.example.umleditor.controller.EditorMode;
import com.example.umleditor.controller.StateContext;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.GridLayout;
import java.util.EnumMap;
import java.util.Map;

/** 左側六個按鈕，ButtonGroup 確保同時只有一顆被選。 */
public class ToolbarPanel extends JPanel {

    private final Map<EditorMode, JToggleButton> buttons = new EnumMap<>(EditorMode.class);

    public ToolbarPanel(StateContext context) {
        setLayout(new GridLayout(0, 1));
        ButtonGroup group = new ButtonGroup();   // 確保同時只有一顆被選
        for (EditorMode mode : EditorMode.values()) {
            JToggleButton btn = new JToggleButton(label(mode));
            group.add(btn);
            add(btn);
            buttons.put(mode, btn);
            btn.addActionListener(e -> context.setCurrentMode(mode));
            if (mode == EditorMode.SELECT) btn.setSelected(true);
        }
    }

    /** 給 StateContext 的 modeChangeListener 呼叫：程式內部換模式時同步按鈕（如 A.6 還原模式）。 */
    public void syncMode(EditorMode mode) {
        JToggleButton btn = buttons.get(mode);
        if (btn != null) btn.setSelected(true);
    }

    /** 把列舉轉成顯示文字。 */
    private String label(EditorMode mode) {
        return switch (mode) {
            case SELECT         -> "Select";
            case ASSOCIATION    -> "Association";
            case GENERALIZATION -> "Generalization";
            case COMPOSITION    -> "Composition";
            case RECT           -> "Rect";
            case OVAL           -> "Oval";
        };
    }
}
