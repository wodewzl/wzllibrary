
package com.bs.bsims.model;

import java.util.List;

public class CrmStaticSaleValueVO {
    private CrmStaticSaleValueVO info;
    private String totalTarget;
    private String totalPayment;
    private List<CrmStaticSaleValueVO> array;
    private List<CrmStaticSaleValueVO> users;
    private String tid;
    private String target;
    private String belong;
    private String fullname;
    private String payment;
    private String compPercent;
    private String compRate;

    private String count;
    private String code;
    private String retinfo;
    private String system_time;

    public List<CrmStaticSaleValueVO> getUsers() {
        return users;
    }

    public void setUsers(List<CrmStaticSaleValueVO> users) {
        this.users = users;
    }

    public CrmStaticSaleValueVO getInfo() {
        return info;
    }

    public void setInfo(CrmStaticSaleValueVO info) {
        this.info = info;
    }

    public String getTotalTarget() {
        return totalTarget;
    }

    public void setTotalTarget(String totalTarget) {
        this.totalTarget = totalTarget;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public CrmStaticSaleValueVO() {
        super();
    }

    public List<CrmStaticSaleValueVO> getArray() {
        return array;
    }

    public void setArray(List<CrmStaticSaleValueVO> array) {
        this.array = array;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCompPercent() {
        return compPercent;
    }

    public void setCompPercent(String compPercent) {
        this.compPercent = compPercent;
    }

    public String getCompRate() {
        return compRate;
    }

    public void setCompRate(String compRate) {
        this.compRate = compRate;
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

}
