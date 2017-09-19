
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class CrmContactVo implements Serializable {

    /*
     * Crm联系人
     */
    private String lid;
    private String lname;
    private String post;
    private String phone;
    private String code;
    private String retinfo;
    private List<CrmContactVo> array;
    private String falgecontant;

    /**
     * 销售阶段
     */

    private String id;
    private String name;

    /**
     * 审批人
     */
    private List<CrmContactVo> appUser;
    private String userid;
    private String fullname;
    private String headpic;

    /**
     * 无张龙客户用的
     */

    private List<CrmContactVo> industry;
    private List<CrmContactVo> level;
    private List<CrmContactVo> source;
    private List<CrmContactVo> number;

    /**
     * 关联商机的数量金额名称
     */
    private String product;
    private String proname;
    private String money;
    private String status;// 根据此状态判断是否限制拜访记录下面的更改商机状态

    private String isDuplicate;

 

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<CrmContactVo> getIndustry() {
        return industry;
    }

    public void setIndustry(List<CrmContactVo> industry) {
        this.industry = industry;
    }

    public List<CrmContactVo> getLevel() {
        return level;
    }

    public void setLevel(List<CrmContactVo> level) {
        this.level = level;
    }

    public List<CrmContactVo> getSource() {
        return source;
    }

    public void setSource(List<CrmContactVo> source) {
        this.source = source;
    }

    public List<CrmContactVo> getAppUser() {
        return appUser;
    }

    public void setAppUser(List<CrmContactVo> appUser) {
        this.appUser = appUser;
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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public List<CrmContactVo> getArray() {
        return array;
    }

    public void setArray(List<CrmContactVo> array) {
        this.array = array;
    }

    public List<CrmContactVo> getNumber() {
        return number;
    }

    public void setNumber(List<CrmContactVo> number) {
        this.number = number;
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

    public String getFalgecontant() {
        return falgecontant;
    }

    public void setFalgecontant(String falgecontant) {
        this.falgecontant = falgecontant;
    }

    public String getIsDuplicate() {
        return isDuplicate;
    }

    public void setIsDuplicate(String isDuplicate) {
        this.isDuplicate = isDuplicate;
    }

    @Override
    public String toString() {
        return getId() + "," + getName();
    }

}
