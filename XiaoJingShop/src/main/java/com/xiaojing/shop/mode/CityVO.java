package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.ArrayList;

/**
 * Created by ${Wuzhanglong} on 2017/5/2.
 */

public class CityVO extends BaseVO {
    private CityVO datas;
    private ArrayList<CityVO> area_list;
    private String province_id;
    private String province_name;
    private ArrayList<CityVO> citys;
    private String city_id;
    private String city_name;
    private ArrayList<CityVO> districts;
    private String district_id="";
    private String district_name="";

    public CityVO getDatas() {
        return datas;
    }

    public void setDatas(CityVO datas) {
        this.datas = datas;
    }

    public ArrayList<CityVO> getArea_list() {
        return area_list;
    }

    public void setArea_list(ArrayList<CityVO> area_list) {
        this.area_list = area_list;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public ArrayList<CityVO> getCitys() {
        return citys;
    }

    public void setCitys(ArrayList<CityVO> citys) {
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

    public ArrayList<CityVO> getDistricts() {
        return districts;
    }

    public void setDistricts(ArrayList<CityVO> districts) {
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



    @Override
    public String toString() {
        if(province_name!=null){
            return province_name;
        }else if(city_name!=null){
            return  city_name;
        }else{
            return district_name;
        }

    }
}


