package com.example.umleditor.view;

import com.example.umleditor.controller.StateContext;
import com.example.umleditor.model.CanvasModel;
import com.example.umleditor.model.GraphicObject;
import com.example.umleditor.model.objects.BasicObject;
import com.sun.jdi.connect.Connector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.nimbus.State;
import javax.tools.Tool;
import java.awt.*;

public class MainFrame extends JFrame{
    private CanvasPanel canvasPanel;
    private ToolbarPanel toolbarPanel;
    private CanvasModel canvasModel;
    private StateContext stateContext;

    public MainFrame(){
        setTitle("UML editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        canvasModel = new CanvasModel();
        stateContext = new StateContext(canvasModel);

        JMenuBar menuBar = new JMenuBar();
        JMenu editMenu = new JMenu("Edit");
        JMenuItem groupItem = new JMenuItem("Group");
        JMenuItem ungroupItem = new JMenuItem(("Ungroup"));
        JMenuItem labelItem = new JMenuItem("Label");

        groupItem.addActionListener(e->{
            canvasModel.groupSelected();
            if(canvasPanel!=null){
                canvasPanel.repaint();
            }
        });

        ungroupItem.addActionListener(e -> {
            canvasModel.ungroupSelected();
            if (canvasPanel != null) {
                canvasPanel.repaint();
            }
        });

        labelItem.addActionListener(e->{
            java.util.List<GraphicObject> selected = canvasModel.getSelectedObjects();
            if(selected.size() == 1 && selected.getFirst() instanceof BasicObject){
                BasicObject target = (BasicObject) selected.getFirst();

                LabelDialog dialog = new LabelDialog(this, target);
                dialog.setVisible(true);

                if(canvasPanel!=null){
                    canvasPanel.repaint();
                }
            }
        });

        toolbarPanel = new ToolbarPanel(stateContext);
        canvasPanel = new CanvasPanel(stateContext, canvasModel);

        add(toolbarPanel, BorderLayout.WEST);
        add(canvasPanel, BorderLayout.CENTER);

        // make sure button binds with statecontext modes when:
        // mode changes by obj -> UI get the event and update button to fit the state
        // Flow:
        // stateContext change mode -> call listener -> exec below function -> call syncMode
        // Update button to selected according to mode
        stateContext.setModeChangeListener(()->{
            toolbarPanel.syncMode(stateContext.getCurrentMode());
        });

        editMenu.add(groupItem);
        editMenu.add(ungroupItem);
        editMenu.add(labelItem);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);

    }
}
