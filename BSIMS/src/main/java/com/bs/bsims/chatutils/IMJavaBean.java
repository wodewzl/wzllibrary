/**
 * 
 */

package com.bs.bsims.chatutils;

import java.io.Serializable;
import java.util.List;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-4-22
 * @version 2.0
 */
public class IMJavaBean implements Serializable {
    private List<IMJavaBean> client;
    private String userId; // 聊天的userId
    private String clientNumber;// 聊天的账号
    private String clientPwd;// 聊天的密码
    private String createDate;// 聊天的注册或者登陆日期
    private String loginToken;// 聊天的token
    private String code;// 聊天的请求的返回码
 
    public List<IMJavaBean> getClient() {
        return client;
    }

    public void setClient(List<IMJavaBean> client) {
        this.client = client;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getClientPwd() {
        return clientPwd;
    }

    public void setClientPwd(String clientPwd) {
        this.clientPwd = clientPwd;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
