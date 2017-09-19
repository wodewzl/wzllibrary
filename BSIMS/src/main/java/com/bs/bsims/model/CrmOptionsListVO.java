
package com.bs.bsims.model;

import java.util.List;

public class CrmOptionsListVO {
    private String code;
    private String retinfo;
    private String system_time;
    private List<CrmOptionsListVO> array;
    private String id;
    private String name;
    private String lid;
    private String lname;
    private String money;
    private List<CrmOptionsListVO> product;
    private String pid;

    public List<CrmOptionsListVO> getProduct() {
        return product;
    }

    public void setProduct(List<CrmOptionsListVO> product) {
        this.product = product;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public List<CrmOptionsListVO> getArray() {
        return array;
    }

    public void setArray(List<CrmOptionsListVO> array) {
        this.array = array;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

}
