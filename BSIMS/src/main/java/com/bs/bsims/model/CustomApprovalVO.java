
package com.bs.bsims.model;

import java.util.List;

public class CustomApprovalVO {
    private CustomApprovalVO array;
    private String code;
    private String retinfo;
    private String system_time;

    private String otype;
    private String oname;
    private String options;
    private String ovalue;

    // private List<CustomApprovalVO> appUser;
    // private List<CustomApprovalVO> insUser;
    private List<CustomApprovalVO> info;
    private List<EmployeeVO> appUser;
    private List<EmployeeVO> insUser;

    public CustomApprovalVO getArray() {
        return array;
    }

    public void setArray(CustomApprovalVO array) {
        this.array = array;
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

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
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

    public List<CustomApprovalVO> getInfo() {
        return info;
    }

    public void setInfo(List<CustomApprovalVO> info) {
        this.info = info;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getOvalue() {
        return ovalue;
    }

    public void setOvalue(String ovalue) {
        this.ovalue = ovalue;
    }

}
