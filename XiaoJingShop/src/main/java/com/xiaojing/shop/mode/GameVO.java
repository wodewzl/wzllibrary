package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/6/2.
 */

public class GameVO extends BaseVO {
    private String game_banner;
    private GameVO datas;
    private List<GameVO> list;
    private String game_id;
    private String game_name;
    private String game_star;
    private String game_logo;
    private String game_card_price;
    private GameVO merchant;
    private String game_desc;
    private String game_ios_download_url;
    private String game_android_download_url;
    private List<String> imgs;
    private String game_banner_url;

    public String getGame_banner() {
        return game_banner;
    }

    public void setGame_banner(String game_banner) {
        this.game_banner = game_banner;
    }

    public GameVO getDatas() {
        return datas;
    }

    public void setDatas(GameVO datas) {
        this.datas = datas;
    }

    public List<GameVO> getList() {
        return list;
    }

    public void setList(List<GameVO> list) {
        this.list = list;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getGame_star() {
        return game_star;
    }

    public void setGame_star(String game_star) {
        this.game_star = game_star;
    }

    public String getGame_logo() {
        return game_logo;
    }

    public void setGame_logo(String game_logo) {
        this.game_logo = game_logo;
    }

    public String getGame_card_price() {
        return game_card_price;
    }

    public void setGame_card_price(String game_card_price) {
        this.game_card_price = game_card_price;
    }

    public GameVO getMerchant() {
        return merchant;
    }

    public void setMerchant(GameVO merchant) {
        this.merchant = merchant;
    }

    public String getGame_desc() {
        return game_desc;
    }

    public void setGame_desc(String game_desc) {
        this.game_desc = game_desc;
    }

    public String getGame_ios_download_url() {
        return game_ios_download_url;
    }

    public void setGame_ios_download_url(String game_ios_download_url) {
        this.game_ios_download_url = game_ios_download_url;
    }

    public String getGame_android_download_url() {
        return game_android_download_url;
    }

    public void setGame_android_download_url(String game_android_download_url) {
        this.game_android_download_url = game_android_download_url;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getGame_banner_url() {
        return game_banner_url;
    }

    public void setGame_banner_url(String game_banner_url) {
        this.game_banner_url = game_banner_url;
    }
}
