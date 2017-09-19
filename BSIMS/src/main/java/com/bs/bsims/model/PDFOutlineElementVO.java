
package com.bs.bsims.model;

import java.io.Serializable;

public class PDFOutlineElementVO implements Serializable {
    private static final long serialVersionUID = -239676522961484066L;

    private String id = "";
    private DepartmentAndEmployeeVO departmentandwmployee = new DepartmentAndEmployeeVO();
    private boolean mhasParent;
    private boolean mhasChild;
    private String parent = "";
    private int level;

    private int user_level = 0;
    private boolean select;

    private boolean search;

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DepartmentAndEmployeeVO getDepartmentandwmployee() {
        return departmentandwmployee;
    }

    public void setDepartmentandwmployee(DepartmentAndEmployeeVO departmentandwmployee) {
        this.departmentandwmployee = departmentandwmployee;
    }

    public boolean isMhasParent() {
        return mhasParent;
    }

    public void setMhasParent(boolean mhasParent) {
        this.mhasParent = mhasParent;
    }

    public boolean isMhasChild() {
        return mhasChild;
    }

    public void setMhasChild(boolean mhasChild) {
        this.mhasChild = mhasChild;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    // private OutlineElement outlineElement;
    private boolean expanded;

    /**
     * @param id
     * @param departmentandwmployee
     * @param mhasParent 是否有父元素
     * @param mhasChild 是否有子元素
     * @param parent
     * @param level
     * @param expanded
     */
    public PDFOutlineElementVO(String id, DepartmentAndEmployeeVO departmentandwmployee,
            boolean mhasParent, boolean mhasChild, String parent, int level,
            boolean expanded, boolean select) {
        super();
        this.id = id;
        this.departmentandwmployee = departmentandwmployee;
        this.mhasParent = mhasParent;
        this.mhasChild = mhasChild;
        this.parent = parent;
        this.level = level;
        this.expanded = expanded;
        this.select = select;
    }

}
