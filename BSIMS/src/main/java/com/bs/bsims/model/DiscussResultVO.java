
package com.bs.bsims.model;

import java.util.ArrayList;

public class DiscussResultVO {
    private ArrayList<DiscussVO> array;
    private String count;
    private String code;
    private String retinfo;
    private String system_time;

    public ArrayList<DiscussVO> getArray() {
        return array;
    }

    public void setArray(ArrayList<DiscussVO> array) {
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

}
