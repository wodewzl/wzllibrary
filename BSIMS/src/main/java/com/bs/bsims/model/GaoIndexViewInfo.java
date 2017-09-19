
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class GaoIndexViewInfo implements Serializable {
    private String onlinenum;
    private String offlinenum;

    private List<EmployeeVO> onlines;
    private List<EmployeeVO> offlines;
    
    private List<EmployeeVO> others;
    public List<EmployeeVO> getOthers() {
        return others;
    }

    public void setOthers(List<EmployeeVO> others) {
        this.others = others;
    }

    private String dateTime;

   

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getOnlinenum() {
        return onlinenum;
    }

    public void setOnlinenum(String onlinenum) {
        this.onlinenum = onlinenum;
    }

    public String getOfflinenum() {
        return offlinenum;
    }

    public void setOfflinenum(String offlinenum) {
        this.offlinenum = offlinenum;
    }

    public List<EmployeeVO> getOnlines() {
        return onlines;
    }

    public void setOnlines(List<EmployeeVO> onlines) {
        this.onlines = onlines;
    }

    public List<EmployeeVO> getOfflines() {
        return offlines;
    }

    public void setOfflines(List<EmployeeVO> offlines) {
        this.offlines = offlines;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

}
