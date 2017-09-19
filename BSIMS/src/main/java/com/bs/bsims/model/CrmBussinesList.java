
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 商机详情实体类
 * 
 * 
 * 
 * */
/*列表的实体类*/
public class CrmBussinesList implements Serializable {

    private List<CrmBussinesList> array;
    private String bid;// 商机id
    private String bname;// 商机名称
    private String money;// 商机预期金额
    private String status;// 商机状态
    private String cname;// 客户名称
    private String code;//
    private String count;// 列表数量
    private String statusName;// 商机状态名称

    private String percent;// 成功率

    private String addtime;// 创建时间

    private String fullname;// 创建者姓名
    private String isread;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
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

    public List<CrmBussinesList> getArray() {
        return array;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setArray(List<CrmBussinesList> array) {
        this.array = array;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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

    private String retinfo;

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

}
