package com.bs.bsims.model;

import java.io.Serializable;

/** 
 * @author peck
 * @Description:   考勤详情 返回的信息  最内层的数据 数据从最内部的开始分配数字
 * @date 2015-5-30 下午8:29:11 
 * @email  971371860@qq.com
 * @version V1.0 
 */

public class WorkAttendanceDetailVO1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -311628400106486685L;
	
//	stime": "2015-05-22 17:00",
//    "etime": "2015-05-29 04:00", WorkAttendanceDetailVO1Attn_days
//    "hours": "33.0"
	
	private String stime = "";
	private String etime = "";
	private String hours = "";
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return stime+"至"+etime+"\t\n共:"+hours+"小时";
	}
}
