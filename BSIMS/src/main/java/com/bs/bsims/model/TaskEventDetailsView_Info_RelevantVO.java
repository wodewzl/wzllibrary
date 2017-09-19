package com.bs.bsims.model;

import java.io.Serializable;

/** 
 * @author peck
 * @Description:  任务详情  服务器返回的数据中info中的 relevant(数组形式) follow_up responsible
 * @date 2015-5-22 下午12:12:34 
 * @email  971371860@qq.com
 * @version V1.0 
 */

public class TaskEventDetailsView_Info_RelevantVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 723738553337022113L;
	
//	"userid":"20",
//	"headpic":"暂无",
//	"fullname":"罗佳"
	
//	"userid":"78",
//	"headpic":"http://cp.beisheng.wang/Uploads/bs0001/Resume/image/1432004181.jpg",
//	"fullname":"李四",
//	"status":"0"
	
	/** 任务id */
	private String userid = "";
	/** 任务id */
	private String headpic = "";
	/** 任务id */
	private String fullname = "";
	/** 任务id */
	private String status = "";
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
