package com.beisheng.synews.mode;

/**
 * created：gaoyao on 2016/11/26 19:03
 */
public class WebViewfinish {
    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    private boolean finish;

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    private String close;

    public WebViewfinish(boolean finish, String close) {
        this.close=close;
        this.finish=finish;
    }
}
