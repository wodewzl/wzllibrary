
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class NoticeObjectVO implements Serializable {
    private static final long serialVersionUID = -239676522961484066L;
    private String postsid;
    private String postsname;
    private String poslevel;
    private String positionsid;
    private String positionsname;
    private List<NoticeObjectVO> positions;
    private boolean isselected;

    private String type;
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NoticeObjectVO> getList() {
        return list;
    }

    public void setList(List<NoticeObjectVO> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private List<NoticeObjectVO> list;
    private String id;
    private String code;

    public String getPostsid() {
        return postsid;
    }

    public void setPostsid(String postsid) {
        this.postsid = postsid;
    }

    public String getPostsname() {
        return postsname;
    }

    public void setPostsname(String postsname) {
        this.postsname = postsname;
    }

    public String getPoslevel() {
        return poslevel;
    }

    public List<NoticeObjectVO> getPositions() {
        return positions;
    }

    public void setPositions(List<NoticeObjectVO> positions) {
        this.positions = positions;
    }

    public void setPoslevel(String poslevel) {
        this.poslevel = poslevel;
    }

    public String getPositionsid() {
        return positionsid;
    }

    public void setPositionsid(String positionsid) {
        this.positionsid = positionsid;
    }

    public String getPositionsname() {
        return positionsname;
    }

    public void setPositionsname(String positionsname) {
        this.positionsname = positionsname;
    }

    public boolean isIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }

    @Override
    public String toString() {
        return positionsid + "," + positionsname;
    }

}
