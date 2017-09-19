/**
 * 
 */
package com.bs.bsims.model;

import java.io.Serializable;

/**

 *          BS北盛最帅程序员

 *         Copyright (c) 2016

 *        湖北北盛科技有限公司

 *        @author 梁骚侠
 
 *        @date 2016-5-20

 *        @version 2.0

 */
public class SalePersonInfo implements Serializable{
 
    private SalePersonInfo info;
    private String userid;
    private String fullname;
    private String headpic;
    private String sex;
    private String dname;
    private String pname;
    private String customerTotal;
    private String contractTotal;
    private String paymentTotal;
    private SalePersonInfo lastMonth;
    private SalePersonInfo thisMonth;
    private String targetMoney;
    private String predictedMoney;
    private String contractMoney;
    private String paymentMoney;
    private String receivableMoney;
    private String customerCount;
    private String contactsCount;
    private String businessCount;
    private String contractCount;
    private String visitCount;
    private String visitCustomerCount;
    private String targetMoneyUnit;
    private String predictedMoneyUnit;
    private String contractMoneyUnit;
    private String paymentMoneyUnit;
    private String receivableMoneyUnit;
    
    public String getTargetMoneyUnit() {
        return targetMoneyUnit;
    }
    public void setTargetMoneyUnit(String targetMoneyUnit) {
        this.targetMoneyUnit = targetMoneyUnit;
    }
    public String getPredictedMoneyUnit() {
        return predictedMoneyUnit;
    }
    public void setPredictedMoneyUnit(String predictedMoneyUnit) {
        this.predictedMoneyUnit = predictedMoneyUnit;
    }
    public String getContractMoneyUnit() {
        return contractMoneyUnit;
    }
    public void setContractMoneyUnit(String contractMoneyUnit) {
        this.contractMoneyUnit = contractMoneyUnit;
    }
    public String getPaymentMoneyUnit() {
        return paymentMoneyUnit;
    }
    public void setPaymentMoneyUnit(String paymentMoneyUnit) {
        this.paymentMoneyUnit = paymentMoneyUnit;
    }
    public String getReceivableMoneyUnit() {
        return receivableMoneyUnit;
    }
    public void setReceivableMoneyUnit(String receivableMoneyUnit) {
        this.receivableMoneyUnit = receivableMoneyUnit;
    }
    private String code;
    private String retinfo;
    public SalePersonInfo getInfo() {
        return info;
    }
    public void setInfo(SalePersonInfo info) {
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
    public String getCustomerTotal() {
        return customerTotal;
    }
    public void setCustomerTotal(String customerTotal) {
        this.customerTotal = customerTotal;
    }
    public String getContractTotal() {
        return contractTotal;
    }
    public void setContractTotal(String contractTotal) {
        this.contractTotal = contractTotal;
    }
    public String getPaymentTotal() {
        return paymentTotal;
    }
    public void setPaymentTotal(String paymentTotal) {
        this.paymentTotal = paymentTotal;
    }
    public SalePersonInfo getLastMonth() {
        return lastMonth;
    }
    public void setLastMonth(SalePersonInfo lastMonth) {
        this.lastMonth = lastMonth;
    }
    public SalePersonInfo getThisMonth() {
        return thisMonth;
    }
    public void setThisMonth(SalePersonInfo thisMonth) {
        this.thisMonth = thisMonth;
    }
    public String getTargetMoney() {
        return targetMoney;
    }
    public void setTargetMoney(String targetMoney) {
        this.targetMoney = targetMoney;
    }
    public String getPredictedMoney() {
        return predictedMoney;
    }
    public void setPredictedMoney(String predictedMoney) {
        this.predictedMoney = predictedMoney;
    }
    public String getContractMoney() {
        return contractMoney;
    }
    public void setContractMoney(String contractMoney) {
        this.contractMoney = contractMoney;
    }
    public String getPaymentMoney() {
        return paymentMoney;
    }
    public void setPaymentMoney(String paymentMoney) {
        this.paymentMoney = paymentMoney;
    }
    public String getReceivableMoney() {
        return receivableMoney;
    }
    public void setReceivableMoney(String receivableMoney) {
        this.receivableMoney = receivableMoney;
    }
    public String getCustomerCount() {
        return customerCount;
    }
    public void setCustomerCount(String customerCount) {
        this.customerCount = customerCount;
    }
    public String getContactsCount() {
        return contactsCount;
    }
    public void setContactsCount(String contactsCount) {
        this.contactsCount = contactsCount;
    }
    public String getBusinessCount() {
        return businessCount;
    }
    public void setBusinessCount(String businessCount) {
        this.businessCount = businessCount;
    }
    public String getContractCount() {
        return contractCount;
    }
    public void setContractCount(String contractCount) {
        this.contractCount = contractCount;
    }
    public String getVisitCount() {
        return visitCount;
    }
    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }
    public String getVisitCustomerCount() {
        return visitCustomerCount;
    }
    public void setVisitCustomerCount(String visitCustomerCount) {
        this.visitCustomerCount = visitCustomerCount;
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

}
