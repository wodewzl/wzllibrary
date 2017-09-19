
package com.bs.bsims.model;

import java.util.List;

public class AttendanceVO {
    private String rowDate;
    private List<EmployeeVO> appUser;
    private List<EmployeeVO> insUser;
    private List<EmployeeVO> witUser;
    private List<EmployeeVO> opinion;
    private String apid;
    private String type;
    private String sptime;
    private String info;
    private String status;
    private String userid;
    private String time;
    private String headpic;
    private String fullname;
    private String dname;
    private String approval;
    private String provened;
    private String typename;
    private String pname;
    private String genre;
    

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public List<EmployeeVO> getAppUser() {
        return appUser;
    }

    public void setAppUser(List<EmployeeVO> appUser) {
        this.appUser = appUser;
    }

    public List<EmployeeVO> getInsUser() {
        return insUser;
    }

    public void setInsUser(List<EmployeeVO> insUser) {
        this.insUser = insUser;
    }

    public String getRowDate() {
        return rowDate;
    }

    public void setRowDate(String rowDate) {
        this.rowDate = rowDate;
    }

    public String getApid() {
        return apid;
    }

    public void setApid(String apid) {
        this.apid = apid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSptime() {
        return sptime;
    }

    public void setSptime(String sptime) {
        this.sptime = sptime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getProvened() {
        return provened;
    }

    public void setProvened(String provened) {
        this.provened = provened;
    }

    public List<EmployeeVO> getWitUser() {
        return witUser;
    }

    public void setWitUser(List<EmployeeVO> witUser) {
        this.witUser = witUser;
    }

    public List<EmployeeVO> getOpinion() {
        return opinion;
    }

    public void setOpinion(List<EmployeeVO> opinion) {
        this.opinion = opinion;
    }

}
