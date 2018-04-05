package com.imyyq.plugin.android.entity;

/**
 * Created by imyyq on 2017/4/16.
 */
public class Selection {
    private int start;
    private int end;
    private String text;


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
