
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.ArrayList;

public class JournalDetailResltVO implements Serializable {
    private ArrayList<JournalDetailVO> array;
    private ArrayList<CcVO> cc;
    private ArrayList<BossCcVO> bosscc;
    private ArrayList<CcVO> ccz;
    private String count;
    private String code;
    private String retinfo;
    private String system_time;
    private String comment;

    public ArrayList<JournalDetailVO> getArray() {
        return array;
    }

    public void setArray(ArrayList<JournalDetailVO> array) {
        this.array = array;
    }

    public ArrayList<CcVO> getCc() {
        return cc;
    }

    public void setCc(ArrayList<CcVO> cc) {
        this.cc = cc;
    }

    public ArrayList<BossCcVO> getBosscc() {
        return bosscc;
    }

    public void setBosscc(ArrayList<BossCcVO> bosscc) {
        this.bosscc = bosscc;
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

    public ArrayList<CcVO> getCcz() {
        return ccz;
    }

    public void setCcz(ArrayList<CcVO> ccz) {
        this.ccz = ccz;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
