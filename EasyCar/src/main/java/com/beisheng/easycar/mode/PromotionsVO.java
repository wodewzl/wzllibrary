package com.beisheng.easycar.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/8/1.
 */

public class PromotionsVO extends BaseVO{
    private PromotionsVO data;
    private List<PromotionsVO> list;
    private String id;
    private String title;
    private String img;
    private String desc;
    private String activity;

    public PromotionsVO getData() {
        return data;
    }

    public void setData(PromotionsVO data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public List<PromotionsVO> getList() {
        return list;
    }

    public void setList(List<PromotionsVO> list) {
        this.list = list;
    }
}
