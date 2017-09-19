package com.beisheng.easycar.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/7/21.
 */

public class NeaybyVO extends BaseVO{
    private List<NeaybyVO> data;
    private List<NeaybyVO> list;
    private String id;
    private String name;
    private String address;
    private String lng;
    private String lat;
    private String citycode;
    private String district;
    private String status;
    private String distance;
    public boolean select=false;

    public List<NeaybyVO> getData() {
        return data;
    }

    public void setData(List<NeaybyVO> data) {
        this.data = data;
    }

    public List<NeaybyVO> getList() {
        return list;
    }

    public void setList(List<NeaybyVO> list) {
        this.list = list;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
