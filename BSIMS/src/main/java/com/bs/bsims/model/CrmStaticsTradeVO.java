
package com.bs.bsims.model;

import java.util.List;

public class CrmStaticsTradeVO {
    private String count;
    private String code;
    private String retinfo;
    private String system_time;
    private CrmStaticsTradeVO info;
    private String totalMoney;
    private String totalNum;
    private String totalPayment;
    private List<CrmStaticsTradeVO> chart;
    private List<CrmStaticsTradeVO> list;
    private String maxchart;
    private String minchart;
    private String monthMoney;
    private String monthPayment;

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

    public CrmStaticsTradeVO getInfo() {
        return info;
    }

    public void setInfo(CrmStaticsTradeVO info) {
        this.info = info;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public List<CrmStaticsTradeVO> getChart() {
        return chart;
    }

    public void setChart(List<CrmStaticsTradeVO> chart) {
        this.chart = chart;
    }

    public List<CrmStaticsTradeVO> getList() {
        return list;
    }

    public void setList(List<CrmStaticsTradeVO> list) {
        this.list = list;
    }

    public String getMaxchart() {
        return maxchart;
    }

    public void setMaxchart(String maxchart) {
        this.maxchart = maxchart;
    }

    public String getMinchart() {
        return minchart;
    }

    public void setMinchart(String minchart) {
        this.minchart = minchart;
    }

    public String getMonthMoney() {
        return monthMoney;
    }

    public void setMonthMoney(String monthMoney) {
        this.monthMoney = monthMoney;
    }

    public String getMonthPayment() {
        return monthPayment;
    }

    public void setMonthPayment(String monthPayment) {
        this.monthPayment = monthPayment;
    }

}
