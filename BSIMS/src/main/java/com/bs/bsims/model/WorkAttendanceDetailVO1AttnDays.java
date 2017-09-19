package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/** 
 * @author peck
 * @Description:  
 * @date 2015-5-31 下午2:32:13 
 * @email  971371860@qq.com
 * @version V1.0 
 */

public class WorkAttendanceDetailVO1AttnDays implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7460256203758492971L;
	
//	{
//	    "nowrlog_days": [
//	        "2015-5-30"
//	    ],
//	    "belate_days": [
//	        "2015-05-30 09:22"
//	    ],
//	    "leavearly_days": "暂无",
//	    "absence_days": "2015-5-4 4,2015-5-30 3"
//	}
	
	private List<String> nowrlog_days;
	private String belate_days = "";
    private String leavearly_days = "";
	private String absence_days = "";
	   public List<String> getNowrlog_days() {
	        return nowrlog_days;
	    }
	    public void setNowrlog_days(List<String> nowrlog_days) {
	        this.nowrlog_days = nowrlog_days;
	    }
	public String getBelate_days() {
		return belate_days;
	}
	public void setBelate_days(String belate_days) {
		this.belate_days = belate_days;
	}
	public String getLeavearly_days() {
		return leavearly_days;
	}
	public void setLeavearly_days(String leavearly_days) {
		this.leavearly_days = leavearly_days;
	}
	public String getAbsence_days() {
		return absence_days;
	}
	public void setAbsence_days(String absence_days) {
		this.absence_days = absence_days;
	}
}
