package com.wuzhanglong.library.mode;

/**
 * Created by Administrator on 2017/2/25.
 */

public class EBMessageVO {
    public String message;
    public String[] params;
    public String msg;
    public Object object;

    public EBMessageVO(String message) {
        this.message = message;
    }
    public EBMessageVO(String message,String msg) {
        this.message = message;
        this.msg=msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
