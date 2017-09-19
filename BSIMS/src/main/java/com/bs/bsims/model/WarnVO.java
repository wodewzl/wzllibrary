
package com.bs.bsims.model;

import java.util.List;

public class WarnVO {
    private List<WarnVO> array;
    private String code;
    private String retinfo;
    private String system_time;
    private String count;
    private String warnid;
    private String wname;
    private String wtid;
    private String wdescription;

    public List<WarnVO> getArray() {
        return array;
    }

    public String getWarnid() {
        return warnid;
    }

    public void setWarnid(String warnid) {
        this.warnid = warnid;
    }

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    public String getWtid() {
        return wtid;
    }

    public void setWtid(String wtid) {
        this.wtid = wtid;
    }

    public String getWdescription() {
        return wdescription;
    }

    public void setWdescription(String wdescription) {
        this.wdescription = wdescription;
    }

    public void setArray(List<WarnVO> array) {
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
