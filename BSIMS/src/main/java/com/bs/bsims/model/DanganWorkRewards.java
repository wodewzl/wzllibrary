
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class DanganWorkRewards implements Serializable {

    private String time;
    private String rclass;
    private String info;
    private String cause;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private List<DanganWorkRewards> array;

    public List<DanganWorkRewards> getArray() {
        return array;
    }

    public void setArray(List<DanganWorkRewards> array) {
        this.array = array;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRclass() {
        return rclass;
    }

    public void setRclass(String rclass) {
        this.rclass = rclass;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    private String type;
    private String count;
    private String code;
}
