package com.example.umleditor.model;

import com.example.umleditor.model.objects.BasicObject;
import com.example.umleditor.model.Port;
import com.example.umleditor.model.objects.CompositeObject;

import java.awt.*;
import java.beans.beancontext.BeanContextServiceProviderBeanInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CanvasModel {
    private List<GraphicObject> objects;

    public CanvasModel(){
        this.objects = new ArrayList<>();
    }

    public void addObject(GraphicObject obj){
        objects.add(obj);
        // normalize depth on object creation
        obj.setDepth(-1);
        objects.sort(Comparator.comparingInt(GraphicObject::getDepth));
        for(int i=0; i<objects.size();i++){
            objects.get(i).setDepth(i+1);
        }
    }

    public List<GraphicObject> getObjects() {
        return objects;
    }

    // locate the topmost object -> can be optimized
    public GraphicObject getTopmostObjectAt(Point p){
        GraphicObject target = null;
        int minDepth = Integer.MAX_VALUE; // min = top
        for(GraphicObject obj: objects){
            if(obj.contains(p) && obj.depth < minDepth){
                minDepth = obj.depth;
                target = obj;
            }
        }
        return target;
    }

    public void unselectAll(){
        for(GraphicObject obj: objects){
            obj.setSelected(false);
        }
    }

    public void bringToFront(GraphicObject target){
        if (target != null){
            target.setDepth(0);
            for(GraphicObject obj: objects){
                if(obj!=target){
                    obj.setDepth(obj.getDepth()+1);
                }
            }
        }
    }

    public void selectObjectsInBounds(Rectangle bounds){
        unselectAll();
        for(GraphicObject obj: objects){
            if(obj instanceof BasicObject){
                if(bounds.contains(obj.getBounds())){
                    obj.setSelected(true);
                }
            }
        }
    }

    public Port getPortAt(Point p){
        GraphicObject topmost = getTopmostObjectAt(p);
        if(topmost instanceof BasicObject){
            BasicObject basicObj = (BasicObject) topmost;
            for(Port port: basicObj.getPorts()){
                if(port.contains(p)){
                    return port;
                }
            }
        }
        return null;
    }

    public void groupSelected(){
        List<GraphicObject> selectedObjects = new ArrayList<>();
        for(GraphicObject obj:objects){
            if(obj.isSelected()){
                selectedObjects.add(obj);
            }
        }

        if(selectedObjects.size() >=2){
            // remove from canvas
            objects.removeAll(selectedObjects);
            for(GraphicObject obj:selectedObjects){
                obj.setSelected(false);
            }
            CompositeObject composite = new CompositeObject(selectedObjects);
            composite.setSelected(true);
            objects.add(composite);
            bringToFront(composite);
        }

    }

    public void ungroupSelected(){
        List<GraphicObject> selectedObjects = new ArrayList<>();
        for(GraphicObject obj:objects){
            if(obj.isSelected()){
                selectedObjects.add(obj);
            }
        }

        if(selectedObjects.size() == 1 && selectedObjects.getFirst() instanceof CompositeObject){
            CompositeObject composite = (CompositeObject) selectedObjects.getFirst();
            // remove composite object from canvas and add back the sub-objects within back to canvas
            objects.remove(composite);
            for(GraphicObject child:composite.getChildren()){
                child.setSelected(true);
                objects.add(child);
                bringToFront(child);
            }
        }
    }

    public List<GraphicObject> getSelectedObjects(){
        List<GraphicObject> selected = new ArrayList<>();
        for(GraphicObject obj:objects){
            if(obj.isSelected()){
                selected.add(obj);
            }
        }
        return selected;
    }
}
