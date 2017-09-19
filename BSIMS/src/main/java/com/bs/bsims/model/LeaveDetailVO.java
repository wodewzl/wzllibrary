
package com.bs.bsims.model;

import java.util.ArrayList;
import java.util.List;

public class LeaveDetailVO {
    private String askleaveid;
    private String typeid;
    private String starttime;
    private String endtime;
    private String duration;
    private String content;
    private String status;
    private String userid;
    private String time;
    private String headpic;
    private String fullname;
    private String dname;
    private String pname;
    private String approval;
    private String timeShow;
    private String genre;
    private ArrayList<String> imgs;
    private List<EmployeeVO> appUser;
    private List<EmployeeVO> insUser;
    private List<EmployeeVO> handUser;
    private List<EmployeeVO> opinion;
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
    public List<EmployeeVO> getOpinion() {
        return opinion;
    }

    public void setOpinion(List<EmployeeVO> opinion) {
        this.opinion = opinion;
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

    public List<EmployeeVO> getHandUser() {
        return handUser;
    }

    public void setHandUser(List<EmployeeVO> handUser) {
        this.handUser = handUser;
    }

    public String getAskleaveid() {
        return askleaveid;
    }

    public void setAskleaveid(String askleaveid) {
        this.askleaveid = askleaveid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getTimeShow() {
        return timeShow;
    }

    public void setTimeShow(String timeShow) {
        this.timeShow = timeShow;
    }

}
