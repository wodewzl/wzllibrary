
package com.bs.bsims.model;

import java.util.List;

public class NoticeObjectResultVO {

    private String count;
    private String code;
    private String retinfo;
    private String system_time;
    private List<NoticeObjectVO> array;

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

    public List<NoticeObjectVO> getArray() {
        return array;
    }

    public void setArray(List<NoticeObjectVO> array) {
        this.array = array;
    }

}
