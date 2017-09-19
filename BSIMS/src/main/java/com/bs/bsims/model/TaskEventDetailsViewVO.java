package com.bs.bsims.model;

import java.io.Serializable;

/** 
 * @author peck
 * @Description:   任务详情 服务器返回的直接数据
 * @date 2015-5-22 下午12:06:26 
 * @email  971371860@qq.com
 * @version V1.0 
 */

public class TaskEventDetailsViewVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2558015816495066379L;
	private TaskEventDetailsView_InfoVO info;
	private String count;
	private String code;
	private String retinfo;
	private String system_time;
	public TaskEventDetailsView_InfoVO getInfo() {
		return info;
	}
	public void setInfo(TaskEventDetailsView_InfoVO info) {
		this.info = info;
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
}
