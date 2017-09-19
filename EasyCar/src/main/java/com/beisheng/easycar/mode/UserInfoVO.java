package com.beisheng.easycar.mode;

import com.wuzhanglong.library.mode.BaseVO;

/**
 * Created by ${Wuzhanglong} on 2017/4/25.
 */

public class UserInfoVO extends BaseVO {
    private UserInfoVO data;
    private String uin;
    private String phone;
    private String deposit;
    private String money;
    private String user_status;
    private String money_status;

    public UserInfoVO getData() {
        return data;
    }

    public void setData(UserInfoVO data) {
        this.data = data;
    }

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getMoney_status() {
        return money_status;
    }

    public void setMoney_status(String money_status) {
        this.money_status = money_status;
    }
}
