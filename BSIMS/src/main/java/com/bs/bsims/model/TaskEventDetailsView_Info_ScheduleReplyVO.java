package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description:
 * @date 2015-5-25 下午4:22:35
 * @email 971371860@qq.com
 * @version V1.0
 */

public class TaskEventDetailsView_Info_ScheduleReplyVO implements Serializable {
	private static final long serialVersionUID = 4998624079089647979L;
	// {
	// "ts_id": "74",
	// "ts_userid": "25",
	// "ts_taskid": "49",
	// "ts_description": "项目一切进行顺利！",
	// "ts_schedule": "10",
	// "ts_time": "1431599073"
	// }
	/** 任务id */
	private String ts_id = "";
	private String ts_userid = "";
	private String ts_taskid = "";
	private String ts_description = "";
	private String ts_schedule = "";
	private String ts_time = "";
	
	public String getTs_id() {
		return ts_id;
	}
	public void setTs_id(String ts_id) {
		this.ts_id = ts_id;
	}
	public String getTs_userid() {
		return ts_userid;
	}
	public void setTs_userid(String ts_userid) {
		this.ts_userid = ts_userid;
	}
	public String getTs_taskid() {
		return ts_taskid;
	}
	public void setTs_taskid(String ts_taskid) {
		this.ts_taskid = ts_taskid;
	}
	public String getTs_description() {
		return ts_description;
	}
	public void setTs_description(String ts_description) {
		this.ts_description = ts_description;
	}
	public String getTs_schedule() {
		return ts_schedule;
	}
	public void setTs_schedule(String ts_schedule) {
		this.ts_schedule = ts_schedule;
	}
	public String getTs_time() {
		return ts_time;
	}
	public void setTs_time(String ts_time) {
		this.ts_time = ts_time;
	}
	
}
