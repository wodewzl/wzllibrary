package com.beisheng.easycar.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/7/24.
 */

public class MyMoneyVO extends BaseVO{
    private MyMoneyVO data;
    private List<MyMoneyVO> list;

    private String id;
    private String uin;
    private String type;
    private String money;
    private String balance;
    private String update_time;
    private String title;

    public MyMoneyVO getData() {
        return data;
    }

    public void setData(MyMoneyVO data) {
        this.data = data;
    }

    public List<MyMoneyVO> getList() {
        return list;
    }

    public void setList(List<MyMoneyVO> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
