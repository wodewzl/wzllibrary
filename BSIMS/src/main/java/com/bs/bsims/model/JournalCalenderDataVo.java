
package com.bs.bsims.model;

import java.util.List;

public class JournalCalenderDataVo {
    private List<JournalCalenderDataVo> array;
    private String date;
    private String lid;
    private String code;
    private String retinfo;
    private String system_time;

    public JournalCalenderDataVo() {
        super();
    }

    public List<JournalCalenderDataVo> getArray() {
        return array;
    }

    public void setArray(List<JournalCalenderDataVo> array) {
        this.array = array;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
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

}
