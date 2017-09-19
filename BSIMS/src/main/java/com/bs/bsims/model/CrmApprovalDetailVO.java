
package com.bs.bsims.model;

import java.util.List;

public class CrmApprovalDetailVO {
    private String code;
    private String retinfo;
    private String system_time;
    private CrmApprovalDetailVO array;

    private String mid;
    private String userid;
    private String addtime;
    private String money;
    private String planned_date;
    private String receipt;
    private String periods;
    private String status;

	private String title;
    private String hstatus;
    private String payment;
    private String receipt_money;
    private String cname;
    private String fullname;
    private String headpic;
    private String dname;
    private String pname;
    private String statusName;
    private String approval;
    private String hmoney;

    private List<EmployeeVO> appUser;
    private List<EmployeeVO> insUser;
    private List<EmployeeVO> opinion;
    private String hstatusName;
    private String sex;
    private String payment_mode;
    private String remark;

    public String getUserid() {
    	return userid;
    }
    
    public void setUserid(String userid) {
    	this.userid = userid;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public CrmApprovalDetailVO getArray() {
        return array;
    }

    public void setArray(CrmApprovalDetailVO array) {
        this.array = array;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPlanned_date() {
        return planned_date;
    }

    public void setPlanned_date(String planned_date) {
        this.planned_date = planned_date;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHstatus() {
        return hstatus;
    }

    public void setHstatus(String hstatus) {
        this.hstatus = hstatus;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getReceipt_money() {
        return receipt_money;
    }

    public void setReceipt_money(String receipt_money) {
        this.receipt_money = receipt_money;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public List<EmployeeVO> getAppUser() {
        return appUser;
    }

    public void setAppUser(List<EmployeeVO> appUser) {
        this.appUser = appUser;
    }

    public List<EmployeeVO> getInsUser() {
        return insUser;
    }

    public void setInsUser(List<EmployeeVO> insUser) {
        this.insUser = insUser;
    }

    public List<EmployeeVO> getOpinion() {
        return opinion;
    }

    public void setOpinion(List<EmployeeVO> opinion) {
        this.opinion = opinion;
    }

    public String getHmoney() {
        return hmoney;
    }

    public void setHmoney(String hmoney) {
        this.hmoney = hmoney;
    }

    public String getHstatusName() {
        return hstatusName;
    }

    public void setHstatusName(String hstatusName) {
        this.hstatusName = hstatusName;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

}
