package com.example.umleditor.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.umleditor.model.objects.BasicObject;
import com.example.umleditor.model.objects.CompositeObject;

/**
 * 擁有所有「最上層」物件。負責 depth 管理、命中查詢、group/ungroup、變更通知。
 * 注意：不持有選取狀態 —— 選取集合由 controller 保管，這裡只提供查詢與操作。
 */
public class CanvasModel {

    // a list storing all the objects that shows on the canvas
    // only store top level object (children of composite object will be manage by its own)
    private final List<GraphicObject> objects = new ArrayList<>();
    // a list storing all the observers, will call the observers if the model changes
    private final List<ModelListener> listeners = new ArrayList<>();

    // --- 觀察者 ---
    public void addListener(ModelListener l) { listeners.add(l); }
    // call observer
    private void fireChanged() { for (ModelListener l : listeners) l.modelChanged(); }
    /** 公開通知入口：controller「直接改了 model 內容」（如改 label）後呼叫，讓畫面重畫。 */
    public void notifyChanged() { fireChanged(); }

    // --- 命令（改狀態後一律 fireChanged）---
    public void addObject(GraphicObject obj) {
        /* add an object to canvas
        * 1. set the depth to -1 temporarily (let it stay on top)
        * 2. add the object to the list
        * 3. bring it to top
        *  */
        // 新物件放最上層（depth 最小）→ 先給最小再正規化
        objects.add(obj);
        bringToFront(obj);
    }

    public void removeObjects(List<GraphicObject> targets) {
        /*
        * remove objects from the canvas by given target (a list of objects)
        * serve as a helper function for grouping
        * 1. Remove all the target from list
        * 2. Normalize the depth to 0~99
        * 3. Call observers
        * */
        objects.removeAll(targets);
        normalizeDepth();
        fireChanged();
    }

    /** 把最後選取的物件提到最上層（depth 調最小，全域規則）。 */
    public void bringToFront(GraphicObject target) {
        /*
        * Take target to the top on the canvas (set depth to smallest)
        * 1. If target not exist, returning null (Weird, but we should also add null check for all the function include object passing)
        * 2. Set the depth to 0
        * 3. For all the objects: depth++
        * 4. Normalize the depth to 0~99
        * 5. call observers
        * */
        if (target == null) return;
        // target 調最小（0），其餘 +1，再正規化維持 0..n
        target.setDepth(0);
        for (GraphicObject obj : objects) {
            if (obj != target) obj.setDepth(obj.getDepth() + 1);
        }
        normalizeDepth();
        fireChanged();
    }

    /**
     * group：controller 把選取清單傳進來，回傳新建的 composite（讓 controller 接著選它）。
     * selected 內可含既有 CompositeObject → 原封不動放入新 composite，形成巢狀（Use Case D）。
     */
    public CompositeObject group(List<GraphicObject> selected) {
        /*
        * Group operation for selected objects
        * 1. If selected size equals to 0 or 1, does not form a group, returning null
        * 2. Create new composite object based on selected objects
        * 3. Bring it to top
        *
        * */
        if (selected.size() < 2) return null;                       // Use Case D.1
        objects.removeAll(selected);
        CompositeObject composite = new CompositeObject(selected);  // 可含 composite → 遞迴巢狀         // 新群組放最上層
        objects.add(composite);
        bringToFront(composite);
        return composite;
    }

    /** ungroup：拆掉一層，回傳被釋放的子物件（讓 controller 接著選它們）。 */
    public List<GraphicObject> ungroup(CompositeObject composite) {
        /*
        * Ungroup the composite object and place the children objects back to list
        * 1. Remove composite object from list
        * 2. Catch children with a temporary list
        * 3. Normalize
        * 4. call observers
        * */
        objects.remove(composite);
        List<GraphicObject> children = new ArrayList<>(composite.getChildren());
        objects.addAll(children);
        normalizeDepth();
        fireChanged();
        return children;
    }

    // --- 查詢（唯讀、無副作用）---
    public List<GraphicObject> getObjects() { return Collections.unmodifiableList(objects); }

    /** 命中查詢：回傳壓在點 p 最上層（depth 最小）的物件。 */
    public GraphicObject getTopmostObjectAt(Point p) {
        /*
        * Return the topmost object for given point
        * Serve as a helper function for selection
        * 1. Iterate through all objects
        * 2. Locate the object with point within bounds
        * 3. Mark the object with min depth
        * 4. Return the target (the topmost object)
        * */
        GraphicObject target = null;
        int minDepth = Integer.MAX_VALUE;        // depth 越小越上層
        for (GraphicObject obj : objects) {
            if (obj.contains(p) && obj.getDepth() < minDepth) {
                minDepth = obj.getDepth();
                target = obj;
            }
        }
        return target;
    }

    /** Use Case B：回傳點 p 命中的 port（只看 BasicObject，composite 不算）。 */
    public Port getPortAt(Point p) {
        /*
        * Get the port at given point p
        * Will call port itself's function to determine if point is within clickable range of a port
        * 1. Order the object by depth (since topmost object should be chosen)
        * 2. For all the objects except composite objects, locate the topmost object with point at clickable range of a port
        * 3. Return the port object if valid port exist
        * */
        // 依 depth 由小到大檢查，優先回傳最上層物件的 port
        List<GraphicObject> ordered = new ArrayList<>(objects);
        ordered.sort(Comparator.comparingInt(GraphicObject::getDepth));
        for (GraphicObject obj : ordered) {
            if (obj instanceof BasicObject basic) {
                for (Port port : basic.getPorts()) {
                    if (port.contains(p)) return port;
                }
            }
        }
        return null;
    }

    /**
     * Use Case C.2：回傳「完全落在」框選矩形內的「可選物件」，供 controller 設為選取。
     * 可選 = BasicObject 或 CompositeObject（Link 不可選）。
     * ※ 一定要把 CompositeObject 也算進來，否則無法框住 composite 再 Group → 遞迴群組會做不出來。
     */
    public List<GraphicObject> getObjectsFullyInside(Rectangle area) {
        /*
        * Returning objects that's within given rectangle area
        * A helper function for drag selection
        * 0. Create a list to handle the result
        * 1. Iterate through all the object in the list
        * 2. Link should be excluded
        * 3. Get the bounds of each object
        * 4. Check if the bounds fully contained by the given area.
        * 5. Add that to the result list
        * 6. Return all the valid object as a list
        * */
        List<GraphicObject> result = new ArrayList<>();
        for (GraphicObject obj : objects) {
            // redundant check, link will always return null for bounds and "contains" check
            // But good to explicit state the requirement
            if (obj instanceof BasicObject || obj instanceof CompositeObject) {
                Rectangle b = obj.getBounds();
                if (b != null && area.contains(b)) result.add(obj);
            }
        }
        return result;
    }

    /** 把 depth 正規化成 0..n，維持原排序（depth 小者在前）。 */
    private void normalizeDepth() {
        /* Normalize the depth for all objects within list
        * Simple idea: arbitrary range to 0~n
        * 1. Sort in increasing order
        * 2. Iterate through list
        * 3. Assign from 0 to n for all the object within the list
        * */
        objects.sort(Comparator.comparingInt(GraphicObject::getDepth));
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).setDepth(i);
        }
    }
}
