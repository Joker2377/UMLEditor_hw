package com.example.umleditor.model;

/** view 實作這個介面並註冊；model 一變就被通知，自己決定重畫。 */
public interface ModelListener {
    void modelChanged();
}
