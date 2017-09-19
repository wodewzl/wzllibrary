
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class CrmTranctVos implements Serializable {

    private CrmTranctVos array;
    private String hid;
    private String title;
    private String money;
    private String payment;
    private String status;
    private String cname;
    private String cid;
    private String percent;
    private String statusName;
    private String count;
    private String code;

    private String p_id;
    private String p_contract;
    private String p_periods;
    private String p_money;
    private String p_planned_date;
    private String p_payment_mode;
    private String p_reminder_time;
    private String p_remark;
    private String p_userid;
    private String p_did;
    private String p_addtime;
    private String content;

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_contract() {
        return p_contract;
    }

    public void setP_contract(String p_contract) {
        this.p_contract = p_contract;
    }

    public String getP_periods() {
        return p_periods;
    }

    public void setP_periods(String p_periods) {
        this.p_periods = p_periods;
    }

    public String getP_money() {
        return p_money;
    }

    public void setP_money(String p_money) {
        this.p_money = p_money;
    }

    public String getP_planned_date() {
        return p_planned_date;
    }

    public void setP_planned_date(String p_planned_date) {
        this.p_planned_date = p_planned_date;
    }

    public String getP_payment_mode() {
        return p_payment_mode;
    }

    public void setP_payment_mode(String p_payment_mode) {
        this.p_payment_mode = p_payment_mode;
    }

    public String getP_reminder_time() {
        return p_reminder_time;
    }

    public void setP_reminder_time(String p_reminder_time) {
        this.p_reminder_time = p_reminder_time;
    }

    public String getP_remark() {
        return p_remark;
    }

    public void setP_remark(String p_remark) {
        this.p_remark = p_remark;
    }

    public String getP_userid() {
        return p_userid;
    }

    public void setP_userid(String p_userid) {
        this.p_userid = p_userid;
    }

    public String getP_did() {
        return p_did;
    }

    public void setP_did(String p_did) {
        this.p_did = p_did;
    }

    public String getP_addtime() {
        return p_addtime;
    }

    public void setP_addtime(String p_addtime) {
        this.p_addtime = p_addtime;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
 

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getMoveEdit() {
        return moveEdit;
    }

    public void setMoveEdit(String moveEdit) {
        this.moveEdit = moveEdit;
    }

    private List<EmployeeVO> insUser;
    private List<EmployeeVO> appUser;
    private List<EmployeeVO> opinion;

    public List<EmployeeVO> getInsUser() {
        return insUser;
    }

    public void setInsUser(List<EmployeeVO> insUser) {
        this.insUser = insUser;
    }

    public List<EmployeeVO> getAppUser() {
        return appUser;
    }

    public void setAppUser(List<EmployeeVO> appUser) {
        this.appUser = appUser;
    }

    public List<EmployeeVO> getOpinion() {
        return opinion;
    }

    public void setOpinion(List<EmployeeVO> opinion) {
        this.opinion = opinion;
    }

    private String starttime;
    private String endtime;
    private String userid;
    private String type;
    private String payment_mode;
    private List<String> product;
    private String product_num;
    private String addtime;
    private String punit;
    private String pmoney;
    private String bid;
    private String bname;
    private String receipt_money;
    private String signing_date;
    private String discount;
    private String remark;

    private String relation;
//    private CrmTranctVos approver;
    private String approver;
    private String client_side;
    private String open;
    private String receipt;
    private String addPayment;

    private List<CrmTranctVos> paymentList;

    private List<CrmTranctVos> planList;

    public String getAddPayment() {
        return addPayment;
    }

    public void setAddPayment(String addPayment) {
        this.addPayment = addPayment;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public List<CrmTranctVos> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<CrmTranctVos> paymentList) {
        this.paymentList = paymentList;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getClient_side() {
        return client_side;
    }

    public void setClient_side(String client_side) {
        this.client_side = client_side;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    private String pid;
    private String planned_date;
    private String reminder_time;
    private String proname;

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

 

    public List<String> getProduct() {
        return product;
    }

    public void setProduct(List<String> product) {
        this.product = product;
    }

    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getPunit() {
        return punit;
    }

    public void setPunit(String punit) {
        this.punit = punit;
    }

    public String getPmoney() {
        return pmoney;
    }

    public void setPmoney(String pmoney) {
        this.pmoney = pmoney;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
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

    public String getCrmEdit() {
        return crmEdit;
    }

    public void setCrmEdit(String crmEdit) {
        this.crmEdit = crmEdit;
    }

    public String getPtotel() {
        return ptotel;
    }

    public void setPtotel(String ptotel) {
        this.ptotel = ptotel;
    }

    private String fullname;
    private String headpic;
    private String dname;
    private String pname;
    private String crmEdit;
    private String moveEdit;
    private String ptotel;
    private String crmApproval;
    private String addPlan;

    public String getAddPlan() {
        return addPlan;
    }

    public void setAddPlan(String addPlan) {
        this.addPlan = addPlan;
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

    public CrmTranctVos getArray() {
        return array;
    }

    public void setArray(CrmTranctVos array) {
        this.array = array;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getReceipt_money() {
        return receipt_money;
    }

    public void setReceipt_money(String receipt_money) {
        this.receipt_money = receipt_money;
    }

    public String getCrmApproval() {
        return crmApproval;
    }

    public void setCrmApproval(String crmApproval) {
        this.crmApproval = crmApproval;
    }

    public String getSigning_date() {
        return signing_date;
    }

    public void setSigning_date(String signing_date) {
        this.signing_date = signing_date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPlanned_date() {
        return planned_date;
    }

    public void setPlanned_date(String planned_date) {
        this.planned_date = planned_date;
    }

    public String getReminder_time() {
        return reminder_time;
    }

    public void setReminder_time(String reminder_time) {
        this.reminder_time = reminder_time;
    }

    public List<CrmTranctVos> getPlanList() {
        return planList;
    }

    public void setPlanList(List<CrmTranctVos> planList) {
        this.planList = planList;
    }

    private CrmTranctVos my_side;
    private List<String> imgs;

    public CrmTranctVos getMy_side() {
        return my_side;
    }

    public void setMy_side(CrmTranctVos my_side) {
        this.my_side = my_side;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    private String changeStatus;
    private String changeStatusName;
    private String remain_money;

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getChangeStatusName() {
        return changeStatusName;
    }

    public void setChangeStatusName(String changeStatusName) {
        this.changeStatusName = changeStatusName;
    }

    public String getRemain_money() {
        return remain_money;
    }

    public void setRemain_money(String remain_money) {
        this.remain_money = remain_money;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
