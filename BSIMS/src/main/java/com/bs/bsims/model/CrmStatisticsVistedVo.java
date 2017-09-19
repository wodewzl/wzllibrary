/**
 * 
 */
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**

 *          BS北盛最帅程序员

 *         Copyright (c) 2016

 *        湖北北盛科技有限公司

 *        @author 梁骚侠
 
 *        @date 2016-5-26

 *        @version 2.0

 */
public class CrmStatisticsVistedVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private CrmStatisticsVistedVo info;
    private String allCustomer;
    private String allNum;
    private String average;
    private List<CrmStatisticsVistedVo> chart;
    private List<CrmStatisticsVistedVo> list;
    private List<CrmStatisticsVistedVo> next;
    
    private String num;
    private String date;
    private String dateTime;
    private String month;
    private String monthNum;
    private String userid;
    private String fullname;
    private String contrast;
    private String code;
    private String maxchart;
    private String retinfo;
    public String getMaxchart() {
        return maxchart;
    }
    public void setMaxchart(String maxchart) {
        this.maxchart = maxchart;
    }
    public CrmStatisticsVistedVo getInfo() {
        return info;
    }
    public void setInfo(CrmStatisticsVistedVo info) {
        this.info = info;
    }
    public String getAllCustomer() {
        return allCustomer;
    }
    public void setAllCustomer(String allCustomer) {
        this.allCustomer = allCustomer;
    }
    public String getAllNum() {
        return allNum;
    }
    public void setAllNum(String allNum) {
        this.allNum = allNum;
    }
    public String getAverage() {
        return average;
    }
    public void setAverage(String average) {
        this.average = average;
    }
    public List<CrmStatisticsVistedVo> getChart() {
        return chart;
    }
    public void setChart(List<CrmStatisticsVistedVo> chart) {
        this.chart = chart;
    }
    public List<CrmStatisticsVistedVo> getList() {
        return list;
    }
    public void setList(List<CrmStatisticsVistedVo> list) {
        this.list = list;
    }
    public List<CrmStatisticsVistedVo> getNext() {
        return next;
    }
    public void setNext(List<CrmStatisticsVistedVo> next) {
        this.next = next;
    }
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
    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public String getMonthNum() {
        return monthNum;
    }
    public void setMonthNum(String monthNum) {
        this.monthNum = monthNum;
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
    public String getContrast() {
        return contrast;
    }
    public void setContrast(String contrast) {
        this.contrast = contrast;
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
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
