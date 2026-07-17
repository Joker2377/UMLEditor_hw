package com.example.umleditor.controller;

import com.example.umleditor.model.GraphicObject;

import java.util.LinkedHashSet;
import java.util.Set;

/** 由 controller 保管的「目前選取集合 + hover 對象」。view（CanvasPanel）只讀它來算 highlighted。 */
public class Selection {

    private final Set<GraphicObject> items = new LinkedHashSet<>();  // 保留加入順序
    private GraphicObject hovered;

    public void selectOnly(GraphicObject obj) { items.clear(); if (obj != null) items.add(obj); }
    public void add(GraphicObject obj)        { if (obj != null) items.add(obj); }
    public void clear()                       { items.clear(); }
    public boolean contains(GraphicObject o)  { return items.contains(o); }
    public Set<GraphicObject> items()         { return items; }   // view / controller 唯讀使用
    public int size()                         { return items.size(); }

    public void setHovered(GraphicObject o)   { this.hovered = o; }
    public GraphicObject getHovered()         { return hovered; }
}
