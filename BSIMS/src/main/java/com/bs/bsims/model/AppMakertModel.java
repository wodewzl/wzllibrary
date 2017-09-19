/**
 * 
 */

package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-9-2
 * @version 2.0
 */
public class AppMakertModel implements Serializable {

    private List<AppMakertModel> array;
    private String mdid;
    private String mname;
    private String malias;
    private String mdes;
    private String micon;
    private String morder;
    private String mdemo;

    public String getMdemo() {
        return mdemo;
    }

    public void setMdemo(String mdemo) {
        this.mdemo = mdemo;
    }

    public List<AppMakertModel> getArray() {
        return array;
    }

    public void setArray(List<AppMakertModel> array) {
        this.array = array;
    }

    public String getMdid() {
        return mdid;
    }

    public void setMdid(String mdid) {
        this.mdid = mdid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMalias() {
        return malias;
    }

    public void setMalias(String malias) {
        this.malias = malias;
    }

    public String getMdes() {
        return mdes;
    }

    public void setMdes(String mdes) {
        this.mdes = mdes;
    }

    public String getMicon() {
        return micon;
    }

    public void setMicon(String micon) {
        this.micon = micon;
    }

    public String getMorder() {
        return morder;
    }

    public void setMorder(String morder) {
        this.morder = morder;
    }

    public String getMstatus() {
        return mstatus;
    }

    public void setMstatus(String mstatus) {
        this.mstatus = mstatus;
    }

    public String getMfree() {
        return mfree;
    }

    public void setMfree(String mfree) {
        this.mfree = mfree;
    }

    public String getMnew() {
        return mnew;
    }

    public void setMnew(String mnew) {
        this.mnew = mnew;
    }

    public String getMopen() {
        return mopen;
    }

    public void setMopen(String mopen) {
        this.mopen = mopen;
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
    public String getMexpire() {
        return mexpire;
    }

    public void setMexpire(String mexpire) {
        this.mexpire = mexpire;
    }
    private String mstatus;
    private String mfree;
    private String mnew;
    private String mopen;
    private String mexpire;
    private String count;
    private String code;
    private String retinfo;

}
