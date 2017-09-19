
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class CrmBussinesListindexVo implements Serializable {

    private CrmBussinesListindexVo array;
    private String bid;
    private String bname;
    private String status;
    private String money;
    private String expirationDate;
    private String type;
    private String source;
    private String remark;
    private String addtime;
    private String relation;
    private String cname;
    private String punit;
    private String pmoney;
    private String fullname;
    private String headpic;
    private String dname;
    private String pname;
    private String statusName;
    private String crmEdit;
    private String cid;
    private String payment;
    private String title;
    private String moveEdit;
    private String ptotle;
    private String lheadpic;
    private String lname;
    private String lpost;
    private String lphone;
    private String isPublic;
    private String proname;
    private String del;
    private String pid;
    private String sourceId;
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    private List<EmployeeVO> followUser;
    private List<EmployeeVO> follow;

    public List<EmployeeVO> getFollow() {
        return follow;
    }

    public void setFollow(List<EmployeeVO> follow) {
        this.follow = follow;
    }

    public List<EmployeeVO> getFollowUser() {
        return followUser;
    }

    public void setFollowUser(List<EmployeeVO> followUser) {
        this.followUser = followUser;
    }

    private List<CrmBussinesListindexVo> product;

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLpost() {
        return lpost;
    }

    public void setLpost(String lpost) {
        this.lpost = lpost;
    }

    public String getLphone() {
        return lphone;
    }

    public void setLphone(String lphone) {
        this.lphone = lphone;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getLheadpic() {
        return lheadpic;
    }

    public void setLheadpic(String lheadpic) {
        this.lheadpic = lheadpic;
    }

    public String getPtotle() {
        return ptotle;
    }

    public void setPtotle(String ptotle) {
        this.ptotle = ptotle;
    }

    public String getMoveEdit() {
        return moveEdit;
    }

    public void setMoveEdit(String moveEdit) {
        this.moveEdit = moveEdit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    private List<EmployeeVO> insUser;

    public List<EmployeeVO> getInsUser() {
        return insUser;
    }

    public void setInsUser(List<EmployeeVO> insUser) {
        this.insUser = insUser;
    }

    private String userid;
    private String code;
    private String percent;
    private String product_num;

    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<CrmBussinesListindexVo> getProduct() {
        return product;
    }

    public void setProduct(List<CrmBussinesListindexVo> product) {
        this.product = product;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
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

    public String getCrmEdit() {
        return crmEdit;
    }

    public void setCrmEdit(String crmEdit) {
        this.crmEdit = crmEdit;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CrmBussinesListindexVo getArray() {
        return array;
    }

    public void setArray(CrmBussinesListindexVo array) {
        this.array = array;
    }

}
