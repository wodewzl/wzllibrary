package com.bs.bsims.model;

/**进度描述实体类
 * @author Administrator
 *
 */
public class TaskEventScheduleDescriptionItem {
	
	/**任务进度ID*/
	private String tsid="";
	
	/**userid*/
	private String userid="";
	
	/**taskid*/
	private String taskid="";
	
	/**描述*/
	private String description="";
	
	/**进度*/
	private String schedule="";
	
	/**时间*/
	private long time;
	
	/**头像*/
	private String headpic="";
	
	/**名字*/
	private String fullname="";

	public String getTsid() {
		return tsid;
	}

	public void setTsid(String tsid) {
		this.tsid = tsid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
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
	
	
}
