package com.example.umleditor;

import com.example.umleditor.view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.text.html.MinimalHTMLWriter;

public class App {
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
