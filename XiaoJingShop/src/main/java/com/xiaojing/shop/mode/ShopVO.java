package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/5/3.
 */

public class ShopVO extends BaseVO {
    private ShopVO datas;
    private List<ShopVO> goods_list;
    private String goods_id;
    private String goods_name;
    private String goods_jingle;
    private String goods_price;
    private String goods_image;
    private String goods_salenum;
    private String is_virtual;
    private String store_name;
    private String goods_image_url;
    private String detail_url;

    private List<ShopVO> article_list;
    private String article_id;
    private String article_title;
    private String article_desc;
    private String article_cover;
    private String start_time;
    private String end_time;
    private String article_url;
    private String[] deliver_info;
    private String article_view;
    private List<ShopVO> favorites_list;
    private List<ShopVO> goodsbrowse_list;

    public String getHasmore() {
        return hasmore;
    }

    public void setHasmore(String hasmore) {
        this.hasmore = hasmore;
    }

    public String getPage_total() {
        return page_total;
    }

    public void setPage_total(String page_total) {
        this.page_total = page_total;
    }

    public ShopVO getDatas() {
        return datas;
    }

    public void setDatas(ShopVO datas) {
        this.datas = datas;
    }

    public List<ShopVO> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<ShopVO> goods_list) {
        this.goods_list = goods_list;
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

    public String getGoods_jingle() {
        return goods_jingle;
    }

    public void setGoods_jingle(String goods_jingle) {
        this.goods_jingle = goods_jingle;
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

    public String getGoods_salenum() {
        return goods_salenum;
    }

    public void setGoods_salenum(String goods_salenum) {
        this.goods_salenum = goods_salenum;
    }

    public String getIs_virtual() {
        return is_virtual;
    }

    public void setIs_virtual(String is_virtual) {
        this.is_virtual = is_virtual;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getGoods_image_url() {
        return goods_image_url;
    }

    public void setGoods_image_url(String goods_image_url) {
        this.goods_image_url = goods_image_url;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public List<ShopVO> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<ShopVO> article_list) {
        this.article_list = article_list;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_desc() {
        return article_desc;
    }

    public void setArticle_desc(String article_desc) {
        this.article_desc = article_desc;
    }

    public String getArticle_cover() {
        return article_cover;
    }

    public void setArticle_cover(String article_cover) {
        this.article_cover = article_cover;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public String[] getDeliver_info() {
        return deliver_info;
    }

    public void setDeliver_info(String[] deliver_info) {
        this.deliver_info = deliver_info;
    }

    public String getArticle_view() {
        return article_view;
    }

    public void setArticle_view(String article_view) {
        this.article_view = article_view;
    }

    public List<ShopVO> getFavorites_list() {
        return favorites_list;
    }

    public void setFavorites_list(List<ShopVO> favorites_list) {
        this.favorites_list = favorites_list;
    }

    public List<ShopVO> getGoodsbrowse_list() {
        return goodsbrowse_list;
    }

    public void setGoodsbrowse_list(List<ShopVO> goodsbrowse_list) {
        this.goodsbrowse_list = goodsbrowse_list;
    }
}
