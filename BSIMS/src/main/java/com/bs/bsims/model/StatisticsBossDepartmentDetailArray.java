package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description: 9.各部门各月考工统计详情接口 array
 * @date 2015-6-25 下午6:35:46
 * @email 971371860@qq.com
 * @version V1.0
 */

public class StatisticsBossDepartmentDetailArray implements Serializable {

	private static final long serialVersionUID = -4460628691161637998L;

	private String allnum;
	private String nextnum;

	private List<StatisticsBossAttendanceIndexShow> list;

	public String getAllnum() {
		return allnum;
	}

	public void setAllnum(String allnum) {
		this.allnum = allnum;
	}

	public String getNextnum() {
		return nextnum;
	}

	public void setNextnum(String nextnum) {
		this.nextnum = nextnum;
	}

	public List<StatisticsBossAttendanceIndexShow> getList() {
		return list;
	}

	public void setList(List<StatisticsBossAttendanceIndexShow> list) {
		this.list = list;
	}
}
