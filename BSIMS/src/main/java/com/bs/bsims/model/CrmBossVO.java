
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;
public class CrmBossVO implements Serializable {
    private String code;
    private String retinfo;
    private String system_time;
    private String newContract;
    private CrmBossVO info;
    private CrmBossVO customer;
    private CrmBossVO business;
    private CrmBossVO contract;
    private String thisWeek;
    private String lastWeek;
    private String status;
    private String changeNum;

    private CrmBossVO target;
    private String monthTarget;
    private String monthSign;
    private String monthPayment;
    private String compRate;
    private String compPercent;

    private CrmBossVO funnel;
    private String predictedMoney;
    private String targetMoney;
    private List<CrmBossVO> array;
    private String statusName;
    private String money;
    
    public String getNewContract() {
        return newContract;
    }

    public void setNewContract(String newContract) {
        this.newContract = newContract;
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

    public CrmBossVO getInfo() {
        return info;
    }

    public void setInfo(CrmBossVO info) {
        this.info = info;
    }

    public CrmBossVO getCustomer() {
        return customer;
    }

    public void setCustomer(CrmBossVO customer) {
        this.customer = customer;
    }

    public CrmBossVO getBusiness() {
        return business;
    }

    public void setBusiness(CrmBossVO business) {
        this.business = business;
    }

    public CrmBossVO getContract() {
        return contract;
    }

    public void setContract(CrmBossVO contract) {
        this.contract = contract;
    }

    public String getThisWeek() {
        return thisWeek;
    }

    public void setThisWeek(String thisWeek) {
        this.thisWeek = thisWeek;
    }

    public String getLastWeek() {
        return lastWeek;
    }

    public void setLastWeek(String lastWeek) {
        this.lastWeek = lastWeek;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(String changeNum) {
        this.changeNum = changeNum;
    }

    public CrmBossVO getTarget() {
        return target;
    }

    public void setTarget(CrmBossVO target) {
        this.target = target;
    }

    public String getMonthTarget() {
        return monthTarget;
    }

    public void setMonthTarget(String monthTarget) {
        this.monthTarget = monthTarget;
    }

    public String getMonthSign() {
        return monthSign;
    }

    public void setMonthSign(String monthSign) {
        this.monthSign = monthSign;
    }

    public String getMonthPayment() {
        return monthPayment;
    }

    public void setMonthPayment(String monthPayment) {
        this.monthPayment = monthPayment;
    }

    public String getCompRate() {
        return compRate;
    }

    public void setCompRate(String compRate) {
        this.compRate = compRate;
    }

    public String getCompPercent() {
        return compPercent;
    }

    public void setCompPercent(String compPercent) {
        this.compPercent = compPercent;
    }

    public CrmBossVO getFunnel() {
        return funnel;
    }

    public void setFunnel(CrmBossVO funnel) {
        this.funnel = funnel;
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

    public List<CrmBossVO> getArray() {
        return array;
    }

    public void setArray(List<CrmBossVO> array) {
        this.array = array;
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

}
