package com.beisheng.easycar.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/7/26.
 */

public class HelpVO extends BaseVO {
    private List<HelpVO> data;
    private List<HelpVO> newsList;
    private String id;
    private String name;
    private String icon;
    private String title;
    private boolean  expend=false;

    public List<HelpVO> getData() {
        return data;
    }

    public void setData(List<HelpVO> data) {
        this.data = data;
    }

    public List<HelpVO> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<HelpVO> newsList) {
        this.newsList = newsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isExpend() {
        return expend;
    }

    public void setExpend(boolean expend) {
        this.expend = expend;
    }
}
