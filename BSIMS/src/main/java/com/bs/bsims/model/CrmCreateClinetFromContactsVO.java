
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class CrmCreateClinetFromContactsVO implements Serializable {
    private String lid;
    private String lname;
    private String department;
    private String post;
    private String address;
    private String name;
    private String website;
    private List<CrmCreateClinetFromContactsVO> array;
    private String count;
    private String code;
    private String retinfo;
    private String system_time;

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<CrmCreateClinetFromContactsVO> getArray() {
        return array;
    }

    public void setArray(List<CrmCreateClinetFromContactsVO> array) {
        this.array = array;
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

    public String getSystem_time() {
        return system_time;
    }

    public void setSystem_time(String system_time) {
        this.system_time = system_time;
    }

    @Override
    public String toString() {
        return getLid() + "," + getLname();
    }

}
