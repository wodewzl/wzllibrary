package com.bs.aikushi;

/**
 * Created by Administrator on 2017/2/25.
 */

public class EBMessageVO {
    public  String message;
    public String url;
    public EBMessageVO(String message,String url) {
        this.message=message;
        this.url=url;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }
}
