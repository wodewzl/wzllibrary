package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/5/13.
 */

public class OptionVO extends BaseVO{
    private OptionVO datas;
    private List<OptionVO> filter;
    private List<OptionVO> list;
    private String title;
    private List<OptionVO> options;
    private String sc_id;
    private String sc_name;
    private String sc_bail;
    private String sc_sort;
    private String order_type;
    private String text;
    private boolean check=false;

    public OptionVO getDatas() {
        return datas;
    }

    public void setDatas(OptionVO datas) {
        this.datas = datas;
    }

    public List<OptionVO> getFilter() {
        return filter;
    }

    public void setFilter(List<OptionVO> filter) {
        this.filter = filter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OptionVO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionVO> options) {
        this.options = options;
    }

    public String getSc_id() {
        return sc_id;
    }

    public void setSc_id(String sc_id) {
        this.sc_id = sc_id;
    }

    public String getSc_name() {
        return sc_name;
    }

    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }

    public String getSc_bail() {
        return sc_bail;
    }

    public void setSc_bail(String sc_bail) {
        this.sc_bail = sc_bail;
    }

    public String getSc_sort() {
        return sc_sort;
    }

    public void setSc_sort(String sc_sort) {
        this.sc_sort = sc_sort;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<OptionVO> getList() {
        return list;
    }

    public void setList(List<OptionVO> list) {
        this.list = list;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
