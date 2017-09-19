
package com.bs.bsims.model;


import java.io.Serializable;
import java.util.List;

public class SignInFather implements Serializable {

    private String count;
    private String code;
    private String retinfo;
    private List<SignInVO> array;

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

    public List<SignInVO> getArray() {
        return array;
    }

    public void setArray(List<SignInVO> array) {
        this.array = array;
    }

}
