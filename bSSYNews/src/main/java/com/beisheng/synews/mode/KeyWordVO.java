
package com.beisheng.synews.mode;

import java.util.List;

public class KeyWordVO {
    private String code;
    private String retinfo;
    private String count;
    private String[] klist;
    private String name;
    private String page;
    private String perpage;
    private String total;

    private List<KeyWordVO> list;
    private String contentid;
    private String title;
    private String creatime;
    private String thumb;
    private String suburl;
    private String read;
    private String typename;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String[] getKlist() {
        return klist;
    }

    public void setKlist(String[] klist) {
        this.klist = klist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatime() {
        return creatime;
    }

    public void setCreatime(String creatime) {
        this.creatime = creatime;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSuburl() {
        return suburl;
    }

    public void setSuburl(String suburl) {
        this.suburl = suburl;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public List<KeyWordVO> getList() {
        return list;
    }

    public void setList(List<KeyWordVO> list) {
        this.list = list;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

}
