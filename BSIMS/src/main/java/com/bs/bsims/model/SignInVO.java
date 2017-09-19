
package com.bs.bsims.model;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class SignInVO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8748828846739650045L;

    private String attendanceid;
    private String address;
    private String lat;
    private String lon;
    private String time;

    /**
     * @author peck
     * @Description: 新增
     * @date 2015/5/14 17:56
     * @email 971371860@qq.com
     * @version V1.0 HttpStatus.SC_OK
     */
    private String atname;
    private String status;
    private String userid;
    private String fullname;

    private String stime;
    private String is_cloud;
    private String malposition;
    private String remark;
    // "attendanceid":"17380",
    // "userid":"13",
    // "lng":"112.138154",
    // "lat":"32.038875",
    // "address":"\u6e56\u5317\u7701\u8944\u9633\u5e02\u6a0a\u57ce\u533a\u957f\u8679\u8def\u9760\u8fd1\u6c11\u53d1\u57ce\u5e02\u5370\u8c61",
    // "time":"1431591287",
    // "remark":"\u6682\u65e0",
    // "pic":"\u6682\u65e0",
    // "atid":"\u6682\u65e0",
    // "atname":"\u4e0a\u73ed",
    // "headpic":"http:\/\/cp.beisheng.wang\/Resume\/image\/1431523057.jpg",
    // "fullname":"\u9648\u96e8",
    // "did":"1",
    // "entrytime":"1427385600",
    // "status":"0"

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMalposition() {
        return malposition;
    }

    public void setMalposition(String malposition) {
        this.malposition = malposition;
    }

    public String getIs_cloud() {
        return is_cloud;
    }

    public void setIs_cloud(String is_cloud) {
        this.is_cloud = is_cloud;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    /**
     * 打卡类型
     * 
     * @return
     */
    public String getAtname() {
        return atname;
    }

    public void setAtname(String atname) {
        this.atname = atname;
    }

    /**
     * 考勤状态：0正常 1迟到 2早退
     * 
     * @return
     */
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public SignInVO() {
        super();
    }

    public String getAttendanceid() {
        return attendanceid;
    }

    public void setAttendanceid(String attendanceid) {
        this.attendanceid = attendanceid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
