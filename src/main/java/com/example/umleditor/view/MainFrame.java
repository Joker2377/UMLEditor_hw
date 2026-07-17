package com.example.umleditor.view;

import com.example.umleditor.controller.StateContext;
import com.example.umleditor.model.CanvasModel;
import com.example.umleditor.model.objects.BasicObject;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;

/** 組裝視窗 + Edit 選單（只接線）。 */
public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("UML editor");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // storing every objects on the canvas
        CanvasModel model = new CanvasModel();
        // manage current mode state
        StateContext context = new StateContext(model);

        // left side toolbar
        ToolbarPanel toolbar = new ToolbarPanel(context);

        // canvas view layer
        CanvasPanel canvas = new CanvasPanel(context, model);
        // context will call observer (toolbar.syncMode) when internal mode change
        context.setModeChangeListener(() -> toolbar.syncMode(context.getCurrentMode()));

        add(toolbar, BorderLayout.WEST);
        add(canvas, BorderLayout.CENTER);
        setJMenuBar(buildEditMenu(context));
    }

    /**
     * Edit 選單：Group / Ungroup / Label。商業判斷都在 controller，這裡只接線；
     * 畫面更新靠 model 觀察者（group/ungroup/applyLabel 內部會通知 → canvas 重畫）。
     */
    private JMenuBar buildEditMenu(StateContext context) {
        JMenuItem group   = new JMenuItem("Group");
        JMenuItem ungroup = new JMenuItem("Ungroup");
        JMenuItem label   = new JMenuItem("Label");

        group.addActionListener(e -> context.groupSelected());
        ungroup.addActionListener(e -> context.ungroupSelected());
        label.addActionListener(e -> {
            BasicObject t = context.singleSelectedBasic();   // 唯一選取的基本物件，否則 null
            if (t == null) return;
            LabelDialog.Result r = new LabelDialog(this,
                    t.getLabel().getText(), t.getLabel().getBackgroundColor()).showDialog();
            if (r != null) context.applyLabel(t, r.text(), r.color());
        });

        JMenu edit = new JMenu("Edit");
        edit.add(group);
        edit.add(ungroup);
        edit.add(label);
        JMenuBar bar = new JMenuBar();
        bar.add(edit);
        return bar;
    }
}
