
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class CustomApprovalListVO implements Serializable{
    private List<CustomApprovalListVO> array;
    private String code;
    private String retinfo;
    private String system_time;

    private String atid;
    private String name;
    private String id;
    private List<CustomApprovalListVO> option;

    public List<CustomApprovalListVO> getArray() {
        return array;
    }

    public void setArray(List<CustomApprovalListVO> array) {
        this.array = array;
    }

    public String getAtid() {
        return atid;
    }

    public void setAtid(String atid) {
        this.atid = atid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CustomApprovalListVO> getOption() {
        return option;
    }

    public void setOption(List<CustomApprovalListVO> option) {
        this.option = option;
    }

}
