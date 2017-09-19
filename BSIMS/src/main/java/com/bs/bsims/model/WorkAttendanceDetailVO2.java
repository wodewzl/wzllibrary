package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/** 
 * @author peck
 * @Description:  考勤详情 返回的信息  最内层的数据 数据从最内部的开始分配数字
 * @date 2015-5-30 下午8:31:26 
 * @email  971371860@qq.com
 * @version V1.0 
 */

public class WorkAttendanceDetailVO2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -145245250227588678L;
	
//	{
//	    "days": "32",
//	    "awork": "-1120",
//	    "list": [
//	        {
//	            "stime": "2015-05-06 09:00",
//	            "etime": "2015-05-29 17:30",
//	            "hours": "130.0"
//	        },
//	        {
//	            "stime": "2015-05-15 09:00",
//	            "etime": "2015-05-28 17:30",
//	            "hours": "78.0"
//	        }
//	    ]
//	}
	
	private String type = "";
	private String days = "";
	private String awork = "";
	private List<WorkAttendanceDetailVO1> list;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getAwork() {
		return awork;
	}
	public void setAwork(String awork) {
		this.awork = awork;
	}
	public List<WorkAttendanceDetailVO1> getList() {
		return list;
	}
	public void setList(List<WorkAttendanceDetailVO1> list) {
		this.list = list;
	}

}
