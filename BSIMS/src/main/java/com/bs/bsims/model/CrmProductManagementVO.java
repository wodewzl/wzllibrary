package com.bs.bsims.model;

public class CrmProductManagementVO{
    private String pid;
    private String name;
    private String money;
    private String unit;
    private String remark;
    public CrmProductManagementVO(String pid, String name, String money, String unit, String remark) {
        super();
        this.pid = pid;
        this.name = name;
        this.money = money;
        this.unit = unit;
        this.remark = remark;
    }
    public CrmProductManagementVO() {
        super();
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
    
    

}
