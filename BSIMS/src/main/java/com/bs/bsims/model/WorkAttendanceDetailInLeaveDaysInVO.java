package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description:
 * @date 2015-5-15 下午6:19:01
 * @email 971371860@qq.com
 * @version V1.0
 */
public class WorkAttendanceDetailInLeaveDaysInVO implements Serializable {
	private static final long serialVersionUID = 4979701905209059607L;

	// "days":"0",
	// "awork":"0"

	private String days;
	private String awork;
	private List<WorkAttendanceDetailVO1> list;

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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "WorkAttendanceDetailInLeaveDaysInVO [days=" + days + ", awork="
				+ awork + "]";
	}

}
