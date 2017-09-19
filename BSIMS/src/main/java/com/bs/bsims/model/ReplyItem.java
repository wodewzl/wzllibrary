package com.bs.bsims.model;

/**
 * 评论实体类
 * 
 * @author Administrator
 * 
 */
public class ReplyItem {
	/** 信息id */
	private String articleid = "";

	/** 回复信息ID */
	private String arid = "";

	/** 回复用户ID */
	private String userid = "";

	private String approvalid = "";

	/** 头像 */
	private String headpic = "";

	/** 名字 */
	private String fullname = "";

	/** 时间 */
	private String time = "";

	/** 内容 */
	private String content = "";

	/** 类型 1.文字（表情）2.语音 */
	private int sort;

	/** 远程文件名 仅语音时使用 */
	private String fileurl;

	/** 远程文件名 仅语音时使用 */
	private String filename;

	/** 语音长度，单位（秒），仅语音使用 */
	private String soundlength;

	/** 任务详情回复信息ID */
	private String trid;

	public ReplyItem(String fullname, String time, String content, String filename, String length, int sort) {
		this.fullname = fullname;
		this.time = time;
		this.content = content;
		this.filename = filename;
		this.soundlength = length;
		this.sort = sort;
	}

	public ReplyItem() {

	}

	public String getTrid() {
		return trid;
	}

	public void setTrid(String trid) {
		this.trid = trid;
	}

	public String getArticleid() {
		return articleid;
	}

	public void setArticleid(String articleid) {
		this.articleid = articleid;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getSoundlength() {
		return soundlength;
	}

	public void setSoundlength(String length) {
		this.soundlength = length;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getApprovalid() {
		return approvalid;
	}

	public void setApprovalid(String approvalid) {
		this.approvalid = approvalid;
	}
}
