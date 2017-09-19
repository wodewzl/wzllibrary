package com.bs.bsims.model;

import java.io.Serializable;

/**
 * @author peck
 * @Description: 1. 任务统计首页接口 返回参数
 * @date 2015-6-19 下午2:25:37
 * @email 971371860@qq.com
 * @version V1.0
 */
public class StatisticsBossTaskIndex implements Serializable {
	private static final long serialVersionUID = -5832876833296057546L;
	// last
	// num 数量
	// status 状态（1进行中，2待初审，3待定审，4完成，5过期）
	// date 下方列表展示（上个月） 日期

	private String num;
	private String status;
	private String date;
	private String dateTime;
	/**
	 * 6.	考勤统计首页接口 特有的
	 */
	private String did;

	/**
	 * 最上方各任务状态统计数据
	 * @return
	 */
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}
}
