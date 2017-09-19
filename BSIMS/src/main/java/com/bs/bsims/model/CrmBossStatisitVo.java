
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class CrmBossStatisitVo implements Serializable {

    private CrmBossStatisitVo info;
    private CrmBossStatisitVo lastMonth;
    private CrmBossStatisitVo customer;
    private CrmBossStatisitVo contract;
    private CrmBossStatisitVo business;
    private String count;
    private String addcount;
    private String dropcount;
    private String receivable;
    private CrmBossStatisitVo targets;
    private String payment;
    private String target;
    private String compPercent;
    private String compRate;
    private CrmBossStatisitVo funnel;
    private List<CrmBossStatisitVo> array;
    private String status;
    private String paymentTotal;
    private String statusName;
    private String money;
    private String maxMoney;
    private String predictedMoney;
    private String targetMoney;
    private CrmBossStatisitVo visit;
    private String visitCount;
    private String customerCount;
    private String visitPerday;
    private CrmBossStatisitVo thisMonth;
    private CrmBossStatisitVo thisSeason;
    private String code;
    private String retinfo;
    private String num;

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

    public CrmBossStatisitVo getInfo() {
        return info;
    }

    public void setInfo(CrmBossStatisitVo info) {
        this.info = info;
    }

    public CrmBossStatisitVo getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(CrmBossStatisitVo lastMonth) {
        this.lastMonth = lastMonth;
    }

    public CrmBossStatisitVo getTargets() {
        return targets;
    }

    public void setTargets(CrmBossStatisitVo targets) {
        this.targets = targets;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public CrmBossStatisitVo getCustomer() {
        return customer;
    }

    public void setCustomer(CrmBossStatisitVo customer) {
        this.customer = customer;
    }

    public CrmBossStatisitVo getContract() {
        return contract;
    }

    public void setContract(CrmBossStatisitVo contract) {
        this.contract = contract;
    }

    public CrmBossStatisitVo getBusiness() {
        return business;
    }

    public void setBusiness(CrmBossStatisitVo business) {
        this.business = business;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAddcount() {
        return addcount;
    }

    public void setAddcount(String addcount) {
        this.addcount = addcount;
    }

    public String getDropcount() {
        return dropcount;
    }

    public void setDropcount(String dropcount) {
        this.dropcount = dropcount;
    }

    public String getReceivable() {
        return receivable;
    }

    public void setReceivable(String receivable) {
        this.receivable = receivable;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public CrmBossStatisitVo getFunnel() {
        return funnel;
    }

    public void setFunnel(CrmBossStatisitVo funnel) {
        this.funnel = funnel;
    }

    public List<CrmBossStatisitVo> getArray() {
        return array;
    }

    public void setArray(List<CrmBossStatisitVo> array) {
        this.array = array;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(String maxMoney) {
        this.maxMoney = maxMoney;
    }

    public String getPredictedMoney() {
        return predictedMoney;
    }

    public void setPredictedMoney(String predictedMoney) {
        this.predictedMoney = predictedMoney;
    }

    public String getTargetMoney() {
        return targetMoney;
    }

    public void setTargetMoney(String targetMoney) {
        this.targetMoney = targetMoney;
    }

    public CrmBossStatisitVo getVisit() {
        return visit;
    }

    public void setVisit(CrmBossStatisitVo visit) {
        this.visit = visit;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(String customerCount) {
        this.customerCount = customerCount;
    }

    public String getVisitPerday() {
        return visitPerday;
    }

    public void setVisitPerday(String visitPerday) {
        this.visitPerday = visitPerday;
    }

    public CrmBossStatisitVo getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(CrmBossStatisitVo thisMonth) {
        this.thisMonth = thisMonth;
    }

    public CrmBossStatisitVo getThisSeason() {
        return thisSeason;
    }

    public void setThisSeason(CrmBossStatisitVo thisSeason) {
        this.thisSeason = thisSeason;
    }

    public String getPaymentTotal() {
        return paymentTotal;
    }

    public void setPaymentTotal(String paymentTotal) {
        this.paymentTotal = paymentTotal;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

}
