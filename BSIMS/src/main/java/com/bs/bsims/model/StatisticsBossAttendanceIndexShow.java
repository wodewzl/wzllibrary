
package com.bs.bsims.model;

import java.io.Serializable;

/**
 * @author peck
 * @Description: 6. 考勤统计首页接口 show下方列表显示数据
 * @date 2015-6-24 下午2:12:57
 * @email 971371860@qq.com
 * @version V1.0
 * @version V1.1 员工档案 获取出勤情况各项接口 专用 name
 */

public class StatisticsBossAttendanceIndexShow implements Serializable {

    private static final long serialVersionUID = 7602395582573279526L;

    // {
    // "num": "27080",
    // "date": "5",
    // "did": "10",
    // "dname": "品宣组",
    // "contrast": "3",
    // "type": "0"
    // },
    private String num;
    private String date;
    private String did;
    private String dname;
    private String contrast;
    private String type;
    private String percent;

    private String number;
    private String person;
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    /**
     * 9.各部门各月考工统计详情接口 封装数据专用
     */
    private String minus;
    /**
     * 9.各部门各月考工统计详情接口 封装数据专用
     */
    private String compare;
    /**
     * 9.各部门各月考工统计详情接口 封装数据专用
     */
    private String all;

    /**
     * 10.各部门下详细的考勤统计接口 专用
     */
    private String fullname;

    /**
     * 10.各部门下详细的考勤统计接口 专用
     */
    private String uid;
    /**
     * 员工档案 获取出勤情况各项接口 专用
     */
    private String name;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    /**
     * 部门名称
     * 
     * @return
     */
    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getContrast() {
        return contrast;
    }

    public void setContrast(String contrast) {
        this.contrast = contrast;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMinus() {
        return minus;
    }

    public void setMinus(String minus) {
        this.minus = minus;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
