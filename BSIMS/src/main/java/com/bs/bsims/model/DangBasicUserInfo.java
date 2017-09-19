
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class DangBasicUserInfo implements Serializable {
    private List<DangBasicUserInfo> array;
    private String userid;
    private String headpic;
    private String fullname;
    private String sex;
    private String dname;
    private String pname;
    private String postsname;
    private String sortLetters;
    private String entrytime;//入职时间
    private String quittime;
    private String chname;
    private String qrname;  
    private String status;  //1正式2试用3实习
    private String workage;      //工龄

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkage() {
        return workage;
    }

    public void setWorkage(String workage) {
        this.workage = workage;
    }

    public String getEntrytime() {
        return entrytime;
    }

    public void setEntrytime(String entrytime) {
        this.entrytime = entrytime;
    }

    public String getQuittime() {
        return quittime;
    }

    public void setQuittime(String quittime) {
        this.quittime = quittime;
    }

    public String getChname() {
        return chname;
    }

    public void setChname(String chname) {
        this.chname = chname;
    }

    public String getQrname() {
        return qrname;
    }

    public void setQrname(String qrname) {
        this.qrname = qrname;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public List<DangBasicUserInfo> getArray() {
        return array;
    }

    public void setArray(List<DangBasicUserInfo> array) {
        this.array = array;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getPostsname() {
        return postsname;
    }

    public void setPostsname(String postsname) {
        this.postsname = postsname;
    }
}
