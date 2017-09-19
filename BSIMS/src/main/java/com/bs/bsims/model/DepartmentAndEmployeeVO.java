
package com.bs.bsims.model;

import java.io.Serializable;

/**
 * 部门员工
 * 
 * @author Administrator
 */
public class DepartmentAndEmployeeVO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    // 部门id
    private String departmentid = "";

    // 部门名称
    private String dname = "";

    // 用户全名
    private String belong = "";

    // 0未读1已读
    private String level = "";

    // 序号
    private String order = "";

    // 用户id
    private String userid = "";

    // 用户头像
    private String headpic = "";

    // 用户全名
    private String fullname = "";

    // 手机号
    private String tel = "";

    // 用户部门ID
    private String did = "";

    // 职位
    private String pname = "";

    // 性别
    private String sex = "";

    private String hxuname ="";

    private String status;
    private boolean haschild;
    private String pid;
    private boolean selected;

    private String sortLetters;
    private String awork;
    private String cornet;
    private String name;
    private String phone;
    private String ismobile;
    private String nickname;
    private String isdefault;//是否必须知会人

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }

    public boolean isHaschild() {
        return haschild;
    }

    public void setHaschild(boolean haschild) {
        this.haschild = haschild;
    }

    public String getCornet() {
        return cornet;
    }

    public void setCornet(String cornet) {
        this.cornet = cornet;
    }

    public String getHxuname() {
        return hxuname;
    }

    public void setHxuname(String hxuname) {
        this.hxuname = hxuname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public DepartmentAndEmployeeVO() {

    }

    public DepartmentAndEmployeeVO(String dname) {
        super();
        this.dname = dname;
    }

    public String getAwork() {
        return awork;
    }

    public void setAwork(String awork) {
        this.awork = awork;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return departmentid + "," + dname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsmobile() {
        return ismobile;
    }

    public void setIsmobile(String ismobile) {
        this.ismobile = ismobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
