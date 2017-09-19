package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/5/5.
 */

public class HistoryShopVO extends BaseVO {
    private HistoryShopVO datas;
    private List<HistoryShopVO> goodsbrowse_list;
    private List<HistoryShopVO> favorites_list;
    private String goods_id;
    private String goods_name;
    private String goods_image;
    private String goods_image_url;
    private String goods_price;
    private String detail_url;

    public HistoryShopVO getDatas() {
        return datas;
    }

    public void setDatas(HistoryShopVO datas) {
        this.datas = datas;
    }

    public List<HistoryShopVO> getGoodsbrowse_list() {
        return goodsbrowse_list;
    }

    public void setGoodsbrowse_list(List<HistoryShopVO> goodsbrowse_list) {
        this.goodsbrowse_list = goodsbrowse_list;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
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

    public List<HistoryShopVO> getFavorites_list() {
        return favorites_list;
    }

    public void setFavorites_list(List<HistoryShopVO> favorites_list) {
        this.favorites_list = favorites_list;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }


}
