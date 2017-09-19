/**
 * 
 */
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**

 *          BS北盛最帅程序员

 *         Copyright (c) 2016

 *        湖北北盛科技有限公司

 *        @author 梁骚侠
 
 *        @date 2016-3-1

 *        @version 1.22

 */
public class JournalFootPrintModel implements Serializable {

    private List<JournalFootPrintModel> array;
  
    private String log_id;
    private String log_user_id;
    private String log_user_name;
    private String log_title;
    private String log_content;
    private String log_createtime;
    private String log_type;
    private String log_class;
    private String code;
    private String retinfo;
    public String getLog_class() {
        return log_class;
    }
    public void setLog_class(String log_class) {
        this.log_class = log_class;
    }
    public List<JournalFootPrintModel> getArray() {
        return array;
    }
    public void setArray(List<JournalFootPrintModel> array) {
        this.array = array;
    }
    public String getLog_id() {
        return log_id;
    }
    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }
    public String getLog_user_id() {
        return log_user_id;
    }
    public void setLog_user_id(String log_user_id) {
        this.log_user_id = log_user_id;
    }
    public String getLog_user_name() {
        return log_user_name;
    }
    public void setLog_user_name(String log_user_name) {
        this.log_user_name = log_user_name;
    }
    public String getLog_title() {
        return log_title;
    }
    public void setLog_title(String log_title) {
        this.log_title = log_title;
    }
    public String getLog_content() {
        return log_content;
    }
    public void setLog_content(String log_content) {
        this.log_content = log_content;
    }
    public String getLog_createtime() {
        return log_createtime;
    }
    public void setLog_createtime(String log_createtime) {
        this.log_createtime = log_createtime;
    }
    public String getLog_type() {
        return log_type;
    }
    public void setLog_type(String log_type) {
        this.log_type = log_type;
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
