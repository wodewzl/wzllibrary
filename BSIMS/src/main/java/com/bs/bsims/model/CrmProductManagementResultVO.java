package com.bs.bsims.model;

import java.util.List;

public class CrmProductManagementResultVO {
    private List<CrmProductManagementVO> array;
    private String code;
    private String retinfo;
    private String system_time;
    private String count;
    public CrmProductManagementResultVO(List<CrmProductManagementVO> array, String code, String retinfo, String system_time, String count) {
        super();
        this.array = array;
        this.code = code;
        this.retinfo = retinfo;
        this.system_time = system_time;
        this.count = count;
    }
    public CrmProductManagementResultVO() {
        super();
    }
    public List<CrmProductManagementVO> getArray() {
        return array;
    }
    public void setArray(List<CrmProductManagementVO> array) {
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
    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }
    
}
