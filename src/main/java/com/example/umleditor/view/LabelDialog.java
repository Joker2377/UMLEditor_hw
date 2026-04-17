package com.example.umleditor.view;

import com.example.umleditor.model.objects.BasicObject;
import javax.swing.*;
import java.awt.*;

public class LabelDialog extends JDialog{
    private JTextField nameField;
    private JButton colorButton;
    private Color selectedColor;

    public LabelDialog(JFrame parent, BasicObject target) {
        // 設定為 Modal (強制回應對話框)，開啟時會阻擋原視窗的互動
        super(parent, "Customize Label Style", true);
        setSize(300, 150);
        setLocationRelativeTo(parent); // 置中於主視窗
        setLayout(new GridLayout(3, 2, 10, 10)); // 3列2欄排版

        // 初始化：預先載入目標物件目前的標籤數值
        String currentText = target.getLabel().getText();
        selectedColor = target.getLabel().getBackgroundColor();

        // UI 區塊 1：文字輸入
        add(new JLabel("Label Name:"));
        nameField = new JTextField(currentText);
        add(nameField);

        // UI 區塊 2：顏色選擇
        add(new JLabel("Label Color:"));
        colorButton = new JButton();
        colorButton.setBackground(selectedColor);
        colorButton.setOpaque(true);
        colorButton.setBorderPainted(false);
        // 點擊按鈕開啟 Java 內建的調色盤
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Label Color", selectedColor);
            if (newColor != null) {
                selectedColor = newColor;
                colorButton.setBackground(selectedColor); // 更新按鈕顏色預覽
            }
        });
        add(colorButton);

        // UI 區塊 3：確認與取消按鈕
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            target.getLabel().setText(nameField.getText());
            target.getLabel().setBackgroundColor(selectedColor);
            dispose(); // 關閉對話框
        });
        add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose()); // 直接關閉，不套用變更
        add(cancelButton);
    }

}
