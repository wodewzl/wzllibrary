package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/4/19.
 */

public class CategoryVO extends BaseVO {
    private CategoryVO data;
    private ArrayList<CategoryVO> categories;
    private String icon_url;
    private String id;
    private String name;
    private String order;
    private String status;
    private String parent_id;
    private String parent_name;
    private ArrayList<CategoryVO> subcategories;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CategoryVO getData() {
        return data;
    }

    public void setData(CategoryVO data) {
        this.data = data;
    }

    public ArrayList<CategoryVO> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryVO> categories) {
        this.categories = categories;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<CategoryVO> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<CategoryVO> subcategories) {
        this.subcategories = subcategories;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }


    private CategoryVO datas;
    private List<CategoryVO> class_list;
    private String gc_id;
    private String gc_name;
    private List<CategoryVO> child;
    private List<CategoryVO> grandson;
    private String gc_image;
    private boolean select=false;

    public CategoryVO getDatas() {
        return datas;
    }

    public void setDatas(CategoryVO datas) {
        this.datas = datas;
    }

    public List<CategoryVO> getClass_list() {
        return class_list;
    }

    public void setClass_list(List<CategoryVO> class_list) {
        this.class_list = class_list;
    }

    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public String getGc_name() {
        return gc_name;
    }

    public void setGc_name(String gc_name) {
        this.gc_name = gc_name;
    }

    public List<CategoryVO> getChild() {
        return child;
    }

    public void setChild(List<CategoryVO> child) {
        this.child = child;
    }

    public List<CategoryVO> getGrandson() {
        return grandson;
    }

    public void setGrandson(List<CategoryVO> grandson) {
        this.grandson = grandson;
    }

    public String getGc_image() {
        return gc_image;
    }

    public void setGc_image(String gc_image) {
        this.gc_image = gc_image;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }
}
