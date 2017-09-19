
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description: 考勤详情 返回的信息
 * @date 2015-5-15 下午6:48:42
 * @email 971371860@qq.com
 * @version V1.1 2015/5/28 16:42 接口返回 头像地址、性别、职位名称
 */
public class WorkAttendanceDetailVO_ implements Serializable {
    private static final long serialVersionUID = -8184364420924595884L;

    // "id":"1337",
    // "u_id":"22",
    // "u_fullname":"聂世明",
    // "d_id":"7",
    // "dept_name":"产品二组",
    // "s_year":"2015",
    // "s_month":"5",
    // "nowrlog_num":"14",
    // "absence_num":"3",
    // "belate_num":"1",
    // "leavearly_num":"3",
    // "leave_days"

    // "id":"116032",
    // "u_id":"25",
    // "u_fullname":"周文杰",
    // "d_id":"7",
    // "dept_name":"产品二组",
    // "s_year":"2015",
    // "s_month":"5",
    // "nowrlog_num":"1",
    // "absence_num":"7",
    // "belate_num":"1",
    // "leavearly_num":"0",
    // "attn_days":{},
    // "leave_days":{},
    // "total_awork":"-1907",
    // "pname":"产品经理",
    // "headpic":"http://cp.beisheng.wang/Uploads/bs0001/Resume/image/20150331/551a8cb0872ec.jpg",
    // "sex":"女",
    // "quekaDate":"2015-5-4 4次缺卡,2015-5-30 3次缺卡",
    // "chidaoDate":"2015-05-30 09:22迟到",
    // "zaotuiDate":"暂无",
    // "logDate":"2015-5-30"

    private String u_id = "";
    private String u_fullname = "";
    private String d_id = "";
    private String dept_name = "";
    private String s_year = "";
    private String s_month = "";
    private String nowrlog_num = "";
    private String absence_num = "";
    private String belate_num = "";
    private String leavearly_num = "";
    private String total_awork = "";
    private String pname = "";
    private String sex = "";
    private String headpic = "";

    private WorkAttendanceDetailVO_ attn_days;

    public WorkAttendanceDetailVO_ getAttn_days() {
        return attn_days;
    }

    public void setAttn_days(WorkAttendanceDetailVO_ attn_days) {
        this.attn_days = attn_days;
    }

    private WorkAttendanceDetailVO_ array;
    private List<String> nowrlog_days;
    private List<String> belate_days;
    private List<String> leavearly_days;
    private String absence_days = "";

    public List<String> getNowrlog_days() {
        return nowrlog_days;
    }

    public void setNowrlog_days(List<String> nowrlog_days) {
        this.nowrlog_days = nowrlog_days;
    }

    public List<String> getBelate_days() {
        return belate_days;
    }

    public void setBelate_days(List<String> belate_days) {
        this.belate_days = belate_days;
    }

  

    public List<String> getLeavearly_days() {
        return leavearly_days;
    }

    public void setLeavearly_days(List<String> leavearly_days) {
        this.leavearly_days = leavearly_days;
    }

    public String getAbsence_days() {
        return absence_days;
    }

    public void setAbsence_days(String absence_days) {
        this.absence_days = absence_days;
    }

    public WorkAttendanceDetailVO_ getArray() {
        return array;
    }

    public void setArray(WorkAttendanceDetailVO_ array) {
        this.array = array;
    }

    private String quekaDate = "";
    private String chidaoDate = "";
    private String zaotuiDate = "";
    private String logDate = "";

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_fullname() {
        return u_fullname;
    }

    public void setU_fullname(String u_fullname) {
        this.u_fullname = u_fullname;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getS_year() {
        return s_year;
    }

    public void setS_year(String s_year) {
        this.s_year = s_year;
    }

    public String getS_month() {
        return s_month;
    }

    public void setS_month(String s_month) {
        this.s_month = s_month;
    }

    public String getNowrlog_num() {
        return nowrlog_num;
    }

    public void setNowrlog_num(String nowrlog_num) {
        this.nowrlog_num = nowrlog_num;
    }

    public String getAbsence_num() {
        return absence_num;
    }

    public void setAbsence_num(String absence_num) {
        this.absence_num = absence_num;
    }

    public String getBelate_num() {
        return belate_num;
    }

    public void setBelate_num(String belate_num) {
        this.belate_num = belate_num;
    }

    public String getLeavearly_num() {
        return leavearly_num;
    }

    public void setLeavearly_num(String leavearly_num) {
        this.leavearly_num = leavearly_num;
    }

    public String getTotal_awork() {
        return total_awork;
    }

    public void setTotal_awork(String total_awork) {
        this.total_awork = total_awork;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getQuekaDate() {
        return quekaDate;
    }

    public void setQuekaDate(String quekaDate) {
        this.quekaDate = quekaDate;
    }

    public String getChidaoDate() {
        return chidaoDate;
    }

    public void setChidaoDate(String chidaoDate) {
        this.chidaoDate = chidaoDate;
    }

    public String getZaotuiDate() {
        return zaotuiDate;
    }

    public void setZaotuiDate(String zaotuiDate) {
        this.zaotuiDate = zaotuiDate;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }
}
