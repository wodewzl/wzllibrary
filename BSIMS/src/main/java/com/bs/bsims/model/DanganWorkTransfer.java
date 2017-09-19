
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class DanganWorkTransfer implements Serializable {

    private String time;
    private String dnamenew;
    private String dnameold;
    private String pnamenew;
    private String pnameold;
    private String zwnamenew;
    private String zwnameold;
    private String reason;
    private List<DanganWorkTransfer> array;
    private String count;
    private String code;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDnamenew() {
        return dnamenew;
    }

    public void setDnamenew(String dnamenew) {
        this.dnamenew = dnamenew;
    }

    public String getDnameold() {
        return dnameold;
    }

    public void setDnameold(String dnameold) {
        this.dnameold = dnameold;
    }

    public String getPnamenew() {
        return pnamenew;
    }

    public void setPnamenew(String pnamenew) {
        this.pnamenew = pnamenew;
    }

    public String getPnameold() {
        return pnameold;
    }

    public void setPnameold(String pnameold) {
        this.pnameold = pnameold;
    }

    public String getZwnamenew() {
        return zwnamenew;
    }

    public void setZwnamenew(String zwnamenew) {
        this.zwnamenew = zwnamenew;
    }

    public String getZwnameold() {
        return zwnameold;
    }

    public void setZwnameold(String zwnameold) {
        this.zwnameold = zwnameold;
    }

    public List<DanganWorkTransfer> getArray() {
        return array;
    }

    public void setArray(List<DanganWorkTransfer> array) {
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
}
