
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class DiscussVO implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 2723935642313523958L;
    private String arid;
    private String userid;
    private String articleid;
    private String content;
    private String sort;
    private String src;
    private String soundlength;
    private String ifcheck;
    private String time;
    private String rid;
    private String isanonymous;
    private String praise;
    private String decline;
    private String islead;
    private String uid;
    private String headpic;
    private String fullname;
    private List<DiscussVO> replay;
    private String replayname;

    private String commentid;
    private String rcomentid;
    private String sounpath;
    private String ispraised;
    private String isdeclined;
    private String cid;

    private boolean agree;
    private boolean oppose;
    private String isboss;
    private String replyCount;

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getIsboss() {
        return isboss;
    }

    public void setIsboss(String isboss) {
        this.isboss = isboss;
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

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getRcomentid() {
        return rcomentid;
    }

    public void setRcomentid(String rcomentid) {
        this.rcomentid = rcomentid;
    }

    public String getSounpath() {
        return sounpath;
    }

    public void setSounpath(String sounpath) {
        this.sounpath = sounpath;
    }

    public String getIspraised() {
        return ispraised;
    }

    public void setIspraised(String ispraised) {
        this.ispraised = ispraised;
    }

    public String getIsdeclined() {
        return isdeclined;
    }

    public void setIsdeclined(String isdeclined) {
        this.isdeclined = isdeclined;
    }

    public String getArid() {
        return arid;
    }

    public void setArid(String arid) {
        this.arid = arid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSoundlength() {
        return soundlength;
    }

    public void setSoundlength(String soundlength) {
        this.soundlength = soundlength;
    }

    public String getIfcheck() {
        return ifcheck;
    }

    public void setIfcheck(String ifcheck) {
        this.ifcheck = ifcheck;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getIsanonymous() {
        return isanonymous;
    }

    public void setIsanonymous(String isanonymous) {
        this.isanonymous = isanonymous;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getDecline() {
        return decline;
    }

    public void setDecline(String decline) {
        this.decline = decline;
    }

    public String getIslead() {
        return islead;
    }

    public void setIslead(String islead) {
        this.islead = islead;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public List<DiscussVO> getReplay() {
        return replay;
    }

    public void setReplay(List<DiscussVO> replay) {
        this.replay = replay;
    }

    public String getReplayname() {
        return replayname;
    }

    public void setReplayname(String replayname) {
        this.replayname = replayname;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

}
