package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.io.Serializable;

/**
 * Created by ${Wuzhanglong} on 2017/4/25.
 */

public class UserInfoVO extends BaseVO implements Serializable{
    private UserInfoVO datas;
    private String username;
    private String userid;
    private String key;
    private String avator;
    private String nickname;
    private String jp_alias;

    public UserInfoVO getDatas() {
        return datas;
    }

    public void setDatas(UserInfoVO datas) {
        this.datas = datas;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getKey() {
        return key;
//        return "ffb58e5bac878a4ed8e4963606468863";
    }


    public void setKey(String key) {
        this.key = key;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getJp_alias() {
        return jp_alias;
    }

    public void setJp_alias(String jp_alias) {
        this.jp_alias = jp_alias;
    }
}
