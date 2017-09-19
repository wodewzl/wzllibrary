
package com.bs.bsims.model;

import java.util.List;

public class MessageVO {
    private MessageVO array;
    private String code;
    private String retinfo;
    private String system_time;
    private WeatherVO weather;

    // private String logreplay;
    // private String sendcc;
    // private String schedulenum;
    // private String culture;
    // private String ideasnum;
    //
    // private String companynum;
    // private String appnum;
    // private String trendsnum;
    // private String noticenum;
    // private String documentnum;
    // private String tasknum;
    //
    // private MessageVO userinfo;
    // private String entry;
    // private String quit;
    // private String qjnum;
    // private String lognum;
    // private String chidao;
    // private String zaotui;

    private MessageVO articles;
    private String count;
    private List<MessageVO> list;
    private String articleid;
    private String title;
    private String time;
    private String noticeid;
    private String sortname;
    private String fullname;
    private String dname;
    private String sortid;
    private String isread;
    private String warnnum;

    public String getWarnnum() {
        return warnnum;
    }

    public void setWarnnum(String warnnum) {
        this.warnnum = warnnum;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getSortid() {
        return sortid;
    }

    public void setSortid(String sortid) {
        this.sortid = sortid;
    }

    // public MessageVO getUserinfo() {
    // return userinfo;
    // }
    //
    // public void setUserinfo(MessageVO userinfo) {
    // this.userinfo = userinfo;
    // }
    //
    // public String getEntry() {
    // return entry;
    // }
    //
    // public void setEntry(String entry) {
    // this.entry = entry;
    // }
    //
    // public String getQuit() {
    // return quit;
    // }
    //
    // public void setQuit(String quit) {
    // this.quit = quit;
    // }
    //
    // public String getQjnum() {
    // return qjnum;
    // }
    //
    // public void setQjnum(String qjnum) {
    // this.qjnum = qjnum;
    // }
    //
    // public String getLognum() {
    // return lognum;
    // }
    //
    // public void setLognum(String lognum) {
    // this.lognum = lognum;
    // }
    //
    // public String getChidao() {
    // return chidao;
    // }
    //
    // public void setChidao(String chidao) {
    // this.chidao = chidao;
    // }
    //
    // public String getZaotui() {
    // return zaotui;
    // }
    //
    // public void setZaotui(String zaotui) {
    // this.zaotui = zaotui;
    // }

    public MessageVO getArticles() {
        return articles;
    }

    public void setArticles(MessageVO articles) {
        this.articles = articles;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<MessageVO> getList() {
        return list;
    }

    public void setList(List<MessageVO> list) {
        this.list = list;
    }

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    // public String getLogreplay() {
    // return logreplay;
    // }
    //
    // public void setLogreplay(String logreplay) {
    // this.logreplay = logreplay;
    // }
    //
    // public String getSendcc() {
    // return sendcc;
    // }
    //
    // public void setSendcc(String sendcc) {
    // this.sendcc = sendcc;
    // }
    //
    // public String getSchedulenum() {
    // return schedulenum;
    // }
    //
    // public void setSchedulenum(String schedulenum) {
    // this.schedulenum = schedulenum;
    // }
    //
    // public String getCulture() {
    // return culture;
    // }
    //
    // public void setCulture(String culture) {
    // this.culture = culture;
    // }
    //
    // public String getIdeasnum() {
    // return ideasnum;
    // }
    //
    // public void setIdeasnum(String ideasnum) {
    // this.ideasnum = ideasnum;
    // }
    //
    // public String getTrendsnum() {
    // return trendsnum;
    // }
    //
    // public void setTrendsnum(String trendsnum) {
    // this.trendsnum = trendsnum;
    // }

    public MessageVO getArray() {
        return array;
    }

    public void setArray(MessageVO array) {
        this.array = array;
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

    public WeatherVO getWeather() {
        return weather;
    }

    public void setWeather(WeatherVO weather) {
        this.weather = weather;
    }

    // public String getCompanynum() {
    // return companynum;
    // }
    //
    // public void setCompanynum(String companynum) {
    // this.companynum = companynum;
    // }
    //
    // public String getAppnum() {
    // return appnum;
    // }
    //
    // public void setAppnum(String appnum) {
    // this.appnum = appnum;
    // }
    //
    // public String getNoticenum() {
    // return noticenum;
    // }
    //
    // public void setNoticenum(String noticenum) {
    // this.noticenum = noticenum;
    // }
    //
    // public String getDocumentnum() {
    // return documentnum;
    // }
    //
    // public void setDocumentnum(String documentnum) {
    // this.documentnum = documentnum;
    // }
    //
    // public String getTasknum() {
    // return tasknum;
    // }
    //
    // public void setTasknum(String tasknum) {
    // this.tasknum = tasknum;
    // }

}
