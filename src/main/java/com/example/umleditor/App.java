package com.example.umleditor;

import com.example.umleditor.view.MainFrame;

import javax.swing.SwingUtilities;

/** 進入點。 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
