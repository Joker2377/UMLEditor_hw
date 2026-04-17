package com.example.umleditor.model;

import java.awt.*;

public class Label {
    private String text;
    private Color backgroundColor;

    public Label(){
        this.text = "";
        this.backgroundColor = Color.WHITE;
    }

    public void draw(Graphics2D g, int centerX, int centerY){
        if (text == null || text.isEmpty()){
            // dont draw bg if no text
            return;
        }

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent() - fm.getDescent();
        int padding = 4;

        // top left coords for rect
        int bgX = centerX - textWidth/2 - padding;
        int bgY = centerY - textHeight/2 - padding;
        int bgWidth = textWidth + padding*2;
        int bgHeight = textHeight + padding*2;

        g.setColor(backgroundColor);
        g.fillRect(bgX, bgY, bgWidth, bgHeight);

        g.setColor(Color.BLACK);
        g.drawString(text, centerX - textWidth / 2, centerY + textHeight / 2);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

}
