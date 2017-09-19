package com.bs.bsims.model;

import java.io.Serializable;

/**
 * 任务事件类
 * 
 * @author Administrator
 * 
 */
public class TaskEventItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -911181988047533869L;

	/** 任务id */
	private String taskid = "";

	/** 标题 */
	private String title = "";

	/** 开始时间 */
	private long starttime;

	/** 结束时间 */
	private long endtime;

	/** 是否有附件 */
	private String annex_size = "";

	/** 附件Json */
	private String annex = "";

	/** 进度 */
	private String schedule = "";

	/** 用户ID */
	private String userid = "";

	/** 内容 */
	private String content = "";

	/** 权限 */
	private String purview = "";
	
	/**时间*/
	private String time = "";
	
	/**头像*/
	private String headpic ="";
	
	/**全名*/
	private String fullname="";
	
	/**部门名称*/
	private String dname="";
	
	/**职位名称*/
	private String pname="";
	
	/**是否已读*/
	private String isread="";
	private String isnoread="";
	
	/** status	1负责人初审确认2 发布人定审确认任务完成状态*/
	private String status="";
	
	private String follow_up="";
	private String responsible="";
	private String relevant="";
	
	
	public String getFollow_up() {
		return follow_up;
	}

	public void setFollow_up(String follow_up) {
		this.follow_up = follow_up;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public String getRelevant() {
		return relevant;
	}

	public void setRelevant(String relevant) {
		this.relevant = relevant;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public String getAnnex_size() {
		return annex_size;
	}

	public void setAnnex_size(String annex_size) {
		this.annex_size = annex_size;
	}

	public String getAnnex() {
		return annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPurview() {
		return purview;
	}

	public void setPurview(String purview) {
		this.purview = purview;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getIsnoread() {
		return isnoread;
	}

	public void setIsnoread(String isnoread) {
		this.isnoread = isnoread;
	}
}
