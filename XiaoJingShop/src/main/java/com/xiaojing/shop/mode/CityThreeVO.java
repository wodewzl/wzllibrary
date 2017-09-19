package com.xiaojing.shop.mode;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/5/2.
 */

public class CityThreeVO {
    private List<CityThreeVO> districts;
    private String district_id;
    private String district_name;

    public List<CityThreeVO> getDistricts() {
        return districts;
    }

    public void setDistricts(List<CityThreeVO> districts) {
        this.districts = districts;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }
}
