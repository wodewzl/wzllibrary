package com.xiaojing.shop.mode;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/5/2.
 */

public class CityTwoVO {
    private List<CityTwoVO> citys;
    private String city_id;
    private String city_name;

    public List<CityTwoVO> getCitys() {
        return citys;
    }

    public void setCitys(List<CityTwoVO> citys) {
        this.citys = citys;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
