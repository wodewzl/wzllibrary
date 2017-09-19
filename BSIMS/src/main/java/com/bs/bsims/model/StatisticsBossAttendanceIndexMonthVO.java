package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description: 7. 考勤统计首页点击具体月份时下方展示接口
 * @date 2015-6-24 下午7:06:24
 * @email 971371860@qq.com
 * @version V1.0
 */
public class StatisticsBossAttendanceIndexMonthVO implements Serializable {

	private static final long serialVersionUID = 8285679295700996178L;

	private List<StatisticsBossAttendanceIndexShow> array;

	/**
	 * 8. 考勤统计饼形图接口 专用 10.各部门下详细的考勤统计接口 专用
	 */
	private String allnum;

	/**
	 * 10.各部门下详细的考勤统计接口 专用
	 */
	private String minus;

	/**
	 * 10.各部门下详细的考勤统计接口 专用
	 */
	private String contrast;

	// "allnum": "135",
	// "minus": "68",
	// "contrast": "3",

	private String code;
	private String retinfo;
	private String system_time;
	/**
	 * 考勤统计饼形图 中间的数据 接口增加一个单位
	 * 2015/6/29 11:21
	 */
	private String unit;

	public List<StatisticsBossAttendanceIndexShow> getArray() {
		return array;
	}

	public void setArray(List<StatisticsBossAttendanceIndexShow> array) {
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

	public String getAllnum() {
		return allnum;
	}

	public void setAllnum(String allnum) {
		this.allnum = allnum;
	}

	public String getMinus() {
		return minus;
	}

	public void setMinus(String minus) {
		this.minus = minus;
	}

	public String getContrast() {
		return contrast;
	}

	public void setContrast(String contrast) {
		this.contrast = contrast;
	}

	/**
	 * 单位(元、天、次...)
	 * @return
	 */
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
