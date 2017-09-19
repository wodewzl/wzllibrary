
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class CrmTranctVo implements Serializable {
    /*
     * crm合同的实体类
     */

    private List<CrmTranctVo> array;
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

    private String starttime;
    private String endtime;
    private String userid;
    private String type;
    private String payment_mode;
    private String product;
    private String product_num;
    private String addtime;
    private String punit;
    private String pmoney;
    private String bid;
    private String bname;
    private String receipt_money;
    private String planmoney;
    private String isread;
    private String direction;
    private String totalMoney;
    private String remain_money;

    public String getRemain_money() {
        return remain_money;
    }

    public void setRemain_money(String remain_money) {
        this.remain_money = remain_money;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
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
    private String ptotel;

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

    public List<CrmTranctVo> getArray() {
        return array;
    }

    public void setArray(List<CrmTranctVo> array) {
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

    public String getPlanmoney() {
        return planmoney;
    }

    public void setPlanmoney(String planmoney) {
        this.planmoney = planmoney;
    }

    private String changeStatus;
    private String changeStatusName;

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

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
