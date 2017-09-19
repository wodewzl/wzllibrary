package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description: 获取出勤情况各项
 * @date 2015-7-2 上午11:14:16
 * @email 971371860@qq.com
 * @version V1.0
 */
public class DanganChuQingInfoLineChartVO implements Serializable {

	private static final long serialVersionUID = 8032674017185100648L;

	/**
	 * 获取出勤情况各项 和其它接口公用
	 */
	private List<StatisticsBossDepartmentDetailArray> chart;
	private String maxchart;
	private String unit;
	private String count;
	private String code;
	private String retinfo;
	private String system_time;

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

	public String getMaxchart() {
		return maxchart;
	}

	public void setMaxchart(String maxchart) {
		this.maxchart = maxchart;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<StatisticsBossDepartmentDetailArray> getChart() {
		return chart;
	}

	public void setChart(List<StatisticsBossDepartmentDetailArray> chart) {
		this.chart = chart;
	}
}
