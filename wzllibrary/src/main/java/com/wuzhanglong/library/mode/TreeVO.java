
package com.wuzhanglong.library.mode;

public class TreeVO {
    private int id;
    private int parentId;
    private String name;
    private long length;
    private String desc;
    private int level;
    private boolean haschild;
    private String childSearchId;
    private String parentSerachId;
    private String searchId;
    private String departmentid;
    private String dname;
    private String type;
    private String parentName;

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public boolean isHaschild() {
        return haschild;
    }

    public void setHaschild(boolean haschild) {
        this.haschild = haschild;
    }

    public TreeVO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getChildSearchId() {
        return childSearchId;
    }

    public void setChildSearchId(String childSearchId) {
        this.childSearchId = childSearchId;
    }

    public String getParentSerachId() {
        return parentSerachId;
    }

    public void setParentSerachId(String parentSerachId) {
        this.parentSerachId = parentSerachId;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

}
