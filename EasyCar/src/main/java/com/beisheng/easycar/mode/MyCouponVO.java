package com.beisheng.easycar.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/7/22.
 */

public class MyCouponVO extends BaseVO{
    private MyCouponVO data;
    private List<MyCouponVO> list;
    private String count;
    private String id;
    private String name;
    private String endtime;
    private String status;
    private String statusname;

    public MyCouponVO getData() {
        return data;
    }

    public void setData(MyCouponVO data) {
        this.data = data;
    }

    public List<MyCouponVO> getList() {
        return list;
    }

    public void setList(List<MyCouponVO> list) {
        this.list = list;
    }

    @Override
    public String getCount() {
        return count;
    }

    @Override
    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }
}
