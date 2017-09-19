package com.beisheng.synews.mode;

/**
 * created：gaoyao on 2016/11/25 17:47
 */
public class ChangeTitle {
    private String title;
    private boolean change;
    public ChangeTitle(String title, boolean change) {
        this.title=title;
        this.change=change;
    }
    public String getTitle(){
        return title;
    }
    public boolean getChange(){
        return change;
    }
}
