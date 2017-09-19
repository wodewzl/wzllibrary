
package com.beisheng.synews.mode;

import java.io.Serializable;
import java.util.List;

public class LocalVO implements Serializable {
    private String name;
    private String id;
    public String code;
    public String retinfo;
    private List<LocalVO> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<LocalVO> getList() {
        return list;
    }

    public void setList(List<LocalVO> list) {
        this.list = list;
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

}
