package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/5/10.
 */

public class ShopCatVO extends BaseVO {
    private ShopCatVO datas;
    private List<ShopCatVO> cart_list;
    private String store_name;
    private List<ShopCatVO> goods;
    private String cart_id;
    private String goods_num;
    private List<ShopCatVO> hot_recomm_list;
    private String nc_distinct;
    private String goods_id;
    private String store_id;
    private String goods_name;
    private String goods_price;
    private String goods_image;
    private String goods_image_url;
    private String sum;
    private String cart_count;
    private boolean isCheck = false;
//    private int count;
    private String have_address;
    private String gc_id;
    private String detail_url;

    public ShopCatVO getDatas() {
        return datas;
    }

    public void setDatas(ShopCatVO datas) {
        this.datas = datas;
    }

    public List<ShopCatVO> getCart_list() {
        return cart_list;
    }

    public void setCart_list(List<ShopCatVO> cart_list) {
        this.cart_list = cart_list;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public List<ShopCatVO> getGoods() {
        return goods;
    }

    public void setGoods(List<ShopCatVO> goods) {
        this.goods = goods;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public List<ShopCatVO> getHot_recomm_list() {
        return hot_recomm_list;
    }

    public void setHot_recomm_list(List<ShopCatVO> hot_recomm_list) {
        this.hot_recomm_list = hot_recomm_list;
    }

    public String getNc_distinct() {
        return nc_distinct;
    }

    public void setNc_distinct(String nc_distinct) {
        this.nc_distinct = nc_distinct;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getGoods_image_url() {
        return goods_image_url;
    }

    public void setGoods_image_url(String goods_image_url) {
        this.goods_image_url = goods_image_url;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getCart_count() {
        return cart_count;
    }

    public void setCart_count(String cart_count) {
        this.cart_count = cart_count;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }

    public String getHave_address() {
        return have_address;
    }

    public void setHave_address(String have_address) {
        this.have_address = have_address;
    }

    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }
}
