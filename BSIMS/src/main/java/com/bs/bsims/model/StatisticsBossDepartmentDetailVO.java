package com.bs.bsims.model;

import java.io.Serializable;

/**
 * @author peck
 * @Description: 9.各部门各月考工统计详情接口 最外层封装
 * @date 2015-6-25 下午6:38:39
 * @email 971371860@qq.com
 * @version V1.0
 */

public class StatisticsBossDepartmentDetailVO implements Serializable {

	private static final long serialVersionUID = 7164681598335669716L;

	private StatisticsBossDepartmentDetailArray array;

	private String code;
	private String retinfo;
	private String system_time;

	public StatisticsBossDepartmentDetailArray getArray() {
		return array;
	}

	public void setArray(StatisticsBossDepartmentDetailArray array) {
		this.array = array;
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
