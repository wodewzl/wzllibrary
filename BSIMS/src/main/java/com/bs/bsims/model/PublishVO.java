
package com.bs.bsims.model;

import java.util.List;

public class PublishVO {
    private String isread;
    private String articleid;
    private String sortid;
    private String title;
    private String userid;
    private String noticeid;
    private String time;

    private String content;
    private String pictures;
    private String classid;
    private String recipient;
    private String checks;
    private String serial_number;
    private String iscomment;
    private String receivingobj;
    private String reader;
    private String sortname;
    private String headpic;
    private String fullname;
    private String did;
    private String dname;
    private List<EmployeeVO> tousers;

    private String isannex;
    private String annex;
    private List<AnnexVO> annexs;
    private String noread;
    private String read;

    public String getNoread() {
        return noread;
    }

    public void setNoread(String noread) {
        this.noread = noread;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getIsannex() {
        return isannex;
    }

    public void setIsannex(String isannex) {
        this.isannex = isannex;
    }

    public String getAnnex() {
        return annex;
    }

    public void setAnnex(String annex) {
        this.annex = annex;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getSortid() {
        return sortid;
    }

    public void setSortid(String sortid) {
        this.sortid = sortid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getChecks() {
        return checks;
    }

    public void setChecks(String checks) {
        this.checks = checks;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getIscomment() {
        return iscomment;
    }

    public void setIscomment(String iscomment) {
        this.iscomment = iscomment;
    }

    public String getReceivingobj() {
        return receivingobj;
    }

    public void setReceivingobj(String receivingobj) {
        this.receivingobj = receivingobj;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public List<EmployeeVO> getTousers() {
        return tousers;
    }

    public void setTousers(List<EmployeeVO> tousers) {
        this.tousers = tousers;
    }

    public List<AnnexVO> getAnnexs() {
        return annexs;
    }

    public void setAnnexs(List<AnnexVO> annexs) {
        this.annexs = annexs;
    }

}
