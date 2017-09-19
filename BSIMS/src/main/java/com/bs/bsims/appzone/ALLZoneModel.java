
package com.bs.bsims.appzone;

import java.io.Serializable;
import java.util.List;

public class ALLZoneModel implements Serializable {

    private List<ALLZoneModel> list;
    private String m_id;
    private String m_userid;
    private String m_title;
    private String m_seetime;
    private String m_state;
    private String m_type;
    private String m_typeid;
    private String m_categories;
    private String attr;
    private String count;
    private String code;
    private String mtype;
    private String isPub;
    private String direction;

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public List<ALLZoneModel> getList() {
        return list;
    }

    public void setList(List<ALLZoneModel> list) {
        this.list = list;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getM_userid() {
        return m_userid;
    }

    public void setM_userid(String m_userid) {
        this.m_userid = m_userid;
    }

    public String getM_title() {
        return m_title;
    }

    public void setM_title(String m_title) {
        this.m_title = m_title;
    }

    public String getM_seetime() {
        return m_seetime;
    }

    public void setM_seetime(String m_seetime) {
        this.m_seetime = m_seetime;
    }

    public String getM_state() {
        return m_state;
    }

    public void setM_state(String m_state) {
        this.m_state = m_state;
    }

    public String getM_type() {
        return m_type;
    }

    public void setM_type(String m_type) {
        this.m_type = m_type;
    }

    public String getM_typeid() {
        return m_typeid;
    }

    public void setM_typeid(String m_typeid) {
        this.m_typeid = m_typeid;
    }

    public String getM_categories() {
        return m_categories;
    }

    public void setM_categories(String m_categories) {
        this.m_categories = m_categories;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsPub() {
        return isPub;
    }

    public void setIsPub(String isPub) {
        this.isPub = isPub;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
