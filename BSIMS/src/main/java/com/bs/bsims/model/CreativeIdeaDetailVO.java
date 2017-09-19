
package com.bs.bsims.model;

import java.util.List;

public class CreativeIdeaDetailVO {

    private String articleid;
    private String title;
    private String content;
    private String pictures;
    // private List<String> pictures;
    private String userid;
    private String recipient;
    private String time;
    private String checks;
    private String type;
    private String status;
    private String accept;
    private String reader;
    private String replynum;
    private String leadnum;
    private String headpic;
    private String fullname;
    private String did;
    private String dname;
    private String isadopt;
    private String checksNew;
    private String isAnonymous;
    private String pname;
    private List<EmployeeVO> insUser;

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(String isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getChecksNew() {
        return checksNew;
    }

    public void setChecksNew(String checksNew) {
        this.checksNew = checksNew;
    }

    public String getIsadopt() {
        return isadopt;
    }

    public void setIsadopt(String isadopt) {
        this.isadopt = isadopt;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // public List<String> getPictures() {
    // return pictures;
    // }
    //
    // public void setPictures(List<String> pictures) {
    // this.pictures = pictures;
    // }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChecks() {
        return checks;
    }

    public void setChecks(String checks) {
        this.checks = checks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String getReplynum() {
        return replynum;
    }

    public void setReplynum(String replynum) {
        this.replynum = replynum;
    }

    public String getLeadnum() {
        return leadnum;
    }

    public void setLeadnum(String leadnum) {
        this.leadnum = leadnum;
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

    public List<EmployeeVO> getInsUser() {
        return insUser;
    }

    public void setInsUser(List<EmployeeVO> insUser) {
        this.insUser = insUser;
    }

}
