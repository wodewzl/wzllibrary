
package com.beisheng.synews.mode;

import java.util.List;

public class LifeVO {
    public String code;
    public String retinfo;
    public String system_time;
    private List<LifeVO> list;
    private String id;
    private String title;
    private List<LifeVO> children;
    private List<LifeVO> banner;
    private String thum;
    private String contentid;
    private String suburl;
    private String thumb;

    private String type;
    private String url;
    private String price;
    private String link;
    private String coverId;
    private String img;

    public List<LifeVO> getList() {
        return list;
    }

    public void setList(List<LifeVO> list) {
        this.list = list;
    }

    public List<LifeVO> getChildren() {
        return children;
    }

    public void setChildren(List<LifeVO> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRetinfo() {
        return retinfo;
    }

    public void setRetinfo(String retinfo) {
        this.retinfo = retinfo;
    }

    public String getSystem_time() {
        return system_time;
    }

    public void setSystem_time(String system_time) {
        this.system_time = system_time;
    }

    public List<LifeVO> getBanner() {
        return banner;
    }

    public void setBanner(List<LifeVO> banner) {
        this.banner = banner;
    }

    public String getThum() {
        return thum;
    }

    public void setThum(String thum) {
        this.thum = thum;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getSuburl() {
        return suburl;
    }

    public void setSuburl(String suburl) {
        this.suburl = suburl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
