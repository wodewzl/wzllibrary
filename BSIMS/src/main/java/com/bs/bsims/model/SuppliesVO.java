
package com.bs.bsims.model;

import java.util.List;

public class SuppliesVO {
    private String maid;
    private String atime;
    private String reason;

    private String totalprice;
    private String info;

    private String status;
    private String userid;
    private String addtime;
    private String headpic;
    private String fullname;
    private String dname;
    private String pname;
    private String approval;
    private List<SuppliesItemVO> material;
    private List<EmployeeVO> appUser;
    private List<EmployeeVO> insUser;

    public String getMaid() {
        return maid;
    }

    public void setMaid(String maid) {
        this.maid = maid;
    }

    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public List<SuppliesItemVO> getMaterial() {
        return material;
    }

    public void setMaterial(List<SuppliesItemVO> material) {
        this.material = material;
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

}
