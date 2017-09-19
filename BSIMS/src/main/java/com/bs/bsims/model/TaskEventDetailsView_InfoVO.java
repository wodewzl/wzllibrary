package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description:
 * @date 2015-5-22 下午12:09:48
 * @email 971371860@qq.com
 * @version V1.0
 */

public class TaskEventDetailsView_InfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -458444966633523787L;

	// "taskid":"92",
	// "title":"测试",
	// "content":"但是发生的分干你妹啊发生地方发生地方 ",
	// "starttime":"1432051200",
	// "endtime":"1432137600",
	// "follow_up":{},
	// "responsible":{},
	// "relevant":[],
	// "schedule":"0",
	// "userid":"22",
	// "status":"0",
	// "headpic":"http://cp.beisheng.wang/Uploads/bs0001/Resume/image/1430622578.jpg",
	// "fullname":"聂世明",
	// "dname":"产品二组",
	// "pname":"IOS工程师",
	// "overtime":"1",
	// "editEtime":"1",
	// "scheduleAdd":"0",
	// "preliminary":"0",
	// "final":"0",
	// "comment":"0"
	
//	 {
//	        "ts_id": "74",
//	        "ts_userid": "25",
//	        "ts_taskid": "49",
//	        "ts_description": "项目一切进行顺利！",
//	        "ts_schedule": "10",
//	        "ts_time": "1431599073"
//	    }

	private TaskEventDetailsView_Info_RelevantVO follow_up;
	private TaskEventDetailsView_Info_RelevantVO responsible;
	private List<TaskEventDetailsView_Info_RelevantVO> relevant;
	private List<TaskEventDetailsView_Info_AnnexVO> annex;
	private List<TaskEventDetailsView_Info_ScheduleReplyVO> scheduleReply;

	/** 任务id */
	private String taskid = "";

	/** 标题 */
	private String title = "";

	/** 开始时间 */
	private long starttime;

	/** 结束时间 */
	private long endtime;

	/** 内容 */
	private String content = "";

	/** 进度 */
	private String schedule = "";

	/** 用户ID */
	private String userid = "";

	/** 头像 */
	private String headpic = "";

	/** 全名 */
	private String fullname = "";

	/** 部门名称 */
	private String dname = "";

	/** 职位名称 */
	private String pname = "";

	/** status 1负责人初审确认2 发布人定审确认任务完成状态 */
	private String status = "";

	/** 进度 */
	private String overtime = "";

	/** 查看者是否有编辑任务结束时间权限（1有0无） */
	private String editEtime = "";

	/** 头像 */
	private String scheduleAdd = "";

	/** 查看者是否有初审权限（1有0无） */
	private String preliminary = "";

	/** 部门名称 */
	private String finals = "";

	/**  0 是否有人评论 职位名称 */
	private String comment = "";

	/**
	 * 跟进人
	 * @return
	 */
	public TaskEventDetailsView_Info_RelevantVO getFollow_up() {
		return follow_up;
	}

	public void setFollow_up(TaskEventDetailsView_Info_RelevantVO follow_up) {
		this.follow_up = follow_up;
	}

	/**
	 * 责任人
	 * @return
	 */
	public TaskEventDetailsView_Info_RelevantVO getResponsible() {
		return responsible;
	}

	public void setResponsible(TaskEventDetailsView_Info_RelevantVO responsible) {
		this.responsible = responsible;
	}

	/**
	 * 相关人
	 * @return
	 */
	public List<TaskEventDetailsView_Info_RelevantVO> getRelevant() {
		return relevant;
	}

	public void setRelevant(List<TaskEventDetailsView_Info_RelevantVO> relevant) {
		this.relevant = relevant;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOvertime() {
		return overtime;
	}

	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}

	/** 查看者是否有编辑任务结束时间权限（1有0无） */
	public String getEditEtime() {
		return editEtime;
	}

	public void setEditEtime(String editEtime) {
		this.editEtime = editEtime;
	}

	public String getScheduleAdd() {
		return scheduleAdd;
	}

	public void setScheduleAdd(String scheduleAdd) {
		this.scheduleAdd = scheduleAdd;
	}

	/** 查看者是否有初审权限（1有0无）  任务进度为100% 之后由责任人初审*/
	public String getPreliminary() {
		return preliminary;
	}

	public void setPreliminary(String preliminary) {
		this.preliminary = preliminary;
	}

	public String getFinals() {
		return finals;
	}

	public void setFinals(String finals) {
		this.finals = finals;
	}

	/**
	 * 0 是否有人评论 评论数量
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * 进度回复 list列表
	 * @return
	 */
	public List<TaskEventDetailsView_Info_ScheduleReplyVO> getScheduleReply() {
		return scheduleReply;
	}

	public void setScheduleReply(
			List<TaskEventDetailsView_Info_ScheduleReplyVO> scheduleReply) {
		this.scheduleReply = scheduleReply;
	}

	/**
	 * 附件
	 * @return List<TaskEventDetailsView_Info_AnnexVO>
	 */
	public List<TaskEventDetailsView_Info_AnnexVO> getAnnex() {
		return annex;
	}

	public void setAnnex(List<TaskEventDetailsView_Info_AnnexVO> annex) {
		this.annex = annex;
	}
}
