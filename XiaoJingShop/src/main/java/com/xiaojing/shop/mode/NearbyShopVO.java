package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/5/13.
 */

public class NearbyShopVO extends BaseVO {
    private NearbyShopVO datas;
    private List<NearbyShopVO> list;
    private String merchant_id;
    private String merchant_name;
    private String merchant_star;
    private String distance;
    private String merchant_class_text;
    private String img_url;
    private NearbyShopVO merchant;
    private String merchant_worktime;
    private String merchant_introduce;
    private String merchant_mobile;
    private String merchant_address;
    private String merchant_lat;
    private String merchant_lng;
    private String img_count;
    private List<String> imgs;
    private NearbyShopVO share_data;
    private String title;
    private String desc;
    private String image;
    private String url;

    public NearbyShopVO getDatas() {
        return datas;
    }

    public void setDatas(NearbyShopVO datas) {
        this.datas = datas;
    }

    public List<NearbyShopVO> getList() {
        return list;
    }

    public void setList(List<NearbyShopVO> list) {
        this.list = list;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_star() {
        return merchant_star;
    }

    public void setMerchant_star(String merchant_star) {
        this.merchant_star = merchant_star;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMerchant_class_text() {
        return merchant_class_text;
    }

    public void setMerchant_class_text(String merchant_class_text) {
        this.merchant_class_text = merchant_class_text;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public NearbyShopVO getMerchant() {
        return merchant;
    }

    public void setMerchant(NearbyShopVO merchant) {
        this.merchant = merchant;
    }

    public String getMerchant_worktime() {
        return merchant_worktime;
    }

    public void setMerchant_worktime(String merchant_worktime) {
        this.merchant_worktime = merchant_worktime;
    }

    public String getMerchant_introduce() {
        return merchant_introduce;
    }

    public void setMerchant_introduce(String merchant_introduce) {
        this.merchant_introduce = merchant_introduce;
    }

    public String getMerchant_mobile() {
        return merchant_mobile;
    }

    public void setMerchant_mobile(String merchant_mobile) {
        this.merchant_mobile = merchant_mobile;
    }

    public String getMerchant_address() {
        return merchant_address;
    }

    public void setMerchant_address(String merchant_address) {
        this.merchant_address = merchant_address;
    }

    public String getMerchant_lat() {
        return merchant_lat;
    }

    public void setMerchant_lat(String merchant_lat) {
        this.merchant_lat = merchant_lat;
    }

    public String getMerchant_lng() {
        return merchant_lng;
    }

    public void setMerchant_lng(String merchant_lng) {
        this.merchant_lng = merchant_lng;
    }

    public String getImg_count() {
        return img_count;
    }

    public void setImg_count(String img_count) {
        this.img_count = img_count;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public NearbyShopVO getShare_data() {
        return share_data;
    }

    public void setShare_data(NearbyShopVO share_data) {
        this.share_data = share_data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
