package com.example.umleditor.view;

import com.example.umleditor.controller.EditorMode;
import com.example.umleditor.controller.StateContext;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ToolbarPanel extends JPanel{
    private Map<EditorMode, JToggleButton> buttonsMap = new HashMap<>();


    public ToolbarPanel(StateContext context){
        setLayout(new GridLayout(6, 1));
        ButtonGroup buttonGroup = new ButtonGroup();

        String[] buttons = {"Select", "Association", "Generalization", "Composition", "Rect", "Oval"};

        for(String btnText : buttons){
            JToggleButton button = new JToggleButton(btnText);
            buttonGroup.add(button); // make sure only one select at one state
            add(button);

            // basically string->enum and set context mode with that enum
            EditorMode mode = EditorMode.valueOf(btnText.toUpperCase());
            buttonsMap.put(mode, button);

            // run when button pressed
            button.addActionListener(e->{
                context.setCurrentMode(mode);
            });


            if(btnText.equals("Select")){
                button.setSelected(true);
            }
        }
    }

    public void syncMode(EditorMode mode){
        JToggleButton btn = buttonsMap.get(mode);
        if(btn != null && !btn.isSelected()){
            btn.setSelected(true);
        }
    }
}
