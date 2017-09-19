
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class JournalListVO1 implements Serializable {
    private JournalListVO1 info;
    private List<JournalListVO1> array;
    private List<JournalListVO1> loglist;
    private String count;
    private String code;
    private String retinfo;
    private String system_time;

    private String logid;
    private String time;
    private String fullname;
    private String headpic;
    private String sex;
    private String dname;
    private String postsname;
    private String positionname;
    private String commentCount;
    private String loguid;
    private String isread;
    private String isboss;
    private String nextid;
    private String preid;

    private String content1;
    private String content2;
    private String content3;
    private String content4;
    private String type;
    private String praise;
    private String typename;
    private String date;
    private String ispraised;
    private boolean agree;
    private boolean oppose;
    private String isfavor;
    private boolean isAll = false;
    public int select;
    private List<JournalListVO1> lastdates;
    private List<JournalListVO1> nextdates;

    private List<JournalListVO1> dates;

    public String getIsboss() {
        return isboss;
    }

    public void setIsboss(String isboss) {
        this.isboss = isboss;
    }

    public String getLoguid() {
        return loguid;
    }

    public void setLoguid(String loguid) {
        this.loguid = loguid;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getPostsname() {
        return postsname;
    }

    public void setPostsname(String postsname) {
        this.postsname = postsname;
    }

    public String getPositionname() {
        return positionname;
    }

    public void setPositionname(String positionname) {
        this.positionname = positionname;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getNextid() {
        return nextid;
    }

    public void setNextid(String nextid) {
        this.nextid = nextid;
    }

    public String getPreid() {
        return preid;
    }

    public void setPreid(String preid) {
        this.preid = preid;
    }

    public List<JournalListVO1> getArray() {
        return array;
    }

    public void setArray(List<JournalListVO1> array) {
        this.array = array;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }

    public String getContent4() {
        return content4;
    }

    public void setContent4(String content4) {
        this.content4 = content4;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIspraised() {
        return ispraised;
    }

    public void setIspraised(String ispraised) {
        this.ispraised = ispraised;
    }

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public boolean isOppose() {
        return oppose;
    }

    public void setOppose(boolean oppose) {
        this.oppose = oppose;
    }

    public String getIsfavor() {
        return isfavor;
    }

    public void setIsfavor(String isfavor) {
        this.isfavor = isfavor;
    }

    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean isAll) {
        this.isAll = isAll;
    }

    public JournalListVO1 getInfo() {
        return info;
    }

    public void setInfo(JournalListVO1 info) {
        this.info = info;
    }

    public List<JournalListVO1> getLoglist() {
        return loglist;
    }

    public void setLoglist(List<JournalListVO1> loglist) {
        this.loglist = loglist;
    }

    public List<JournalListVO1> getDates() {
        return dates;
    }

    public void setDates(List<JournalListVO1> dates) {
        this.dates = dates;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public List<JournalListVO1> getLastdates() {
        return lastdates;
    }

    public void setLastdates(List<JournalListVO1> lastdates) {
        this.lastdates = lastdates;
    }

    public List<JournalListVO1> getNextdates() {
        return nextdates;
    }

    public void setNextdates(List<JournalListVO1> nextdates) {
        this.nextdates = nextdates;
    }

}
