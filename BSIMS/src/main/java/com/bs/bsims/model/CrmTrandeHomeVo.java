
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class CrmTrandeHomeVo implements Serializable {

    /***
     * 合同下大列表的三个切换的实体类 / /* 拜访记录
     */
    private CrmBussinesListindexVo contract;
    private String vid;
    private String info;
    private String time;
    private String objective;
    private String mode;
    private String lon;
    private String lat;
    private String address;
    private String bid;
    private String hid;
    private String cname;
    private String cid;
    private String fullName;
    private String headpic;
    private String positionsName;
    private String departmentName;
    private String objectiveName;
    private String modeName;
    private String bname;
    private String crmEdit;// 编辑权限 0没有 1有权限
    private String comment;// 回复数量tranVo
    private String praise;// 点赞
    private String decline;// 点踩
    private String count;
    private String code;
    private String retinfo;
    private String isdeclined;
    private String ispraised;
    private boolean agree;
    private boolean oppose;
    private List<String> imgs;
    private String hname;
    
    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    /***
     * 
     * 汇款记录
     */
    private String mid;
//    private String money;
    private String planned_date;
    private String periods;
    private String payment_mode;
    private String status;
    private String uid;
    private String fullname;
  private String receipt;

    
    
    
    
    
    
    
    
    
    
    
    
    public String getReceipt() {
    return receipt;
}

public void setReceipt(String receipt) {
    this.receipt = receipt;
}

    /***
     * 
     * 产品
     */
    private String pid;
    private String name;
    private String money;
    private String unit;
    private String remark;
    private String totalPrice;
    
    
    

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CrmBussinesListindexVo getContract() {
        return contract;
    }

    public void setContract(CrmBussinesListindexVo contract) {
        this.contract = contract;
    }

    private List<CrmTrandeHomeVo> array;

    public List<CrmTrandeHomeVo> getArray() {
        return array;
    }

    public void setArray(List<CrmTrandeHomeVo> array) {
        this.array = array;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getPositionsName() {
        return positionsName;
    }

    public void setPositionsName(String positionsName) {
        this.positionsName = positionsName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getCrmEdit() {
        return crmEdit;
    }

    public void setCrmEdit(String crmEdit) {
        this.crmEdit = crmEdit;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getDecline() {
        return decline;
    }

    public void setDecline(String decline) {
        this.decline = decline;
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

    public String getIsdeclined() {
        return isdeclined;
    }

    public void setIsdeclined(String isdeclined) {
        this.isdeclined = isdeclined;
    }

    public String getIspraised() {
        return ispraised;
    }

    public void setIspraised(String ispraised) {
        this.ispraised = ispraised;
    }

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public boolean isOppose() {
        return oppose;
    }

    public void setOppose(boolean oppose) {
        this.oppose = oppose;
    }
    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getPlanned_date() {
        return planned_date;
    }

    public void setPlanned_date(String planned_date) {
        this.planned_date = planned_date;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

   
}
