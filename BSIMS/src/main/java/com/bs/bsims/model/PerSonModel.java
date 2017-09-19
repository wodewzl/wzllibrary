/**
 * 
 */

package com.bs.bsims.model;

import java.io.Serializable;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-4-2
 * @version 1.23
 */
public class PerSonModel implements Serializable {

    private PerSonModel info;
    private String userid;
    private String fullname;
    private String headpic;
    private String sex;
    private String dname;
    private String pname;
    private String belate;
    private String absence;
    private String times;
    private String leaveearly;
    private String logTimes;
    private String nolog;
    private String askleaveHours;
    private String askleaveMins;
    private String askleave;
    private String overtime;
    private String fulltime;
    private String overtimeHours;
    private String overtimeMins;

    public String getBelate() {
        return belate;
    }

    public void setBelate(String belate) {
        this.belate = belate;
    }

    public String getAbsence() {
        return absence;
    }

    public void setAbsence(String absence) {
        this.absence = absence;
    }

    public String getLeaveearly() {
        return leaveearly;
    }

    public void setLeaveearly(String leaveearly) {
        this.leaveearly = leaveearly;
    }

    public String getNolog() {
        return nolog;
    }

    public void setNolog(String nolog) {
        this.nolog = nolog;
    }

    public String getAskleave() {
        return askleave;
    }

    public void setAskleave(String askleave) {
        this.askleave = askleave;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public String getFulltime() {
        return fulltime;
    }

    public void setFulltime(String fulltime) {
        this.fulltime = fulltime;
    }

    public String getAskleaveHours() {
        return askleaveHours;
    }

    public void setAskleaveHours(String askleaveHours) {
        this.askleaveHours = askleaveHours;
    }

    public String getAskleaveMins() {
        return askleaveMins;
    }

    public void setAskleaveMins(String askleaveMins) {
        this.askleaveMins = askleaveMins;
    }

    public String getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(String overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public String getOvertimeMins() {
        return overtimeMins;
    }

    public void setOvertimeMins(String overtimeMins) {
        this.overtimeMins = overtimeMins;
    }

    private String code;

    public PerSonModel getInfo() {
        return info;
    }

    public void setInfo(PerSonModel info) {
        this.info = info;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
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

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getLogTimes() {
        return logTimes;
    }

    public void setLogTimes(String logTimes) {
        this.logTimes = logTimes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
