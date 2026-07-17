package com.example.umleditor.view;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.GridLayout;

/** 純輸入收集器：回傳使用者填的名稱與顏色；要不要套用、怎麼套用交給呼叫端（controller）。 */
public class LabelDialog extends JDialog {

    public record Result(String text, Color color) {}

    private Result result;                 // null = 使用者按了 Cancel

    private final JTextField nameField;
    private Color selectedColor;

    public LabelDialog(JFrame parent, String currentText, Color currentColor) {
        super(parent, "Customize Label Style", true);   // modal
        setSize(300, 150);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(3, 2, 10, 10));

        this.selectedColor = currentColor;

        // 文字輸入（預填 currentText）
        add(new JLabel("Label Name:"));
        nameField = new JTextField(currentText == null ? "" : currentText);
        add(nameField);

        // 顏色選擇（預填 currentColor，用 JColorChooser）
        add(new JLabel("Label Color:"));
        JButton colorButton = new JButton();
        colorButton.setBackground(selectedColor);
        colorButton.setOpaque(true);
        colorButton.setBorderPainted(false);
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Label Color", selectedColor);
            if (newColor != null) {
                selectedColor = newColor;
                colorButton.setBackground(selectedColor);
            }
        });
        add(colorButton);

        // OK → 設定 result；Cancel → result 維持 null
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            result = new Result(nameField.getText(), selectedColor);
            dispose();
        });
        add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);
    }

    /** 顯示對話框（modal 阻塞），關閉後回傳結果；null 代表取消。 */
    public Result showDialog() {
        setVisible(true);
        return result;
    }
}
