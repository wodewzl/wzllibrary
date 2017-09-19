
package com.bs.bsims.model;

import java.util.List;

public class ScheduleVO {
    private ScheduleVO array;
    private String count;
    private String code;
    private String retinfo;
    private String system_time;

    private List<ScheduleVO> schedule;
    private String startime;
    private String endtime;
    private String myself;
    private String id;
    private String title;
    private String allday;
    private String open;
    private String fullname;
    private String dname;
    private String pname;
    private List<ScheduleVO> task;
    private String starttime;

    private String date;

    public ScheduleVO getArray() {
        return array;
    }

    public void setArray(ScheduleVO array) {
        this.array = array;
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

    public String getRetinfo() {
        return retinfo;
    }

    public void setRetinfo(String retinfo) {
        this.retinfo = retinfo;
    }

    public String getSystem_time() {
        return system_time;
    }

    public void setSystem_time(String system_time) {
        this.system_time = system_time;
    }

    public String getStartime() {
        return startime;
    }

    public void setStartime(String startime) {
        this.startime = startime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getMyself() {
        return myself;
    }

    public void setMyself(String myself) {
        this.myself = myself;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAllday() {
        return allday;
    }

    public void setAllday(String allday) {
        this.allday = allday;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
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

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ScheduleVO> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleVO> schedule) {
        this.schedule = schedule;
    }

    public List<ScheduleVO> getTask() {
        return task;
    }

    public void setTask(List<ScheduleVO> task) {
        this.task = task;
    }

}
